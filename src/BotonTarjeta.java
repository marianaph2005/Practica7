import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BotonTarjeta extends JButton {
    private Tarjeta tarjeta;
    private ImageIcon imagenFrontal;
    private static final ImageIcon IMAGEN_REVERSO;
    public static final int ANCHO_BOTON = 120;
    public static final int ALTO_BOTON = 180;

    private VentanaJuego ventana;

    static {
        ImageIcon img = null;
        try {
            img = new ImageIcon("G:\\4toSemestre\\POO\\Practica-7\\src\\imagenReverso.png");
            // Dimensiones originales
            int originalWidth = img.getIconWidth();
            int originalHeight = img.getIconHeight();

            // Calcular la escala con proporción
            double scale = Math.min(
                    (double) ANCHO_BOTON / originalWidth,
                    (double) ALTO_BOTON / originalHeight
            );

            // Nuevas dimensiones
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            // Escalar la imagen
            Image imgEscalada = img.getImage().getScaledInstance(
                    newWidth, newHeight, Image.SCALE_SMOOTH
            );

            img = new ImageIcon(imgEscalada);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de reverso: " + e.getMessage());
            img = null;
        }
        IMAGEN_REVERSO = img;
    }

    public BotonTarjeta(Tarjeta tarjeta, ActionListener accion, VentanaJuego ventana) {
        this.tarjeta = tarjeta;
        this.ventana = ventana;

        this.setPreferredSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));
        this.setMinimumSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));
        this.setMaximumSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));

        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);

        // Cargar imagen frontal
        this.imagenFrontal = cargarImagen(tarjeta);

        // Establecer imagen inicial
        this.setIcon(IMAGEN_REVERSO);

        // Agregar listener
        this.addActionListener(accion);
    }

    private ImageIcon cargarImagen(Tarjeta tarjeta) {
        try {
            String ruta = "G:\\4toSemestre\\POO\\Practica-7\\src\\imagenes\\";
            String tipo = tarjeta.getTipo();

            if (tipo.equals("canción")) {
                TarjetaCancion c = (TarjetaCancion) tarjeta;
                ruta += "canciones/" + modificarNombre(c.getNombreCancion()) + ".png";
            } else if (tipo.equals("película")) {
                TarjetaPelicula p = (TarjetaPelicula) tarjeta;
                ruta += "peliculas/" + modificarNombre(p.getNombre()) + ".png";
            } else if (tipo.equals("miraculous")) {
                TarjetaMiraculous m = (TarjetaMiraculous) tarjeta;
                String nombre = m.esHeroe() ? m.getNombre() : m.getKwami();
                ruta += "miraculous/" + modificarNombre(nombre) + ".png";
            }

            // Intenta cargar la imagen
            ImageIcon iconoOriginal = new ImageIcon(ruta);
            int originalWidth = iconoOriginal.getIconWidth();
            int originalHeight = iconoOriginal.getIconHeight();

            // Calcular la escala con proporción
            double scale = Math.min(
                    (double) ANCHO_BOTON / originalWidth,
                    (double) ALTO_BOTON / originalHeight
            );

            // Nuevas dimensiones
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            // Escalar la imagen
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(
                    newWidth, newHeight, Image.SCALE_SMOOTH
            );
            return new ImageIcon(imgEscalada);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }


    private String modificarNombre(String original) {
        return original.toLowerCase().replace(" ", "").
                replace("'", "").replace("?", "");
    }

    public void actualizarImagen() {
        if (tarjeta.estaDescubierta() || tarjeta.estaVolteada()) {
            setIcon(imagenFrontal);
        } else {
            setIcon(IMAGEN_REVERSO);
        }
    }
}