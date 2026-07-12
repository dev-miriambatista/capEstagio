package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Util.DatabaseConfig;
import jakarta.persistence.TypedQuery;
import org.example.Model.Encerramento;

import java.util.List;

public class EncerramentoRepository {

    private static EntityManagerFactory emf = DatabaseConfig.getEntityManagerFactory();

    public void cadastrar(Encerramento encerramento) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(encerramento);
        em.getTransaction().commit();
        em.close();
    }

    // Verifica se uma candidatura já foi encerrada
    public boolean jaEncerrada(int candidaturaId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Encerramento e WHERE e.candidatura.id = :candidaturaId",
                Long.class);
        query.setParameter("candidaturaId", candidaturaId);
        long total = query.getSingleResult();
        em.close();
        return total > 0;
    }

    public List<Encerramento> listar() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Encerramento> query = em.createQuery(
                "SELECT e FROM Encerramento e", Encerramento.class);
        List<Encerramento> lista = query.getResultList();
        em.close();
        return lista;
    }
}