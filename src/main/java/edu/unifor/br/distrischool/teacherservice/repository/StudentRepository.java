package edu.unifor.br.distrischool.teacherservice.repository;

import edu.unifor.br.distrischool.teacherservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegistrationNumber(String registrationNumber);

    Optional<Student> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

}