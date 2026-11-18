# Plano de Projeto: Sistema de Avaliação e Nivelamento de Graduados

Este documento apresenta o plano estrutural para o desenvolvimento da aplicação web baseada nos requisitos fornecidos, utilizando **Java 21 com Spring Boot (Maven)** no back-end, **Angular** no front-end, banco de dados **PostgreSQL** e **Docker** para containerização.

---
## 1. Estrutura Geral do Repositório (Monorepo)

O projeto será organizado em um repositório único dividido em três frentes principais para facilitar a orquestração via Docker Compose:

```text
sang-system/
├── docker-compose.yml          # Orquestração (PostgreSQL, OpenLDAP, Backend, Frontend)
├── PRD.md                      # Documento de Requisitos do Produto
├── PLANO.md                    # Plano Arquitetural e Banco de Dados
├── backend/                    # API RESTful em Java + Spring Boot (Maven)
└── frontend/                   # Aplicação SPA em Angular
```
---

## 2. Arquitetura em Camadas — Backend (`backend/`)

O backend seguirá o padrão **Layered Architecture** combinado com **Hexagonal Ports & Adapters** especificamente para o módulo de integração de efetivo (permitindo alternar entre o banco local/mock e a futura API oficial sem impacto no domínio).

### 2.1. Estrutura de Pacotes (`br.mil.sang`)
```text
backend/
├── Dockerfile
├── pom.xml
└── src/main/
    ├── java/br/mil/sang/
    │   ├── config/                 # Configurações de infraestrutura
    │   │   ├── SecurityConfig.java         # Regras HTTP, CORS e filtros JWT
    │   │   ├── LdapAuthConfig.java         # Conexão e bind com servidor LDAP
    │   │   └── OpenApiConfig.java          # Documentação Swagger/OpenAPI
    │   │
    │   ├── security/               # Autenticação e Autorização
    │   │   ├── JwtTokenProvider.java       # Geração e validação de tokens JWT
    │   │   ├── JwtAuthenticationFilter.java# Interceptador de requisições
    │   │   └── CustomUserDetails.java      # Dados funcionais (SARAM, Posto, Antiguidade)
    │   │
    │   ├── controller/             # Camada de Apresentação (REST Controllers)
    │   │   ├── AuthController.java         # POST /api/auth/login
    │   │   ├── UserController.java         # Gestão de papéis e usuários
    │   │   ├── EfetivoController.java      # Consulta de militares elegíveis
    │   │   ├── ProcessoController.java     # Abertura e listagem de ciclos anuais
    │   │   ├── DesignacaoController.java   # Vínculo Setor <-> Avaliador
    │   │   ├── AvaliacaoController.java    # Ficha do Avaliador (sem DELETE)
    │   │   ├── NivelamentoController.java  # Ficha de revisão do Coronel
    │   │   └── RankingController.java      # Consolidação e ordenação final
    │   │
    │   ├── dto/                    # Objetos de Transferência de Dados (Records Java)
    │   │   ├── request/                    # Payloads de entrada validados (@Valid)
    │   │   └── response/                   # Projeções de saída e fichas consolidadas
    │   │
    │   ├── service/                # Camada de Regras de Negócio
    │   │   ├── AuthService.java            # Orquestra login LDAP + carga de perfil
    │   │   ├── HierarquiaValidator.java    # Trava de precedência (posto e antiguidade)
    │   │   ├── AvaliacaoService.java       # Cálculo de notas, imutabilidade e persistência
    │   │   ├── NivelamentoService.java     # Validação de justificativa e revisão
    │   │   └── RankingService.java         # Ordenação por Pontuação DESC e Antiguidade ASC
    │   │
    │   ├── integration/            # Porta e Adaptadores para API Externa de Efetivo
    │   │   ├── EfetivoProviderPort.java    # Interface de contrato de efetivo
    │   │   ├── DatabaseMockEfetivoAdapter.java # Implementação inicial (Seed PostgreSQL)
    │   │   └── RestApiEfetivoAdapter.java  # Implementação futura via WebClient/RestClient
    │   │
    │   ├── domain/                 # Camada de Domínio e Persistência
    │   │   ├── entity/                     # Entidades JPA (@Entity)
    │   │   ├── enums/                      # RoleName, TipoVinculo, StatusProcesso
    │   │   ├── model/                      # CriteriosAvaliacao (mapeado para JSONB)
    │   │   └── repository/                 # Interfaces Spring Data JPA
    │   │
    │   └── exception/              # Tratamento Global de Erros (@RestControllerAdvice)
    │       ├── PrecedenciaHierarquicaException.java
    │       ├── RegraNegocioException.java
    │       └── GlobalExceptionHandler.java
    │
    └── resources/
        ├── application.yml
        └── db/migration/           # Controle de versão do banco via Flyway
            ├── V1__init_schema.sql         # Criação de tabelas, índices e constraints
            └── V2__seed_dados_teste.sql    # Carga inicial de efetivo, usuários e processos
```

---

## 3. Arquitetura em Camadas — Frontend (`frontend/`)

O frontend em **Angular** será estruturado sob o padrão **Core / Shared / Features**, garantindo isolamento de responsabilidades, carregamento sob demanda (*Lazy Loading*) e controle de rotas por perfil.

