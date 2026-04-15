package com.teamheart.login.service;

import com.teamheart.login.entity.Usuario;
import com.teamheart.login.exception.EmailJaCadastradoException;
import com.teamheart.login.exception.UsuarioNaoEncontradoException;
import com.teamheart.login.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void deveCadastrarUsuarioComSenhaCriptografada() {
        when(usuarioRepository.existsByEmailIgnoreCase("ana@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.cadastrarUsuario("Ana", "ana@email.com", "123456");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario salvo = captor.getValue();
        assertEquals("Ana", salvo.getNome());
        assertEquals("ana@email.com", salvo.getEmail());
        assertEquals("senha-criptografada", salvo.getSenha());
    }

    @Test
    void deveImpedirCadastroComEmailDuplicado() {
        when(usuarioRepository.existsByEmailIgnoreCase("ana@email.com")).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
            () -> usuarioService.cadastrarUsuario("Ana", "ana@email.com", "123456"));
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorEmail("ana@email.com");

        assertEquals("ana@email.com", encontrado.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
            () -> usuarioService.buscarPorEmail("naoexiste@email.com"));
    }
}
