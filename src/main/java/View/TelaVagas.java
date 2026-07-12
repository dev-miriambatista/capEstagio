package View;

import Repository.EmpresaRepository;
import Repository.VagaRepository;
import org.example.Model.Empresa;
import org.example.Model.Vaga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TelaVagas extends JInternalFrame {

    private JTextField campoTitulo, campoDescricao;
    private JComboBox<Empresa> comboEmpresasCadastro, comboFiltroEmpresa;
    private JTable tabelaVagas;
    private DefaultTableModel modeloTabela;
    private List<Vaga> vagasExibidas;

    private VagaRepository vagaRepository = new VagaRepository();

    public TelaVagas() {
        setTitle("Gerenciamento de Vagas");
        setClosable(true);
        setMaximizable(true);
        setSize(900, 530);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        // ── TÍTULO ───────────────────────────────────────────────────────────
        add(Estilo.criarLabelTitulo("Gerenciamento de Vagas", 20, 10, 300, 30));

        // ── FILTRO ───────────────────────────────────────────────────────────
        add(Estilo.criarLabel("Filtrar por Empresa:", 20, 48, 150, 25));

        comboFiltroEmpresa = new JComboBox<>();
        comboFiltroEmpresa.addItem(null);
        carregarEmpresas(comboFiltroEmpresa);
        comboFiltroEmpresa.setBounds(160, 45, 200, 30);
        Estilo.estilizarCombo(comboFiltroEmpresa);
        add(comboFiltroEmpresa);

        JButton botaoFiltrar = new JButton("Filtrar");
        botaoFiltrar.setBounds(370, 45, 90, 30);
        Estilo.estilizarBotao(botaoFiltrar);
        add(botaoFiltrar);

        JButton botaoMostrarTodas = new JButton("Todas");
        botaoMostrarTodas.setBounds(470, 45, 90, 30);
        Estilo.estilizarBotao(botaoMostrarTodas);
        add(botaoMostrarTodas);

        // ── TABELA ───────────────────────────────────────────────────────────
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Título", "Descrição", "Empresa"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaVagas = new JTable(modeloTabela);
        Estilo.estilizarTabela(tabelaVagas, new int[]{40, 150, 190, 150});

        JScrollPane scroll = new JScrollPane(tabelaVagas);
        scroll.setBounds(20, 85, 540, 320);
        Estilo.estilizarScrollPane(scroll);
        add(scroll);

        // Botões abaixo da tabela
        JButton botaoEditar = new JButton("Editar Selecionada");
        botaoEditar.setBounds(20, 420, 170, 35);
        Estilo.estilizarBotao(botaoEditar);
        add(botaoEditar);

        JButton botaoExcluir = new JButton("Excluir Selecionada");
        botaoExcluir.setBounds(200, 420, 170, 35);
        Estilo.estilizarBotao(botaoExcluir);
        add(botaoExcluir);

        // ── FORMULÁRIO CADASTRO (direita) ─────────────────────────────────
        add(Estilo.criarLabelTitulo("Nova Vaga", 590, 10, 200, 30));

        add(Estilo.criarLabel("Título da Vaga:", 590, 58, 150, 25));
        campoTitulo = new JTextField();
        campoTitulo.setBounds(590, 85, 270, 35);
        Estilo.estilizarCampo(campoTitulo);
        add(campoTitulo);

        add(Estilo.criarLabel("Descrição:", 590, 133, 150, 25));
        campoDescricao = new JTextField();
        campoDescricao.setBounds(590, 158, 270, 35);
        Estilo.estilizarCampo(campoDescricao);
        add(campoDescricao);

        add(Estilo.criarLabel("Empresa Vinculada:", 590, 208, 160, 25));
        comboEmpresasCadastro = new JComboBox<>();
        carregarEmpresas(comboEmpresasCadastro);
        comboEmpresasCadastro.setBounds(590, 235, 270, 35);
        Estilo.estilizarCombo(comboEmpresasCadastro);
        add(comboEmpresasCadastro);

        JButton botaoSalvar = new JButton("Salvar Vaga");
        botaoSalvar.setBounds(590, 300, 270, 40);
        Estilo.estilizarBotao(botaoSalvar);
        add(botaoSalvar);

        // ── AÇÕES ─────────────────────────────────────────────────────────────
        botaoFiltrar.addActionListener(e -> {
            Empresa empresaSelecionada = (Empresa) comboFiltroEmpresa.getSelectedItem();
            if (empresaSelecionada == null) { carregarTabela(); return; }
            try {
                preencherTabela(vagaRepository.buscarPorEmpresa(empresaSelecionada.getId()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoMostrarTodas.addActionListener(e -> {
            comboFiltroEmpresa.setSelectedIndex(0);
            carregarTabela();
        });

        botaoSalvar.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            String descricao = campoDescricao.getText().trim();
            Empresa empresaSelecionada = (Empresa) comboEmpresasCadastro.getSelectedItem();

            if (titulo.isEmpty()) {
                Estilo.aplicarBorda(campoTitulo, Estilo.COR_ERRO);
                JOptionPane.showMessageDialog(this, "O título da vaga é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (titulo.matches(".*\\d.*")) {
                Estilo.aplicarBorda(campoTitulo, Estilo.COR_ERRO);
                JOptionPane.showMessageDialog(this, "O título da vaga não deve conter números!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (empresaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma empresa!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Estilo.aplicarBorda(campoTitulo, Estilo.COR_BORDA);

            try {
                Vaga novaVaga = new Vaga();
                novaVaga.setTitulo(titulo);
                novaVaga.setDescricao(descricao);
                novaVaga.setEmpresa(empresaSelecionada);
                vagaRepository.cadastrar(novaVaga);
                campoTitulo.setText("");
                campoDescricao.setText("");
                JOptionPane.showMessageDialog(this, "Vaga salva com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar vaga: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoEditar.addActionListener(e -> {
            int linha = tabelaVagas.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma vaga na tabela para editar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Vaga vaga = vagasExibidas.get(linha);
            JTextField fTitulo = new JTextField(vaga.getTitulo());
            JTextField fDesc   = new JTextField(vaga.getDescricao());
            Object[] campos = {"Título:", fTitulo, "Descrição:", fDesc};
            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Editar Vaga", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (resultado != JOptionPane.OK_OPTION) return;
            if (fTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O título é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                vaga.setTitulo(fTitulo.getText().trim());
                vaga.setDescricao(fDesc.getText().trim());
                vagaRepository.editar(vaga);
                JOptionPane.showMessageDialog(this, "Vaga atualizada com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoExcluir.addActionListener(e -> {
            int linha = tabelaVagas.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma vaga na tabela para excluir.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Vaga vaga = vagasExibidas.get(linha);
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a vaga \"" + vaga.getTitulo() + "\"?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmacao != JOptionPane.YES_OPTION) return;
            try {
                vagaRepository.deletar(vaga.getId());
                JOptionPane.showMessageDialog(this, "Vaga excluída com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível excluir: esta vaga possui candidaturas vinculadas.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        carregarTabela();
        setVisible(true);
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
                    v.getTitulo() != null ? v.getTitulo() : "Sem Título",
                    v.getDescricao() != null ? v.getDescricao() : "",
                    v.getEmpresa() != null ? v.getEmpresa().getNFantasia() : "—"
            });
        }
    }

    private void carregarEmpresas(JComboBox<Empresa> comboBox) {
        try {
            for (Empresa emp : new EmpresaRepository().listar()) comboBox.addItem(emp);
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível carregar empresas.");
        }
    }
}
