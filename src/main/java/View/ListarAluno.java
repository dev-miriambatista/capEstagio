package View;

import Repository.AlunoRepository;
import org.example.Model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ListarAluno extends JInternalFrame {

    private AlunoRepository alunoRepository = new AlunoRepository();
    private DefaultTableModel modeloTabela;
    private JTable TB_Aluno;
    private List<Aluno> alunosExibidos;

    public ListarAluno() {
        setTitle("Relatório de Alunos");
        setClosable(true);
        setMaximizable(true);
        setSize(950, 520);
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

        JLabel labelTitulo = new JLabel("Lista de Alunos");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBounds(20, 15, 300, 30);
        add(labelTitulo);

        modeloTabela = new DefaultTableModel(new String[]{
                "Matrícula", "Nome", "Curso", "Período", "E-mail", "Telefone"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        TB_Aluno = new JTable(modeloTabela);
        TB_Aluno.setFont(new Font("Arial", Font.PLAIN, 13));
        TB_Aluno.setRowHeight(28);
        TB_Aluno.setGridColor(new Color(200, 200, 200));
        TB_Aluno.setSelectionBackground(new Color(41, 128, 185));
        TB_Aluno.setSelectionForeground(Color.WHITE);
        TB_Aluno.setBackground(Color.WHITE);
        TB_Aluno.setForeground(Color.DARK_GRAY);
        TB_Aluno.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TB_Aluno.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = TB_Aluno.getTableHeader();
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));

        Estilo.estilizarTabela(TB_Aluno, new int[]{80, 160, 160, 65, 250, 160});

        JScrollPane scroll = new JScrollPane(TB_Aluno);
        scroll.setBounds(20, 55, 900, 370);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));
        add(scroll);

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBounds(560, 440, 110, 40);
        Estilo.estilizarBotao(botaoExcluir);
        add(botaoExcluir);

        JButton botaoEditar = new JButton("Editar");
        botaoEditar.setBounds(680, 440, 110, 40);
        Estilo.estilizarBotao(botaoEditar);
        add(botaoEditar);

        // --- AÇÃO: EDITAR ---
        botaoEditar.addActionListener(e -> {
            int linhaSelecionada = TB_Aluno.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um aluno na tabela para editar.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Aluno aluno = alunosExibidos.get(linhaSelecionada);

            JTextField fNome    = new JTextField(aluno.getNome());
            JTextField fCurso   = new JTextField(aluno.getCurso());
            JTextField fPeriodo = new JTextField(String.valueOf(aluno.getPeriodo()));
            JTextField fEmail   = new JTextField(aluno.getEmail());
            JTextField fTel     = new JTextField(aluno.getTelefone());
            JTextField fObs     = new JTextField(aluno.getObs());

            Object[] campos = {
                    "Nome:",     fNome,
                    "Curso:",    fCurso,
                    "Período:",  fPeriodo,
                    "E-mail:",   fEmail,
                    "Telefone:", fTel,
                    "Obs:",      fObs
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Editar Aluno — " + aluno.getNome(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (resultado != JOptionPane.OK_OPTION) return;

            if (fNome.getText().trim().isEmpty() || fCurso.getText().trim().isEmpty()
                    || fPeriodo.getText().trim().isEmpty() || fEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, Curso, Período e E-mail são obrigatórios.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                aluno.setNome(fNome.getText().trim());
                aluno.setCurso(fCurso.getText().trim());
                aluno.setPeriodo(Integer.parseInt(fPeriodo.getText().trim()));
                aluno.setEmail(fEmail.getText().trim());
                aluno.setTelefone(fTel.getText().trim());
                aluno.setObs(fObs.getText().trim());

                alunoRepository.editar(aluno);
                JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!");
                carregarTabela();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Período deve ser um número inteiro.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- AÇÃO: EXCLUIR ---
        botaoExcluir.addActionListener(e -> {
            int linhaSelecionada = TB_Aluno.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um aluno na tabela para excluir.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Aluno aluno = alunosExibidos.get(linhaSelecionada);

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o aluno \"" + aluno.getNome() + "\"?\nEsta ação não pode ser desfeita.",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacao != JOptionPane.YES_OPTION) return;

            try {
                alunoRepository.deletar(aluno.getId());
                JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível excluir: este aluno possui candidaturas vinculadas.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            alunosExibidos = alunoRepository.listar();
            modeloTabela.setRowCount(0);
            for (Aluno aluno : alunosExibidos) {
                modeloTabela.addRow(new Object[]{
                        aluno.getMatricula(),
                        aluno.getNome(),
                        aluno.getCurso(),
                        aluno.getPeriodo(),
                        aluno.getEmail(),
                        aluno.getTelefone()
                });
            }
            if (alunosExibidos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Não há alunos cadastrados no momento.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}