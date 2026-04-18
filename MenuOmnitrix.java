import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
        int yText = height / 2 + 8;

        g2.setColor(isHovered ? hoverLabelColor : labelColor);
        g2.drawString(buttonText, xText, yText);

        super.paintComponent(g);
    }
}

public class MenuOmnitrix extends JFrame {

    private static final String IMG_CREDITOS = "menu_creditos.png";
    private static final String IMG_JUGAR = "menu_jugar.png";
    private static final String IMG_ACTUALIZACIONES = "menu_actualizaciones.png";
    private static final String IMG_CONFIG = "menu_config.png";

    public MenuOmnitrix() {
        setTitle("OMNITRIX - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
     
        JPanel panelPrincipal = new JPanel() {
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
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        Dimension buttonSize = new Dimension(400, 70);

        JLabel lblTituloText = new JLabel("PROYECTO GEOMETRÍA");
        lblTituloText.setFont(new Font("sansserif", Font.BOLD, 50));
        lblTituloText.setForeground(Color.WHITE);
        lblTituloText.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(Box.createVerticalGlue());
        panelPrincipal.add(lblTituloText);
        panelPrincipal.add(Box.createVerticalStrut(50));

        ImagenButton btnJugar = new ImagenButton(IMG_JUGAR, "Jugar");
        btnJugar.setMaximumSize(buttonSize);
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImagenButton btnCreditos = new ImagenButton(IMG_CREDITOS, "Créditos");
        btnCreditos.setMaximumSize(buttonSize);
        btnCreditos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreditos.addActionListener(e -> mostrarVentanaCreditos());

        ImagenButton btnActualizaciones = new ImagenButton(IMG_ACTUALIZACIONES, "Actualizaciones");
        btnActualizaciones.setMaximumSize(buttonSize);
        btnActualizaciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImagenButton btnConfig = new ImagenButton(IMG_CONFIG, "Configuración");
        btnConfig.setMaximumSize(buttonSize);
        btnConfig.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfig.addActionListener(e -> mostrarVentanaConfiguracion());

        panelPrincipal.add(btnJugar);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnCreditos);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnActualizaciones);
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(btnConfig);
        panelPrincipal.add(Box.createVerticalGlue());

        add(panelPrincipal);
    }

    private void mostrarVentanaCreditos() {
        JDialog ventanaCreditos = new JDialog(this, "Créditos", true);
        ventanaCreditos.setUndecorated(true);
        ventanaCreditos.setSize(400, 500);
        ventanaCreditos.setLocationRelativeTo(this);

        JPanel panelFondo = crearPanelVentana();
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("DESARROLLADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 255, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelFondo.add(lblTitulo);

        String[] nombres = {"Usuario 1", "Usuario 2", "Usuario 3", "Usuario 4", "Usuario 5", "Usuario 6", "Usuario 7", "Usuario 8"};
        for (String nombre : nombres) {
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
            lblNombre.setForeground(Color.WHITE);
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelFondo.add(lblNombre);
        }

        panelFondo.add(Box.createVerticalGlue());
        JButton btnCerrar = new JButton("X");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> ventanaCreditos.dispose());
        panelFondo.add(btnCerrar);
        panelFondo.add(Box.createVerticalStrut(20));

        ventanaCreditos.add(panelFondo);
        ventanaCreditos.setVisible(true);
    }

    private void mostrarVentanaConfiguracion() {
        JDialog ventanaConfig = new JDialog(this, "Configuración", true);
        ventanaConfig.setUndecorated(true);
        ventanaConfig.setSize(400, 450);
        ventanaConfig.setLocationRelativeTo(this);

        JPanel panelFondo = crearPanelVentana();
        panelFondo.setLayout(new BoxLayout(panelFondo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("CONFIGURACIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 255, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnSonidoOn = new JButton("Encender Sonido");
        JButton btnSonidoOff = new JButton("Apagar Sonido");
        JButton btnBorrarDatos = new JButton("Borrar Datos de Partida");
        JButton btnCerrar = new JButton("X");

        btnSonidoOn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSonidoOff.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBorrarDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSonidoOn.addActionListener(e -> System.out.println("Sonido activado"));
        btnSonidoOff.addActionListener(e -> System.out.println("Sonido desactivado"));
        
        btnBorrarDatos.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(ventanaConfig, "¿Seguro que deseas borrar los datos?");
            if (confirm == JOptionPane.YES_OPTION) System.out.println("Datos borrados");
        });

        btnCerrar.addActionListener(e -> ventanaConfig.dispose());

        panelFondo.add(lblTitulo);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(btnSonidoOn);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(btnSonidoOff);
        panelFondo.add(Box.createVerticalStrut(10));
        panelFondo.add(btnBorrarDatos);
        panelFondo.add(Box.createVerticalGlue());
        panelFondo.add(btnCerrar);
        panelFondo.add(Box.createVerticalStrut(20));

        ventanaConfig.add(panelFondo);
        ventanaConfig.setVisible(true);
    }
    

    private JPanel crearPanelVentana() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 15, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(0, 255, 0));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 30, 30);
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createPlaceholderImages();
            new MenuOmnitrix().setVisible(true);
        });
    }

    private static void createPlaceholderImages() {
        createImage(IMG_JUGAR, Color.decode("#a3705f"), "");
        createImage(IMG_CREDITOS, Color.decode("#6b8e23"), "");
        createImage(IMG_ACTUALIZACIONES, Color.decode("#1e90ff"), "");
        createImage(IMG_CONFIG, Color.decode("#555555"), "");
    }

    private static void createImage(String path, Color color, String label) {
        File file = new File(path);
        if (file.exists()) return;
        BufferedImage img = new BufferedImage(400, 70, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, 400, 70);
        g2.dispose();
        try { ImageIO.write(img, "png", file); } catch (IOException e) { e.printStackTrace(); }
    }
}