# 📦 Simplified Inventory Management

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

Uma API REST desenvolvida em **Java + Spring Boot** para gerenciamento completo de estoque. O sistema permite o controle de categorias, produtos, variações de produtos (cores, tamanhos, etc.), upload de imagens para a nuvem e registro detalhado de movimentações (entradas e saídas).

## ✨ Funcionalidades

- **Gestão de Categorias:** Criação, listagem, atualização e remoção de categorias de produtos.
- **Gestão de Produtos e Variações:** Cadastro de produtos pai e suas respectivas variações físicas, permitindo controle granular do estoque.
- **Upload de Fotos:** Integração nativa com **AWS S3** para armazenamento e recuperação de fotos das variações dos produtos.
- **Movimentações de Estoque:** Registro de fluxo de mercadorias (Entradas e Saídas) garantindo a rastreabilidade (Audit).
- **Filtros Avançados:** Consultas dinâmicas utilizando *Spring Data JPA Specifications* para produtos e movimentações.
- **Respostas Padronizadas:** Utilização de DTOs, paginação customizada e tratamento global de exceções (Global Exception Handler).
- **Mapeamento de Objetos:** Integração com *MapStruct* para conversão ágil e segura entre Entidades e DTOs.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java
* **Framework:** Spring Boot
* **Persistência de Dados:** Spring Data JPA / Hibernate
* **Banco de Dados:** H2 Database
* **Migrações:** Flyway ou Liquibase (Gerenciamento no diretório `db/migration`)
* **Mapeamento de Objetos:** MapStruct (Conversão entre Entidades e DTOs)
* **Storage:** Amazon S3 (AWS SDK)
* **Documentação:** OpenAPI 3 / Swagger UI
* **Gerenciador de Dependências:** Maven

## 📍 Endpoints Principais

Abaixo estão os principais endpoints da aplicação. A documentação interativa e completa (com exemplos de JSON) está disponível no Swagger.

### 🏷️ Categorias
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/categories` | Lista todas as categorias (suporta paginação). |
| `GET` | `/categories/{id}` | Busca os detalhes de uma categoria específica. |
| `POST` | `/categories` | Cadastra uma nova categoria. |
| `PUT` | `/categories/{id}` | Atualiza os dados de uma categoria existente. |
| `DELETE` | `/categories/{id}` | Desativa uma categoria (Soft-delete). |

### 📦 Produtos e Variações
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/products` | Lista os produtos (suporta paginação e filtros dinâmicos). |
| `GET` | `/products/{id}` | Retorna as informações de um produto específico. |
| `GET` | `/products/variants/{id}` | Detalha uma variação (cor, tamanho, foto, etc). |
| `POST` | `/products` | Cria um novo produto base. |
| `POST` | `/products/variants` | Adiciona uma nova variação a um produto. |
| `PUT` | `/products/{id}` | Atualiza um produto. |
| `PUT` | `/products/variants/{id}` | Atualiza os dados da variação. |
| `PATCH` | `/products/variants/{id}/photos`| Faz o upload de uma imagem (via AWS S3) para a variação. |
| `DELETE` | `/products/variants/{id}/photos`| Faz a remoção de uma imagem (via AWS S3) para a variação. |
| `DELETE` | `/products/{id}` | Desativa um produto do sistema. (Soft-delete) |
| `DELETE` | `/products/variants/{id}` | Desativa uma variação de produto. (Soft-delete) |

