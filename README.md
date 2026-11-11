# Linear Programming

![Java 21](https://img.shields.io/badge/Java-21-blue)
![Spring Boot 4.1.0](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)
![Thymeleaf](https://img.shields.io/badge/Template-Thymeleaf-005F0F)
![TailwindCSS](https://img.shields.io/badge/Style-TailwindCSS-06B6D4)

---

## 📋 Visão Geral

O **Linear Programming** é uma plataforma web voltada para a resolução, visualização e análise passo a
passo de algoritmos de programação linear e problema de transporte.

O projeto é fruto do processo de refatoração de uma aplicação legada baseada em linha de comando (CLI)
para uma arquitetura web MVC moderna, escalável e amigável. A aplicação permite que os usuários insiram
parâmetros de entrada dinâmicos e acompanhem o histórico detalhado das iterações geradas por cada método
de resolução.

---

## 🧮 Algoritmos Suportados

* **Simplex:** Algoritmo para resolver problemas de programação linear com o Método Simplex.
* **Transporte:** [Em desenvolvimento].

---

## 🏛 Arquitetura e Decisões de Design

* **Padrão Strategy (Solver<P, R>):** Utiliza Generics para tratar cada algoritmo como uma estratégia de
cálculo isolada. Permite adicionar novos algoritmos sem alterar o código existente (Open/Closed Principle).
* **Componentes Stateless (Thread-Safe):** Os motores de cálculo (Solvers) não possuem estado mutável.
Os dados de entrada são isolados no objeto Problem e o resultado/histórico no Result,garantindo segurança
total para requisições concorrentes.
* **Form Binding Pragmático (KISS):** Ausência de DTOs separados por não haver persistência em banco de
dados ou dados sensíveis. Os próprios objetos de domínio (Problem e Result) trafegam entre as views do
Thymeleaf e os Solvers.

---

## 📂 Estrutura do Projeto

```text
LinearProgramming/
├── src/main/java/com/lutavafs/linearprogramming
│   ├── controller/      # WebController para as rotas do Thymeleaf
│   ├── domain/
│   │   ├── simplex/     # Models e Enums (StatusResult, SimplexProblem, Tableau)
│   │   └── transport/   # Models depreciados do algoritmo de transporte
│   ├── exception/       # Excessões personalizadas
│   ├── handler/         # Tratamento Global de Exceções (GlobalExceptionHandler)
│   ├── solver/          # Interface e suas implementações (SimplexSolver)
│   └── util/            # Utilitários para formatação matemática
├── src/main/resources/
│   ├── static/          # Arquivos estáticos (CSS, JS)
│   └── templates/       # Templates HTML do Thymeleaf (index, simplex-result)
└── src/test/            # Testes dos Solvers e MockMvc dos Controllers
```

---

## ⚡ Como Executar

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas no seu ambiente:
* **Java 21** (recomenda-se fortemente a utilização da distribuição Temurin ou OpenJDK)
* **Maven 3.9+** (para o gerenciamento completo das dependências)

### Passo a Passo

```bash
# 1. Clonar o repositório
git clone https://github.com/Lutava2245/LinearProgramming.git
cd LinearProgramming

# 2. Executar os testes unitários e de integração
mvn clean test

# 3. Subir a aplicação
mvn spring-boot:run
```

**Acesse no navegador: http://localhost:8080**