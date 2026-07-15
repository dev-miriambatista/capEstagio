# 📚 CapEstagio - Sistema de Gerenciamento de Estágios

## 📌 Sobre o Projeto

O **CapEstagio** é um sistema desktop desenvolvido para auxiliar no gerenciamento de processos relacionados a estágios acadêmicos, permitindo o controle de alunos, empresas, vagas disponíveis, candidaturas e acompanhamento dos estágios.

O projeto foi desenvolvido com foco na organização das informações, automatização dos processos e aplicação de conceitos de orientação a objetos, persistência de dados e arquitetura em camadas.

Projeto desenvolvido pela equipe **Capivaras Tech**, como entrega da disciplina de Programação Orientada a Objetos, no curso de Análise e Desenvolvimento de Sistemas (3º período) da Faculdade Senac São José dos Pinhais.

---

## 📦 Download

**[Baixar o instalador (.exe)](https://github.com/dev-miriambatista/capEstagio/releases/download/v1.0.1/CapEstagio-1.0.exe)** — disponível na página de [Releases](https://github.com/dev-miriambatista/capEstagio/releases)

Não precisa instalar nenhum banco de dados nem configurar nada: o sistema usa **SQLite embutido**, e o banco (`capestagio.db`) é criado automaticamente na primeira execução.

---

## 🚀 Funcionalidades

### 👨‍🎓 Gerenciamento de Alunos

* Cadastro de alunos;
* Consulta e listagem de alunos cadastrados;
* Atualização de informações;
* Controle dos dados acadêmicos.

### 🏢 Gerenciamento de Empresas

* Cadastro de empresas;
* Consulta de empresas parceiras;
* Gerenciamento das informações empresariais.

### 💼 Gerenciamento de Vagas

* Cadastro de vagas de estágio;
* Visualização das oportunidades disponíveis;
* Controle das vagas oferecidas pelas empresas.

### 📝 Gerenciamento de Candidaturas

* Registro de candidaturas dos alunos;
* Controle do processo seletivo;
* Consulta das candidaturas realizadas.

### 📊 Acompanhamento de Estágios

* Registro do acompanhamento do estágio;
* Controle das atividades realizadas;
* Organização das informações durante o período do estágio.

### ✅ Encerramento de Estágios

* Registro da conclusão dos estágios;
* Controle dos encerramentos realizados.

---

## 🛠️ Tecnologias Utilizadas

### Linguagem

* ☕ Java 17

### Interface Gráfica

* Java Swing

### Persistência de Dados

* Jakarta Persistence API (JPA)
* Hibernate ORM

### Banco de Dados

* **SQLite** (padrão, embutido — zero configuração)
* MySQL (opcional, para uso em produção multiusuário — veja [Rodando em produção com MySQL](#-rodando-em-produção-com-mysql))

### Ferramentas

* IntelliJ IDEA
* Maven
* Git e GitHub

---

## 🏗️ Arquitetura do Projeto

O projeto foi organizado seguindo uma separação por responsabilidades:

```
src
│
├── Model
│   ├── Aluno
│   ├── Empresa
│   ├── Vaga
│   ├── Candidatura
│   ├── Acompanhamento
│   └── Encerramento
│
├── Repository
│   ├── AlunoRepository
│   ├── EmpresaRepository
│   ├── VagaRepository
│   ├── CandidaturaRepository
│   ├── AcompanhamentoRepository
│   └── EncerramentoRepository
│
├── View
│   ├── Telas do sistema
│   ├── Listagens
│   ├── Validações
│   └── Estilização da interface
│
└── Main
    └── Inicialização da aplicação
```

---

## ⚙️ Como Executar o Projeto

### 1. Pré-requisitos

Antes de executar, tenha instalado:

* Java JDK 17 ou superior;
* Maven;
* IntelliJ IDEA (ou IDE compatível).

Não é necessário instalar SQLite separadamente — o driver já vem embutido nas dependências do projeto.

### 2. Banco de Dados

Não é preciso criar nem configurar nenhum banco antes de rodar. Por padrão, a aplicação usa **SQLite** e o arquivo `capestagio.db` é criado automaticamente, na pasta do programa, na primeira execução (via `hibernate.hbm2ddl.auto=update`).

Se quiser guardar o banco em outro caminho, defina a variável de ambiente `CAPESTAGIO_DB_URL` (ex: `jdbc:sqlite:C:/caminho/capestagio.db`) antes de rodar — isso é opcional.

### 3. Executando a Aplicação

Pela IDE, execute a classe:

```
src/main/java/org/example/Main.java
```

Ou via terminal:

```bash
mvn clean package
java -jar target/CapEstagio.jar
```

A aplicação iniciará a tela principal do sistema.

---

## 🖥️ Interface do Sistema

O sistema possui uma interface gráfica desktop com telas independentes para:

* Cadastro;
* Consulta;
* Atualização;
* Exclusão;
* Gerenciamento das informações de estágio.

---

## 📦 Gerando o instalador Windows (.exe)

O projeto usa o `maven-shade-plugin` para empacotar todas as dependências (Hibernate, driver SQLite) num único jar (`target/CapEstagio.jar`), usado como entrada para o `jpackage`:

```bash
mvn clean package

jpackage ^
  --input target ^
  --name CapEstagio ^
  --main-jar CapEstagio.jar ^
  --main-class org.example.Main ^
  --type exe ^
  --icon src/main/resources/logo.ico ^
  --win-shortcut ^
  --win-menu ^
  --app-version 1.0
```

> Requer JDK 17+ com `jpackage` e o [WiX Toolset](https://wixtoolset.org/) instalado (Windows, para gerar `.exe`).

Como a versão padrão usa SQLite, o instalador já roda "out of the box" — pode ser distribuído direto como GitHub Release, sem precisar de nenhum script de banco.

---

## 🐬 Rodando em produção com MySQL

Para uso real (multiusuário, com banco centralizado), é possível reconfigurar o projeto para MySQL, revertendo 3 pontos:

1. **`pom.xml`** — trocar a dependência `sqlite-jdbc` + `hibernate-community-dialects` pela `mysql-connector-j`.
2. **`src/main/resources/META-INF/persistence.xml`** — trocar o driver para `com.mysql.cj.jdbc.Driver` e remover a propriedade `hibernate.dialect` (detectado automaticamente).
3. **`src/main/java/org/example/Util/DatabaseConfig.java`** — voltar a ler usuário, senha e URL das variáveis de ambiente (`CAPESTAGIO_DB_URL`, `CAPESTAGIO_DB_USER`, `CAPESTAGIO_DB_PASSWORD`), com padrão `jdbc:mysql://localhost:3306/capestagio`.

Depois disso, crie o banco `capestagio` — automaticamente (rodando o app uma vez, com `hbm2ddl.auto=update`) ou manualmente com o script pronto [`capestagio_schema.sql`](./capestagio_schema.sql):

```bash
mysql -u root -p < capestagio_schema.sql
```

---

## 📚 Conceitos Aplicados

Durante o desenvolvimento foram aplicados conceitos como:

* Programação Orientada a Objetos (POO);
* Encapsulamento;
* Organização em camadas (Model, Repository, View);
* CRUD completo;
* Persistência com Hibernate;
* Mapeamento objeto-relacional (ORM);
* Validação de dados (e-mail, telefone, CNPJ com dígito verificador);
* Separação entre interface e regras de negócio.

---

## 👩‍💻 Desenvolvimento

Projeto desenvolvido pela equipe **Capivaras Tech** como entrega acadêmica, aplicando conceitos de engenharia de software, persistência de dados e desenvolvimento Java.

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos e educacionais.
