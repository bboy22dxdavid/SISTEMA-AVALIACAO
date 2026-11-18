# PRD — Sistema de Avaliação e Nivelamento de Graduados (SANG)

**Versão:** 2.0 (Consolidada)  
**Status:** Pronto para Desenvolvimento  

---

## 1. Visão Geral do Produto
O **Sistema de Avaliação e Nivelamento de Graduados (SANG)** tem como objetivo substituir o processo atual baseado em planilhas eletrônicas por uma plataforma web centralizada, auditável e segura. O sistema gerencia o ciclo anual de avaliação de militares de carreira (de **Cabo a Oficial**), permitindo que chefes de seção atribuam notas e que o comando (**Coronel**) realize a revisão e o nivelamento final, gerando um ranking oficial ordenado por pontuação e antiguidade.

---

## 2. Perfis de Acesso (Roles) e Regras de Negócio

### 2.1. Atores do Sistema
* **Coordenador (`ROLE_COORDINATOR` - Seção SPN):** Administrador do processo. Abre ciclos anuais (`Processos`), gerencia permissões de acesso, vincula os Avaliadores às suas respectivas seções/setores e define o Revisor.
* **Avaliador (`ROLE_EVALUATOR` - Chefe de Seção):** Preenche a ficha de avaliação inicial dos militares subordinados à sua seção.
* **Revisor (`ROLE_REVIEWER` - Exclusivo para Posto de Coronel - Cel):** Audita as avaliações submetidas, podendo concordar com as notas ou alterá-las mediante justificativa obrigatória.
* **Avaliado (Objeto da Avaliação):** Militares **exclusivamente de carreira**, ativos, abrangendo a faixa hierárquica de **Cabo a Oficial**.

### 2.2. Regras de Negócio e Hierarquia
1. **Elegibilidade de Vínculo:** Militares temporários são bloqueados automaticamente na sincronização/consulta e nunca aparecem como avaliados.
2. **Trava de Precedência Hierárquica (Antiguidade):** O sistema deve impedir tecnicamente que um Avaliador avalie um militar **mais antigo ou de posto superior** ao seu (validação cruzada via `cod_posto` e `antiguidade`).
3. **Imutabilidade e Auditoria:** Uma vez submetida, a ficha de avaliação **não pode ser deletada** (operações `DELETE` bloqueadas na API e no banco), garantindo rastreabilidade histórica para consultas futuras.
4. **Critérios de Avaliação:** São 15 critérios avaliados em escala inteira de **1 a 5** (pontuação mínima: 15 | máxima: 75):
   * Qualidade do trabalho
   * Produtividade no trabalho
   * Conhecimento profissional
   * Eficiência no trabalho em equipe
   * Emprego de meios materiais
   * Planejamento
   * Julgamento
   * Responsabilidade
   * Disciplina
   * Apresentação pessoal
   * Liderança
   * Iniciativa
   * Adaptabilidade
   * Comunicação oral e escrita
   * Relacionamento no ambiente de trabalho
5. **Pareceres Qualitativos:** Preenchimento obrigatório dos campos textuais *Conceito Moral*, *Conceito Profissional* e *Justificativa*.
6. **Fluxo de Nivelamento (Revisão):** O Revisor visualiza a nota original do Avaliador. Caso altere qualquer nota, a justificativa da alteração torna-se obrigatória. A nota válida para o ranking final será a nota consolidada após o nivelamento.
7. **Critério de Desempate no Ranking:** Em caso de empate na pontuação final, a ordenação utilizará a **antiguidade** do militar como critério de desempate (o militar mais antigo figura à frente).

---

## 3. Requisitos Funcionais (RF)

