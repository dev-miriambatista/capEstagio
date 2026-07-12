package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "acompanhamentos")
public class Acompanhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "candidatura_id", nullable = false)
    private Candidatura candidatura;

    @Column(name = "data_registro", nullable = false)
    private String dataRegistro;

    @Column(name = "supervisor", length = 150, nullable = false)
    private String supervisor;

    @Column(name = "relatorio_parcial", columnDefinition = "TEXT")
    private String relatorioParcial;

    // Construtor vazio (Obrigatório para o JPA)
    public Acompanhamento() {}

    // Construtor completo para facilitar nossa vida na View
    public Acompanhamento(Candidatura candidatura, String dataRegistro, String supervisor, String relatorioParcial) {
        this.candidatura = candidatura;
        this.dataRegistro = dataRegistro;
        this.supervisor = supervisor;
        this.relatorioParcial = relatorioParcial;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Candidatura getCandidatura() { return candidatura; }
    public void setCandidatura(Candidatura candidatura) { this.candidatura = candidatura; }

    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }

    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }

    public String getRelatorioParcial() { return relatorioParcial; }
    public void setRelatorioParcial(String relatorioParcial) { this.relatorioParcial = relatorioParcial; }
}