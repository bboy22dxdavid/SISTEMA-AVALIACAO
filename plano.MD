# Plano de Projeto: Sistema de Avaliação e Nivelamento de Graduados

Este documento apresenta o plano estrutural para o desenvolvimento da aplicação web baseada nos requisitos fornecidos, utilizando **Java 21 com Spring Boot (Maven)** no back-end, **Angular** no front-end, banco de dados **PostgreSQL** e **Docker** para containerização.

---

## 1. Esboço do Layout (Front-end)

Com base nas especificações de telas, o layout será responsivo, baseado em um componente global de estrutura (Header/Sidebar, Main Container, Footer) e navegação via rotas.

### 1.1. Estrutura Global das Telas (Wireframe Conceitual)
```
+-------------------------------------------------------------------------+
| [LOGO] (Home)             Menu 1  |  Menu 2  |  Menu 3       [PERFIL V] | -> HEADER / TOOLBAR
+-------------------------------------------------------------------------+
|                                                                         |
|  [ CONTAINER CENTRAL ]                                                  |
|  (Conteúdo Dinâmico injetado via <router-outlet>)                       |
|                                                                         |
+-------------------------------------------------------------------------+
| [LOGO] Titulo do Sistema / Rodapé                                       | -> FOOTER
+-------------------------------------------------------------------------+
```

### 1.2. Fluxo e Elementos por Tela
*   **Tela Inicial (Ranking):**
    *   **Título:** "Sistema de Avaliação e Nivelamento de Graduados".
    *   **Filtros:** Botão/campo de pesquisa por militares.
    *   **Tabela de Ranking:** Colunas (`Foto` | `Nome` | `Pontuação Total` | `Ação`). O botão de ação redireciona o usuário para a tela de avaliação correspondente. Ordenação nativa decrescente pela pontuação mais alta.
*   **Tela de Login:**
    *   Formulário centralizado contendo os campos: `username` e `password`.
*   **Tela de Cadastro de Usuário (Avaliadores/Militares):**
    *   Formulário completo com os campos: `id`, `saram`, `nome de guerra`, `password`, `seção`, `cod_posto`, `posto`, `quadro`, `função`, `antiguidade`, `ativo`.
*   **Tela de Avaliação e Nivelamento:**
    *   Barra de pesquisa para vincular e buscar o militar avaliado.
    *   Tabela/Formulário dinâmico listando os 15 conceitos de avaliação (com inputs de nota de 0 a 10).
    *   Campos de texto adicionais para: `justificativa`, `conceito moral` e `conceito profissional`.
    *   *Modo Nivelamento (Comando):* Permite herdar as notas do avaliador original, alterar os valores se necessário e exige obrigatoriamente o preenchimento do campo `justificativa` da alteração.

---

## 2. Modelagem e Relacionamento do Banco de Dados (PostgreSQL)

O banco de dados será estruturado de forma relacional para garantir a integridade referencial. As tabelas de `Avaliacao` e `Nivelamento` se conectam aos usuários e ao processo vigente.

### 2.1. Diagrama de Relacionamento (Mer)
```
  [Processo] (1) <------- (N) [Avaliacao] (N) -------> (1) [Avaliados]
                                  |
                                  | (1)
                                  v
                            (1) [Nivelamento]
                                  |
                                  +-----> (1) [Usuarios] (Comando)
  
  [Usuarios] (N) <-------> (N) [Roles]
```

### 2.2. Detalhamento das Tabelas e Chaves
1.  **Tabela `tb_users` (Avaliadores / Administradores)**
    *   `id` (PK - SERIAL/BIGINT)
    *   `username` (VARCHAR, UNIQUE)
    *   `password` (VARCHAR)
2.  **Tabela `tb_roles` (Papéis de Acesso)**
    *   `id` (PK - SERIAL)
    *   `role` (VARCHAR - ex: 'ROLE_AVALIADOR', 'ROLE_COMANDO', 'ROLE_ADMIN')
