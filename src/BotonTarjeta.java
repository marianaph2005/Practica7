import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BotonTarjeta extends JButton {
    private Tarjeta tarjeta;
    private ImageIcon imagenFrontal;
    private static final ImageIcon IMAGEN_REVERSO = new ImageIcon("G:\\4toSemestre\\POO\\Practica-7\\src\\imagenReverso.png");

    public BotonTarjeta(Tarjeta tarjeta, ActionListener accion) {
        this.tarjeta = tarjeta;
        this.setPreferredSize(new Dimension(100, 120));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.setFocusPainted(false);

        this.imagenFrontal = cargarImagen(tarjeta);
        this.setIcon(IMAGEN_REVERSO);
        System.out.println(IMAGEN_REVERSO);
        this.addActionListener(accion);
    }

    private ImageIcon cargarImagen(Tarjeta tarjeta) {

       try {
           String ruta = "G:\\4toSemestre\\POO\\Practica-7\\src\\";
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
           System.out.println(ruta);
           ImageIcon iconoOriginal = new ImageIcon(ruta);
           Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                   this.getPreferredSize().width - 10,
                   this.getPreferredSize().height - 10,
                   Image.SCALE_SMOOTH);
           return new ImageIcon(imagenEscalada);
       }catch ( Exception e ) {
           System.err.println("Error al cargar la imagen:" + e.getMessage());
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
            System.out.println(imagenFrontal);
        } else {
            setIcon(IMAGEN_REVERSO);
            System.out.println(IMAGEN_REVERSO);
        }
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }
}