
# Spring Workout Api

Esse projeto foi desenvolvido para ajudar usuarios a catalogarem os seus treinos na academia, registrarem as sua evolução de peso e acompanharem o seu progresso ao longo prazo. O sistema permite organizar seus treinos diarios(Tipo do Treino, repetiçoes, sets e etc), e também trazer estatisticas sobre a sua evolução de peso ao longo do tempo.


## Stack utilizada

**Back-end:** Java 21, Spring Boot 4.0.0, Mysql, Flyway, Lombok, Spring doc, actuator, Junit, Mockito, Token JWT, auth0 e Docker.


## Rodando localmente

#### 1- Clonar o Repositório
- Abra seu terminal ou prompt de comando e clone o projeto com o seguinte comando:

```bash
  git clone https://github.com/PedroUchoa/Spring-Workout-Project
```

#### 2- Importação, Execução e Compilação do o Projeto na IDE

- Importe o projeto em sua IDE como um projeto Maven.

- Deixe que a IDE resolva e baixe todas as dependências do pom.xml.

```bash
  mvn clean package
```

- Rode o comando Maven para compilar o projeto antes da criação do container.

#### 3- Rodando no Docker
- Rode o comando docker para criação do container e uso da aplicação e banco de dados no docker. 

```bash
  docker-compose up --build
```

#### 4- Ver a documentação

- Acesse o caminho correto para acessar a documentação

```
http://localhost:8080/swagger-ui.html
```

#### 5- Sobre Migrations
- Para facilitar o uso do projeto as proprias migrations geram o banco de Dados e também povoam o mesmo com um user de teste e alguns exercicios.

- Credentcias do User de Teste -> Email: admin@email.com e Password: admin

## Screenshots

<img width="493" height="791" alt="Captura de tela 2025-12-06 185134" src="https://github.com/user-attachments/assets/f4cf9cff-a1fb-4213-b34b-88985d301188" />

## Funcionalidades

- Criação de Usuarios:
- Monitoramento de Peso:
- Estatisticas de Peso 
- Catalogo de Treinos
- Catalogo de Exercicios
- Autenticação e Segurança


## Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  mvn test
```

