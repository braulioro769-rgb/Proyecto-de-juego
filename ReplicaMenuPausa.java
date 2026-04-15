import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReplicaMenuPausa extends JFrame {

    public ReplicaMenuPausa() {
      
        setTitle("Réplica Menú de Pausa - Minecraft Preview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 750); 
        setLocationRelativeTo(null); 
        setResizable(false); 
       
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(850, 750));

        
        PanelFondoJuego fondoJuego = new PanelFondoJuego();
        fondoJuego.setBounds(0, 0, 850, 750);
        layeredPane.add(fondoJuego, JLayeredPane.DEFAULT_LAYER);

       
        PanelMenuPausa menuPausa = new PanelMenuPausa();
        menuPausa.setBounds(0, 0, 850, 750);
        layeredPane.add(menuPausa, JLayeredPane.PALETTE_LAYER);

       
        setContentPane(layeredPane);
        pack();
        setVisible(true);
    }

   
    class PanelMenuPausa extends JPanel {
        public PanelMenuPausa() {
            setOpaque(false); // Transparente para ver el fondo oscurecido
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 0, 10, 0); // Espaciado vertical entre bloques
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;

           
            gbc.gridy = 0;
            JLabel labelMincreft = crearEtiquetaMinecraft("juego", 50, true);
            add(labelMincreft, gbc);

            gbc.gridy = 1;
            gbc.insets = new Insets(-5, 0, 30, 0); // Ajuste negativo para que 'PREVIEW' esté justo debajo
            JLabel labelPreview = crearEtiquetaMinecraft("PREVIEW", 22, false);
            labelPreview.setForeground(new Color(252, 222, 171)); // Color beige/caramelo de PREVIEW
            add(labelPreview, gbc);

            // --- 2. Bloque de Botones Principales (Gris Claro) ---
            gbc.insets = new Insets(10, 0, 10, 0); // Restablecer insets
            gbc.gridy = 2;
            add(crearBotonMinecraft("Resume Game"), gbc);
            gbc.gridy = 3;
            add(crearBotonMinecraft("Browse Add-ons!"), gbc);
            gbc.gridy = 4;
            add(crearBotonMinecraft("Save & Quit"), gbc);

            // --- 3. Fila Inferior de Iconos y Estados ---
            JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panelInferior.setOpaque(false);

            // Icono 1: Configuración (Verde)
            panelInferior.add(crearIconoVerde());
            // Icono 2: Libro (Simulado)
            panelInferior.add(crearIconoSimulado(new Color(210, 180, 140), "📖")); // Color marrón libro
            // Icono 3: Mandos (Y + Cámara)
            panelInferior.add(crearIconoMandoSimulado());

            gbc.gridy = 5;
            gbc.insets = new Insets(40, 0, 10, 0); // Separar de los botones grandes
            add(panelInferior, gbc);

            // --- 4. Barra de "Game is paused" ---
            gbc.gridy = 6;
            add(crearBarraEstadoPausado(), gbc);
        }

        // Método auxiliar para crear etiquetas con estilo pixelado
        private JLabel crearEtiquetaMinecraft(String texto, int tamano, boolean negrita) {
            JLabel label = new JLabel(texto, SwingConstants.CENTER);
            // Usamos Monospaced como la fuente más parecida predeterminada
            label.setFont(new Font("Monospaced", negrita ? Font.BOLD : Font.PLAIN, tamano));
            label.setForeground(Color.WHITE);
            return label;
        }

        // Método auxiliar para crear botones grises estilo bloque
        private JButton crearBotonMinecraft(String texto) {
            JButton btn = new JButton(texto);
            btn.setPreferredSize(new Dimension(380, 48)); // Dimensiones similares
            btn.setFocusPainted(false);
            btn.setBackground(new Color(191, 191, 191)); // Gris claro de los botones
            btn.setForeground(Color.BLACK); // Texto negro en los botones claros
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            
            // Borde tipo bloque (línea negra de 2px)
            Border borderNegro = BorderFactory.createLineBorder(Color.BLACK, 2);
            btn.setBorder(borderNegro);

            // Efecto Hover (cambio de color al pasar el mouse)
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(220, 220, 220)); }
                public void mouseExited(MouseEvent e) { btn.setBackground(new Color(191, 191, 191)); }
            });
            return btn;
        }

        // -- Simulación de Iconos --
        
        // Icono de Configuración (Panel verde con símbolo)
        private JPanel crearIconoVerde() {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(50, 50));
            p.setBackground(new Color(0, 148, 0)); // Verde Minecraft
            p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            JLabel label = new JLabel("⚙", SwingConstants.CENTER); // Usamos emoji de engranaje
            label.setFont(new Font("SansSerif", Font.BOLD, 24));
            label.setForeground(Color.WHITE);
            p.add(label);
            return p;
        }

        // Icono Libro (Borde gris, interior marrón)
        private JPanel crearIconoSimulado(Color colorFondo, String simbolo) {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(50, 50));
            p.setBackground(colorFondo);
            p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // Borde negro exterior
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
            JLabel label = new JLabel(simbolo, SwingConstants.CENTER);
            label.setFont(new Font("Monospaced", Font.PLAIN, 24));
            p.add(label);
            return p;
        }

        // Icono Mandos (Panel gris oscuro con Y y Cámara)
        private JPanel crearIconoMandoSimulado() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            p.setPreferredSize(new Dimension(90, 50));
            p.setBackground(new Color(120, 120, 120)); // Gris oscuro mando
            p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            
            JLabel y = new JLabel("Y", SwingConstants.CENTER);
            y.setFont(new Font("SansSerif", Font.BOLD, 18));
            y.setForeground(new Color(255, 255, 100)); // Amarillo Y
            p.add(y);

            JLabel cam = new JLabel("📷", SwingConstants.CENTER); // Usamos emoji cámara
            cam.setFont(new Font("SansSerif", Font.PLAIN, 20));
            p.add(cam);

            return p;
        }

        // Barra de estado verde inferior
        private JPanel crearBarraEstadoPausado() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            p.setPreferredSize(new Dimension(280, 25)); // Barra delgada
            p.setBackground(new Color(0, 100, 0)); // Verde oscuro barra
            
            JLabel iconoPausa = new JLabel("||", SwingConstants.CENTER);
            iconoPausa.setFont(new Font("SansSerif", Font.BOLD, 16));
            iconoPausa.setForeground(Color.WHITE);
            p.add(iconoPausa);

            JLabel t = new JLabel("Game is paused", SwingConstants.CENTER);
            t.setFont(new Font("Monospaced", Font.PLAIN, 14));
            t.setForeground(Color.WHITE);
            p.add(t);
            return p;
        }

        // Capa de oscurecimiento de fondo
        @Override
        protected void paintComponent(Graphics g) {
            // Fondo oscuro semitransparente (como la imagen original)
            g.setColor(new Color(0, 0, 0, 180)); // 180 es la opacidad
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    // --- CLASE: Panel de Fondo (Simula el juego) ---
    class PanelFondoJuego extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibujamos un fondo degradado oscuro para simular el Nether/End
            GradientPaint gradiente = new GradientPaint(0, 0, new Color(20, 0, 30), 0, getHeight(), new Color(10, 0, 15));
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(gradiente);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.DARK_GRAY);
            g.drawString("MUNDO DE JUEGO (PAUSADO)", 20, 20);
        }
    }

    // --- PUNTO DE ENTRADA MAIN ---
    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new ReplicaMenuPausa());
    }
}