package View;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class Estilo {

    // ── PALETA DE CORES ──────────────────────────────────────────────────────
    public static final Color COR_TOPO      = new Color(41, 128, 185);
    public static final Color COR_BASE      = new Color(44, 62, 80);
    public static final Color COR_BORDA     = Color.GRAY;
    public static final Color COR_ERRO      = Color.RED;
    public static final Color COR_FUNDO     = Color.WHITE;

    // ── PAINEL COM GRADIENTE ─────────────────────────────────────────────────
    /**
     * Cria um JPanel com o gradiente padrão do sistema (igual ao TelaPrincipal).
     * Basta usar como contentPane: setContentPane(Estilo.criarPainelGradiente());
     */
    public static JPanel criarPainelGradiente() {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradiente = new GradientPaint(
                        0, 0, COR_TOPO, 0, getHeight(), COR_BASE);
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painel.setLayout(null);
        return painel;
    }

    // ── LABEL TÍTULO (branco, bold) ───────────────────────────────────────────
    public static JLabel criarLabelTitulo(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, w, h);
        return label;
    }

    // ── LABEL DE CAMPO (branco) ───────────────────────────────────────────────
    public static JLabel criarLabel(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, w, h);
        return label;
    }

    // ── BORDA ARREDONDADA ─────────────────────────────────────────────────────
    public static void aplicarBorda(JTextField campo, Color cor) {
        campo.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y,
                                    int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, width - 1, height - 1, 12, 12);
                g2.dispose();
            }
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(8, 12, 8, 12);
            }
        });
    }

    // ── CAMPO DE TEXTO ─────────────────────────────────────────────────────────
    public static void estilizarCampo(JTextField campo) {
        aplicarBorda(campo, COR_BORDA);
        campo.setOpaque(true);
        campo.setBackground(COR_FUNDO);
        campo.setForeground(Color.BLACK);
        campo.setCaretColor(Color.BLACK);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    // ── COMBO BOX ─────────────────────────────────────────────────────────────
    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(Color.BLACK);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    // ── SCROLL / TEXT AREA ───────────────────────────────────────────────────
    public static void estilizarScrollPane(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(COR_TOPO, 1));
    }

    // ── RADIO BUTTON ─────────────────────────────────────────────────────────
    public static void estilizarRadio(JRadioButton radio) {
        radio.setOpaque(false);
        radio.setForeground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    // ── TABELA ────────────────────────────────────────────────────────────────
    public static void estilizarTabela(JTable tabela, int[] larguras) {
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(28);
        tabela.setGridColor(new Color(200, 200, 200));
        tabela.setSelectionBackground(COR_TOPO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(Color.DARK_GRAY);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_BASE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));

        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    // ── BOTÃO ─────────────────────────────────────────────────────────────────
    public static void estilizarBotao(JButton botao) {
        botao.setFocusPainted(false);
        botao.setBackground(new Color(230, 230, 230));
        botao.setForeground(Color.BLACK);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
