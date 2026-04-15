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
    private Color hoverLabelColor = new Color(150, 255, 150); // Verde suave al pasar el mouse
    private String buttonText;

    public ImagenButton(String imagePath, String text) {
        super(""); // No poner texto directo en el JButton
        this.buttonText = text;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            originalImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error cargando imagen: " + imagePath);
            // Imagen por defecto o manejo de error si no se encuentra
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

        // 1. Dibujar Imagen de Fondo
        if (originalImage != null) {
            // Escalar imagen si es necesario (para mejor rendimiento, esto debería hacerse una vez al redimensionar)
            if (scaledImage == null || scaledImage.getWidth() != width || scaledImage.getHeight() != height) {
                scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gImg = scaledImage.createGraphics();
                gImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gImg.drawImage(originalImage, 0, 0, width, height, null);
                gImg.dispose();
            }
            g2.drawImage(scaledImage, 0, 0, null);
        } else {
            // Fondo de color de respaldo si no hay imagen
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, width, height);
        }

        // 2. Efecto de Hover (Sombra o resplandor suave)
        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 80)); // Sombra negra semi-transparente
            g2.fillRect(0, 0, width, height);
        }

        // 3. Dibujar Texto y Línea inferior (como en la imagen)
        Font font = new Font("Arial", Font.PLAIN, 20); // Fuente más limpia y similar
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);
        int textHeight = fm.getHeight();

        int xText = (width - textWidth) / 2;
        int yText = height - 40; // Posición vertical del texto

        // Color del texto (con efecto de hover)
         g2.setColor(isHovered ? hoverLabelColor : labelColor);
        g2.drawString(buttonText, xText, yText);

        // Dibujar la línea blanca debajo del texto
        g2.setColor(Color.WHITE);
        int lineWidth = textWidth + 20;
        int xLine = (width - lineWidth) / 2;
        int yLine = yText + 10;
        g2.fillRect(xLine, yLine, lineWidth, 2);

        super.paintComponent(g);
    }
}

// --- CLASE PRINCIPAL DEL MENÚ OMNITRIX ---
public class MenuOmnitrix extends JFrame {

   
    private static final String IMG_JUGAR = "menu_jugar.png"; 
    private static final String IMG_CREDITOS = "menu_creditos.png"; 
    private static final String IMG_ACTUALIZACIONES = "menu_actualizaciones.png"; 

    public MenuOmnitrix() {
        setTitle("OMNITRIX - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setUndecorated(true); // Sin bordes de ventana

        // Panel principal con fondo degradado y patrón radial
        JPanel panelPrincipal = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Fondo degradado radial azul oscuro
                Point2D center = new Point2D.Float(width / 2f, height / 2f);
                float radius = Math.max(width, height);
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(10, 40, 90), new Color(5, 15, 40)}; // Tonos azules
                RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
                g2.setPaint(p);
                g2.fillRect(0, 0, width, height);

                // Dibujar el patrón de líneas radiales verdes (efecto "circuito")
                g2.setColor(new Color(0, 255, 0, 60)); // Verde neón semi-transparente
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
        gbc.insets = new Insets(20, 20, 20, 20); // Espaciado entre elementos

        // 1. Título "OMNITRIX" (Texto y Símbolo)
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelTitulo.setOpaque(false);

        // Símbolo del Omnitrix (Círculo verde con 'X') - Dibujado manualmente
        JPanel simbolo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 255, 0)); // Verde neón
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(5, 5, 40, 40); // Círculo exterior
                g2.drawLine(15, 15, 35, 35); // Línea 1 de la X
                g2.drawLine(15, 35, 35, 15); // Línea 2 de la X
            }
        };
        simbolo.setPreferredSize(new Dimension(50, 50));
        simbolo.setOpaque(false);
        panelTitulo.add(simbolo);

        // Texto "OMNITRIX"
        JLabel lblTituloText = new JLabel("juego");
        lblTituloText.setFont(new Font("sansserif", Font.BOLD, 60));
        lblTituloText.setForeground(Color.WHITE);
        panelTitulo.add(lblTituloText);

        // Línea verde debajo del título
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

        // Contenedor para título y línea
        JPanel contenedorTitulo = new JPanel(new BorderLayout());
        contenedorTitulo.setOpaque(false);
        contenedorTitulo.add(panelTitulo, BorderLayout.CENTER);
        contenedorTitulo.add(lineaTitulo, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Ocupa las tres columnas de botones
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(50, 0, 80, 0); // Espacio superior e inferior
        panelPrincipal.add(contenedorTitulo, gbc);


        // 2. Botones de Imagen (Jugar, Créditos, Actualizaciones)
        Dimension buttonSize = new Dimension(280, 450); // Tamaño de los paneles/botones de imagen

        ImagenButton btnJugar = new ImagenButton(IMG_JUGAR, "Jugar");
        btnJugar.setPreferredSize(buttonSize);

        ImagenButton btnCreditos = new ImagenButton(IMG_CREDITOS, "Créditos");
        btnCreditos.setPreferredSize(buttonSize);

        ImagenButton btnActualizaciones = new ImagenButton(IMG_ACTUALIZACIONES, "Registro de actualizaciones");
        btnActualizaciones.setPreferredSize(buttonSize);

        // Configurar GridBagConstraints para los botones
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 30, 0, 30); // Espaciado horizontal entre botones
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(btnJugar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panelPrincipal.add(btnCreditos, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        panelPrincipal.add(btnActualizaciones, gbc);


        // 3. Botón de Salir (Opcional, en la esquina inferior)
        JButton btnSalir = new JButton("X Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSalir.setForeground(Color.LIGHT_GRAY);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(50, 0, 20, 20);
        panelPrincipal.add(btnSalir, gbc);

        add(panelPrincipal);
    }

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos de AWT
        SwingUtilities.invokeLater(() -> {
            // Asegúrate de crear imágenes de marcador de posición si no tienes las reales
            createPlaceholderImages();
            new MenuOmnitrix().setVisible(true);
        });
    }

    // --- Función auxiliar para crear imágenes de marcador de posición (borrar en producción) ---
    private static void createPlaceholderImages() {
        createImage(IMG_JUGAR, Color.decode("#a3705f"), "☕ JUGAR");
        createImage(IMG_CREDITOS, Color.decode("#6b8e23"), "⛰️ CRÉDITOS");
        createImage(IMG_ACTUALIZACIONES, Color.decode("#1e90ff"), "🤖 UPDATES");
    }

    private static void createImage(String path, Color color, String label) {
        File file = new File(path);
        if (file.exists()) return; // No sobrescribir si ya existe

        BufferedImage img = new BufferedImage(280, 450, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, 280, 450);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, (280 - fm.stringWidth(label)) / 2, 225);
        
        g2.dispose();
        try {
            ImageIO.write(img, "png", file);
            System.out.println("Creado marcador de posición: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}