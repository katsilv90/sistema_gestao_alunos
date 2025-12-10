# Sistema de Gestão de Alunos

![JDK 21])
![Spring Boot]

Sistema web para gestão de **alunos**, **cursos** e **unidades curriculares (UCs)**, desenvolvido com **Spring Boot** e **Thymeleaf**, utilizando **HTML**, **CSS** e **JavaScript**.

---

## Estrutura do Projeto

### Controller
- `AlunoController`  
- `CursoController`  
- `HomeController`  
- `UnidadeCurricularController`  

### Model
- `AlunoModel`  
- `CursoModel`  
- `UnidadeCurricularModel`  

### Repository
- `AlunoRepository`  
- `CursoRepository`  
- `UnidadeCurricularRepository`  

### Service
- `AlunoService`  
- `CursoService`  
- `UnidadeCurricularService`  

---

## Funcionalidades
- Adicionar, listar, pesquisar e remover alunos, cursos e UCs.  
- Filtro instantâneo de alunos por nome.  
- Validação de formulários no frontend.  
- Mensagens de sucesso/erro (toasts) e popups de confirmação.  
- Responsivo para dispositivos móveis.  

---

## Tecnologias
- **Java 21**  
- Spring Boot  
- Thymeleaf  
- HTML, CSS, JavaScript  
- H2 Database (para testes)  

---

## Como Executar
1. Clonar o repositório:
   ```bash
   git clone <url-do-repositório>
Abrir numa IDE compatível (IntelliJ, Eclipse, etc.).

Certificar-se de que o JDK 21 está configurado.

Executar a aplicação Spring Boot (Application.java).

Aceder à aplicação em http://localhost:8080.

Observações
Utiliza fragments Thymeleaf para navbar, header e footer.

Cada aluno está associado a um curso e pode ter várias UCs.
