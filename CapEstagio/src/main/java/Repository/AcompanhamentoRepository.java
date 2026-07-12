package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Util.DatabaseConfig;
import jakarta.persistence.TypedQuery;
import org.example.Model.Acompanhamento;

import java.util.List;

public class AcompanhamentoRepository {

    // Carrega a unidade de persistência definida no seu persistence.xml
    private static EntityManagerFactory emf = DatabaseConfig.getEntityManagerFactory();

    // Método para salvar um novo acompanhamento no banco de dados
    public void cadastrar(Acompanhamento acompanhamento) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(acompanhamento); // O persist joga o objeto para o banco

        entityManager.getTransaction().commit(); // O commit confirma a gravação
        entityManager.close(); // Fecha a fábrica de conexões desta transação
    }

    // Método para listar todos os acompanhamentos (Útil para a tela de relatórios depois)
    public List<Acompanhamento> listar() {
        EntityManager entityManager = emf.createEntityManager();

        // JPQL: Uma query baseada na sua classe Java (Model), não na tabela física
        TypedQuery<Acompanhamento> query = entityManager.createQuery(
                "SELECT a FROM Acompanhamento a", Acompanhamento.class);

        List<Acompanhamento> lista = query.getResultList();
        entityManager.close();
        return lista;
    }
}