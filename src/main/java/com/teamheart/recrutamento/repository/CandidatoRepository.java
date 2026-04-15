package com.teamheart.recrutamento.repository;

import com.teamheart.recrutamento.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
