package com.teamheart.login.service;

import com.teamheart.login.entity.Usuario;
import com.teamheart.login.exception.EmailJaCadastradoException;
import com.teamheart.login.exception.UsuarioNaoEncontradoException;
import com.teamheart.login.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrarUsuario(String nome, String email, String senha) {
        log.info("Cadastrando novo usuário: {}", email);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailJaCadastradoException("Já existe um usuário cadastrado com o e-mail informado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        log.info("Buscando usuário pelo e-mail: {}", email);

        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }
}
