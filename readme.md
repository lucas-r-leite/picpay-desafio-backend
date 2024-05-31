# Desafio Backend

Este é um projeto de backend desenvolvido em Spring Boot com integração ao banco de dados MariaDB e documentação com Swagger.

O repositório do desafio pode ser encontrado no link a seguir [repositório do desafio](https://github.com/PicPay/picpay-desafio-backend)

## Pré-requisitos

- Docker
- Docker Compose

## Estrutura Básica do Projeto

- `UserController.java`: Controlador responsável pelas operações relacionadas aos usuários(SignUp and GetUsers).
- `WalletController.java`: Controlador responsável pelas operações relacionadas às carteiras.

## Configuração

### Arquivo `.env`

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_NAME=nome_do_banco
DB_USER=usuario_do_banco
DB_PASSWORD=senha_do_banco
MYSQL_ROOT_PASSWORD=senha_root
```

Para faciliar o os teste, utilizei o usuário root. Sendo assim o DB_USER=root e DB_PASSWORD=MYSQL_ROOT_PASSWORD.

Caso deseje utilizar outro usuário, as seguintes linhas devem ser adicionadas ao arquivo docker-compose.yalm:

```
 MYSQL_USER: ${DB_USER}
 MYSQL_PASSWORD: ${DB_PASSWORD}
```

## Docker

Certifique-se de ter o Docker e o Docker Compose instalados na sua máquina, bem como o .env configurado com as variáveis de ambiente. Para construir e iniciar os containers, utilize os seguintes comandos:

```
docker-compose up -build
```

Este comando irá:

1. Construir a imagem do aplicativo Spring Boot a partir do Dockerfile.
2. Iniciar os containers do MariaDB e do aplicativo Spring Boo

## Swagger

A documentação da API pode ser acessada através do Swagger na seguinte URL:

```
http://localhost:8080/swagger-ui/index.html
```
