package com.teamheart.api;

import com.teamheart.login.entity.Usuario;
import com.teamheart.login.repository.UsuarioRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Admin TeamHeart");
        usuario.setEmail("admin@teamheart.com");
        usuario.setSenha(passwordEncoder.encode("123456"));

        usuarioRepository.save(usuario);
    }

    @Test
    void deveRealizarLoginComSucessoValidandoStatusBodyEContrato() {

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body("""
                    {
                      "nome": "Admin TeamHeart",
                      "email": "admin@teamheart.com",
                      "senha": "123456"
                    }
                    """)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath(
                "schemas/login-success-schema.json"
            ))
            .body("mensagem",
                equalTo("Login realizado com sucesso!"))
            .body("nomeUsuario",
                equalTo("Admin TeamHeart"));
    }

    @Test
    void deveRecusarLoginInvalidoValidandoStatusBodyEContrato() {

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body("""
                    {
                      "nome": "Admin TeamHeart",
                      "email": "admin@teamheart.com",
                      "senha": "senhaErrada"
                    }
                    """)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(400)
            .body(matchesJsonSchemaInClasspath(
                "schemas/error-schema.json"
            ))
            .body("status", equalTo(400))
            .body("message",
                containsString("Senha incorreta"));
    }
}

