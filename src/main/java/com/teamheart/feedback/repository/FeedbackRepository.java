package com.teamheart.feedback.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.teamheart.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    @Query("SELECT f FROM Feedback f JOIN FETCH f.funcionario")
    List<Feedback> findAllWithFuncionario();
}
