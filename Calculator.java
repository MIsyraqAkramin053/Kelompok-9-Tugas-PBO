import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/** Kalkulator bergaya iOS (tema glass) - Java Swing. Jalankan: javac CalculatorGlass.java && java CalculatorGlass */
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

    /** Panel latar: gradasi krem, lingkaran oranye, dan kartu kaca. */
    static class GlassBackground extends JPanel {
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(new Color(0xF0, 0xE8, 0xDE));
            g.fillRect(0, 0, w, h);

            // lingkaran oranye di belakang kartu (memberi efek tembus kaca)
            float r = 260;
            float cx = w * 0.62f, cy = h * 0.68f;
            g.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), r,
                new float[]{0f, 0.8f, 1f},
                new Color[]{new Color(0xE0, 0x93, 0x4A), new Color(0xF0, 0xB3, 0x5A), new Color(0xF0, 0xB3, 0x5A, 0)}));
            g.fill(new Ellipse2D.Float(cx - r, cy - r, 2 * r, 2 * r));

            // bayangan kartu
            RoundRectangle2D card = new RoundRectangle2D.Float(14, 14, w - 28, h - 28, 56, 56);
            for (int i = 8; i >= 1; i--) {
                g.setColor(new Color(90, 60, 30, 5));
                g.fill(new RoundRectangle2D.Float(14 - i, 14 - i + 6, w - 28 + 2 * i, h - 28 + 2 * i, 56 + i, 56 + i));
            }
            // badan kaca
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

        // ---- Header (ikon riwayat) ----
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

        // ---- Display ----
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

        // ---- Tombol ----
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

    // ---------- Logika ----------
    private void press(String k) {
        if (err && !k.equals("AC")) return;
        switch (k) {
            case "AC": cur = "0"; left = null; op = null; newEntry = true; err = false; exprText = ""; break;
            case "+/-":
                if (!cur.equals("0")) cur = cur.startsWith("-") ? cur.substring(1) : "-" + cur;
                break;
            case "%": cur = clean(new BigDecimal(cur).divide(BigDecimal.valueOf(100))); break;
            case ".":
                if (newEntry) { cur = "0"; newEntry = false; if (op == null) exprText = ""; }
                if (!cur.contains(".")) cur += ".";
                break;
            case "=": equal(); break;
            case DIV: case MUL: case SUB: case ADD: operator(k); break;
            default: digit(k);
        }
        update();
    }

    private void digit(String d) {
        if (newEntry) { cur = "0"; newEntry = false; if (op == null) exprText = ""; }
        if (cur.equals("0")) cur = d;
        else if (cur.replaceAll("[-.]", "").length() < 12) cur += d;
    }

    private void operator(String o) {
        BigDecimal v = new BigDecimal(cur);
        if (op != null && !newEntry) {
            try { v = calc(left, v, op); } catch (ArithmeticException ex) { error(); return; }
            cur = clean(v);
        }
        left = v;
        op = o;
        newEntry = true;
        exprText = fmt(clean(left)) + " " + o;
    }

    private void equal() {
        if (op == null) return;
        BigDecimal b = new BigDecimal(cur);
        try {
            BigDecimal res = calc(left, b, op);
            exprText = fmt(clean(left)) + " " + op + " " + fmt(clean(b));
            history.add(new String[]{exprText, fmt(clean(res))});
            cur = clean(res);
            op = null;
            newEntry = true;
        } catch (ArithmeticException ex) { error(); }
    }

    private void error() { err = true; cur = "Error"; op = null; left = null; exprText = ""; newEntry = true; update(); }

    private BigDecimal calc(BigDecimal a, BigDecimal b, String o) {
        if (o.equals(ADD)) return a.add(b);
        if (o.equals(SUB)) return a.subtract(b);
        if (o.equals(MUL)) return a.multiply(b);
        if (b.signum() == 0) throw new ArithmeticException("div0");
        return a.divide(b, 12, RoundingMode.HALF_UP);
    }

    private static String clean(BigDecimal v) {
        v = v.stripTrailingZeros();
        if (v.scale() < 0) v = v.setScale(0);
        return v.toPlainString();
    }

    private static String fmt(String s) {
        if (s.equals("Error")) return s;
        boolean neg = s.startsWith("-");
        if (neg) s = s.substring(1);
        int dot = s.indexOf('.');
        String ip = dot >= 0 ? s.substring(0, dot) : s;
        String fp = dot >= 0 ? s.substring(dot) : "";
        return (neg ? "-" : "") + new DecimalFormat("#,##0").format(new BigInteger(ip)) + fp;
    }

    private void update() {
        String t = fmt(cur);
        resLabel.setText(t);
        int len = t.length();
        resLabel.setFont(new Font(FONT, Font.PLAIN, len <= 9 ? 48 : len <= 12 ? 38 : 30));
        exprLabel.setText(exprText.isEmpty() ? " " : exprText);
        int n = history.size();
        for (int i = 0; i < 2; i++) {
            int idx = n - 2 + i;
            boolean has = idx >= 0;
            hExpr[i].setText(has ? history.get(idx)[0] : " ");
            hRes[i].setText(has ? history.get(idx)[1] : " ");
        }
    }

    // ---------- Riwayat ----------
    private void showHistory() {
        JDialog d = new JDialog(this, "History", true);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(0xF7, 0xF2, 0xEC));
        area.setForeground(DISPLAY);
        area.setFont(new Font(FONT, Font.PLAIN, 15));
        area.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        StringBuilder sb = new StringBuilder();
        for (int i = history.size() - 1; i >= 0; i--)
            sb.append(history.get(i)[0]).append("  =  ").append(history.get(i)[1]).append("\n");
        area.setText(history.isEmpty() ? "Belum ada riwayat." : sb.toString());
        JButton clear = new JButton("Hapus Riwayat");
        clear.addActionListener(e -> { history.clear(); update(); d.dispose(); });
        d.add(new JScrollPane(area), BorderLayout.CENTER);
        d.add(clear, BorderLayout.SOUTH);
        d.setSize(320, 360);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    // ---------- Keyboard ----------
    private void bindKeys() {
        for (char ch = '0'; ch <= '9'; ch++) bind(KeyStroke.getKeyStroke(ch), String.valueOf(ch));
        bind(KeyStroke.getKeyStroke('.'), ".");
        bind(KeyStroke.getKeyStroke('+'), ADD);
        bind(KeyStroke.getKeyStroke('-'), SUB);
        bind(KeyStroke.getKeyStroke('*'), MUL);
        bind(KeyStroke.getKeyStroke('/'), DIV);
        bind(KeyStroke.getKeyStroke('%'), "%");
        bind(KeyStroke.getKeyStroke('='), "=");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "AC");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BS");
    }

    private void bind(KeyStroke ks, String key) {
        String id = "k" + ks;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, id);
        getRootPane().getActionMap().put(id, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (key.equals("BS")) {
                    if (!newEntry && !err) {
                        cur = cur.length() > 1 ? cur.substring(0, cur.length() - 1) : "0";
                        if (cur.equals("-")) cur = "0";
                        update();
                    }
                } else press(key);
            }
        });
    }

    // ---------- Tombol kaca ----------
    static class GlassButton extends JButton {
        private final boolean operator;

        GlassButton(String text, boolean operator) {
            super(text);
            this.operator = operator;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(text.equals("0") ? new Dimension(150, 68) : new Dimension(68, 68));
        }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            boolean wide = getText().equals("0");
            int margin = 4;
            int h = wide ? Math.min(getHeight(), getWidth() / 2) - margin : Math.min(getWidth(), getHeight()) - margin;
            int w = wide ? getWidth() - margin : h;
            int x = wide ? margin / 2 : (getWidth() - h) / 2;
            int y = (getHeight() - h) / 2 - 1;
            boolean down = getModel().isPressed();
            if (down) { y += 1; }

            // bayangan lembut
            for (int i = 5; i >= 1; i--) {
                g.setColor(new Color(110, 70, 30, down ? 4 : 7));
                g.fillRoundRect(x - i / 2, y + 3 - i / 2 + i, w + i, h + i, h + i, h + i);
            }
            // badan kaca (gradasi tembus pandang)
            Color top, bottom;
            if (operator) {
                top = new Color(255, 160, 20, 255);
                bottom = new Color(255, 140, 0, 215);
            } else {
                top = new Color(255, 250, 242, 245);
                bottom = new Color(250, 225, 195, 160);
            }
            if (getModel().isRollover() && !down) {
                top = brighten(top);
                bottom = brighten(bottom);
            }
            g.setPaint(new GradientPaint(0, y, top, 0, y + h, bottom));
            g.fillRoundRect(x, y, w, h, h, h);

            // kilau bagian atas
            Shape old = g.getClip();
            g.setClip(new RoundRectangle2D.Float(x, y, w, h, h, h));
            g.setPaint(new GradientPaint(0, y, new Color(255, 255, 255, operator ? 90 : 150),
                0, y + h * 0.55f, new Color(255, 255, 255, 0)));
            g.fillRoundRect(x + 2, y + 1, w - 4, (int) (h * 0.55), h, h);
            g.setClip(old);

            // garis tepi kaca
            g.setStroke(new BasicStroke(1.2f));
            g.setPaint(new GradientPaint(0, y, new Color(255, 255, 255, 230),
                0, y + h, new Color(255, 255, 255, 80)));
            g.drawRoundRect(x, y, w - 1, h - 1, h, h);

            // teks
            g.setColor(operator ? Color.WHITE : TEXT);
            g.setFont(new Font(FONT, Font.PLAIN, 26));
            FontMetrics fm = g.getFontMetrics();
            String t = getText();
            int tx = wide ? x + 28 : x + (w - fm.stringWidth(t)) / 2;
            int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(t, tx, ty);
            g.dispose();
        }

        private static Color brighten(Color c) {
            return new Color(Math.min(255, c.getRed() + 12), Math.min(255, c.getGreen() + 12),
                Math.min(255, c.getBlue() + 12), c.getAlpha());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }
}