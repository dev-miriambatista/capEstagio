package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "empresas")
public class Empresa  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String CNPJ,
            rSocial,
            NFantasia,
            endereco,
            tel,
            contato,
            obs;

    // Construtor vazio — obrigatório para o JPA
    public Empresa() {}

    public Empresa(
            String CNPJ,
            String rSocial,
            String NFantasia,
            String endereco,
            String tel,
            String contato
    ) {
        this.CNPJ = CNPJ;
        this.rSocial = rSocial;
        this.NFantasia = NFantasia;
        this.endereco = endereco;
        this.tel = tel;
        this.contato = contato;
    }

    public int getId() { return id; }

    public String getCNPJ() {
        return CNPJ;

    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;

    }

    public String getrSocial() {
        return rSocial;

    }

    public void setrSocial(String rSocial) {
        this.rSocial = rSocial;

    }

    public String getNFantasia() {
        return NFantasia;

    }

    public void setNFantasia(String NFantasia) {
        this.NFantasia = NFantasia;

    }

    public String getEndereco() {
        return endereco;

    }

    public void setEndereco(String end) {
        this.endereco = end;

    }

    public String getTel() {
        return tel;

    }

    public void setTel(String tel) {
        this.tel = tel;

    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
            this.contato = contato;

        }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

        @Override
        public String toString() {
            return this.NFantasia;

    }



}