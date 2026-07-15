package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.Model.Vaga;

import java.util.List;

public class VagaRepository {

    private static EntityManagerFactory emf = PersistenceConfig.getEntityManagerFactory();

    public void cadastrar(Vaga vaga) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vaga);
        em.getTransaction().commit();
        em.close();
    }

    public void editar(Vaga vaga) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(vaga);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Vaga vaga = em.find(Vaga.class, id);
        if (vaga != null) em.remove(vaga);
        em.getTransaction().commit();
        em.close();
    }

    public List<Vaga> buscarPorEmpresa(int empresaId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Vaga> query = em.createQuery(
                "SELECT v FROM Vaga v WHERE v.empresa.id = :empresaId", Vaga.class);
        query.setParameter("empresaId", empresaId);
        List<Vaga> vagas = query.getResultList();
        em.close();
        return vagas;
    }

    public List<Vaga> listar() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Vaga> query = em.createQuery(
                "SELECT v FROM Vaga v", Vaga.class);
        List<Vaga> vagas = query.getResultList();
        em.close();
        return vagas;
    }
}