package View;

import Repository.EncerramentoRepository;
import org.example.Model.Encerramento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarEncerramento extends JInternalFrame {

    private EncerramentoRepository encerramentoRepository = new EncerramentoRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Encerramento;
    private List<Encerramento> encerramentosExibidos;
    private JTextArea areaJustificativa;

    public ListarEncerramento() {
        setTitle("Relatório de Encerramentos");
        setClosable(true);
        setMaximizable(true);
        setSize(960, 520);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Lista de Encerramentos", 20, 15, 350, 30));

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new String[]{
                "ID", "Candidatura", "Aluno", "Data Encerramento", "Status Final"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Encerramento = new JTable(modeloTabela);
        Estilo.estilizarTabela(TB_Encerramento, new int[]{40, 180, 180, 130, 110});

        JScrollPane scroll = new JScrollPane(TB_Encerramento);
        scroll.setBounds(20, 55, 650, 345);
        Estilo.estilizarScrollPane(scroll);
        add(scroll);

        // --- PAINEL DIREITO: Justificativa ---
        add(Estilo.criarLabel("Justificativa:", 690, 55, 220, 25));
        areaJustificativa = new JTextArea();
        areaJustificativa.setEditable(false);
        areaJustificativa.setLineWrap(true);
        areaJustificativa.setWrapStyleWord(true);
        areaJustificativa.setBackground(Color.WHITE);
        JScrollPane scrollJust = new JScrollPane(areaJustificativa);
        scrollJust.setBounds(690, 80, 240, 320);
        Estilo.estilizarScrollPane(scrollJust);
        add(scrollJust);

        // --- SELEÇÃO NA TABELA: mostra justificativa ---
        TB_Encerramento.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = TB_Encerramento.getSelectedRow();
                if (linha != -1 && encerramentosExibidos != null && linha < encerramentosExibidos.size()) {
                    String just = encerramentosExibidos.get(linha).getJustificativa();
                    areaJustificativa.setText(just != null ? just : "");
                } else {
                    areaJustificativa.setText("");
                }
            }
        });

        SwingUtilities.invokeLater(this::carregarTabela);
    }

    private void carregarTabela() {
        try {
            encerramentosExibidos = encerramentoRepository.listar();
            modeloTabela.setRowCount(0);
            areaJustificativa.setText("");

            if (Validador.isListaVazia(encerramentosExibidos)) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum encerramento cadastrado no momento.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Encerramento enc : encerramentosExibidos) {
                String nomeAluno = (enc.getCandidatura() != null && enc.getCandidatura().getAluno() != null)
                        ? enc.getCandidatura().getAluno().getNome() : "—";
                String descCandidatura = enc.getCandidatura() != null
                        ? "Cand. #" + enc.getCandidatura().getId() : "—";
                modeloTabela.addRow(new Object[]{
                        enc.getId(),
                        descCandidatura,
                        nomeAluno,
                        enc.getDataEncerramento(),
                        enc.getStatusFinal()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar encerramentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}