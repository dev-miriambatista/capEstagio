package Model;

public class Alunos {
    private int matricula, telefone,periodo;
    private String nome, curso, email, obs;

    public Alunos(
            int matricula,
            int telefone,
            String nome,
            String curso,
            int periodo,
            String email,
            String obs
            ){

        this.matricula = matricula;
        this.telefone = telefone;
        this.nome = nome;
        this.curso = curso;
        this.periodo = periodo;
        this.email = email;
        this.obs = obs;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getTelefone() {

        return telefone;
          }

    public void setTelefone(int telefone) {

        this.telefone = telefone;

    }

    public String getNome() {

        return nome;

    }

    public void setNome(String nome) {

        this.nome = nome;
            }

    public String getCurso() {

        return curso;

    }

    public void setCurso(String curso) {

        this.curso = curso;

    }

    public int getPeriodo() {

        return periodo;

    }

    public void setPeriodo(int periodo) {

        this.periodo = periodo;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getObs() {

        return obs;

    }

    public void setObs(String obs) {

        this.obs = obs;

    }
}
