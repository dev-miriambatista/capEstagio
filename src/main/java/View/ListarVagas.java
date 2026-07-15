package View;

import Repository.EmpresaRepository;
import Repository.VagaRepository;
import org.example.Model.Empresa;
import org.example.Model.Vaga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarVagas extends JInternalFrame {

    private VagaRepository vagaRepository = new VagaRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Vaga;
    private List<Vaga> vagasExibidas;
    private JComboBox<Empresa> comboFiltro;

    public ListarVagas() {
        setTitle("Relatório de Vagas");
        setClosable(true);
        setMaximizable(true);
        setSize(950, 520);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Lista de Vagas", 20, 15, 300, 30));

        // --- FILTRO POR EMPRESA ---
        add(Estilo.criarLabel("Filtrar por Empresa:", 20, 55, 150, 25));
        comboFiltro = new JComboBox<>();
        comboFiltro.addItem(null); // opção "Todas"
        carregarEmpresas();
        comboFiltro.setBounds(170, 52, 220, 30);
        Estilo.estilizarCombo(comboFiltro);
        add(comboFiltro);

        JButton botaoFiltrar = new JButton("Filtrar");
        botaoFiltrar.setBounds(400, 52, 90, 30);
        Estilo.estilizarBotao(botaoFiltrar);
        add(botaoFiltrar);

        JButton botaoTodas = new JButton("Todas");
        botaoTodas.setBounds(500, 52, 90, 30);
        Estilo.estilizarBotao(botaoTodas);
        add(botaoTodas);

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new String[]{
                "ID", "Título", "Descrição", "Empresa"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Vaga = new JTable(modeloTabela);
        Estilo.estilizarTabela(TB_Vaga, new int[]{40, 180, 340, 200});

        JScrollPane scroll = new JScrollPane(TB_Vaga);
        scroll.setBounds(20, 95, 900, 350);
        Estilo.estilizarScrollPane(scroll);
        add(scroll);

        // --- AÇÕES ---
        botaoFiltrar.addActionListener(e -> {
            Empresa emp = (Empresa) comboFiltro.getSelectedItem();
            if (emp == null) { carregarTabela(); return; }
            try {
                preencherTabela(vagaRepository.buscarPorEmpresa(emp.getId()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoTodas.addActionListener(e -> {
            comboFiltro.setSelectedIndex(0);
            carregarTabela();
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            vagasExibidas = vagaRepository.listar();
            preencherTabela(vagasExibidas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vagas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Vaga> vagas) {
        vagasExibidas = vagas;
        modeloTabela.setRowCount(0);
        for (Vaga v : vagas) {
            modeloTabela.addRow(new Object[]{
                    v.getId(),
                    v.getTitulo() != null ? v.getTitulo() : "—",
                    v.getDescricao() != null ? v.getDescricao() : "—",
                    v.getEmpresa() != null ? v.getEmpresa().getNFantasia() : "—"
            });
        }
        if (vagas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Não há vagas cadastradas no momento.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void carregarEmpresas() {
        try {
            for (Empresa emp : new EmpresaRepository().listar()) comboFiltro.addItem(emp);
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível carregar empresas para o filtro.");
        }
    }
}