### 🔄 Movimentações (Entradas e Saídas)
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/movements` | Lista o histórico de movimentações (suporta paginação e filtros). |
| `POST` | `/movements` | Registra uma nova movimentação (`INPUT` ou `OUTPUT`) no estoque. |

*(Nota: Os caminhos exatos dos endpoints podem conter prefixos como `/api/v1` dependendo da sua configuração base).*

## 📁 Estrutura do Projeto

A arquitetura do projeto segue o padrão em camadas (Layered Architecture):

```text
src/main/java/com/leo/estoque_api/
├── config/       # Configurações do sistema (Swagger, AWS S3)
├── controller/   # Endpoints REST (Categorias, Produtos, Movimentações)
├── dto/          # Objetos de Transferência de Dados e Mappers
├── exceptions/   # Tratamento customizado de erros globais e regras de negócio
├── infra/        # Serviços de infraestrutura externa (ex: Integração AWS S3)
├── model/        # Entidades de domínio (JPA) e Enums
├── repository/   # Interfaces do Spring Data JPA e Specifications
└── service/      # Regras de negócio e casos de uso
```

## 🛠️ Como Executar Localmente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- Banco de Dados (PostgreSQL/MySQL configurado localmente ou via Docker)
- Credenciais da AWS (Para uso do S3)

### Passos para rodar

1. **Clone o repositório:**

```bash
git clone [https://github.com/SEU_USUARIO/estoque-api.git](https://github.com/SEU_USUARIO/estoque-api.git)
cd estoque-api
```

### Passos para rodar

1. **Clone o repositório:**

```bash
git clone [https://github.com/SEU_USUARIO/estoque-api.git](https://github.com/SEU_USUARIO/estoque-api.git)
cd estoque-api
```

2. **Configure as Variáveis de Ambiente:**
   Renomeie ou edite o arquivo `src/main/resources/application.properties` com as credenciais do seu banco de dados e da AWS S3:

```properties
# Configurações do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
spring.datasource.username=seu_user
spring.datasource.password=sua_senha

# Configurações AWS S3
aws.s3.bucket-name=seu-bucket-name
aws.region=sa-east-1
aws.access-key-id=SUA_ACCESS_KEY
aws.secret-access-key=SUA_SECRET_KEY
```

3. **Compile e baixe as dependências:**

```bash
./mvnw clean install
```

4. **Inicie a aplicação:**

```bash
./mvnw spring-boot:run
```

*A API estará rodando em `http://localhost:8080*`

## 📚 Documentação da API (Swagger)

A API está totalmente documentada utilizando o padrão OpenAPI. Para testar os endpoints, visualizar os schemas de *Request* e *Response* e verificar os códigos de status HTTP, acesse:

**Acesso Local:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html?utm_source=gemini)

Aqui está o trecho atualizado, trocando a seção de Contribuição pelas **Próximas Atualizações**. Adicionei uns emojis e formatação em lista para deixar o visual bem profissional no seu repositório:

## 🛠️ Como Executar Localmente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- Banco de Dados (PostgreSQL/MySQL configurado localmente ou via Docker)
- Credenciais da AWS (Para uso do S3)

### Passos para rodar

1. **Clone o repositório:**

```bash
git clone [https://github.com/SEU_USUARIO/estoque-api.git](https://github.com/SEU_USUARIO/estoque-api.git)
cd estoque-api
```

2. **Configure as Variáveis de Ambiente:**
   Renomeie ou edite o arquivo `src/main/resources/application.properties` com as credenciais do seu banco de dados e da AWS S3:

```properties
# Configurações do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
spring.datasource.username=seu_user
spring.datasource.password=sua_senha

# Configurações AWS S3
aws.s3.bucket-name=seu-bucket-name
aws.region=sa-east-1
aws.access-key-id=SUA_ACCESS_KEY
aws.secret-access-key=SUA_SECRET_KEY

```

3. **Compile e baixe as dependências:**

```bash
./mvnw clean install

```

4. **Inicie a aplicação:**

```bash
./mvnw spring-boot:run

```

*A API estará rodando em `http://localhost:8080*`

## 📚 Documentação da API (Swagger)

A API está totalmente documentada utilizando o padrão OpenAPI. Para testar os endpoints, visualizar os schemas de *Request* e *Response* e verificar os códigos de status HTTP, acesse:

**Acesso Local:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html?utm_source=gemini)

## 🔜 Próximas Atualizações

* [ ] Configuração do CORS
* [ ] Implementação de Segurança com Spring Security + JWT
* [ ] Cobertura de Testes Unitários

## 📝 Licença

Este projeto está sob a licença [MIT](https://choosealicense.com/licenses/mit/?utm_source=gemini). Sinta-se livre para usá-lo e modificá-lo.

```

```
