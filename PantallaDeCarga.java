import javax.swing.*;
import java.awt.*;

public class PantallaDeCarga extends JFrame {

    private JProgressBar barraProgreso;
    private JLabel labelCargando;

    public PantallaDeCarga() {
      
        setTitle("Cargando..."); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null); 
        setUndecorated(true);

      
        getContentPane().setBackground(Color.BLACK);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


   
        Font fuenteTexto = new Font("SansSerif", Font.BOLD, 18);

        labelCargando = new JLabel("cargando...");
        labelCargando.setFont(fuenteTexto);
        labelCargando.setForeground(Color.WHITE); 

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelCargando, gbc);


        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setPreferredSize(new Dimension(300, 25)); 

       
        barraProgreso.setForeground(Color.WHITE);
        barraProgreso.setBackground(new Color(64, 64, 64)); 
        barraProgreso.setBorderPainted(false);

 
        barraProgreso.setStringPainted(true);
        barraProgreso.setFont(fuenteTexto); 
        barraProgreso.setForeground(Color.WHITE);

        // NOTA: Para mover el texto a la DERECHA de la barra, por defecto Swing lo centra.
        // La forma más fiel de hacerlo como la foto es usar un Layout diferente o un panel intermedio,
        // pero por simplicidad de código, lo mantendremos centrado. Si necesitas que esté a la derecha
        // EXACTAMENTE, dímelo y te muestro un diseño más avanzado con paneles.

     
        gbc.gridy = 1;
        add(barraProgreso, gbc);
    }

    public void setProgreso(int valor) {
        SwingUtilities.invokeLater(() -> {
            barraProgreso.setValue(valor);
        });
    }

 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PantallaDeCarga pantalla = new PantallaDeCarga();
            pantalla.setVisible(true);

            new Thread(() -> {
                try {
                    for (int i = 0; i <= 100; i++) {
                        pantalla.setProgreso(i);

                        Thread.sleep(50);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start(); 
        });
    }
}