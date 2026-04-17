import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// --- CLASE DEL BOTÓN DE IMAGEN ---
class ImagenButton extends JButton {
    private BufferedImage originalImage;
    private BufferedImage scaledImage;
    private boolean isHovered = false;
    private Color labelColor = Color.WHITE;
    private Color hoverLabelColor = new Color(150, 255, 150); 
    private String buttonText;

    public ImagenButton(String imagePath, String text) {
        super("");
        this.buttonText = text;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            originalImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error cargando imagen: " + imagePath);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();

        if (originalImage != null) {
            if (scaledImage == null || scaledImage.getWidth() != width || scaledImage.getHeight() != height) {
                scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gImg = scaledImage.createGraphics();
                gImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gImg.drawImage(originalImage, 0, 0, width, height, null);
                gImg.dispose();
            }
            g2.drawImage(scaledImage, 0, 0, null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, width, height);
        }

        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRect(0, 0, width, height);
        }

        Font font = new Font("Arial", Font.PLAIN, 20);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);

        int xText = (width - textWidth) / 2;
        int yText = height - 40;

        g2.setColor(isHovered ? hoverLabelColor : labelColor);
        g2.drawString(buttonText, xText, yText);

        g2.setColor(Color.WHITE);
        int lineWidth = textWidth + 20;
        int xLine = (width - lineWidth) / 2;
        int yLine = yText + 10;
        g2.fillRect(xLine, yLine, lineWidth, 2);

        super.paintComponent(g);
    }
}

// --- CLASE PRINCIPAL ---
public class MenuOmnitrix extends JFrame {

    private static final String IMG_JUGAR = "menu_jugar.png"; 
    private static final String IMG_CREDITOS = "menu_creditos.png"; 
    private static final String IMG_ACTUALIZACIONES = "menu_actualizaciones.png"; 

    public MenuOmnitrix() {
        setTitle("OMNITRIX - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        JPanel panelPrincipal = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                Point2D center = new Point2D.Float(width / 2f, height / 2f);
                float radius = Math.max(width, height);
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(10, 40, 90), new Color(5, 15, 40)};
                RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
                g2.setPaint(p);
                g2.fillRect(0, 0, width, height);

                g2.setColor(new Color(0, 255, 0, 60));
                g2.setStroke(new BasicStroke(2));
                int numLines = 30;
                for (int i = 0; i < numLines; i++) {
                    double angle = 2 * Math.PI * i / numLines;
                    int x2 = (int) (width / 2 + Math.cos(angle) * width);
                    int y2 = (int) (height / 2 + Math.sin(angle) * height);
                    g2.drawLine(width / 2, height / 2, x2, y2);
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelTitulo.setOpaque(false);

        JPanel simbolo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 255, 0));
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(5, 5, 40, 40);
                g2.drawLine(15, 15, 35, 35);
                g2.drawLine(15, 35, 35, 15);
            }
        };
        simbolo.setPreferredSize(new Dimension(50, 50));
        simbolo.setOpaque(false);
        panelTitulo.add(simbolo);

        JLabel lblTituloText = new JLabel("JUEGO");
        lblTituloText.setFont(new Font("sansserif", Font.BOLD, 60));
        lblTituloText.setForeground(Color.WHITE);
        panelTitulo.add(lblTituloText);

        JPanel lineaTitulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 255, 0));
                g.fillRect(0, 0, getWidth(), 4);
            }
        };
        lineaTitulo.setPreferredSize(new Dimension(400, 4));
        lineaTitulo.setOpaque(false);

        JPanel contenedorTitulo = new JPanel(new BorderLayout());
        contenedorTitulo.setOpaque(false);
        contenedorTitulo.add(panelTitulo, BorderLayout.CENTER);
        contenedorTitulo.add(lineaTitulo, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(50, 0, 80, 0);
        panelPrincipal.add(contenedorTitulo, gbc);

        // Botones
        Dimension buttonSize = new Dimension(280, 450);

        ImagenButton btnJugar = new ImagenButton(IMG_JUGAR, "Jugar");
        btnJugar.setPreferredSize(buttonSize);

        ImagenButton btnCreditos = new ImagenButton(IMG_CREDITOS, "Créditos");
        btnCreditos.setPreferredSize(buttonSize);
        // --- ACCIÓN DE CRÉDITOS ---
        btnCreditos.addActionListener(e -> mostrarVentanaCreditos());

        ImagenButton btnActualizaciones = new ImagenButton(IMG_ACTUALIZACIONES, "Registro de actualizaciones");
        btnActualizaciones.setPreferredSize(buttonSize);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 30, 0, 30);
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrincipal.add(btnJugar, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panelPrincipal.add(btnCreditos, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelPrincipal.add(btnActualizaciones, gbc);

        JButton btnSalir = new JButton("X Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSalir.setForeground(Color.LIGHT_GRAY);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridx = 2; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(50, 0, 20, 20);
        panelPrincipal.add(btnSalir, gbc);

        add(panelPrincipal);
    }

    // --- NUEVO MÉTODO PARA MOSTRAR LA PESTAÑA DE CRÉDITOS ---
    private void mostrarVentanaCreditos() {
        JDialog ventanaCreditos = new JDialog(this, "Créditos", true);
        ventanaCreditos.setUndecorated(true); // Estilo limpio sin bordes de Windows
        ventanaCreditos.setSize(400, 500);
        ventanaCreditos.setLocationRelativeTo(this);

        // Panel con diseño del Omnitrix
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo oscuro
                g2.setColor(new Color(15, 15, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                // Borde verde neón
                g2.setColor(new Color(0, 255, 0));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 30, 30);
            }
        };
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));
        panelFondo.setOpaque(false);

        // Título de la pestaña
        JLabel lblTitulo = new JLabel("DESARROLLADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 255, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Lista de nombres (8 nombres como pediste)
        String[] nombres = {
            "Usuario 1",
            "Usuario 2",
            "Usuario 3",
            "Usuario 4",
            "Usuario 5",
            "Usuario 6",
            "Usuario 7",
            "Usuario 8"
        };

        panelFondo.add(lblTitulo);

        for (String nombre : nombres) {
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNombre.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            panelFondo.add(lblNombre);
        }

        // Botón para cerrar la pestaña
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.setBackground(new Color(0, 255, 0));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> ventanaCreditos.dispose());
        
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(btnCerrar);
        panelFondo.add(Box.createVerticalStrut(20));

        ventanaCreditos.add(panelFondo);
        ventanaCreditos.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createPlaceholderImages();
            new MenuOmnitrix().setVisible(true);
        });
    }

    private static void createPlaceholderImages() {
        createImage(IMG_JUGAR, Color.decode("#a3705f"), "JUGAR");
        createImage(IMG_CREDITOS, Color.decode("#6b8e23"), "CRÉDITOS");
        createImage(IMG_ACTUALIZACIONES, Color.decode("#1e90ff"), "UPDATES");
    }

    private static void createImage(String path, Color color, String label) {
        File file = new File(path);
        if (file.exists()) return;
        BufferedImage img = new BufferedImage(280, 450, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, 280, 450);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, (280 - fm.stringWidth(label)) / 2, 225);
        g2.dispose();
        try { ImageIO.write(img, "png", file); } catch (IOException e) { e.printStackTrace(); }
    }
}