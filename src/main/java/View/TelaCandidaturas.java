package View;

import Repository.CandidaturaRepository;
import Repository.VagaRepository;
import Repository.AlunoRepository;
import org.example.Model.Candidatura;
import org.example.Model.Vaga;
import org.example.Model.Aluno;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaCandidaturas extends JInternalFrame {

    private JComboBox<Object> comboVaga, comboAluno, comboStatusCadastro, comboStatusAlterar;
    private JTable tabelaCandidaturas;
    private DefaultTableModel modeloTabela;
    private JTextArea areaHistorico;
    private JButton botaoSalvar, botaoAlterarStatus;

    private CandidaturaRepository candidaturaRepository = new CandidaturaRepository();
    private List<Candidatura> candidaturasExibidas;

    private static final String[] STATUS_OPCOES = {"Pendente", "Em Análise", "Aprovado", "Reprovado"};

    public TelaCandidaturas() {
        setTitle("Gerenciamento de Candidaturas");
        setClosable(true);
        setMaximizable(true);
        setSize(900, 560);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        // ── TÍTULO ───────────────────────────────────────────────────────────
        add(Estilo.criarLabelTitulo("Gerenciamento de Candidaturas", 20, 8, 350, 30));

        // ── FORMULÁRIO CADASTRO (esquerda) ────────────────────────────────────
        add(Estilo.criarLabel("Vaga:", 20, 45, 100, 25));
        comboVaga = new JComboBox<>();
        comboVaga.setBounds(20, 68, 280, 35);
        Estilo.estilizarCombo(comboVaga);
        carregarVagas(comboVaga);
        add(comboVaga);

        add(Estilo.criarLabel("Aluno:", 20, 110, 100, 25));
        comboAluno = new JComboBox<>();
        comboAluno.setBounds(20, 133, 280, 35);
        Estilo.estilizarCombo(comboAluno);
        carregarAlunos(comboAluno);
        add(comboAluno);

        add(Estilo.criarLabel("Status Inicial:", 20, 175, 150, 25));
        comboStatusCadastro = new JComboBox<>(STATUS_OPCOES);
        comboStatusCadastro.setBounds(20, 198, 280, 35);
        Estilo.estilizarCombo(comboStatusCadastro);
        add(comboStatusCadastro);

        botaoSalvar = new JButton("Salvar Candidatura");
        botaoSalvar.setBounds(20, 248, 280, 40);
        Estilo.estilizarBotao(botaoSalvar);
        add(botaoSalvar);

        // ── ALTERAR STATUS ────────────────────────────────────────────────────
        add(Estilo.criarLabel("Alterar Status da Selecionada:", 20, 308, 260, 25));
        comboStatusAlterar = new JComboBox<>(STATUS_OPCOES);
        comboStatusAlterar.setBounds(20, 333, 280, 35);
        Estilo.estilizarCombo(comboStatusAlterar);
        add(comboStatusAlterar);

        botaoAlterarStatus = new JButton("Alterar Status");
        botaoAlterarStatus.setBounds(20, 383, 280, 40);
        Estilo.estilizarBotao(botaoAlterarStatus);
        add(botaoAlterarStatus);

        // ── TABELA (direita, cima) ────────────────────────────────────────────
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Vaga", "Aluno", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaCandidaturas = new JTable(modeloTabela);
        Estilo.estilizarTabela(tabelaCandidaturas, new int[]{40, 160, 180, 110});

        JScrollPane scrollTabela = new JScrollPane(tabelaCandidaturas);
        scrollTabela.setBounds(320, 45, 550, 265);
        Estilo.estilizarScrollPane(scrollTabela);
        add(scrollTabela);

        // ── HISTÓRICO (direita, baixo) ─────────────────────────────────────
        add(Estilo.criarLabel("Histórico de Alterações:", 320, 320, 250, 25));

        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setLineWrap(true);
        areaHistorico.setWrapStyleWord(true);
        areaHistorico.setBackground(Color.WHITE);
        JScrollPane scrollHistorico = new JScrollPane(areaHistorico);
        scrollHistorico.setBounds(320, 348, 550, 160);
        Estilo.estilizarScrollPane(scrollHistorico);
        add(scrollHistorico);

        // ── AÇÕES ─────────────────────────────────────────────────────────────
        botaoSalvar.addActionListener(e -> {
            Object vagaSelecionada  = comboVaga.getSelectedItem();
            Object alunoSelecionado = comboAluno.getSelectedItem();
            String statusInicial    = (String) comboStatusCadastro.getSelectedItem();

            if (vagaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma vaga!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int vagaId  = ((Vaga) vagaSelecionada).getId();
            int alunoId = ((Aluno) alunoSelecionado).getId();

            if (candidaturaRepository.existeCandidaturaDuplicada(alunoId, vagaId)) {
                JOptionPane.showMessageDialog(this,
                        "Este aluno já possui uma candidatura para esta vaga.\nNão é permitido cadastrar a mesma candidatura duas vezes.",
                        "Candidatura Duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Candidatura candidatura = new Candidatura();
                candidatura.setVaga((Vaga) vagaSelecionada);
                candidatura.setAluno((Aluno) alunoSelecionado);
                candidatura.setStatus(statusInicial);
                candidaturaRepository.cadastrar(candidatura);
                JOptionPane.showMessageDialog(this, "Candidatura salva com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar candidatura: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoAlterarStatus.addActionListener(e -> {
            int linhaSelecionada = tabelaCandidaturas.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma candidatura na tabela!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Candidatura candidaturaSelecionada = candidaturasExibidas.get(linhaSelecionada);
            String novoStatus = (String) comboStatusAlterar.getSelectedItem();
            String statusAtual = candidaturaSelecionada.getStatus();

            if ("Concluído".equals(statusAtual) || "Interrompido".equals(statusAtual)) {
                JOptionPane.showMessageDialog(this,
                        "Esta candidatura já foi encerrada e não pode ter o status alterado.",
                        "Operação Não Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                candidaturaRepository.atualizarStatus(candidaturaSelecionada.getId(), novoStatus);
                JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabelaCandidaturas.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaCandidaturas.getSelectedRow();
                if (linha != -1 && candidaturasExibidas != null && linha < candidaturasExibidas.size()) {
                    areaHistorico.setText(candidaturasExibidas.get(linha).getHistoricoAlteracoes());
                } else {
                    areaHistorico.setText("");
                }
            }
        });

        carregarTabela();
        setVisible(true);
    }

    private void carregarTabela() {
        try {
            candidaturasExibidas = candidaturaRepository.listar();
            modeloTabela.setRowCount(0);
            areaHistorico.setText("");
            for (Candidatura c : candidaturasExibidas) {
                modeloTabela.addRow(new Object[]{
                        c.getId(),
                        c.getVaga()  != null ? c.getVaga().getTitulo()  : "—",
                        c.getAluno() != null ? c.getAluno().getNome()   : "—",
                        c.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidaturas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarVagas(JComboBox<Object> comboBox) {
        try {
            for (Vaga v : new VagaRepository().listar()) comboBox.addItem(v);
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível carregar vagas.");
        }
    }

    private void carregarAlunos(JComboBox<Object> comboBox) {
        try {
            for (Aluno a : new AlunoRepository().listar()) comboBox.addItem(a);
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível carregar alunos.");
        }
    }
}
