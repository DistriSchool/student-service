-- V1__create_students_tables.sql

CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          registration_number VARCHAR(20) NOT NULL UNIQUE,
                          full_name VARCHAR(150) NOT NULL,
                          date_of_birth DATE NOT NULL,
                          cpf VARCHAR(11) NOT NULL UNIQUE,
                          rg VARCHAR(20),
                          rg_issuer VARCHAR(50),
                          rg_issue_date DATE,
    -- Audit
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_by BIGINT,
                          updated_by BIGINT
);