* **RF01 - Autenticação Corporativa:** Integração via **Spring Security + LDAP** para validar `username` e `password` diretamente na base corporativa.
* **RF02 - Controle de Acesso (RBAC):** Proteção de rotas no Angular (*Route Guards*) e endpoints no Spring (`@PreAuthorize`) de acordo com o perfil do usuário logado.
* **RF03 - Sincronização de Efetivo:** Consumo de API externa para importação e atualização dos dados dos militares, filtrando apenas militares de carreira e ativos.
* **RF04 - Gestão de Processos:** O Coordenador pode criar e listar ciclos anuais de avaliação identificados pelo `Ano` (ex: `2026`).
* **RF05 - Designação por Setor:** O Coordenador filtra militares por `Seção` e vincula qual `Avaliador` responderá por aquele setor no processo vigente.
* **RF06 - Preenchimento de Avaliação:** Busca de subordinados elegíveis (aplicando filtro automático de seção e precedência hierárquica) e persistência da ficha inicial.
* **RF07 - Revisão e Nivelamento:** Interface comparativa para o Coronel revisar notas, registrar justificativa de alteração e consolidar a ficha.
* **RF08 - Dashboard e Ranking:** Listagem ordenada por `Pontuação Final DESC, Antiguidade ASC`, permitindo visualizar a ficha completa de cada militar.

---

## 4. Arquitetura Técnica e Stack

### 4.1. Tecnologias Definidas
* **Backend:** Java 17/21 + **Spring Boot 3.x** (Spring Web, Spring Data JPA, Spring Security + LDAP, Flyway).
* **Gerenciador de Dependências:** **Maven**.
* **Frontend:** **Angular** (v17+ com TypeScript, RxJS e biblioteca de componentes UI).
* **Banco de Dados:** **PostgreSQL 16**.
* **Infraestrutura / Containerização:** **Docker** e **Docker Compose** (orquestrando containers de Backend, Frontend/Nginx, PostgreSQL e OpenLDAP local para testes de desenvolvimento).

### 4.2. Estratégia para API de Efetivo (Contingência)
* Utilização do padrão **Adapter/Port** no backend (`EfetivoProviderPort`).
* Enquanto o acesso à API externa oficial não estiver liberado, o sistema operará com um `DatabaseMockEfetivoAdapter`, alimentado via **Flyway/SQL Seed** no PostgreSQL com dados fictícios realistas (abrangendo postos de Cabo a Oficial, antiguidades distintas, setores e vínculos temporários/carreira).
* Quando a API externa for disponibilizada, bastará implementar o `RestApiEfetivoAdapter` e ativá-lo via configuração (`application.yml`), sem alterar as regras de negócio.

### 4.3. Modelo de Dados (PostgreSQL)
* `roles`: `id`, `name` (`ROLE_COORDINATOR`, `ROLE_EVALUATOR`, `ROLE_REVIEWER`).
* `users`: `id`, `username` (LDAP), `saram`, `posto`, `cod_posto`, `antiguidade`, `role_id`, `ativo`.
* `efetivo`: `id`, `saram` (UNIQUE), `nome_guerra`, `foto_url`, `secao`, `cod_posto`, `posto`, `quadro`, `funcao`, `antiguidade`, `tipo_vinculo` (`CARREIRA`, `TEMPORARIO`), `ativo`.
* `processos`: `id`, `ano` (UNIQUE), `status` (`ABERTO`, `ENCERRADO`), `created_at`.
* `designacoes_setor`: `id`, `processo_id` (FK), `secao`, `avaliador_id` (FK `users`).
* `avaliacoes`: `id`, `processo_id` (FK), `avaliado_id` (FK `efetivo`), `avaliador_id` (FK `users`), `criterios` (`JSONB` com os 15 atributos), `nota_total` (`INTEGER`), `justificativa` (`TEXT`), `conceito_moral` (`TEXT`), `conceito_profissional` (`TEXT`), `created_at`.
* `nivelamentos`: `id`, `avaliacao_id` (FK `avaliacoes` - UNIQUE), `revisor_id` (FK `users`), `criterios` (`JSONB` com os 15 atributos revisados), `nota_total_revisada` (`INTEGER`), `justificativa_revisao` (`TEXT`), `conceito_moral` (`TEXT`), `conceito_profissional` (`TEXT`), `created_at`.

