package edu.unifor.br.distrischool.studentservice.service;

import edu.unifor.br.distrischool.studentservice.dto.*;
import edu.unifor.br.distrischool.studentservice.entity.Student;
import edu.unifor.br.distrischool.studentservice.entity.Student.StudentStatus;
import edu.unifor.br.distrischool.studentservice.repository.StudentRepository;
import edu.unifor.br.distrischool.studentservice.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    //private final KafkaEventService kafkaEventService;
    private final EmailService emailService;

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO request, Long createdBy) {
        log.info("Tentativa de criar estudante: {}", request.getEmail());

        // Validar duplicatas
        validateDuplicates(request);

        // Criar estudante primeiro (sem user_id)
        Student student = Student.builder()
                .registrationNumber(generateRegistrationNumber())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .cpf(request.getCpf())
                .rg(request.getRg())
                .rgIssuer(request.getRgIssuer())
                .rgIssueDate(request.getRgIssueDate())
                .build();

        student = studentRepository.save(student);
        log.info("Estudante criado com ID temporário: {}", student.getId());

        try {
            // Criar usuário no Auth-Service


            // Atualizar student com user_id
            student = studentRepository.save(student);

            // Publicar evento
            //kafkaEventService.publishStudentEvent("student.created", student);

            // Enviar email de boas-vindas
            emailService.sendWelcomeEmail(request.getEmail(), student.getRegistrationNumber());

            log.info("Estudante criado com sucesso: {} - Matrícula: {}",
                    student.getFullName(), student.getRegistrationNumber());

            return StudentResponseDTO.from(student);

        } catch (Exception e) {
            log.error("Erro ao criar usuário no Auth-Service. Revertendo criação do estudante.", e);
            // Compensação: deletar student se falhar
            studentRepository.delete(student);
            throw new StudentCreationException("Erro ao criar estudante: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Buscando estudante por ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com ID: " + id));

        return StudentResponseDTO.from(student);
    }


    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByRegistrationNumber(String registrationNumber) {
        log.info("Buscando estudante por matrícula: {}", registrationNumber);

        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Estudante não encontrado com matrícula: " + registrationNumber));

        return StudentResponseDTO.from(student);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentByCpf(String cpf) {
        log.info("Buscando estudante por CPF: {}", cpf);

        Student student = studentRepository.findByCpf(cpf)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com CPF: " + cpf));

        return StudentResponseDTO.from(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
        log.info("Listando todos os estudantes - Página: {}", pageable.getPageNumber());

        return studentRepository.findAll(pageable)
                .map(StudentResponseDTO::from);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentUpdateDTO request, Long updatedBy) {
        log.info("Atualizando estudante ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com ID: " + id));

        // Validar mudanças de CPF/RG se fornecidos
        if (request.getCpf() != null && !request.getCpf().equals(student.getCpf())) {
            if (studentRepository.existsByCpf(request.getCpf())) {
                throw new DuplicateStudentException("CPF já cadastrado: " + request.getCpf());
            }
        }

        // Atualizar campos
        updateStudentFields(student, request);
        student.setUpdatedBy(updatedBy);

        student = studentRepository.save(student);

        // Publicar evento
        //kafkaEventService.publishStudentEvent("student.updated", student);

        log.info("Estudante atualizado com sucesso: {}", student.getId());

        return StudentResponseDTO.from(student);
    }

    @Transactional
    public StudentResponseDTO updateStudentStatus(Long id, StudentStatus newStatus, Long updatedBy) {
        log.info("Atualizando status do estudante ID: {} para {}", id, newStatus);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com ID: " + id));

        student = studentRepository.save(student);

        // Publicar evento
        // kafkaEventService.publishStudentStatusChangedEvent(student, oldStatus, newStatus);

        return StudentResponseDTO.from(student);
    }

    @Transactional
    public void deleteStudent(Long id, Long deletedBy) {
        log.info("Deletando estudante ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com ID: " + id));

        // Soft delete: apenas marcar como INACTIVE
        studentRepository.delete(student);

        // Publicar evento
        // kafkaEventService.publishStudentEvent("student.deleted", student);

        log.info("Estudante deletado (soft delete): {}", id);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private void validateDuplicates(StudentRequestDTO request) {
        if (studentRepository.existsByCpf(request.getCpf())) {
            throw new DuplicateStudentException("CPF já cadastrado: " + request.getCpf());
        }

        if (request.getRg() != null && studentRepository.existsByRg(request.getRg())) {
            throw new DuplicateStudentException("RG já cadastrado: " + request.getRg());
        }
    }

    private String generateRegistrationNumber() {
        // Formato: 2025001234 (ano + sequencial)
        int year = LocalDate.now().getYear();
        long count = studentRepository.count() + 1;
        return String.format("%d%06d", year, count);
    }

    private String generateTempPassword() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    private void updateStudentFields(Student student, StudentUpdateDTO request) {
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }
        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getCpf() != null) {
            student.setCpf(request.getCpf());
        }
        if (request.getRg() != null) {
            student.setRg(request.getRg());
        }
        if (request.getRgIssuer() != null) {
            student.setRgIssuer(request.getRgIssuer());
        }
        if (request.getRgIssueDate() != null) {
            student.setRgIssueDate(request.getRgIssueDate());
        }
    }
}