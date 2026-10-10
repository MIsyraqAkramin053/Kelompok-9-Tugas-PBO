import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {

    static final Color TEXT = new Color(0x5B, 0x46, 0x36);
    static final Color DISPLAY = new Color(0x2A, 0x2A, 0x2A);
    static final Color MUTED = new Color(0x9A, 0x8C, 0x80);
    static final String DIV = "\u00F7", MUL = "\u00D7", SUB = "\u2212", ADD = "+";
    static final String FONT = "SansSerif";

    private String cur = "0";
    private BigDecimal left;
    private String op;
    private boolean newEntry = true, err = false;
    private String exprText = "";
    private final List<String[]> history = new ArrayList<>();

    private final JLabel[] hExpr = {new JLabel(" "), new JLabel(" ")};
    private final JLabel[] hRes = {new JLabel(" "), new JLabel(" ")};
    private final JLabel exprLabel = new JLabel(" ");
    private final JLabel resLabel = new JLabel("0");

    static class GlassBackground extends JPanel {
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(new Color(0xF0, 0xE8, 0xDE));
            g.fillRect(0, 0, w, h);

            float r = 260;
            float cx = w * 0.62f, cy = h * 0.68f;
            g.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), r,
                new float[]{0f, 0.8f, 1f},
                new Color[]{new Color(0xE0, 0x93, 0x4A), new Color(0xF0, 0xB3, 0x5A), new Color(0xF0, 0xB3, 0x5A, 0)}));
            g.fill(new Ellipse2D.Float(cx - r, cy - r, 2 * r, 2 * r));

            RoundRectangle2D card = new RoundRectangle2D.Float(14, 14, w - 28, h - 28, 56, 56);
            for (int i = 8; i >= 1; i--) {
                g.setColor(new Color(90, 60, 30, 5));
                g.fill(new RoundRectangle2D.Float(14 - i, 14 - i + 6, w - 28 + 2 * i, h - 28 + 2 * i, 56 + i, 56 + i));
            }

            g.setPaint(new GradientPaint(0, 14, new Color(255, 255, 255, 235), 0, h - 14, new Color(255, 255, 255, 70)));
            g.fill(card);
            g.setPaint(new GradientPaint(0, 14, new Color(255, 255, 255, 220), 0, h - 14, new Color(255, 255, 255, 60)));
            g.setStroke(new BasicStroke(1.5f));
            g.draw(card);
            g.dispose();
        }
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        JPanel root = new GlassBackground();
        root.setLayout(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(34, 34, 40, 34));
        setContentPane(root);

        JButton hist = new JButton() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(DISPLAY);
                g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g.draw(new Arc2D.Double(cx - 8, cy - 8, 16, 16, 90, 300, Arc2D.OPEN));
                g.drawLine(cx, cy, cx, cy - 5);
                g.drawLine(cx, cy, cx + 4, cy + 2);
                g.drawLine(cx - 8, cy - 3, cx - 11, cy - 7);
                g.drawLine(cx - 8, cy - 3, cx - 4, cy - 6);
                g.dispose();
            }
        };
        hist.setPreferredSize(new Dimension(36, 36));
        hist.setContentAreaFilled(false);
        hist.setBorderPainted(false);
        hist.setFocusPainted(false);
        hist.setFocusable(false);
        hist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hist.addActionListener(e -> showHistory());
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setOpaque(false);
        header.add(hist);
        // Display
        JPanel display = new JPanel();
        display.setOpaque(false);
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        display.setPreferredSize(new Dimension(300, 190));
        for (int i = 0; i < 2; i++) {
            style(hExpr[i], 11, MUTED);
            style(hRes[i], 11, MUTED);
            display.add(hExpr[i]);
            display.add(hRes[i]);
            display.add(Box.createVerticalStrut(8));
        }
        display.add(Box.createVerticalGlue());
        style(exprLabel, 18, MUTED);
        style(resLabel, 48, DISPLAY);
        display.add(exprLabel);
        display.add(resLabel);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(header, BorderLayout.NORTH);
        top.add(display, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        // Tombol
        JPanel pad = new JPanel(new GridBagLayout());
        pad.setOpaque(false);
        String[][] rows = {
            {"AC", "+/-", "%", DIV},
            {"7", "8", "9", MUL},
            {"4", "5", "6", SUB},
            {"1", "2", "3", ADD},
            {"0", ".", "="}
        };
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
        for (int r = 0; r < rows.length; r++) {
            int col = 0;
            for (String label : rows[r]) {
                c.gridy = r;
                c.gridx = col;
                c.gridwidth = label.equals("0") ? 2 : 1;
                GlassButton b = new GlassButton(label, isOperator(label));
                b.addActionListener(e -> press(label));
                pad.add(b, c);
                col += c.gridwidth;
            }
        }
        root.add(pad, BorderLayout.CENTER);

        bindKeys();
        setSize(400, 740);
        setLocationRelativeTo(null);
        update();
    }

    private static boolean isOperator(String l) {
        return l.equals(DIV) || l.equals(MUL) || l.equals(SUB) || l.equals(ADD) || l.equals("=");
    }

    private static void style(JLabel l, int size, Color col) {
        l.setFont(new Font(FONT, Font.PLAIN, size));
        l.setForeground(col);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setAlignmentX(Component.RIGHT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, l.getPreferredSize().height + 6));
    }

UH
    
