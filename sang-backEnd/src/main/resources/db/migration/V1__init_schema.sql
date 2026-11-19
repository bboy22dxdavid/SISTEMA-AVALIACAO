CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(100) NOT NULL UNIQUE,
                       saram VARCHAR(20) NOT NULL UNIQUE,
                       posto VARCHAR(50) NOT NULL,
                       cod_posto VARCHAR(20) NOT NULL,
                       antiguidade INTEGER NOT NULL,
                       role_id UUID NOT NULL,
                       ativo BOOLEAN NOT NULL DEFAULT TRUE,
                       CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE efetivo (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         saram VARCHAR(20) NOT NULL UNIQUE,
                         nome_guerra VARCHAR(100) NOT NULL,
                         foto_url VARCHAR(255),
                         secao VARCHAR(50) NOT NULL,
                         cod_posto VARCHAR(20) NOT NULL,
                         posto VARCHAR(50) NOT NULL,
                         quadro VARCHAR(30) NOT NULL,
                         funcao VARCHAR(100) NOT NULL,
                         antiguidade INTEGER NOT NULL,
                         tipo_vinculo VARCHAR(20) NOT NULL CHECK (tipo_vinculo IN ('CARREIRA', 'TEMPORARIO')),
                         ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE processos (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           ano INTEGER NOT NULL UNIQUE,
                           status VARCHAR(20) NOT NULL DEFAULT 'ABERTO' CHECK (status IN ('ABERTO', 'ENCERRADO')),
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE designacoes_setor (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   processo_id UUID NOT NULL,
                                   secao VARCHAR(50) NOT NULL,
                                   avaliador_id UUID NOT NULL,
                                   CONSTRAINT fk_designacao_processo FOREIGN KEY (processo_id) REFERENCES processos(id),
                                   CONSTRAINT fk_designacao_avaliador FOREIGN KEY (avaliador_id) REFERENCES users(id),
                                   CONSTRAINT uq_processo_secao UNIQUE (processo_id, secao)
);

CREATE TABLE avaliacoes (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            processo_id UUID NOT NULL,
                            avaliado_id UUID NOT NULL,
                            avaliador_id UUID NOT NULL,
                            criterios JSONB NOT NULL,
                            nota_total INTEGER NOT NULL CHECK (nota_total BETWEEN 15 AND 75),
                            justificativa TEXT NOT NULL,
                            conceito_moral TEXT NOT NULL,
                            conceito_profissional TEXT NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_avaliacao_processo FOREIGN KEY (processo_id) REFERENCES processos(id),
                            CONSTRAINT fk_avaliacao_avaliado FOREIGN KEY (avaliado_id) REFERENCES efetivo(id),
                            CONSTRAINT fk_avaliacao_avaliador FOREIGN KEY (avaliador_id) REFERENCES users(id),
                            CONSTRAINT uq_avaliacao_processo_militar UNIQUE (processo_id, avaliado_id)
);

CREATE TABLE nivelamentos (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              avaliacao_id UUID NOT NULL UNIQUE,
                              revisor_id UUID NOT NULL,
                              criterios JSONB NOT NULL,
                              nota_total_revisada INTEGER NOT NULL CHECK (nota_total_revisada BETWEEN 15 AND 75),
                              justificativa_revisao TEXT,
                              conceito_moral TEXT NOT NULL,
                              conceito_profissional TEXT NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_nivelamento_avaliacao FOREIGN KEY (avaliacao_id) REFERENCES avaliacoes(id),
                              CONSTRAINT fk_nivelamento_revisor FOREIGN KEY (revisor_id) REFERENCES users(id)
);

CREATE OR REPLACE FUNCTION impedir_exclusao_avaliacao()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'Operação bloqueada: fichas de avaliação são imutáveis e não podem ser deletadas.';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bloquear_delete_avaliacoes
    BEFORE DELETE ON avaliacoes
    FOR EACH ROW
    EXECUTE FUNCTION impedir_exclusao_avaliacao();
