import javax.swing.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Creamos jugadores y el juego
            java.util.List<String> nombres = Arrays.asList("Mar", "Sol");
            JuegoMemorama juego = new JuegoMemorama("canción", nombres); // Cambia a "canción" o "miraculous" si quieres

            // Creamos el panel de juego
            PanelJuego panelJuego = new PanelJuego(juego);

            // Ventana
            JFrame ventana = new JFrame(" Memorama");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(800, 600);
            ventana.setLocationRelativeTo(null);
            ventana.add(panelJuego);
            ventana.setVisible(true);
        });
    }
}
