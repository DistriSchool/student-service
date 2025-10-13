package edu.unifor.br.distrischool.studentservice.controller;

import edu.unifor.br.distrischool.studentservice.dto.StudentRequestDTO;
import edu.unifor.br.distrischool.studentservice.dto.StudentResponseDTO;
import edu.unifor.br.distrischool.studentservice.dto.StudentUpdateDTO;
import edu.unifor.br.distrischool.studentservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @RequestBody StudentRequestDTO request) {

        log.info("POST /api/students - Criando estudante: {}", request.getEmail());

        StudentResponseDTO response = studentService.createStudent(request, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {

        log.info("GET /api/students/{} - Buscando estudante", id);

        StudentResponseDTO response = studentService.getStudentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @RequestParam(required = false) String query,
            @PageableDefault(size = 20, sort = "fullName", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.info("GET /api/students - Listando todos os estudantes - Página: {}",
                pageable.getPageNumber());
        Page<StudentResponseDTO> response = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody @Valid StudentUpdateDTO request) {

        log.info("PUT /api/students/{} - Atualizando estudante", id);

        StudentResponseDTO response = studentService.updateStudent(id, request, null);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        log.info("DELETE /api/students/{} - Deletando estudante", id);

        studentService.deleteStudent(id, null);

        return ResponseEntity.noContent().build();
    }
}