package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Util.DatabaseConfig;
import jakarta.persistence.TypedQuery;
import org.example.Model.Empresa;

import java.util.List;

public class EmpresaRepository {

    private static EntityManagerFactory emf =
            DatabaseConfig.getEntityManagerFactory();

    public static void cadastrar(Empresa empresa) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(empresa);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void deletar(Integer id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Empresa empresa = entityManager.find(Empresa.class, id);
        if (empresa != null) entityManager.remove(empresa);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void editar(Empresa empresa) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(empresa);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Empresa> listar() {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Empresa> query = entityManager.createQuery("SELECT e FROM Empresa e", Empresa.class);
        List<Empresa> empresas = query.getResultList();
        entityManager.close();
        return empresas;
    }

    public boolean existeCNPJ(String cnpj) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(e) FROM Empresa e WHERE e.CNPJ = :cnpj", Long.class);
        query.setParameter("cnpj", cnpj);
        long count = query.getSingleResult();
        entityManager.close();
        return count > 0;
    }
}