3.  **Tabela `tb_users_roles` (Associativa N:N)**
    *   `user_id` (FK -> `tb_users.id`, PK composta)
    *   `role_id` (FK -> `tb_roles.id`, PK composta)
4.  **Tabela `tb_avaliados` (Dados Militares dos Avaliados)**
    *   `id` (PK - SERIAL/BIGINT)
    *   `saram` (VARCHAR, UNIQUE)
    *   `nguerra` (VARCHAR)
    *   `secao` (VARCHAR)
    *   `cod_posto` (VARCHAR)
    *   `posto` (VARCHAR)
    *   `quadro` (VARCHAR)
    *   `funcao` (VARCHAR)
    *   `antiguidade` (INTEGER)
    *   `ativo` (BOOLEAN)
5.  **Tabela `tb_processos` (Ano de Vigência)**
    *   `id` (PK - SERIAL)
    *   `ano` (INTEGER, UNIQUE)
6.  **Tabela `tb_avaliacoes` (Notas do Avaliador)**
    *   `id` (PK - SERIAL/BIGINT)
    *   `avaliado_id` (FK -> `tb_avaliados.id`)
    *   `avaliador_id` (FK -> `tb_users.id`)
    *   `processo_id` (FK -> `tb_processos.id`)
    *   *Campos de Conceito (NUMERIC/INT):* `qualidade_trabalho`, `produtividade_trabalho`, `conhecimento_profissional`, `eficiencia_trabalho`, `meios_materiais`, `planejamento`, `julgamento`, `responsabilidade`, `disciplina`, `apresentacao_pessoal`, `lideranca`, `iniciativa`, `adaptabilidade`, `comunicacao_oral_escrita`, `relacionamento_ambiente_trabalho`.
    *   *Campos de Texto:* `justificativa`, `conceito_moral`, `conceito_profissional`.
7.  **Tabela `tb_nivelamentos` (Revisão pelo Comando)**
    *   `id` (PK - SERIAL/BIGINT)
    *   `avaliacao_id` (FK -> `tb_avaliacoes.id`, UNIQUE)
    *   `comando_id` (FK -> `tb_users.id`)
    *   *Campos de Conceito Reavaliados (NUMERIC/INT):* (Mesma estrutura de conceitos da avaliação para permitir modificações).
    *   *Campos de Texto:* `justificativa` (Obrigatória se houver alteração de nota), `conceito_moral`, `conceito_profissional`.

---

## 3. Estrutura do Projeto em Camadas

### 3.1. Back-end (Java 21 + Spring Boot + Maven)

A arquitetura seguirá o padrão clássico multicamadas (Layered Architecture), separando as responsabilidades de entrada, lógica de negócio, persistência de dados e segurança.

```
sistema-avaliacao-backend/
│
├── pom.xml                               # Configurações de dependências Maven
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── militar/
        │           └── avaliacao/
        │               ├── SistemaAvaliacaoApplication.java  # Classe Principal
        │               │
        │               ├── config/       # Configurações gerais (CORS, Swagger, Docker Profile)
        │               │   └── SecurityConfig.java
        │               │
        │               ├── controller/   # Camada de Endpoints REST (Exposição da API)
        │               │   ├── UserController.java
        │               │   ├── AvaliacaoController.java
        │               │   ├── NivelamentoController.java
        │               │   ├── ProcessoController.java
        │               │   └── RoleController.java
        │               │
        │               ├── service/      # Camada de Negócios (Regras, validação de notas e justificativas)
        │               │   ├── UserService.java
        │               │   ├── AvaliacaoService.java
        │               │   ├── NivelamentoService.java
        │               │   └── ProcessoService.java
        │               │
        │               ├── repository/   # Camada de Acesso a Dados (Spring Data JPA)
        │               │   ├── UserRepository.java
        │               │   ├── AvaliadoRepository.java
        │               │   ├── AvaliacaoRepository.java
        │               │   └── NivelamentoRepository.java
        │               │
        │               ├── model/        # Entidades de Domínio (Mapeamento JPA/Hibernate)
        │               │   ├── User.java
        │               │   ├── Role.java
        │               │   ├── Avaliado.java
        │               │   ├── Avaliacao.java
        │               │   ├── Nivelamento.java
        │               │   └── Processo.java
        │               │
        │               └── dto/          # Data Transfer Objects (Tráfego seguro de dados e payloads)
        │                   ├── UserDTO.java
        │                   ├── AvaliacaoRequestDTO.java
        │                   └── RankingResponseDTO.java
        │
        └── resources/
            ├── application.yml           # Configurações de conexão DB, Portas e Profiles
            └── db/
                └── migration/            # Scripts Flyway ou Liquibase (Evolução do banco de dados)
                    └── V1__create_tables.sql
```

