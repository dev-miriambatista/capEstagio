# CapEstágio

Sistema desktop de gestão de estágios (alunos, empresas, vagas, candidaturas, acompanhamentos e encerramentos), desenvolvido em Java Swing com persistência via JPA/Hibernate. Projeto de TCC do curso de Análise e Desenvolvimento de Sistemas — Faculdade Senac PR.

Por padrão, roda com **SQLite embutido** (banco em arquivo, zero configuração), pensado para quem quiser baixar o instalador e testar direto. Também tem suporte a **MySQL** para uso em ambiente de produção real (multiusuário) — veja a seção [Rodando em produção com MySQL](#rodando-em-produção-com-mysql).

## Download

📦 **[Baixar o instalador (.exe)](#)** — versão mais recente na página de [Releases](#) *(link a atualizar)*

Não precisa instalar MySQL nem configurar nada: o banco de dados (SQLite) já vem embutido e é criado automaticamente na primeira execução.

## Demonstração

*(GIF/vídeo do sistema em uso — a adicionar)*

## Tecnologias

- Java 17
- Swing (interface gráfica)
- JPA / Hibernate 6
- SQLite (`sqlite-jdbc`) — banco padrão, embutido
- MySQL (`mysql-connector-j`) — opcional, para produção
- Maven

## Rodando a versão demo (SQLite)

Não tem pré-requisito de banco de dados — é só ter **JDK 17** instalado.

```bash
mvn clean package
java -jar target/CapEstagio.jar
```

Na primeira execução, o Hibernate (`hibernate.hbm2ddl.auto=update`) cria automaticamente o arquivo `capestagio.db` (SQLite) na pasta onde o programa é executado, já com as 6 tabelas.

Se quiser guardar o banco em outro lugar, defina a variável de ambiente:

| Variável | Exemplo | Obrigatória |
|---|---|---|
| `CAPESTAGIO_DB_URL` | `jdbc:sqlite:C:/caminho/capestagio.db` | Não (padrão: `jdbc:sqlite:capestagio.db`, criado ao lado do programa) |

## Gerando o instalador Windows (.exe)

O projeto usa o `maven-shade-plugin` pra empacotar todas as dependências (Hibernate, driver SQLite) num único jar (`target/CapEstagio.jar`), que serve de entrada pro `jpackage`.

1. Gere o jar:
   ```bash
   mvn clean package
   ```
2. Gere o instalador (requer JDK 17+ com `jpackage`, e o [WiX Toolset](https://wixtoolset.org/) instalado no Windows para o tipo `exe`):
   ```bash
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
   > **Atenção:** no Windows, o `jpackage` exige o ícone no formato **`.ico`**, não `.png`. Se só tiver o `logo.png`, converta antes (ex: [convertio.co](https://convertio.co/png-ico/)) e salve como `src/main/resources/logo.ico`.
3. O instalador (`CapEstagio-1.0.exe`) é gerado na pasta atual. Como a versão padrão usa SQLite, ele já roda "out of the box" — pode ser distribuído direto como GitHub Release, sem precisar anexar nenhum script de banco.

## Rodando em produção com MySQL

Para uso real (multiusuário, com banco centralizado), o recomendado é reconfigurar o projeto para MySQL. É reverter 3 pontos:

1. **`pom.xml`** — trocar a dependência `sqlite-jdbc` + `hibernate-community-dialects` pela `mysql-connector-j`:
   ```xml
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-j</artifactId>
       <version>8.3.0</version>
   </dependency>
   ```
2. **`src/main/resources/META-INF/persistence.xml`** — trocar o driver e remover a propriedade `hibernate.dialect` (o Hibernate detecta o dialect do MySQL automaticamente):
   ```xml
   <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
   ```
3. **`src/main/java/org/example/Util/DatabaseConfig.java`** — voltar a ler usuário e senha das variáveis de ambiente e usar uma URL MySQL:
   ```java
   String url = System.getenv("CAPESTAGIO_DB_URL");
   String user = System.getenv("CAPESTAGIO_DB_USER");
   String password = System.getenv("CAPESTAGIO_DB_PASSWORD");

   if (url == null) url = "jdbc:mysql://localhost:3306/capestagio";
   if (user == null) user = "root";
   if (password == null) password = "";

   overrides.put("jakarta.persistence.jdbc.url", url);
   overrides.put("jakarta.persistence.jdbc.user", user);
   overrides.put("jakarta.persistence.jdbc.password", password);
   ```

Depois disso, é só ter o MySQL rodando localmente e criar o banco `capestagio` — automaticamente (rodando o app uma vez, com `hbm2ddl.auto=update`) ou manualmente com o script pronto [`capestagio_schema.sql`](./capestagio_schema.sql):
```bash
mysql -u root -p < capestagio_schema.sql
```

## Estrutura do projeto

```
src/main/java/org/example/
├── Main.java                 # ponto de entrada
├── Model/                    # entidades JPA (Aluno, Empresa, Vaga, Candidatura, Acompanhamento, Encerramento)
├── Util/DatabaseConfig.java  # criação centralizada do EntityManagerFactory (SQLite por padrão)
src/main/java/Repository/     # DAOs de acesso a dados
src/main/java/View/           # telas Swing
src/main/resources/META-INF/persistence.xml  # configuração JPA
capestagio_schema.sql          # script de criação do banco MySQL (só necessário no caminho de produção)
```
