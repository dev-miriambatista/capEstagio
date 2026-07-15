package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.Model.Candidatura;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CandidaturaRepository {

    private static EntityManagerFactory emf = PersistenceConfig.getEntityManagerFactory();

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Cadastra uma nova candidatura, já registrando a primeira linha do histórico
    public void cadastrar(Candidatura candidatura) {
        String dataAtual = LocalDateTime.now().format(FORMATO_DATA);
        String linhaInicial = dataAtual + " - Candidatura criada com status \"" + candidatura.getStatus() + "\"";
        candidatura.setHistoricoAlteracoes(linhaInicial);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(candidatura);
        em.getTransaction().commit();
        em.close();
    }

    // Verifica se já existe candidatura do mesmo aluno para a mesma vaga
    public boolean existeCandidaturaDuplicada(int alunoId, int vagaId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Candidatura c WHERE c.aluno.id = :alunoId AND c.vaga.id = :vagaId",
                Long.class);
        query.setParameter("alunoId", alunoId);
        query.setParameter("vagaId", vagaId);
        long total = query.getSingleResult();
        em.close();
        return total > 0;
    }

    // Atualiza o status de uma candidatura já existente, adicionando uma linha no histórico
    public void atualizarStatus(int candidaturaId, String novoStatus) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Candidatura candidatura = em.find(Candidatura.class, candidaturaId);
        if (candidatura != null) {
            String statusAntigo = candidatura.getStatus();
            String dataAtual = LocalDateTime.now().format(FORMATO_DATA);

            String novaLinha = dataAtual + " - Status alterado de \"" + statusAntigo
                    + "\" para \"" + novoStatus + "\"";

            String historicoAtual = candidatura.getHistoricoAlteracoes();
            String historicoAtualizado = (historicoAtual == null || historicoAtual.isEmpty())
                    ? novaLinha
                    : historicoAtual + "\n" + novaLinha;

            candidatura.setStatus(novoStatus);
            candidatura.setHistoricoAlteracoes(historicoAtualizado);
            em.merge(candidatura);
        }

        em.getTransaction().commit();
        em.close();
    }

    public List<Candidatura> listar() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Candidatura> query = em.createQuery(
                "SELECT c FROM Candidatura c", Candidatura.class);
        List<Candidatura> candidaturas = query.getResultList();
        em.close();
        return candidaturas;
    }

    // Lista apenas candidaturas que ainda não foram encerradas
    public List<Candidatura> listarAtivas() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Candidatura> query = em.createQuery(
                "SELECT c FROM Candidatura c WHERE c.status NOT IN ('Concluído', 'Interrompido')",
                Candidatura.class);
        List<Candidatura> candidaturas = query.getResultList();
        em.close();
        return candidaturas;
    }

    // Busca candidaturas de uma vaga específica
    public List<Candidatura> buscarPorVaga(int vagaId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Candidatura> query = em.createQuery(
                "SELECT c FROM Candidatura c WHERE c.vaga.id = :vagaId", Candidatura.class);
        query.setParameter("vagaId", vagaId);
        List<Candidatura> candidaturas = query.getResultList();
        em.close();
        return candidaturas;
    }
}