### 3.2. Front-end (Angular)

No Angular, utilizaremos uma arquitetura modular por funcionalidades (Feature Modules) com foco em componentização reutilizável, serviços centralizados para consumo de APIs e guards de proteção de rotas.

```
sistema-avaliacao-frontend/
│
├── angular.json
├── package.json
└── src/
    ├── index.html
    ├── main.ts
    └── app/
        ├── app.config.ts                 # Configurações globais (HTTP Interceptors, Providers)
        ├── app.routes.ts                 # Definição e proteção das rotas da aplicação
        ├── app.component.ts              # Componente raiz da aplicação
        │
        ├── core/                         # Singleton Services, Guards de segurança e Interceptors
        │   ├── guards/
        │   │   └── auth.guard.ts         # Protege rotas para usuários não logados
        │   ├── interceptors/
        │   │   └── token.interceptor.ts  # Injeta dinamicamente tokens JWT nas requisições
        │   └── services/
        │       └── api.service.ts
        │
        ├── shared/                       # Componentes reutilizáveis (Uteis em todas as telas)
        │   ├── components/
        │   │   ├── header/                # Toolbar descrita nos requisitos
        │   │   ├── footer/                # Rodapé do sistema
        │   │   └── sidebar/               # Menu lateral opcional
        │   └── models/
        │       └── militar.model.ts      # Interfaces TypeScript espelhando os DTOs do Back-end
        │
        └── features/                     # Módulos ou Diretórios por Funcionalidades/Telas
            ├── login/                    # Tela de autenticação
            │   ├── login.component.ts
            │   └── login.component.html
            │
            ├── dashboard-ranking/        # Tela Inicial / Ranking dos Militares
            │   ├── ranking.component.ts
            │   └── ranking.component.html
            │
            ├── usuario-cadastro/         # Tela de Cadastro de Usuário
            │   ├── cadastro.component.ts
            │   └── cadastro.component.html
            │
            └── avaliacao-nivelamento/    # Tela de Avaliação e Nivelamento
                ├── avaliacao.component.ts
                └── avaliacao.component.html
```

---

## 4. Estrutura de Infraestrutura (Docker)

Para orquestrar o ambiente local e simular o clone de banco de dados populado, o projeto usará a seguinte topologia no arquivo `docker-compose.yml`:

```
raiz-do-projeto/
├── docker-compose.yml
├── backend/ (Contém a aplicação Spring Boot + Dockerfile)
├── frontend/ (Contém a aplicação Angular + Dockerfile Nginx)
└── database/
    └── init/
        └── 01_populate_militars.sql     # Script SQL para carregar o banco populado de simulação
```

*   **Serviço 1 (`db`):** Imagem oficial `postgres`. Montará um volume local para persistência e carregará os dados iniciais do diretório `init` automaticamente ao iniciar pela primeira vez.
*   **Serviço 2 (`backend`):** Imagem base `openjdk:21`. Compila o JAR via Maven e expõe a porta `8080`. Depende do serviço de banco de dados estar saudável.
*   **Serviço 3 (`frontend`):** Imagem do `nginx` servindo os arquivos estáticos compilados do Angular, mapeando as portas internas para acesso web (`http://localhost:4200` ou similar).
