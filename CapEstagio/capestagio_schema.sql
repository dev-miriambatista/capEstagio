-- =====================================================================
-- CapEstagio - Script de criação do banco de dados
-- Gerado a partir das entidades JPA em org.example.Model
-- Compatível com MySQL 8.0+
--
-- Observação: o hibernate.hbm2ddl.auto=update no persistence.xml já cria
-- essas tabelas automaticamente na primeira execução. Este script existe
-- para quem quiser montar o banco manualmente antes de rodar o app
-- (ex: quem baixar a release do .exe), sem depender do Hibernate.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS capestagio
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE capestagio;

-- ---------------------------------------------------------------------
-- Aluno
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS alunos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    matricula  VARCHAR(255),
    nome       VARCHAR(255),
    telefone   VARCHAR(255),
    curso      VARCHAR(255),
    periodo    INT NOT NULL,
    email      VARCHAR(255),
    obs        VARCHAR(255)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Empresa
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS empresas (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    CNPJ       VARCHAR(255),
    rSocial    VARCHAR(255),
    NFantasia  VARCHAR(255),
    endereco   VARCHAR(255),
    tel        VARCHAR(255),
    contato    VARCHAR(255),
    obs        VARCHAR(255)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Vaga (N:1 com Empresa)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS vagas (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(255),
    descricao   VARCHAR(255),
    empresa_id  INT NOT NULL,
    CONSTRAINT fk_vaga_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresas(id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Candidatura (N:1 com Vaga e Aluno)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS candidaturas (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    vaga_id               INT NOT NULL,
    aluno_id              INT NOT NULL,
    status                VARCHAR(20) NOT NULL,
    historico_alteracoes  TEXT,
    CONSTRAINT fk_candidatura_vaga
        FOREIGN KEY (vaga_id) REFERENCES vagas(id),
    CONSTRAINT fk_candidatura_aluno
        FOREIGN KEY (aluno_id) REFERENCES alunos(id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Encerramento (N:1 com Candidatura)
-- Observação: data_encerramento é String no Model (não DATE), então o
-- tipo aqui acompanha o mapeamento atual da entidade Java.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS encerramentos (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    candidatura_id     INT NOT NULL,
    data_encerramento  VARCHAR(255) NOT NULL,
    status_final       VARCHAR(50) NOT NULL,
    justificativa      TEXT NOT NULL,
    CONSTRAINT fk_encerramento_candidatura
        FOREIGN KEY (candidatura_id) REFERENCES candidaturas(id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Acompanhamento (N:1 com Candidatura)
-- Mesma observação: data_registro é String no Model.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS acompanhamentos (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    candidatura_id      INT NOT NULL,
    data_registro       VARCHAR(255) NOT NULL,
    supervisor          VARCHAR(150) NOT NULL,
    relatorio_parcial   TEXT,
    CONSTRAINT fk_acompanhamento_candidatura
        FOREIGN KEY (candidatura_id) REFERENCES candidaturas(id)
) ENGINE=InnoDB;
