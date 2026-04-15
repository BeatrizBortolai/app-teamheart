# Projeto - Cidades ESG Inteligentes | TeamHeart

Aplicacao Spring Boot desenvolvida no contexto ESG, com foco em autenticacao de usuarios, gestao de feedbacks internos, cadastro de funcionarios e fluxo de recrutamento e selecao com priorizacao de diversidade.

## Estrutura do projeto

```text
teamheart/
├── .github/workflows/ci.yml
├── Dockerfile
├── docker-compose.yml
├── docker-compose.staging.yml
├── docker-compose.production.yml
├── .env.example
├── .env.staging.example
├── .env.production.example
├── README.md
├── render.yaml
├── Documentacao_Tecnica_TeamHeart.pdf
└── src/
```

## Como executar localmente com Docker

### Pre-requisitos
- Docker instalado
- Docker Compose instalado
- Acesso ao banco Oracle utilizado pela disciplina

### Passos
1. Copie o arquivo de exemplo de variaveis de ambiente:
   ```bash
   cp .env.example .env.example
   ```

2. Edite o arquivo `.env` e preencha com suas credenciais reais do Oracle.

3. Suba a aplicacao:
   ```bash
   docker compose up --build
   ```

4. A aplicacao ficara disponivel em:
   - API: `http://localhost:8080`
   - Swagger: `http://localhost:8080/swagger-ui/index.html`

### Observacoes importantes
- O projeto utiliza banco Oracle externo da disciplina, por isso o `docker-compose.yml` orquestra a aplicacao e a configuracao do ambiente, sem subir um banco local.
- Os logs da aplicacao sao persistidos em volume Docker nomeado: `teamheart-logs`.
- A rede do servico e criada explicitamente como `teamheart-network`.

## Pipeline CI/CD

A automacao foi implementada com **GitHub Actions**.

### Ferramenta utilizada
- GitHub Actions
- GitHub Environments: `staging` e `production`
- Render para hospedagem dos ambientes reais
- Docker para empacotamento da aplicacao

### Etapas do pipeline

1. **Build e testes**
   - Checkout do repositorio
   - Configuracao do Java 21
   - Cache do Maven
   - Execucao de `./mvnw -B clean verify`
   - Geracao do artefato `.jar`

2. **Deploy em staging**
   - Validacao da composicao do ambiente com `docker compose config`
   - Disparo do **Deploy Hook** do Render para o servico `teamheart-staging`
   - Publicacao automatica do ambiente de staging no Render

3. **Deploy em producao**
   - Validacao da composicao do ambiente com `docker compose config`
   - Disparo do **Deploy Hook** do Render para o servico `teamheart-production`
   - Publicacao automatica do ambiente de producao no Render

### Funcionamento do pipeline
O workflow foi separado em tres jobs:
- um job de integracao continua (`build-and-test`)
- um job de deploy real para **staging** via Render Deploy Hook
- um job de deploy real para **production** via Render Deploy Hook

Cada ambiente possui configuracao propria no Render e arquivo de override para reforcar a separacao entre `staging` e `production`.

### Arquivo de pipeline
- `.github/workflows/ci.yml`

## Deploy real no Render

Este projeto foi preparado para **deploy real** no Render com dois ambientes:
- `teamheart-staging`
- `teamheart-production`

### Arquivos adicionados
- `render.yaml`: define os dois servicos web no Render
- `.github/workflows/ci.yml`: dispara os deploy hooks de staging e producao

### Secrets necessarios no GitHub
No repositorio do GitHub, configure os seguintes secrets:
- `RENDER_STAGING_DEPLOY_HOOK`
- `RENDER_PRODUCTION_DEPLOY_HOOK`

### Variaveis necessarias no Render
Em cada servico do Render, configure:
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

Para facilitar a entrega, este pacote ja inclui a documentacao tecnica em PDF:
- `Documentacao_Tecnica_TeamHeart.pdf`

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
