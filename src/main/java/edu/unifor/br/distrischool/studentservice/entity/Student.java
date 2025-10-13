package edu.unifor.br.distrischool.studentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 20)
    private String registrationNumber; // Matrícula

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(name = "rg_issuer", length = 50)
    private String rgIssuer; // Órgão emissor

    @Column(name = "rg_issue_date")
    private LocalDate rgIssueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy; // ID do usuário que criou

    @Column(name = "updated_by")
    private Long updatedBy; // ID do usuário que atualizou

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StudentStatus {
        PENDING,      // Pendente de aprovação/matrícula
        ACTIVE,       // Ativo/Matriculado
        INACTIVE,     // Inativo (trancado, suspenso)
        TRANSFERRED,  // Transferido
        GRADUATED,    // Formado
        EXPELLED,     // Expulso
        DROPPED_OUT   // Desistente
    }
}

// ============= EMBEDDABLE CLASSES =============

// ============= STUDENT DOCUMENT ENTITY =============

