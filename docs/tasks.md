# TASKS — Sistema de Avaliação e Nivelamento de Graduados (SANG)

Lista de tarefas técnicas e funcionais divididas por etapas. Marque `[x]` somente após validar o **Critério de Aprovação** de cada item.

---

## Fase 1: Infraestrutura e Banco de Dados

- [x] **TASK-01: Criar ambiente containerizado via Docker Compose**
  - **Descrição:** Configurar `docker-compose.yml` na raiz do projeto contendo os serviços `postgres` (PostgreSQL 16), `openldap` (servidor LDAP para testes locais de autenticação) e  `phpldapadmin` (opcional, para gerenciamento visual de usuários de teste).
  - **Critério de Aprovação:** Ao executar `docker-compose up -d`, todos os containers sobem com status `healthy`/`running`, sendo possível conectar no PostgreSQL na porta `5433` e realizar consultas/bind no servidor LDAP na porta `389`.

- [X] **TASK-02: Criar estrutura inicial do projeto Backend (Spring Boot + Maven)**
  - **Descrição:** Inicializar o projeto em `/backend` com Java 21, Spring Boot 3.x e dependências no `pom.xml` (Spring Web, Spring Data JPA, Spring Security, Spring LDAP, Flyway, PostgreSQL Driver, Validation e JWT).
  - **Critério de Aprovação:** O comando `mvn clean compile` executa sem erros e a aplicação inicializa conectando-se ao container do PostgreSQL.

- [X] **TASK-03: Implementar Migrations do Banco de Dados e Trava de Imutabilidade (Flyway)**
  - **Descrição:** Criar `V1__init_schema.sql` com as tabelas (`roles`, `users`, `efetivo`, `processos`, `designacoes_setor`, `avaliacoes`, `nivelamentos`), chaves estrangeiras, constraints de unicidade e uma *Trigger* no PostgreSQL impedindo comandos `DELETE` na tabela `avaliacoes`.
  - **Critério de Aprovação:** Ao subir o backend, o Flyway aplica a migration com sucesso; uma tentativa direta de executar `DELETE FROM avaliacoes` no banco retorna erro bloqueando a exclusão.

- [X] **TASK-04: Criar carga inicial de dados fictícios (Seed SQL & LDIF)**
  - **Descrição:** Criar `V2__seed_dados_teste.sql` populando os papéis (`ROLE_COORDINATOR`, `ROLE_EVALUATOR`, `ROLE_REVIEWER`), um processo inicial, militares variados (de Cabo a Coronel, com diferentes antiguidades e vínculos `CARREIRA` e `TEMPORARIO`) e cadastrar os usuários correspondentes no container OpenLDAP.
  - **Critério de Aprovação:** As tabelas possuem registros de teste consistentes e os usuários existem no diretório LDAP pronto para login.

---

## Fase 2: Segurança, Autenticação LDAP e Integração de Efetivo

- [X] **TASK-05: Implementar Autenticação via LDAP e Emissão de Token JWT**
  - **Descrição:** Configurar `LdapAuthConfig`, `SecurityConfig` e o endpoint `POST /api/auth/login` para autenticar `username` e `password` no LDAP, buscar o perfil/posto do militar na tabela `users` e devolver um token JWT contendo roles e dados funcionais.
  - **Critério de Aprovação:** Requisição `POST /api/auth/login` com credenciais LDAP válidas retorna HTTP `200` com JWT válido; credenciais inválidas ou usuário sem perfil cadastrado retornam HTTP `401`/`403`.

- [X] **TASK-06: Implementar Camada de Integração de Efetivo (Port & Mock Adapter)**
  - **Descrição:** Criar a interface `EfetivoProviderPort` e a implementação `DatabaseMockEfetivoAdapter` lendo da tabela `efetivo`, aplicando obrigatoriamente o filtro de elegibilidade (`tipo_vinculo = 'CARREIRA'` e `ativo = true`) e expondo `GET /api/efetivo`.
  - **Critério de Aprovação:** O endpoint `GET /api/efetivo` retorna paginadamente apenas militares ativos e de carreira; nenhum militar cadastrado como `TEMPORARIO` aparece no retorno da API.

