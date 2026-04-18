import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReplicaMenuPausa extends JFrame {

    public ReplicaMenuPausa() {
        setTitle("Menú de Pausa - OMNITRIX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panelPausa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradiente = new GradientPaint(0, 0, new Color(20, 0, 30), 0, getHeight(), new Color(10, 0, 15));
                g2.setPaint(gradiente);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panelPausa.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel lblTitulo = new JLabel("JUEGO PAUSADO");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 50));
        lblTitulo.setForeground(Color.WHITE);
        panelPausa.add(lblTitulo, gbc);

        gbc.gridy = 1;
        panelPausa.add(crearBotonMinecraft("Reanudar", e -> dispose()), gbc);
        gbc.gridy = 2;
        panelPausa.add(crearBotonMinecraft("Configuración", e -> mostrarVentanaConfiguracion()), gbc);
        gbc.gridy = 3;
        panelPausa.add(crearBotonMinecraft("Salir al Menú", e -> System.exit(0)), gbc);

        setContentPane(panelPausa);
        setVisible(true);
    }

    private JButton crearBotonMinecraft(String texto, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(380, 50));
        btn.setBackground(new Color(191, 191, 191));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.setFocusPainted(false);
        btn.addActionListener(accion);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(220, 220, 220)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(191, 191, 191)); }
        });
        return btn;
    }

    // --- MÉTODO FALTANTE ---
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReplicaMenuPausa());
    }
}