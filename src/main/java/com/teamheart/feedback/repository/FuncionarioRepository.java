package com.teamheart.feedback.repository;

import com.teamheart.feedback.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FuncionarioRepository extends JpaRepository<Funcionario, UUID> {
    Optional<Funcionario> findById(UUID id);
}