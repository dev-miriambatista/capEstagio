package View;

import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("CapyEstagio");
        setSize(1000, 600);
        //setUndecorated(true);
        //setShape(new RoundRectangle2D.Double(0, 0, 1200, 600, 30, 30));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criando um JDesktopPane customizado com Gradiente e a marca do sistema
            // --- 1. CONFIGURANDO O ÍCONE DA JANELA ---
        try {
                java.net.URL imgUrl = getClass().getResource("/logo.png");
                if (imgUrl != null) {
                    Image iconeJanela = new ImageIcon(imgUrl).getImage();
                    setIconImage(iconeJanela); // Muda o ícone do canto superior esquerdo!
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar o ícone da janela.");
            }

            // --- 2. CONFIGURANDO O FUNDO COM MARCA D'ÁGUA ---
            JDesktopPane desktopPane = new JDesktopPane() {
                private Image imagemLogo;

                {
                    try {
                        java.net.URL imgUrl = getClass().getResource("/logo.png");
                        if (imgUrl != null) {
                            imagemLogo = new ImageIcon(imgUrl).getImage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();

                    // Fundo Gradiente
                    Color corTopo = new Color(41, 128, 185);
                    Color corBase = new Color(44, 62, 80);
                    GradientPaint gradiente = new GradientPaint(0, 0, corTopo, 0, height, corBase);
                    g2d.setPaint(gradiente);
                    g2d.fillRect(0, 0, width, height);

                    // Desenhando a Logo como Marca d'Água (Ofuscada)
                    // Desenhando a Logo como Marca d'Água (Ofuscada e Redimensionada)
                    if (imagemLogo != null) {
                        // 1. Pega o tamanho original da imagem
                        int imgWidth = imagemLogo.getWidth(null);
                        int imgHeight = imagemLogo.getHeight(null);

                        // 2. Calcula a proporção para a imagem ocupar no máximo 85% da altura ou largura da tela
                        double scaleX = (double) width / imgWidth;
                        double scaleY = (double) height / imgHeight;
                        double scale = Math.min(scaleX, scaleY) * 0.99; // 0.85 = 85% do espaço disponível

                        // 3. Aplica a escala para achar o novo tamanho
                        int newW = (int) (imgWidth * scale);
                        int newH = (int) (imgHeight * scale);

                        // 4. Calcula o X e Y para centralizar a imagem com o NOVO tamanho
                        int x = (width - newW) / 2;
                        int y = (height - newH) / 2;

                        // MÁGICA DA TRANSPARÊNCIA: 15% de visibilidade
                        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f);
                        g2d.setComposite(alpha);

                        // Desenha a imagem com o novo tamanho (newW e newH)
                        g2d.drawImage(imagemLogo, x, y, newW, newH, this);

                        // Restaura a opacidade para 100%
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                    }
                }
            };
    add(desktopPane, BorderLayout.CENTER);

        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemAluno = new JMenuItem("Aluno");
        itemAluno.addActionListener(e -> {
            TelaAluno tela = new TelaAluno();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemEmpresa = new JMenuItem("Empresa");
        itemEmpresa.addActionListener(e -> {
            TelaEmpresa tela = new TelaEmpresa();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemVaga = new JMenuItem("Vagas");
        itemVaga.addActionListener(e -> {
            TelaVagas tela = new TelaVagas();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemCandidatura = new JMenuItem("Candidaturas");
        itemCandidatura.addActionListener(e -> {
            TelaCandidaturas tela = new TelaCandidaturas();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        // Criando o item de menu para o Acompanhamento
        JMenuItem itemAcompanhamento = new JMenuItem("Acompanhamento");
        itemAcompanhamento.addActionListener(e -> {
            TelaAcompanhamento tela = new TelaAcompanhamento();
            desktopPane.add(tela); // Adiciona dentro da janela principal (MDI)
            tela.setVisible(true);
        });

        // Criando o item de menu para o Encerramento
        JMenuItem itemEncerramento = new JMenuItem("Encerramento");
        itemEncerramento.addActionListener(e -> {
            TelaEncerramento tela = new TelaEncerramento();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));

        menuCadastros.add(itemAluno);
        menuCadastros.add(itemEmpresa);
        menuCadastros.add(itemVaga);
        menuCadastros.add(itemCandidatura);
        menuCadastros.add(itemAcompanhamento); // Adiciona o botão ao menu suspenso
        menuCadastros.add(itemEncerramento);
        menuCadastros.add(itemSair);

        JMenu menuRelatorios = new JMenu("Relatórios");

        JMenuItem itemAlunoRel = new JMenuItem("Aluno");
        itemAlunoRel.addActionListener(e -> {
            ListarAluno tela = new ListarAluno();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemEmpresaRel = new JMenuItem("Empresa");
        itemEmpresaRel.addActionListener(e -> {
            ListarEmpresa tela = new ListarEmpresa();
            desktopPane.add(tela);
            tela.setVisible(true);
        });
        JMenuItem itemVagaRel = new JMenuItem("Vagas");
        itemVagaRel.addActionListener(e -> {
            ListarVagas tela = new ListarVagas();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemCandidaturaRel = new JMenuItem("Candidaturas");
        itemCandidaturaRel.addActionListener(e -> {
            ListarCandidaturas tela = new ListarCandidaturas();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemAcompanhamentoRel = new JMenuItem("Acompanhamentos");
        itemAcompanhamentoRel.addActionListener(e -> {
            ListarAcompanhamento tela = new ListarAcompanhamento();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        JMenuItem itemEncerramentoRel = new JMenuItem("Encerramentos");
        itemEncerramentoRel.addActionListener(e -> {
            ListarEncerramento tela = new ListarEncerramento();
            desktopPane.add(tela);
            tela.setVisible(true);
        });

        menuRelatorios.add(itemAlunoRel);
        menuRelatorios.add(itemEmpresaRel);
        menuRelatorios.add(itemVagaRel);
        menuRelatorios.add(itemCandidaturaRel);
        menuRelatorios.add(itemAcompanhamentoRel);
        menuRelatorios.add(itemEncerramentoRel);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuCadastros);
        menuBar.add(menuRelatorios);
        setJMenuBar(menuBar);

        setVisible(true);
    }
}