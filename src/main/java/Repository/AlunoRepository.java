package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.Model.Aluno;

import java.util.List;

public class AlunoRepository {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("CapEStoque");

    public static void cadastrar(Aluno aluno) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(aluno);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void deletar(Integer id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Aluno aluno = entityManager.find(Aluno.class, id);
        if (aluno != null) entityManager.remove(aluno);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void editar(Aluno aluno) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(aluno);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Aluno> listar() {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Aluno> query = entityManager.createQuery("SELECT a FROM Aluno a", Aluno.class);
        List<Aluno> alunos = query.getResultList();
        entityManager.close();
        return alunos;
    }

    public boolean existeMatricula(String matricula) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(a) FROM Aluno a WHERE a.matricula = :matricula", Long.class);
        query.setParameter("matricula", matricula);
        long count = query.getSingleResult();
        entityManager.close();
        return count > 0;
    }
}