---

## Fase 3: Regras de Negócio e Endpoints REST (Backend)

- [X] **TASK-07: Implementar Gestão de Processos Anuais (Coordenador)**
  - **Descrição:** Criar `ProcessoController` e `ProcessoService` (`GET /api/processos` e `POST /api/processos`) restritos a `ROLE_COORDINATOR` para abertura e listagem de ciclos por `ano`.
  - **Critério de Aprovação:** Um Coordenador cria um novo processo (ex: `2026`) e recebe HTTP `201`; tentar duplicar o mesmo ano retorna erro `409 Conflict` e usuários sem papel de Coordenador recebem `403 Forbidden`.

- [X] **TASK-08: Implementar Gestão de Perfis e Designação por Setor (Coordenador)**
  - **Descrição:** Criar endpoints em `UserController` e `DesignacaoController` para atribuir papéis aos usuários (validando se `ROLE_REVIEWER` possui posto de Coronel) e vincular qual `Avaliador` responde por cada `secao` no processo ativo.
  - **Critério de Aprovação:** O sistema bloqueia com erro de negócio a atribuição de `ROLE_REVIEWER` a um militar que não seja Coronel (`CEL`) e salva corretamente o vínculo único `(processo_id, secao)`.

- [X] **TASK-09: Implementar Avaliação Inicial com Trava de Precedência Hierárquica**
  - **Descrição:** Criar `POST /api/avaliacao` e `GET /api/avaliacao` (`ROLE_EVALUATOR`), validando: (a) notas inteiras de `1` a `5` nos 15 critérios, (b) soma automática do total (15 a 75), (c) campos qualitativos preenchidos e (d) `HierarquiaValidator` impedindo avaliar militar de posto superior ou mais antigo. Não implementar endpoint `DELETE`.
  - **Critério de Aprovação:** A avaliação de um subordinado mais moderno é salva com a soma exata dos 15 critérios; tentar avaliar um militar mais antigo ou de outro setor retorna HTTP `422 Unprocessable Entity`.

- [ ] **TASK-10: Implementar Nivelamento e Revisão do Coronel**
  - **Descrição:** Criar `POST /api/nivelamento` e `GET /api/nivelamento` (`ROLE_REVIEWER`) vinculados a uma avaliação existente. Comparar os 15 critérios originais com os revisados e exigir `justificativa_revisao` não vazia sempre que qualquer nota divergir da original.
  - **Critério de Aprovação:** O Revisor consegue aprovar notas iguais sem justificativa de alteração, mas recebe erro de validação se alterar qualquer nota de critério e deixar a justificativa em branco.

- [ ] **TASK-11: Implementar Consolidação e Ordenação do Ranking**
  - **Descrição:** Criar `GET /api/ranking/{processoId}` retornando os militares avaliados ordenados pela pontuação final (`nota_total_revisada` quando nivelado, ou `nota_total` quando pendente de revisão) em ordem decrescente, usando a **antiguidade** como critério de desempate.
  - **Critério de Aprovação:** Em um cenário de teste com dois militares empatados com 65 pontos, o militar de menor número/maior antiguidade aparece rigorosamente na posição superior da lista JSON.

---

## Fase 4: Frontend Angular e Experiência Visual

- [ ] **TASK-12: Inicializar Projeto Angular e Estrutura Visual Padrão (App Shell)**
  - **Descrição:** Criar aplicação em `/frontend`, configurar rotas *Lazy Loaded* e construir `MainLayoutComponent` contendo: **Toolbar superior** (logo à esquerda com link para home, menu central dinâmico por role, perfil e botão sair à direita), **Container central** e **Footer** (logo à esquerda + título oficial).
  - **Critério de Aprovação:** A estrutura visual renderiza corretamente em desktop e mobile, exibindo no menu apenas os links permitidos para o perfil logado.