### 4.4. Endpoints RESTful (`/api`)
* `POST /api/auth/login` — Autenticação LDAP e emissão de token JWT com *roles* e dados funcionais.
* `GET /api/users` | `POST /api/users` — Gestão de usuários e atribuição de papéis.
* `GET /api/efetivo` — Busca paginada e filtrada (seção, nome, elegibilidade).
* `GET /api/processos` | `POST /api/processos` — Abertura e listagem dos ciclos anuais.
* `GET /api/designacoes` | `POST /api/designacoes` — Vinculação de Avaliador por setor/processo.
* `GET /api/avaliacao` | `POST /api/avaliacao` — Criação e consulta das fichas iniciais (sem método `DELETE`).
* `GET /api/nivelamento` | `POST /api/nivelamento` — Revisão do Coronel e consolidação da nota.
* `GET /api/ranking/{processoId}` — Retorna a lista ordenada por pontuação e antiguidade.

---

## 5. Instruções Visuais e Experiência do Usuário (Angular UI)

### 5.1. Estrutura Global (App Shell)
* **Toolbar / Header Superior:**
  * *Esquerda:* Logomarca institucional clicável (redireciona para `/dashboard`).
  * *Centro:* Menu de navegação horizontal exibindo apenas os links permitidos para a *Role* logada (*Ranking*, *Avaliar*, *Nivelamento*, *Coordenação*).
  * *Direita:* Indicador com Posto + Nome de Guerra + Perfil ativo e botão **Sair (Logout)**.
* **Container Central:** Área responsiva centralizada com fundo neutro claro (`#F4F6F9`) e cartões (`cards`) brancos com sombras suaves para formulários e tabelas.
* **Footer:** Logomarca à esquerda (link para início) e o título *"Sistema de Avaliação e Nivelamento de Graduados"*.

### 5.2. Fluxo das Telas
1. **Login (`/login`):** Card centralizado com inputs de `Usuário (LDAP)` e `Senha`, validação de campos obrigatórios e alerta de erro caso a autenticação LDAP falhe.
2. **Inicial / Ranking (`/dashboard`):**
   * *Filtros:* Seletor de **Processo (Ano)**, campo de busca rápida e botão `+ Novo Processo` (exclusivo `ROLE_COORDINATOR`).
   * *Tabela:* Ordenada por `Pontuação Final` (decrescente) e `Antiguidade` (crescente). Colunas: `Foto`, `Posto / Nome de Guerra`, `Seção`, `Status` (Avaliado / Nivelado), `Pontuação` e `Ações` (botão **Ver Ficha** em modo leitura e botão contextual **Avaliar** ou **Revisar**).
3. **Coordenação (`/coordenacao`):**
   * *Aba 1 - Perfis de Acesso:* Busca de usuários do LDAP para atribuir `ROLE_COORDINATOR`, `ROLE_EVALUATOR` ou `ROLE_REVIEWER` (validando se o Revisor possui posto de Coronel).
   * *Aba 2 - Designação por Setor:* Lista todas as seções existentes no efetivo e permite selecionar no dropdown qual usuário `ROLE_EVALUATOR` responderá por cada seção no ano vigente.
4. **Avaliação / Nivelamento (`/avaliacao/:id` e `/nivelamento/:id`):**
   * *Cabeçalho:* Busca de militar (filtrada automaticamente pela seção e precedência hierárquica) e card resumo com Foto, SARAM, Posto, Quadro e Antiguidade.
   * *Tabela de Critérios:* Lista os 15 itens com seletores de `1` a `5`. *Na visão do Revisor, uma coluna extra exibe a nota original atribuída pelo Avaliador ao lado do seletor de revisão.*
   * *Fechamento:* Campos de texto para *Conceito Moral*, *Conceito Profissional* e *Justificativa* (destacada como obrigatória caso o Revisor altere qualquer nota), acompanhados de um contador dinâmico da **Nota Total** antes de salvar.
