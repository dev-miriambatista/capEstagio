package View;

import Repository.EmpresaRepository;
import org.example.Model.Empresa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ListarEmpresa extends JInternalFrame {

    private EmpresaRepository empresaRepository = new EmpresaRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Empresa;
    private List<Empresa> empresasExibidas;

    public ListarEmpresa() {
        setTitle("Relatório de Empresas");
        setClosable(true);
        setMaximizable(true);
        setSize(960, 520);
        setLayout(null);

        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color corTopo = new Color(41, 128, 185);
                Color corBase = new Color(44, 62, 80);
                GradientPaint gradiente = new GradientPaint(0, 0, corTopo, 0, getHeight(), corBase);
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelFundo.setLayout(null);
        setContentPane(painelFundo);

        JLabel labelTitulo = new JLabel("Lista de Empresas");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBounds(20, 15, 300, 30);
        add(labelTitulo);

        modeloTabela = new DefaultTableModel(new String[]{
                "CNPJ", "Razão Social", "Nome Fantasia", "Endereço", "Telefone", "Responsável"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Empresa = new JTable(modeloTabela);
        TB_Empresa.setFont(new Font("Arial", Font.PLAIN, 13));
        TB_Empresa.setRowHeight(28);
        TB_Empresa.setGridColor(new Color(200, 200, 200));
        TB_Empresa.setSelectionBackground(new Color(41, 128, 185));
        TB_Empresa.setSelectionForeground(Color.WHITE);
        TB_Empresa.setBackground(Color.WHITE);
        TB_Empresa.setForeground(Color.DARK_GRAY);
        TB_Empresa.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TB_Empresa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = TB_Empresa.getTableHeader();
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));

        Estilo.estilizarTabela(TB_Empresa, new int[]{120, 160, 150, 160, 110, 130});

        JScrollPane scroll = new JScrollPane(TB_Empresa);
        scroll.setBounds(20, 55, 910, 370);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));
        add(scroll);

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBounds(570, 440, 110, 40);
        Estilo.estilizarBotao(botaoExcluir);
        add(botaoExcluir);

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.setBounds(690, 440, 110, 40);
        Estilo.estilizarBotao(botaoEditar);
        add(botaoEditar);

        // --- AÇÃO: EDITAR ---
        botaoEditar.addActionListener(e -> {
            int linhaSelecionada = TB_Empresa.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma empresa na tabela para editar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Empresa empresa = empresasExibidas.get(linhaSelecionada);

            JTextField fRSocial   = new JTextField(empresa.getrSocial());
            JTextField fNFantasia = new JTextField(empresa.getNFantasia());
            JTextField fEndereco  = new JTextField(empresa.getEndereco());
            JTextField fTel       = new JTextField(empresa.getTel());
            JTextField fContato   = new JTextField(empresa.getContato());

            Object[] campos = {
                    "Razão Social:",        fRSocial,
                    "Nome Fantasia:",       fNFantasia,
                    "Endereço:",            fEndereco,
                    "Telefone:",            fTel,
                    "Contato Responsável:", fContato
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Editar Empresa — " + empresa.getNFantasia(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (resultado != JOptionPane.OK_OPTION) return;

            if (fRSocial.getText().trim().isEmpty() || fNFantasia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Razão Social e Nome Fantasia são obrigatórios.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                empresa.setrSocial(fRSocial.getText().trim());
                empresa.setNFantasia(fNFantasia.getText().trim());
                empresa.setEndereco(fEndereco.getText().trim());
                empresa.setTel(fTel.getText().trim());
                empresa.setContato(fContato.getText().trim());

                empresaRepository.editar(empresa);
                JOptionPane.showMessageDialog(this, "Empresa atualizada com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- AÇÃO: EXCLUIR ---
        botaoExcluir.addActionListener(e -> {
            int linhaSelecionada = TB_Empresa.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma empresa na tabela para excluir.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Empresa empresa = empresasExibidas.get(linhaSelecionada);

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a empresa \"" + empresa.getNFantasia() + "\"?\nEsta ação não pode ser desfeita.",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacao != JOptionPane.YES_OPTION) return;

            try {
                empresaRepository.deletar(empresa.getId());
                JOptionPane.showMessageDialog(this, "Empresa excluída com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível excluir: esta empresa possui vagas vinculadas.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        SwingUtilities.invokeLater(this::carregarTabela);
    }

    private void carregarTabela() {
        try {
            empresasExibidas = empresaRepository.listar();
            modeloTabela.setRowCount(0);

            if (Validador.isListaVazia(empresasExibidas)) {
                JOptionPane.showMessageDialog(this,
                        "Nenhuma empresa cadastrada no momento.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Empresa empresa : empresasExibidas) {
                modeloTabela.addRow(new Object[]{
                        empresa.getCNPJ(),
                        empresa.getrSocial(),
                        empresa.getNFantasia(),
                        empresa.getEndereco(),
                        empresa.getTel(),
                        empresa.getContato()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empresas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}