- [ ] **TASK-13: Implementar Tela de Login, Guards e Interceptor JWT**
  - **Descrição:** Criar `LoginComponent` (`/login`) com formulário `username` e `password`, `JwtInterceptor` para anexar o token nas chamadas HTTP e `AuthGuard` / `RoleGuard` protegendo as rotas internas.
  - **Critério de Aprovação:** Acessar `/dashboard` sem login redireciona para `/login`; preencher credenciais LDAP válidas redireciona para o dashboard e exibe o Posto/Nome de Guerra na toolbar.

- [ ] **TASK-14: Implementar Tela Inicial — Dashboard e Tabela de Ranking**
  - **Descrição:** Criar `RankingComponent` (`/dashboard`) com filtro de Processo (Ano), barra de busca por nome/SARAM, botão `+ Novo Processo` (visível apenas para Coordenador) e tabela com colunas: `Foto`, `Posto/Nome`, `Status`, `Pontuação` e `Ações` (botão para abrir modal/tela da **Ficha Completa** e botão contextual para **Avaliar/Revisar**).
  - **Critério de Aprovação:** A tabela reflete a ordem exata da API, filtra em tempo real pela busca e abre a visualização completa de leitura da ficha ao clicar na ação correspondente.

- [ ] **TASK-15: Implementar Tela de Coordenação**
  - **Descrição:** Criar `CoordenacaoComponent` (`/coordenacao`) com formulário para buscar usuários e definir seus papéis (`Coordenador`, `Avaliador`, `Revisor`) e interface de listagem por `Seção` para designar o Avaliador de cada setor.
  - **Critério de Aprovação:** O Coordenador consegue selecionar um setor na tela, escolher um avaliador e salvar a designação com feedback visual de sucesso.

- [ ] **TASK-16: Implementar Tela de Avaliação (Visão Avaliador)**
  - **Descrição:** Criar `FormAvaliacaoComponent` (`/avaliacao`) com busca de militar subordinado, card de dados funcionais, tabela interativa dos 15 critérios (botões de `1` a `5`), contador dinâmico da nota total e campos para *Conceito Moral*, *Conceito Profissional* e *Justificativa*.
  - **Critério de Aprovação:** O avaliador preenche todos os critérios, visualiza a soma atualizada em tempo real na tela e salva a ficha sem erros.

- [ ] **TASK-17: Implementar Tela de Nivelamento (Visão Coronel / Revisor)**
  - **Descrição:** Criar `FormNivelamentoComponent` (`/nivelamento/:avaliacaoId`) exibindo lado a lado a nota original do Avaliador e o seletor de nota do Revisor para os 15 critérios, destacando a obrigatoriedade do campo *Justificativa da Alteração* caso alguma nota seja modificada.
  - **Critério de Aprovação:** Ao alterar qualquer nota de critério na interface, o campo de justificativa torna-se obrigatório na validação do formulário antes de habilitar o envio.

---

## Fase 5: Empacotamento Final e Validação Ponta a Ponta

- [ ] **TASK-18: Integrar Backend e Frontend no Docker Compose Final**
  - **Descrição:** Criar os `Dockerfile` multi-stage para `/backend` (Maven build + JRE) e `/frontend` (Angular build + Nginx com proxy reverso para `/api`) e adicioná-los ao `docker-compose.yml` principal.


Get-Content .\database\ldap\bootstrap.ldif | docker exec -i sang-openldap ldapadd -c -x -D "cn=admin,dc=fab,dc=mil,dc=br" -w admin123
docker exec -i sang-openldap ldapadd -c -x -D "cn=admin,dc=fab,dc=mil,dc=br" -w admin < database/ldap/bootstrap.ldif

