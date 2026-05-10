# 📱 Projeto Final Desenvolvimento Backend - PUCPR

## 📌 Descrição do Projeto

### Este projeto trata-se de uma API RESTful desenvolvida em Kotlin com Spring Boot. A principio ele executava apenas tarefas de manipulacao de usuarios e sessões. No contexto atual, ela realiza a gestão de uma loja de venda de peças para carros.

---

## 👨‍💻 Informações

* **Desenvolvedor:** Gustavo Alfredo
* **Universidade:** PUCPR - Curitiba
* **Pós-Graduação:** Desenvolvimento de Aplicativos Moveis
* **Curso:** Desenvolvimento de Backend
* **Data:** 05/26

---

## 🚀 Tecnologias


* Linguagem principal: Kotlin 1.9+
* Framework base: Spring Boot
* Endpoints REST: Spring Web
* Autenticação e autorização: Spring Security
* Acesso ao banco de dados: Spring Data JPA
* ORM (mapeamento objeto-relacional): Hibernate
* Banco de dados em memória: H2 Database
* Geração e validação de tokens: JWT
* Validação de dados de entrada: Jakarta Validation
* Documentação interativa da API: Springdoc / Swagger UI

---

## 🏗️ Arquitetura

### 🧱 MCV

Foi utilizada a arquitetura MVC(Model-View-Controller) seguindo o padrao inicial do projeto.

## 🎥 Vídeo Explicativo

📼 [Clique Aqui](https://youtu.be/8UjdyZzX_fA)

---

## ▶️ Como Executar o Projeto

### 📦 Pré-requisitos

* **JDK 17** ou superior — [Download](https://www.oracle.com/br/java/technologies/downloads/)
* **Gradle** (ou use o wrapper `./gradlew` incluso no projeto)
* **Git** — [Download](https://git-scm.com/)
* **IDE**: **IntelliJ IDEA** (recomendada) ou **VS Code** com extensão Kotlin

---

### 🔧 Passos para Instalação e execução

1. Clone o repositório:

```bash
git clone https://github.com/Gustavocorrea0/projeto-final-desenvolvimento-backend-pucpr.git
```

2. Acesse a pasta do projeto:

```bash
cd authserver
```

3. Instale as dependências(Linux/Windows):

```bash
./gradlew dependencies
```
```bash
gradlew.bat dependencies
```
---

### 📦 Iniciar Projeto

1. Via terminal (Gradle Wrapper):

```bash
./gradlew bootRun
```

2. Via IntelliJ IDEA:

* Abra o projeto: `File → Open → selecione a pasta raiz`
* Aguarde o IntelliJ indexar e baixar as dependências
* Localize a classe `AuthserverApplication.kt`
* Clique no ícone ▶ ao lado da função `main` e selecione **Run**

Após iniciar o API estará disponível em:
```
http://localhost:8080
```

---

### 🔧 Endpoints disponíveis

---
### Autenticação

| Método | Endpoint 
|---|---|
| `POST` | `/users/login` |
---
### Usuarios (users)


| Método | Endpoint 
|---|---|
| `POST` | `/users` |
| `GET` | `/users` | 
| `GET` | `/users/{id}` | 
| `PATCH` | `/users/{id}` |
| `PUT` | `/users/{id}/roles/{role}` | 
| `DELETE` | `/users/{id}` |

---
### Roles (roles)

| Método | Endpoint 
|---|---|
| `POST` | `/users` |

---
### Clientes (clients)

| Método   | Endpoint        
|----------|-----------------|
| `POST`   | `/clients`      |
| `GET`    | `/clients`      |
| `GET`    | `/clients/{id}` |
| `PATCH`  | `/users/{id}`   |
| `DELETE` | `/users/{id}`   |

---
### Peças (parts)

| Método   | Endpoint          
|----------|-------------------|
| `POST`   | `/parts`          |
| `GET`    | `/parts`          |
| `GET`    | `/parts/{id}`     |
| `PATCH`  | `/parts/{id}`     |
| `PATCH`  | `/parts/add/{id}` |
| `DELETE` | `/parts/{id}`     |

---
### Vendas (sales)

| Método  | Endpoint      
|---------|---------------|
| `POST`  | `/sales`      |
| `GET`   | `/sales`      |
| `GET`   | `/sales/{id}` |
