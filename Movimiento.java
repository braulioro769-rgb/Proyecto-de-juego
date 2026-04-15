import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Movimiento extends JPanel {
    private BufferedImage spriteSheet;
    private BufferedImage[][] animaciones;

    private int estadoActual = 1; 
    private int frameActual = 0;
    private int x = 300, y = 200;
    private int velocidad = 15;
    
    private boolean moviendose = false;
    private boolean atacando = false;
    private boolean mirandoIzquierda = false; // Esta es la variable clave
    private boolean mirandoderecha = false;
    // Coordenadas de Columnas
    private final int[] colX = {0, 192, 339, 600};
    private final int[] colW = {201, 200, 253, 197};
    
    // Coordenadas de Filas
    private final int[] filaY = {0, 164, 311, 449, 590, 751, 898, 1057};
    private final int[] filaH = {156, 141, 135, 129, 135, 139, 155, 150};

    public Movimiento() {
        setOpaque(false);
        try {
            // Asegúrate de que la ruta sea correcta o usa solo "fire.png" si está en la carpeta del proyecto
            spriteSheet = ImageIO.read(new File("C:\\Users\\braul\\Downloads\\fire.png"));
            animaciones = new BufferedImage[8][4];

            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 4; c++) {
                    animaciones[f][c] = spriteSheet.getSubimage(
                        colX[c], filaY[f], colW[c], filaH[f]
                    );
                }
            }

            Timer animTimer = new Timer(110, e -> {
                if (moviendose || atacando) {
                    frameActual = (frameActual + 1) % 4;
                    if (atacando && frameActual == 0) {
                        atacando = false;
                        // Al terminar el ataque, vuelve a estado "perfil" (2 o 3 según tu sheet) o frente (1)
                        estadoActual = 1; 
                    }
                } else {
                    frameActual = 0;
                }
                repaint();
            });
            animTimer.start();

            setFocusable(true);
            configurarControles();

        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }

    private void configurarControles() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (atacando) return;

                switch (key) {
                    case KeyEvent.VK_UP:
                        estadoActual = 0; y -= velocidad; moviendose = true; break;
                    case KeyEvent.VK_DOWN:
                        estadoActual = 1; y += velocidad; moviendose = true; break;
                    case KeyEvent.VK_LEFT:
                        estadoActual = 2; 
                        x -= velocidad; 
                        moviendose = true; 
                        mirandoIzquierda = true; // Mirar a la izquierda
                        break;
                    case KeyEvent.VK_RIGHT:
                        estadoActual = 3; 
                        x += velocidad; 
                        moviendose = true; 
                        mirandoderecha = false; // Mirar a la derecha
                        break;
                    
                    // Ataques: Ahora respetarán automáticamente la dirección de "mirandoIzquierda"
                    case KeyEvent.VK_1: ejecutarAtaque(4);
                    mirandoIzquierda = true; break;
                    case KeyEvent.VK_2: ejecutarAtaque(5); 
                    mirandoderecha = true; break;
                    case KeyEvent.VK_3: ejecutarAtaque(6);
                    mirandoIzquierda = true; break;
                    case KeyEvent.VK_4: ejecutarAtaque(7); 
                    mirandoderecha = false; break;
                }
            }

            private void ejecutarAtaque(int fila) {
                atacando = true;
                moviendose = false;
                estadoActual = fila;
                frameActual = 0;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || 
                    key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                    moviendose = false;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (animaciones == null) return;

        BufferedImage frame = animaciones[estadoActual][frameActual];
        int drawW = 180; 
        int drawH = 160; 

        if (mirandoIzquierda) {
            // Dibuja TODO invertido (Caminata y Ataque)
            g.drawImage(frame, x + 60, y, -60, 60, null);
        } else {
            // Dibuja TODO normal (Caminata y Ataque hacia la derecha)
            g.drawImage(frame, x, y, 60, 60, null);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Fire Engine - Ataque Direccional");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Movimiento());
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}