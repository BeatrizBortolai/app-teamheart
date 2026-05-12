package com.teamheart.steps;

import com.teamheart.login.entity.Usuario;
import com.teamheart.login.repository.UsuarioRepository;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class ApiSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Response response;
    private String requestBody;

    @Before
    public void configurarAmbiente() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Dado("que possuo credenciais validas")
    public void credenciaisValidas() {

        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Admin TeamHeart");
        usuario.setEmail("admin@teamheart.com");
        usuario.setSenha(passwordEncoder.encode("123456"));

        usuarioRepository.save(usuario);

        requestBody = """
            {
              "nome": "Admin TeamHeart",
              "email": "admin@teamheart.com",
              "senha": "123456"
            }
            """;
    }

    @Dado("que possuo credenciais invalidas")
    public void credenciaisInvalidas() {

        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Admin TeamHeart");
        usuario.setEmail("admin@teamheart.com");
        usuario.setSenha(passwordEncoder.encode("123456"));

        usuarioRepository.save(usuario);

        requestBody = """
            {
              "nome": "Admin TeamHeart",
              "email": "admin@teamheart.com",
              "senha": "senhaErrada"
            }
            """;
    }

    @Dado("que nao existe candidato cadastrado com o ID informado")
    public void candidatoNaoExiste() {

    }

    @Quando("envio uma requisicao POST para {string}")
    public void envioPost(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Quando("envio uma requisicao GET para {string}")
    public void envioGet(String endpoint) {

        response = RestAssured
            .given()
            .accept(ContentType.JSON)
            .when()
            .get(endpoint);
    }

    @Então("o sistema deve retornar status code {int}")
    public void validarStatusCode(Integer statusCode) {

        response.then().statusCode(statusCode);
    }

    @Então("o corpo da resposta deve conter a mensagem de login realizado")
    public void validarLoginComSucesso() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/login-success-schema.json"))
            .body("mensagem", equalTo("Login realizado com sucesso!"))
            .body("nomeUsuario", equalTo("Admin TeamHeart"));
    }

    @Então("o corpo da resposta deve conter mensagem de erro")
    public void validarErro() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/error-schema.json"))
            .body("status", equalTo(400))
            .body("message", containsString("Senha incorreta"));
    }

    @Então("o corpo da resposta deve conter mensagem de candidato nao encontrado")
    public void validarCandidatoNaoEncontrado() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/error-schema.json"))
            .body("status", equalTo(404))
            .body("message", matchesPattern("(?i).*n[ãa]o encontrado.*"));
    }

    @Dado("que possuo dados validos de um candidato")
    public void dadosValidosCandidato() {

        requestBody = """
            {
              "nome": "Joao Silva",
              "email": "joao.silva@email.com",
              "genero": "Masculino",
              "etnia": "Pardo",
              "localizacao": "Sao Paulo, SP",
              "experienciaAnos": 5
            }
            """;
    }

    @Dado("que possuo dados validos para registro")
    public void dadosValidosRegistro() {

        usuarioRepository.deleteAll();

        requestBody = """
            {
              "nome": "Novo Usuario",
              "email": "novo.usuario@email.com",
              "senha": "SenhaForte123"
            }
            """;
    }

    @Dado("que ja existe um usuario cadastrado com email \"admin@teamheart.com\"")
    public void usuarioJaExiste() {

        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Admin TeamHeart");
        usuario.setEmail("admin@teamheart.com");
        usuario.setSenha(passwordEncoder.encode("123456"));

        usuarioRepository.save(usuario);

        requestBody = """
            {
              "nome": "Outro Usuario",
              "email": "admin@teamheart.com",
              "senha": "123456"
            }
            """;
    }

    @Dado("que ja existe um candidato cadastrado com email \"candidato@email.com\"")
    public void candidatoJaExiste() {

        requestBody = """
            {
              "nome": "Candidato Duplicado",
              "email": "candidato@email.com",
              "genero": "Feminino",
              "etnia": "Branco",
              "localizacao": "Rio de Janeiro, RJ",
              "experienciaAnos": 3
            }
            """;
    }

    @Quando("envio uma requisicao POST para \"{string}\" com os dados do candidato")
    public void envioPostCandidato(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Quando("envio uma requisicao POST para \"{string}\" com os dados do usuario")
    public void envioPostUsuario(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Quando("envio uma requisicao POST para \"{string}\" com email duplicado")
    public void envioPostEmailDuplicado(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Então("o corpo da resposta deve conter os dados do candidato cadastrado")
    public void validarCandidatoCadastrado() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/candidato-schema.json"))
            .body("nome", equalTo("Joao Silva"))
            .body("email", equalTo("joao.silva@email.com"));
    }

    @Então("o corpo da resposta deve conter mensagem de sucesso")
    public void validarMensagemSucesso() {

        response.then()
            .body("message", containsString("sucesso"));
    }

    @Então("o corpo da resposta deve conter mensagem de conflito")
    public void validarMensagemConflito() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/error-schema.json"))
            .body("status", equalTo(409))
            .body("message", containsString("conflict"));
    }

    @Dado("que existem vagas cadastradas na plataforma")
    public void vagasExistem() {

    }

    @Dado("que possuo dados validos de um processo de selecao")
    public void dadosValidosSelecao() {

        requestBody = """
            {
              "statusSelecao": "ABERTA",
              "dataInicio": "2026-05-15",
              "dataFim": "2026-06-15",
              "vagaId": 1
            }
            """;
    }

    @Quando("envio uma requisicao POST para \"{string}\" com os dados da selecao")
    public void envioPostSelecao(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Então("o corpo da resposta deve conter uma lista de vagas")
    public void validarListaVagas() {

        response.then()
            .statusCode(200)
            .body("size()", org.hamcrest.Matchers.greaterThanOrEqualTo(0));
    }

    @Então("o corpo da resposta deve conter os dados da selecao criada")
    public void validarSelecaoCriada() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/selecao-schema.json"))
            .body("statusSelecao", equalTo("ABERTA"));
    }

    @Dado("que existem funcionarios cadastrados na plataforma")
    public void funcionariosExistem() {

    }

    @Dado("que possuo dados validos de um feedback")
    public void dadosValidosFeedback() {

        requestBody = """
            {
              "titulo": "Feedback Positivo",
              "descricao": "Excelente desempenho no projeto ESG",
              "funcionarioId": 1,
              "classificacao": "POSITIVO"
            }
            """;
    }

    @Quando("envio uma requisicao POST para \"{string}\" com os dados do feedback")
    public void envioPostFeedback(String endpoint) {

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(endpoint);
    }

    @Então("o corpo da resposta deve conter uma lista de funcionarios")
    public void validarListaFuncionarios() {

        response.then()
            .statusCode(200)
            .body("size()", org.hamcrest.Matchers.greaterThanOrEqualTo(0));
    }

    @Então("o corpo da resposta deve conter os dados do feedback registrado")
    public void validarFeedbackRegistrado() {

        response.then()
            .body(matchesJsonSchemaInClasspath("schemas/feedback-schema.json"))
            .body("titulo", equalTo("Feedback Positivo"));
    }
}
