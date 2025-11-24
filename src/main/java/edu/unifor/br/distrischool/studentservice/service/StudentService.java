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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final StudentKafkaEventService kafkaEventService;

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO request, Long createdBy) {
        log.info("Tentativa de criar estudante: {}", request.getEmail());

        validateDuplicates(request);

        Student student = Student.builder()
                .registrationNumber(generateRegistrationNumber())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .build();

        student = studentRepository.save(student);
        log.info("Estudante criado com ID temporário: {}", student.getId());

        try {
            String tempPassword = request.getPassword();
            if (tempPassword == null || tempPassword.isBlank()) {
                tempPassword = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 10);
            }

            UserEvent userEvent = UserEvent.builder()
                    .eventType("user.create")
                    .userId(student.getId())
                    .email(request.getEmail())
                    .name(request.getFullName())
                    .role("STUDENT")
                    .password(tempPassword)
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build();

            kafkaEventService.publishUserCreateEvent(userEvent);

            log.info("Estudante criado com sucesso: {} - Matrícula: {}",
                    student.getFullName(), student.getRegistrationNumber());

            return StudentResponseDTO.from(student);

        } catch (Exception e) {
            log.error("Erro ao criar usuário no Auth-Service. Revertendo criação do estudante.", e);
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

        if (request.getCpf() != null && !request.getCpf().equals(student.getCpf())) {
            if (studentRepository.existsByCpf(request.getCpf())) {
                throw new DuplicateStudentException("CPF já cadastrado: " + request.getCpf());
            }
        }

        updateStudentFields(student, request);

        student = studentRepository.save(student);

        log.info("Estudante atualizado com sucesso: {}", student.getId());

        return StudentResponseDTO.from(student);
    }


    @Transactional
    public void deleteStudent(Long id, Long deletedBy) {
        log.info("Deletando estudante ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado com ID: " + id));

        studentRepository.delete(student);


        log.info("Estudante deletado (soft delete): {}", id);
    }

    private void validateDuplicates(StudentRequestDTO request) {
        if (studentRepository.existsByCpf(request.getCpf())) {
            throw new DuplicateStudentException("CPF já cadastrado: " + request.getCpf());
        }
    }

    private String generateRegistrationNumber() {
        int year = LocalDate.now().getYear();
        long count = studentRepository.count() + 1;
        return String.format("%d%06d", year, count);
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
    }
}