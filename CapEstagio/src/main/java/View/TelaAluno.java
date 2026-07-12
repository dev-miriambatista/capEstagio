package View;

import Repository.AlunoRepository;
import org.example.Model.Aluno;

import javax.swing.*;
import java.awt.*;

public class TelaAluno extends JInternalFrame {

    public TelaAluno() {

        setTitle("Cadastro Aluno");
        setClosable(true);
        setMaximizable(true);
        setSize(450, 530);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Cadastro de Aluno", 30, 10, 300, 30));

        add(Estilo.criarLabel("Matrícula", 30, 50, 130, 25));
        JTextField campoMatricula = new JTextField();
        campoMatricula.setBounds(180, 47, 200, 35);
        Estilo.estilizarCampo(campoMatricula);
        add(campoMatricula);

        add(Estilo.criarLabel("Nome", 30, 98, 130, 25));
        JTextField campoNome = new JTextField();
        campoNome.setBounds(180, 95, 200, 35);
        Estilo.estilizarCampo(campoNome);
        add(campoNome);

        add(Estilo.criarLabel("Curso", 30, 148, 130, 25));
        JTextField campoCurso = new JTextField();
        campoCurso.setBounds(180, 145, 200, 35);
        Estilo.estilizarCampo(campoCurso);
        add(campoCurso);

        add(Estilo.criarLabel("Período (1–10)", 30, 198, 130, 25));
        JTextField campoPeriodo = new JTextField();
        campoPeriodo.setBounds(180, 195, 200, 35);
        Estilo.estilizarCampo(campoPeriodo);
        add(campoPeriodo);

        add(Estilo.criarLabel("E-mail", 30, 248, 130, 25));
        JTextField campoEmail = new JTextField();
        campoEmail.setBounds(180, 245, 200, 35);
        Estilo.estilizarCampo(campoEmail);
        add(campoEmail);

        add(Estilo.criarLabel("Telefone", 30, 298, 130, 25));
        JTextField campoTel = new JTextField();
        campoTel.setBounds(180, 295, 200, 35);
        Estilo.estilizarCampo(campoTel);
        add(campoTel);

        add(Estilo.criarLabel("Observações", 30, 348, 130, 25));
        JTextField campoObs = new JTextField();
        campoObs.setBounds(180, 345, 200, 35);
        Estilo.estilizarCampo(campoObs);
        add(campoObs);

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(165, 420, 120, 40);
        Estilo.estilizarBotao(botaoSalvar);
        add(botaoSalvar);

        botaoSalvar.addActionListener(e -> {

            JTextField[] camposObrigatorios = {
                    campoMatricula, campoNome, campoCurso,
                    campoPeriodo, campoEmail, campoTel
            };

            boolean valido = true;
            for (JTextField campo : camposObrigatorios) {
                if (campo.getText().trim().isEmpty()) {
                    Estilo.aplicarBorda(campo, Estilo.COR_ERRO);
                    valido = false;
                } else {
                    Estilo.aplicarBorda(campo, Estilo.COR_BORDA);
                }
            }
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!Validador.isPeriodoValido(campoPeriodo.getText())) {
                JOptionPane.showMessageDialog(null,
                        "Período inválido. Informe um número inteiro entre 1 e 10.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoPeriodo, Estilo.COR_ERRO);
                return;
            }

            if (!Validador.isEmailValido(campoEmail.getText())) {
                JOptionPane.showMessageDialog(null,
                        "Por favor, informe um e-mail válido (ex: aluno@email.com).",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoEmail, Estilo.COR_ERRO);
                return;
            }

            if (!Validador.isTelefoneValido(campoTel.getText())) {
                JOptionPane.showMessageDialog(null,
                        "Telefone inválido. Informe um número com DDD (10 ou 11 dígitos).",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoTel, Estilo.COR_ERRO);
                return;
            }

            try {
                // Verificar matrícula duplicada
                AlunoRepository alunoRepo = new AlunoRepository();
                if (alunoRepo.existeMatricula(campoMatricula.getText().trim())) {
                    JOptionPane.showMessageDialog(null, "Já existe um aluno cadastrado com esta matrícula.",
                            "Matrícula Duplicada", JOptionPane.WARNING_MESSAGE);
                    Estilo.aplicarBorda(campoMatricula, Estilo.COR_ERRO);
                    return;
                }

                Aluno aluno = new Aluno();
                aluno.setMatricula(campoMatricula.getText().trim());
                aluno.setNome(campoNome.getText().trim());
                aluno.setCurso(campoCurso.getText().trim());
                aluno.setPeriodo(Integer.parseInt(campoPeriodo.getText().trim()));
                aluno.setEmail(campoEmail.getText().trim());
                aluno.setTelefone(campoTel.getText().trim());
                aluno.setObs(campoObs.getText().trim());

                AlunoRepository.cadastrar(aluno);

                for (JTextField campo : camposObrigatorios) {
                    campo.setText("");
                    Estilo.aplicarBorda(campo, Estilo.COR_BORDA);
                }
                campoObs.setText("");

                JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao cadastrar aluno: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
