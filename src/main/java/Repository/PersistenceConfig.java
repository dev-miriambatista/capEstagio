package Repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PersistenceConfig {

    private static final EntityManagerFactory EMF = criarEntityManagerFactory();

    private static EntityManagerFactory criarEntityManagerFactory() {
        String pastaDados = System.getenv("APPDATA") + File.separator + "CapEstagio";
        File pasta = new File(pastaDados);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String caminhoBanco = new File(pasta, "capestagio.db").getAbsolutePath();
        String urlJdbc = "jdbc:sqlite:" + caminhoBanco + "?foreign_keys=on";

        Map<String, String> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.url", urlJdbc);

        return Persistence.createEntityManagerFactory("CapEStoque", overrides);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return EMF;
    }
}