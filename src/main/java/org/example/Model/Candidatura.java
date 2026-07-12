package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "candidaturas")
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "historico_alteracoes", columnDefinition = "TEXT")
    private String historicoAlteracoes;

    public Candidatura() {}

    public Candidatura(int id, Vaga vaga, Aluno aluno, String status, String historicoAlteracoes) {
        this.id = id;
        this.vaga = vaga;
        this.aluno = aluno;
        this.status = status;
        this.historicoAlteracoes = historicoAlteracoes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Vaga getVaga() { return vaga; }
    public void setVaga(Vaga vaga) { this.vaga = vaga; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHistoricoAlteracoes() { return historicoAlteracoes; }
    public void setHistoricoAlteracoes(String historicoAlteracoes) { this.historicoAlteracoes = historicoAlteracoes; }
    @Override
    public String toString() {
        // Se o aluno e a vaga não forem nulos, mostra "Nome do Aluno - Título da Vaga"
        String nomeAluno = (aluno != null) ? aluno.getNome() : "Aluno s/ Nome";
        String tituloVaga = (vaga != null) ? vaga.getTitulo() : "Vaga s/ Título";
        return "Candidatura #" + id + ": " + nomeAluno + " (" + tituloVaga + ")";
    }
}