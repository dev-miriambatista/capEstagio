package View;

import Repository.CandidaturaRepository;
import org.example.Model.Candidatura;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarCandidaturas extends JInternalFrame {

    private CandidaturaRepository candidaturaRepository = new CandidaturaRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Candidatura;
    private List<Candidatura> candidaturasExibidas;
    private JTextArea areaHistorico;
    private JComboBox<String> comboFiltroStatus;

    private static final String[] STATUS_FILTRO = {
            "Todas", "Pendente", "Em Análise", "Aprovado", "Reprovado", "Concluído", "Interrompido"
    };

    public ListarCandidaturas() {
        setTitle("Relatório de Candidaturas");
        setClosable(true);
        setMaximizable(true);
        setSize(960, 560);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Lista de Candidaturas", 20, 15, 350, 30));

        // --- FILTRO POR STATUS ---
        add(Estilo.criarLabel("Filtrar por Status:", 20, 55, 140, 25));
        comboFiltroStatus = new JComboBox<>(STATUS_FILTRO);
        comboFiltroStatus.setBounds(160, 52, 160, 30);
        Estilo.estilizarCombo(comboFiltroStatus);
        add(comboFiltroStatus);

        JButton botaoFiltrar = new JButton("Filtrar");
        botaoFiltrar.setBounds(330, 52, 90, 30);
        Estilo.estilizarBotao(botaoFiltrar);
        add(botaoFiltrar);

        JButton botaoTodas = new JButton("Todas");
        botaoTodas.setBounds(430, 52, 90, 30);
        Estilo.estilizarBotao(botaoTodas);
        add(botaoTodas);

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new String[]{
                "ID", "Vaga", "Aluno", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Candidatura = new JTable(modeloTabela);
        Estilo.estilizarTabela(TB_Candidatura, new int[]{40, 220, 220, 120});

        JScrollPane scroll = new JScrollPane(TB_Candidatura);
        scroll.setBounds(20, 95, 610, 340);
        Estilo.estilizarScrollPane(scroll);
        add(scroll);

        // --- HISTÓRICO (painel direito) ---
        add(Estilo.criarLabel("Histórico de Alterações:", 650, 95, 250, 25));
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setLineWrap(true);
        areaHistorico.setWrapStyleWord(true);
        areaHistorico.setBackground(Color.WHITE);
        JScrollPane scrollHistorico = new JScrollPane(areaHistorico);
        scrollHistorico.setBounds(650, 120, 280, 315);
        Estilo.estilizarScrollPane(scrollHistorico);
        add(scrollHistorico);

        // --- SELEÇÃO NA TABELA: mostra histórico ---
        TB_Candidatura.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int linha = TB_Candidatura.getSelectedRow();
                if (linha != -1 && candidaturasExibidas != null && linha < candidaturasExibidas.size()) {
                    areaHistorico.setText(candidaturasExibidas.get(linha).getHistoricoAlteracoes());
                } else {
                    areaHistorico.setText("");
                }
            }
        });

        // --- AÇÕES ---
        botaoFiltrar.addActionListener(e -> {
            String statusSelecionado = (String) comboFiltroStatus.getSelectedItem();
            if ("Todas".equals(statusSelecionado)) { carregarTabela(); return; }
            try {
                List<Candidatura> todas = candidaturaRepository.listar();
                List<Candidatura> filtradas = todas.stream()
                        .filter(c -> statusSelecionado.equals(c.getStatus()))
                        .collect(java.util.stream.Collectors.toList());
                preencherTabela(filtradas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoTodas.addActionListener(e -> {
            comboFiltroStatus.setSelectedIndex(0);
            carregarTabela();
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            candidaturasExibidas = candidaturaRepository.listar();
            preencherTabela(candidaturasExibidas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidaturas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Candidatura> lista) {
        candidaturasExibidas = lista;
        modeloTabela.setRowCount(0);
        areaHistorico.setText("");
        for (Candidatura c : lista) {
            modeloTabela.addRow(new Object[]{
                    c.getId(),
                    c.getVaga()  != null ? c.getVaga().getTitulo()  : "—",
                    c.getAluno() != null ? c.getAluno().getNome()   : "—",
                    c.getStatus()
            });
        }
    }
}