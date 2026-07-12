package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "encerramentos")
public class Encerramento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "candidatura_id", nullable = false)
    private Candidatura candidatura;

    @Column(name = "data_encerramento", nullable = false)
    private String dataEncerramento;

    @Column(name = "status_final", length = 50, nullable = false)
    private String statusFinal; // Vai receber "Concluído" ou "Interrompido"

    @Column(name = "justificativa", columnDefinition = "TEXT", nullable = false)
    private String justificativa;

    public Encerramento() {}

    public Encerramento(Candidatura candidatura, String dataEncerramento, String statusFinal, String justificativa) {
        this.candidatura = candidatura;
        this.dataEncerramento = dataEncerramento;
        this.statusFinal = statusFinal;
        this.justificativa = justificativa;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Candidatura getCandidatura() { return candidatura; }
    public void setCandidatura(Candidatura candidatura) { this.candidatura = candidatura; }

    public String getDataEncerramento() { return dataEncerramento; }
    public void setDataEncerramento(String dataEncerramento) { this.dataEncerramento = dataEncerramento; }

    public String getStatusFinal() { return statusFinal; }
    public void setStatusFinal(String statusFinal) { this.statusFinal = statusFinal; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
}