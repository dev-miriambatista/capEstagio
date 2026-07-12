package org.example.Util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Ponto único de criação do EntityManagerFactory.
 *
 * Versão demo/portfólio: usa SQLite embutido (arquivo local, sem servidor,
 * sem usuário/senha), para que qualquer pessoa consiga baixar e rodar o
 * programa sem precisar instalar ou configurar nada.
 *
 * Por padrão, o arquivo capestagio.db fica em uma pasta fixa dentro dos
 * dados do usuário (%APPDATA%\CapEstagio no Windows), garantindo que o
 * mesmo banco seja usado não importa de onde o .exe for iniciado (atalho
 * do menu Iniciar, área de trabalho, etc). Dá pra sobrescrever o caminho
 * com a variável de ambiente CAPESTAGIO_DB_URL
 * (ex: jdbc:sqlite:C:/caminho/capestagio.db), mas isso raramente é necessário.
 *
 * Para rodar em produção com MySQL (recomendado para uso real, multiusuário),
 * veja as instruções no README — é preciso reverter driver/dialect no
 * persistence.xml e as dependências no pom.xml.
 */
public class DatabaseConfig {

    private static final String PERSISTENCE_UNIT = "CapEStoque";
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            Map<String, String> overrides = new HashMap<>();

            String url = System.getenv("CAPESTAGIO_DB_URL");

            // Fallback: arquivo SQLite em pasta fixa de dados do usuário,
            // criado automaticamente se não existir. Isso evita que o banco
            // "suma" quando o .exe é iniciado de diretórios de trabalho
            // diferentes (atalho do menu Iniciar, área de trabalho, etc).
            if (url == null) {
                String appData = System.getenv("APPDATA"); // Windows
                String pastaBase = (appData != null) ? appData : System.getProperty("user.home");
                java.io.File pastaApp = new java.io.File(pastaBase, "CapEstagio");
                pastaApp.mkdirs();
                String caminhoDb = new java.io.File(pastaApp, "capestagio.db").getAbsolutePath();
                url = "jdbc:sqlite:" + caminhoDb.replace("\\", "/");
            }

            // SQLite não aplica chaves estrangeiras por padrão, mesmo com
            // @JoinColumn/hbm2ddl criando as constraints no schema. Sem isso,
            // é possível excluir um Aluno/Candidatura que ainda tem
            // Candidatura/Acompanhamento/Encerramento vinculado, deixando
            // registros órfãos (causa dos erros "Unable to find ... with id X"
            // nas telas de Candidaturas e Acompanhamento).
            url += url.contains("?") ? "&foreign_keys=on" : "?foreign_keys=on";

            overrides.put("jakarta.persistence.jdbc.url", url);

            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, overrides);
        }
        return emf;
    }
}