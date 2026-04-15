package com.teamheart.recrutamento.repository;

import com.teamheart.recrutamento.entity.Selecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
}
