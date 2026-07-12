# 📚 CapEstagio - Sistema de Gerenciamento de Estágios

## 📌 Sobre o Projeto

O **CapEstagio** é um sistema desktop desenvolvido para auxiliar no gerenciamento de processos relacionados a estágios acadêmicos, permitindo o controle de alunos, empresas, vagas disponíveis, candidaturas e acompanhamento dos estágios.

O projeto foi desenvolvido com foco na organização das informações, automatização dos processos e aplicação de conceitos de orientação a objetos, persistência de dados e arquitetura em camadas.

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

# 🛠️ Tecnologias Utilizadas

## Linguagem

* ☕ Java

## Interface Gráfica

* Java Swing

## Persistência de Dados

* Jakarta Persistence API (JPA)
* Hibernate ORM

## Banco de Dados

* MySQL

## Ferramentas

* IntelliJ IDEA
* Maven
* Git e GitHub

---

# 🏗️ Arquitetura do Projeto

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

# ⚙️ Como Executar o Projeto

## 1. Pré-requisitos

Antes de executar, tenha instalado:

* Java JDK 17 ou superior;
* MySQL;
* IntelliJ IDEA (ou IDE compatível);
* Maven.

---

## 2. Configuração do Banco de Dados

Crie um banco MySQL:

```sql
CREATE DATABASE capestagio;
```

Depois configure as credenciais no arquivo:

```
src/main/resources/META-INF/persistence.xml
```

Altere:

```xml
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="1234"/>
```

conforme sua configuração local.

---

## 3. Executando a Aplicação

Execute a classe:

```
src/main/java/org/example/Main.java
```

A aplicação iniciará a tela principal do sistema.

---

# 🖥️ Interface do Sistema

O sistema possui uma interface gráfica desktop com telas independentes para:

* Cadastro;
* Consulta;
* Atualização;
* Exclusão;
* Gerenciamento das informações de estágio.

---

# 📚 Conceitos Aplicados

Durante o desenvolvimento foram aplicados conceitos como:

* Programação Orientada a Objetos (POO);
* Encapsulamento;
* Organização em camadas;
* CRUD completo;
* Persistência com Hibernate;
* Mapeamento objeto-relacional (ORM);
* Validação de dados;
* Separação entre interface e regras de negócio.

---

# 👩‍💻 Desenvolvimento

Projeto desenvolvido como parte de estudos e prática de desenvolvimento de sistemas, aplicando conceitos de engenharia de software, banco de dados e desenvolvimento Java.

---

# 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos e educacionais.
