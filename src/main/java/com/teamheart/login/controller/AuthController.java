package com.teamheart.login.controller;

import com.teamheart.login.dto.LoginRequest;
import com.teamheart.login.dto.LoginResponse;
import com.teamheart.login.entity.Usuario;
import com.teamheart.login.exception.UsuarioNaoEncontradoException;
import com.teamheart.login.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de Registro (Sign Up) e Login (Sign In) de Usuários")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuário", description =
        "Autentica um usuário usando email e senha, verificando a credencial criptografada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido."),
        @ApiResponse(responseCode = "400", description = "Credenciais inválidas."),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Tentando login para o email: {}", request.email());

        Usuario usuario = usuarioService.buscarPorEmail(request.email());

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            logger.warn("Senha incorreta para o usuário: {}", request.email());
            throw new IllegalArgumentException("Senha incorreta.");
        }

        logger.info("Login bem-sucedido para o usuário: {}", usuario.getEmail());
        return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", usuario.getNome()));
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário",
        description = "Cria um novo usuário no banco de dados, criptografando a senha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Erro de validação dos campos."),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado.")
    })
    public ResponseEntity<String> register(@Valid @RequestBody LoginRequest request) {
        logger.info("Registrando novo usuário: {}", request.email());

        usuarioService.cadastrarUsuario(request.nome(), request.email(), request.senha());

        logger.info("Usuário registrado com sucesso: {}", request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
    }
}
