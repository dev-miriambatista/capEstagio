package View;

import Repository.AcompanhamentoRepository;
import org.example.Model.Acompanhamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarAcompanhamento extends JInternalFrame {

    private AcompanhamentoRepository acompanhamentoRepository = new AcompanhamentoRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Acompanhamento;
    private List<Acompanhamento> acompanhamentosExibidos;
    private JTextArea areaRelatorio;

    public ListarAcompanhamento() {
        setTitle("Relatório de Acompanhamentos");
        setClosable(true);
        setMaximizable(true);
        setSize(960, 540);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Lista de Acompanhamentos", 20, 15, 350, 30));

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new String[]{
                "ID", "Candidatura", "Aluno", "Data", "Supervisor"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Acompanhamento = new JTable(modeloTabela);
        Estilo.estilizarTabela(TB_Acompanhamento, new int[]{40, 200, 160, 100, 160});

        JScrollPane scroll = new JScrollPane(TB_Acompanhamento);
        scroll.setBounds(20, 55, 660, 360);
        Estilo.estilizarScrollPane(scroll);
        add(scroll);

        // --- PAINEL DIREITO: Relatório Parcial ---
        add(Estilo.criarLabel("Relatório Parcial:", 700, 55, 220, 25));
        areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setLineWrap(true);
        areaRelatorio.setWrapStyleWord(true);
        areaRelatorio.setBackground(Color.WHITE);
        JScrollPane scrollRelatorio = new JScrollPane(areaRelatorio);
        scrollRelatorio.setBounds(700, 80, 230, 335);
        Estilo.estilizarScrollPane(scrollRelatorio);
        add(scrollRelatorio);

        // --- SELEÇÃO NA TABELA: mostra relatório parcial ---
        TB_Acompanhamento.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = TB_Acompanhamento.getSelectedRow();
                if (linha != -1 && acompanhamentosExibidos != null && linha < acompanhamentosExibidos.size()) {
                    String rel = acompanhamentosExibidos.get(linha).getRelatorioParcial();
                    areaRelatorio.setText(rel != null ? rel : "");
                } else {
                    areaRelatorio.setText("");
                }
            }
        });

        SwingUtilities.invokeLater(this::carregarTabela);
    }

    private void carregarTabela() {
        try {
            acompanhamentosExibidos = acompanhamentoRepository.listar();
            modeloTabela.setRowCount(0);
            areaRelatorio.setText("");

            if (Validador.isListaVazia(acompanhamentosExibidos)) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum acompanhamento cadastrado no momento.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Acompanhamento a : acompanhamentosExibidos) {
                String nomeAluno = (a.getCandidatura() != null && a.getCandidatura().getAluno() != null)
                        ? a.getCandidatura().getAluno().getNome() : "—";
                String descCandidatura = a.getCandidatura() != null
                        ? "Cand. #" + a.getCandidatura().getId() : "—";
                modeloTabela.addRow(new Object[]{
                        a.getId(),
                        descCandidatura,
                        nomeAluno,
                        a.getDataRegistro(),
                        a.getSupervisor()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar acompanhamentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}