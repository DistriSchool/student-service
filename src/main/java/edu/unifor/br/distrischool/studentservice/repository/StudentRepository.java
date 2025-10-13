package edu.unifor.br.distrischool.studentservice.repository;

import edu.unifor.br.distrischool.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ========== BUSCA POR IDENTIFICADORES ÚNICOS ==========

    Optional<Student> findByRegistrationNumber(String registrationNumber);

    Optional<Student> findByCpf(String cpf);

    // ========== VERIFICAÇÃO DE EXISTÊNCIA ==========


    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);


}