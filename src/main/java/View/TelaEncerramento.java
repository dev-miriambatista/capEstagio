package View;

import Repository.CandidaturaRepository;
import Repository.EncerramentoRepository;
import org.example.Model.Candidatura;
import org.example.Model.Encerramento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaEncerramento extends JInternalFrame {

    private JComboBox<Candidatura> comboCandidatura;
    private JTextField campoData;
    private JRadioButton radioConcluido;
    private JRadioButton radioInterrompido;
    private JTextArea campoJustificativa;

    public TelaEncerramento() {
        setTitle("Encerramento de Estágio");
        setClosable(true);
        setMaximizable(true);
        setSize(500, 520);
        setLayout(null);

        JPanel painelFundo = Estilo.criarPainelGradiente();
        setContentPane(painelFundo);

        add(Estilo.criarLabelTitulo("Encerramento de Estágio", 30, 10, 300, 30));

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

        add(Estilo.criarLabel("Status Final:", 30, 148, 130, 25));
        radioConcluido = new JRadioButton("Concluído");
        radioConcluido.setBounds(180, 148, 110, 25);
        Estilo.estilizarRadio(radioConcluido);

        radioInterrompido = new JRadioButton("Interrompido");
        radioInterrompido.setBounds(300, 148, 140, 25);
        Estilo.estilizarRadio(radioInterrompido);

        ButtonGroup grupoStatus = new ButtonGroup();
        grupoStatus.add(radioConcluido);
        grupoStatus.add(radioInterrompido);
        radioConcluido.setSelected(true);

        add(radioConcluido);
        add(radioInterrompido);

        add(Estilo.criarLabel("Justificativa:", 30, 185, 130, 25));
        campoJustificativa = new JTextArea();
        campoJustificativa.setLineWrap(true);
        campoJustificativa.setWrapStyleWord(true);
        campoJustificativa.setBackground(Color.WHITE);
        JScrollPane scrollJustificativa = new JScrollPane(campoJustificativa);
        scrollJustificativa.setBounds(30, 215, 420, 180);
        Estilo.estilizarScrollPane(scrollJustificativa);
        add(scrollJustificativa);

        JButton botaoEncerrar = new JButton("Encerrar Estágio");
        botaoEncerrar.setBounds(155, 418, 180, 40);
        Estilo.estilizarBotao(botaoEncerrar);
        add(botaoEncerrar);

        botaoEncerrar.addActionListener(e -> {

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

            if (campoJustificativa.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "A justificativa é obrigatória.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Candidatura candidaturaSelecionada = (Candidatura) comboCandidatura.getSelectedItem();

            EncerramentoRepository encerramentoRepo = new EncerramentoRepository();
            if (encerramentoRepo.jaEncerrada(candidaturaSelecionada.getId())) {
                JOptionPane.showMessageDialog(this,
                        "Esta candidatura já foi encerrada anteriormente.\nNão é possível encerrar duas vezes.",
                        "Operação Não Permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String statusFinal = radioConcluido.isSelected() ? "Concluído" : "Interrompido";

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Confirma o encerramento desta candidatura como \"" + statusFinal + "\"?\nEsta ação não poderá ser desfeita.",
                    "Confirmar Encerramento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacao != JOptionPane.YES_OPTION) return;

            try {
                Encerramento encerramento = new Encerramento(
                        candidaturaSelecionada,
                        campoData.getText().trim(),
                        statusFinal,
                        campoJustificativa.getText().trim()
                );
                encerramentoRepo.cadastrar(encerramento);
                new CandidaturaRepository().atualizarStatus(candidaturaSelecionada.getId(), statusFinal);
                JOptionPane.showMessageDialog(this, "Estágio encerrado com sucesso!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao encerrar estágio: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void carregarCandidaturas() {
        try {
            List<Candidatura> lista = new CandidaturaRepository().listarAtivas();
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Não há candidaturas ativas para encerrar.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Candidatura c : lista) comboCandidatura.addItem(c);
        } catch (Exception e) {
            System.out.println("Erro ao carregar candidaturas: " + e.getMessage());
        }
    }
}
