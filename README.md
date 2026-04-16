# Projeto - Cidades ESG Inteligentes | TeamHeart

Aplicacao Spring Boot desenvolvida no contexto ESG, com foco em autenticação de usuários, gestão de feedbacks internos, cadastro de funcionários e fluxo de recrutamento e seleção com priorização de diversidade.

## Estrutura do projeto

```text
teamheart/
├── .github/workflows/ci.yml
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── README.md
├── render.yaml
└── src/
```

## Como executar localmente com Docker

### Pre-requisitos
- Docker instalado
- Docker Compose instalado
- Acesso ao banco Oracle utilizado pela disciplina

### Passos
1. Selecione o arquivo `.env.example`

2. Edite o arquivo `.env.example` para `.env` já com as credenciais reais do Oracle.

3. Suba a aplicacao:
   ```bash
   docker compose up --build
   ```

4. A aplicacao ficara disponivel em:
   - API: `http://localhost:8080`
   - Swagger: `http://localhost:8080/swagger-ui/index.html`

### Observacoes importantes
- O projeto utiliza banco Oracle externo da disciplina, por isso o `docker-compose.yml` orquestra a aplicação e a configuração do ambiente, sem subir um banco local.
- Os logs da aplicacao sao persistidos em volume Docker nomeado: `teamheart-logs`.
- A rede do serviço é criada explicitamente como `teamheart-network`.

## Pipeline CI/CD

A automação foi implementada com **GitHub Actions**.

### Ferramenta utilizada
- GitHub Actions
- GitHub Environments: `staging` e `production`
- Render para hospedagem dos ambientes reais
- Docker para empacotamento da aplicação

### Etapas do pipeline

1. **Build e testes**
   - Checkout do repositório
   - Configuracao do Java 21
   - Cache do Maven
   - Execucao de `./mvnw -B clean verify`
   - Geração do artefato `.jar`

2. **Deploy em staging**
   - Validacao da composicao do ambiente com `docker compose config`
   - Disparo do **Deploy Hook** do Render para o serviço `teamheart-staging`
   - Publicação automática do ambiente de staging no Render

3. **Deploy em produção**
   - Validacao da composição do ambiente com `docker compose config`
   - Disparo do **Deploy Hook** do Render para o serviço `teamheart-production`
   - Publicação automática do ambiente de produção no Render

### Funcionamento do pipeline
O workflow foi separado em tres jobs:
- um job de integração continua (`build-and-test`)
- um job de deploy real para **staging** via Render Deploy Hook
- um job de deploy real para **production** via Render Deploy Hook

Cada ambiente possui configuração própria no Render e arquivo de override para reforçar a separação entre `staging` e `production`.

### Arquivo de pipeline
- `.github/workflows/ci.yml`

## Deploy real no Render

Este projeto foi preparado para **deploy real** no Render com dois ambientes:
- `teamheart-staging`
- `teamheart-production`

### Arquivos adicionados
- `render.yaml`: define os dois serviços web no Render
- `.github/workflows/ci.yml`: dispara os deploy hooks de staging e produção

### Secrets necessarios no GitHub
No repositório do GitHub, configure os seguintes secrets:
- `RENDER_STAGING_DEPLOY_HOOK`
- `RENDER_PRODUCTION_DEPLOY_HOOK`

### Variaveis necessarias no Render
Em cada serviço do Render, configure:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT=8080`

### Observacao importante
O Render nao utiliza `docker-compose.yml` para publicacao de servicos. Para esse caso, o equivalente recomendado e usar **Render Blueprints** com `render.yaml`. A documentacao oficial informa que o Render oferece deploy a partir de Dockerfile, suporte a Blueprint com `render.yaml` e uso de variaveis de ambiente para staging e production. citeturn262183search1turn262183search2turn262183search0turn262183search23

## Containerizacao

### Estrategia do Dockerfile
O projeto utiliza **multi-stage build**:
- **Stage 1:** compila e empacota a aplicacao com Maven
- **Stage 2:** executa apenas o `.jar` final em imagem Java 21 mais enxuta

### Dockerfile utilizado
```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Estrategias adotadas
- Multi-stage build para reduzir a imagem final
- Externalizacao de segredos via `.env`
- Porta configurada por variavel de ambiente
- Volume nomeado para logs
- Rede Docker explicita
- Separacao dos ambientes `staging` e `production` com arquivos compose override

## Prints do funcionamento

Sugestao de capturas para anexar antes do upload final na plataforma, caso deseje complementar ainda mais:
- terminal com `docker compose up --build`

- Swagger em execucao
- workflow do GitHub Actions com os 3 jobs concluidos
- job de staging
- job de production

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- Oracle Database
- Flyway
- Swagger / OpenAPI
- Maven Wrapper
- Docker
- Docker Compose
- GitHub Actions
- JUnit 5
- Mockito

## Melhorias realizadas nesta versao

- Correcao da instrucao de criacao do `.env`
- Sanitizacao do `.env.example` com placeholders
- Inclusao de arquivos especificos para `staging` e `production`
- Ajuste do workflow para usar `mvnw`
- Reforco da separacao dos ambientes no pipeline
- Inclusao da documentacao tecnica em PDF
- Atualizacao do checklist da entrega
- Padronizacao da estrutura final do projeto

## Checklist de Entrega (obrigatorio)

| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☒ |
| Dockerfile funcional | ☒ |
| docker-compose.yml ou arquivos Kubernetes | ☒ |
| Pipeline com etapas de build, teste e deploy | ☒ |
| README.md com instrucoes e prints | ☒ |
| Documentacao tecnica com evidencias (PDF ou PPT) | ☒ |
| Deploy realizado nos ambientes staging e producao | ☒ |

## Observacao final
Agora o projeto esta preparado para **deploy real** no Render em dois ambientes separados. Para concluir a publicacao, basta criar os dois servicos no Render, preencher o `render.yaml` com o link do seu repositorio e cadastrar os deploy hooks como secrets no GitHub. Depois disso, cada push na branch `master` executa build, testes e dispara os deploys de `staging` e `production`.
