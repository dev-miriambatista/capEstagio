package View;

import Repository.AcompanhamentoRepository;
import Repository.CandidaturaRepository;
import org.example.Model.Acompanhamento;
import org.example.Model.Candidatura;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaAcompanhamento extends JInternalFrame {

    private JComboBox<Candidatura> comboCandidatura;
    private JTextField campoData;
    private JTextField campoSupervisor;
    private JTextArea campoRelatorio;

    public TelaAcompanhamento() {
        setTitle("Acompanhamento de Estágio");
        setClosable(true);
        setMaximizable(true);
        setSize(500, 560);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Acompanhamento de Estágio", 30, 10, 300, 30));

        add(Estilo.criarLabel("Candidatura:", 30, 50, 140, 25));
        comboCandidatura = new JComboBox<>();
        comboCandidatura.setBounds(180, 47, 270, 35);
        Estilo.estilizarCombo(comboCandidatura);
        carregarCandidaturas();
        add(comboCandidatura);

        add(Estilo.criarLabel("Data (DD/MM/AAAA):", 30, 98, 145, 25));
        campoData = new JTextField();
        campoData.setBounds(180, 95, 270, 35);
        Estilo.estilizarCampo(campoData);
        campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        add(campoData);

        add(Estilo.criarLabel("Supervisor:", 30, 148, 130, 25));
        campoSupervisor = new JTextField();
        campoSupervisor.setBounds(180, 145, 270, 35);
        Estilo.estilizarCampo(campoSupervisor);
        add(campoSupervisor);

        add(Estilo.criarLabel("Relatório Parcial:", 30, 195, 140, 25));
        campoRelatorio = new JTextArea();
        campoRelatorio.setLineWrap(true);
        campoRelatorio.setWrapStyleWord(true);
        campoRelatorio.setBackground(Color.WHITE);
        JScrollPane scrollRelatorio = new JScrollPane(campoRelatorio);
        scrollRelatorio.setBounds(30, 222, 420, 200);
        Estilo.estilizarScrollPane(scrollRelatorio);
        add(scrollRelatorio);

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(175, 445, 150, 40);
        Estilo.estilizarBotao(botaoSalvar);
        add(botaoSalvar);

        botaoSalvar.addActionListener(e -> {

            if (comboCandidatura.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma candidatura!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!Validador.isDataValida(campoData.getText())) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato DD/MM/AAAA.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoData, Estilo.COR_ERRO);
                return;
            }
            Estilo.aplicarBorda(campoData, Estilo.COR_BORDA);

            if (campoSupervisor.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do supervisor é obrigatório.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                Estilo.aplicarBorda(campoSupervisor, Estilo.COR_ERRO);
                return;
            }
            Estilo.aplicarBorda(campoSupervisor, Estilo.COR_BORDA);

            if (campoRelatorio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O relatório parcial não pode estar em branco.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Candidatura candidaturaSelecionada = (Candidatura) comboCandidatura.getSelectedItem();

            try {
                Acompanhamento novoAcompanhamento = new Acompanhamento(
                        candidaturaSelecionada,
                        campoData.getText().trim(),
                        campoSupervisor.getText().trim(),
                        campoRelatorio.getText().trim()
                );
                new AcompanhamentoRepository().cadastrar(novoAcompanhamento);
                JOptionPane.showMessageDialog(this, "Acompanhamento salvo com sucesso!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar acompanhamento: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void carregarCandidaturas() {
        try {
            CandidaturaRepository repo = new CandidaturaRepository();
            List<Candidatura> lista = repo.listarAtivas();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Não há candidaturas ativas no momento.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Candidatura c : lista) comboCandidatura.addItem(c);
        } catch (Exception e) {
            System.out.println("Erro ao carregar candidaturas: " + e.getMessage());
        }
    }
}