### 3.1. Estrutura de Diretórios (`src/app/`)
```text
frontend/
├── Dockerfile
├── nginx.conf                      # Proxy reverso /api -> backend e rotas SPA
└── src/app/
    ├── core/                       # Instâncias únicas (Singletons) e Segurança
    │   ├── auth/
    │   │   ├── auth.service.ts             # Login LDAP, armazenamento de JWT e estado do usuário
    │   │   ├── auth.guard.ts               # Bloqueia acesso não autenticado
    │   │   └── role.guard.ts               # Valida ROLE_COORDINATOR, EVALUATOR, REVIEWER
    │   ├── interceptors/
    │   │   ├── jwt.interceptor.ts          # Injeta Bearer Token nas requisições
    │   │   └── error.interceptor.ts        # Tratamento global de erros HTTP (401, 403, 422)
    │   └── models/                         # Interfaces TypeScript espelhando os DTOs
    │       ├── user.model.ts
    │       ├── efetivo.model.ts
    │       ├── criterio.model.ts
    │       └── avaliacao.model.ts
    │
    ├── shared/                     # Componentes reutilizáveis e Shell Visual
    │   ├── layout/
    │   │   ├── main-layout.component.ts    # Estrutura mestre (Toolbar + Container + Footer)
    │   │   ├── toolbar.component.ts        # Logo esquerda, menu centro, perfil/sair direita
    │   │   └── footer.component.ts         # Logo esquerda + Título institucional
    │   └── components/
    │       ├── ficha-visualizacao.component.ts # Modal/Tela de leitura da ficha completa
    │       └── militar-card.component.ts   # Card resumo com Foto, SARAM, Posto e Antiguidade
    │
    └── features/                   # Módulos Funcionais (Lazy Loaded)
        ├── login/
        │   └── login.component.ts          # Formulário LDAP (username/password)
        ├── dashboard/
        │   └── ranking.component.ts        # Filtro de ano, busca e tabela ordenada
        ├── coordenacao/
        │   ├── gestao-usuarios.component.ts# Atribuição de roles aos usuários LDAP
        │   └── designacao-setor.component.ts# Vínculo de Avaliador por setor/processo
        ├── avaliacao/
        │   └── form-avaliacao.component.ts # Busca subordinado + 15 critérios (1 a 5) + pareceres
        └── nivelamento/
            └── form-nivelamento.component.ts# Visão comparativa do Coronel + justificativa
```

---

## 3.2. Esboço do Layout (Front-end)

Com base nas especificações de telas, o layout será responsivo, baseado em um componente global de estrutura (Header/Sidebar, Main Container, Footer) e navegação via rotas.

### 3.3. Estrutura Global das Telas (Wireframe Conceitual)
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

---

## 4. Modelagem e Relacionamentos do Banco de Dados (PostgreSQL)

O banco de dados será estruturado de forma relacional para garantir a integridade referencial. As tabelas de `Avaliacao` e `Nivelamento` se conectam aos usuários e ao processo vigente.

### 4.1. Diagrama Entidade-Relacionamento (DER)

```text
+------------------+       +-----------------------+       +--------------------+

|      roles       |       |         users         |       |     processos      |
+------------------+       +-----------------------+       +--------------------+

| PK id            |1     *| PK id                 |       | PK id              |
|    name (UNIQUE) |<------| FK role_id            |       |    ano (UNIQUE)    |
+------------------+       |    username (UNIQUE)  |       |    status          |

                           |    saram (UNIQUE)     |       |    created_at      |
                           |    posto              |       +--------------------+
                           |    cod_posto          |              |1         |1
                           |    antiguidade        |              |          |
                           |    ativo              |              |          |
                           +-----------------------+              |          |

                              |1        |1        |1              |          |
               +--------------+         |         +--------+      |          |

               |                        |                  |      |          |
               v *                      v *                v *    v *        |
+----------------------+   +-------------------------+   +--------------------+

|  designacoes_setor   |   |       avaliacoes        |   |    nivelamentos    |
+----------------------+   +-------------------------+   +--------------------+

| PK id                |   | PK id                   |1 1| PK id              |
| FK processo_id  -----+-->| FK processo_id     <----+---| FK avaliacao_id    |
| FK avaliador_id      |   | FK avaliado_id     -----+   | FK revisor_id      |
|    secao             |   | FK avaliador_id         |   |    criterios(JSONB)|
| (UNIQUE: proc+secao) |   |    criterios (JSONB)    |   |    nota_revisada   |
+----------------------+   |    nota_total           |   |    justif_revisao  |

                           |    justificativa        |   |    conceito_moral  |
                           |    conceito_moral       |   |    conceito_prof   |
                           |    conceito_profissional|   |    created_at      |
                           |    created_at           |   +--------------------+
                           | (UNIQUE: proc+avaliado) |
                           +-------------------------+
                                        | *
                                        |
  ```                                      

### 5. Fluxo e Elementos por Tela
*   **Tela Inicial (Ranking):**
    *   **Título:** "Sistema de Avaliação e Nivelamento de Graduados".
    *   **Filtros:** Botão/campo de pesquisa por militares.
    *   **Tabela de Ranking:** Colunas (`Foto` | `Nome` | `Pontuação Total` | `Ação`). O botão de ação redireciona o usuário para a tela de avaliação correspondente. Ordenação nativa decrescente pela pontuação mais alta.
*   **Tela de Login:**
    *   Formulário centralizado contendo os campos: `username` e `password`.
*   **Tela de Avaliação e Nivelamento:**
    *   Barra de pesquisa para vincular e buscar o militar avaliado.
    *   Tabela/Formulário dinâmico listando os 15 conceitos de avaliação (com inputs de nota de 0 a 10).
    *   Campos de texto adicionais para: `justificativa`, `conceito moral` e `conceito profissional`.
    *   *Modo Nivelamento (Comando):* Permite herdar as notas do avaliador original, alterar os valores se necessário e exige obrigatoriamente o preenchimento do campo `justificativa` da alteração.

---


### 6. Detalhamento das Tabelas e Chaves
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




