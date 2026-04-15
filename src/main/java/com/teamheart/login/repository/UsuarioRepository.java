package com.teamheart.login.repository;

import com.teamheart.login.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
}
