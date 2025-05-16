import javax.swing.*;
import java.awt.*;

public class TarjetaMiraculous extends Tarjeta {
    private String nombre; // Nombre del héroe
    private String kwami; // Nombre del kwami
    private boolean esHeroe; // Indica si es héroe o kwami
    private ImageIcon imagenAlternativa; // Imagen de identidad secreta o objeto miraculous
    private static final int DURACION_EFECTO = 1000; //Tiempo en milisegundos

    public TarjetaMiraculous(String nombre, String kwami, boolean esHeroe) {
        super("miraculous"); //LLama al constructor de la clase padre
        this.nombre = nombre;
        this.kwami = kwami;
        this.esHeroe = esHeroe;

        cargarImagenEfecto();
    }

    //Método que carga la imagen alternativa
    private void cargarImagenEfecto() {
        try {
            String rutaImagen;
            if (esHeroe) {
                // Cargar imagen de identidad secreta
                rutaImagen = "G:\\4toSemestre\\POO\\Practica-7\\" +
                        "src\\imagenes\\miraculous\\identidades\\" +
                        modificarNombre(nombre) + ".png";
            } else {
                // Cargar imagen del miraculous
                rutaImagen = "G:\\4toSemestre\\POO\\Practica-7\\" +
                        "src\\imagenes\\miraculous\\objetos\\" +
                        modificarNombre(kwami) + ".png";
            }

            imagenAlternativa = new ImageIcon(rutaImagen);

            // Escalar la imagen
            Image imgEscalada = imagenAlternativa.getImage().getScaledInstance(
                    120, 120, Image.SCALE_SMOOTH
            );
            imagenAlternativa = new ImageIcon(imgEscalada);

            // Verificar si se cargó correctamente
            if (imagenAlternativa.getIconWidth() <= 0) {
                System.err.println("No se pudo cargar la imagen alternativa para "
                        + (esHeroe ? nombre : kwami));
                imagenAlternativa = null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen alternativa: " + e.getMessage());
            imagenAlternativa = null;
        }
    }

    public String getKwami() {
        return kwami;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esHeroe() {
        return esHeroe;
    }

    // Método para obtener el color asociado a cada kwami
    private Color obtenerColorKwami() {
        switch (kwami.toLowerCase()) {
            case "tikki": return new Color(255, 0, 0); // Rojo
            case "plagg": return new Color(0, 0, 0); // Negro
            case "sass": return new Color(0, 180, 180); // Turquesa
            case "fluff": return new Color(220, 220, 255); // Azul claro
            case "roarr": return new Color(165, 42, 42); // Guinda
            case "barkk": return new Color(255, 165, 0); // Naranja
            default: return new Color(150, 150, 150); // Gris por defecto
        }
    }

    //Método que limpia y simplifica los nombres para que coincida con el archivo
    private String modificarNombre(String original) {
        return original.toLowerCase().replace(" ", "");
    }

    // Método que se ejecuta al voltear la tarjeta
    @Override
    public void efectoAlVoltear(VentanaJuego ventana, BotonTarjeta boton) {
        if (imagenAlternativa == null) return; //Si no hay imagen no hace nada

        // Guardar la imagen original
        final Icon iconoOriginal = boton.getIcon();

        // Crear un timer para mostrar la imagen alternativa
        Timer mostrarImagen = new Timer(300, e -> {
            // Mostrar imagen alternativa
            boton.setIcon(imagenAlternativa);

            // Programar el regreso a la imagen original después de un tiempo
            Timer ocultarImagen = new Timer(500, e2 -> {
                // Volver a la imagen original
                boton.setIcon(iconoOriginal);
            });
            ocultarImagen.setRepeats(false); //Solo se hace una vez
            ocultarImagen.start(); //Inicia el segundo timer
        });

        mostrarImagen.setRepeats(false); //Solo se hace una vez
        mostrarImagen.start(); //Inicia el primer timer
    }

    //Método para cuando se forma un par correcto
    @Override
    public void efectoAlFormarPar(VentanaJuego ventana, Tarjeta otraTarjeta,
                                  BotonTarjeta botonEsta, BotonTarjeta botonOtra) {
        // Desactivar botones para que no se puedan presionar
        botonEsta.setEnabled(false);
        botonOtra.setEnabled(false);

        // Mostrar destellos con el color del kwami
        Color colorKwami = obtenerColorKwami();

        // Calcula las posiciones en la pantalla de los dos botones
        final Point posBoton1 = SwingUtilities.convertPoint(botonEsta,
                0, 0, ventana.getLayeredPane());
        final Point posBoton2 = SwingUtilities.convertPoint(botonOtra,
                0, 0, ventana.getLayeredPane());

        // Coordenadas para los destellos (centro de los botones)
        final int x1 = posBoton1.x + botonEsta.getWidth() / 2;
        final int y1 = posBoton1.y + botonEsta.getHeight() / 2;
        final int x2 = posBoton2.x + botonOtra.getWidth() / 2;
        final int y2 = posBoton2.y + botonOtra.getHeight() / 2;

        // Crear panel para dibujar efectos
        JPanel panelDestellos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Dibuja destellos alrededor de ambas tarjetas
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                dibujarDestellos(g2d, x1, y1, colorKwami);
                dibujarDestellos(g2d, x2, y2, colorKwami);

                // Dibuja una línea entre las dos tarjetas
                g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g2d.setColor(colorKwami);
                g2d.drawLine(x1, y1, x2, y2);
            }

            //Método que dibuja los rayos
            private void dibujarDestellos(Graphics2D g2d, int x, int y,
                                          Color color) {
                // Configuración de los destellos
                int numRayos = 12;
                int longitudRayo = 40;

                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(3f));

                for (int i = 0; i < numRayos; i++) {
                    double angulo = 2 * Math.PI * i / numRayos;
                    int x2 = x + (int)(Math.cos(angulo) * longitudRayo);
                    int y2 = y + (int)(Math.sin(angulo) * longitudRayo);

                    g2d.drawLine(x, y, x2, y2);
                }

                // Círculos
                for (int radio = 10; radio <= 30; radio += 10) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(),
                            color.getBlue(), 255 - radio * 5));
                    g2d.drawOval(x - radio, y - radio, radio * 2, radio * 2);
                }
            }
        };

        // Configurar el panel
        panelDestellos.setOpaque(false);
        panelDestellos.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());

        // Lo agrega temporalmente sobre la ventana
        JLayeredPane layeredPane = ventana.getLayeredPane();
        layeredPane.add(panelDestellos, JLayeredPane.POPUP_LAYER);

        // Se después quita depués de un tiempo
        Timer timer = new Timer(DURACION_EFECTO, e -> {
            layeredPane.remove(panelDestellos);
            layeredPane.repaint(); //Vuelve a dibujar la ventana
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Método que define cuándo una tarjeta es par con otra
    @Override
    public boolean esParCon(Tarjeta otraTarjeta) {
        if (!(otraTarjeta instanceof TarjetaMiraculous)) return false;

        TarjetaMiraculous otroMiraculous = (TarjetaMiraculous) otraTarjeta;
        // Es par si tienen el mismo kwami y uno es héroe y el otro no
        return this.kwami.equals(otroMiraculous.kwami) &&
                (this.esHeroe != otroMiraculous.esHeroe);
    }
}