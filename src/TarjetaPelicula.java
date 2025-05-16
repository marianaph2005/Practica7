import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class TarjetaPelicula extends Tarjeta {
    private String nombre;
    private String genero; // Romance, Terror, Animada
    private static Clip clip;

    public TarjetaPelicula(String nombre, String genero) {
        super("película");
        this.nombre = nombre;
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean esParCon(Tarjeta otraTarjeta) {
        if (!(otraTarjeta instanceof TarjetaPelicula)) return false;
        TarjetaPelicula otraPelicula = (TarjetaPelicula) otraTarjeta;
        return otraPelicula.getGenero().equals(this.genero);
    }

    @Override
    public void efectoAlVoltear(VentanaJuego ventana, BotonTarjeta boton) {
        // Efecto de color según el género
        Color colorGenero = getColorGenero();

        // Crear un borde temporal
        final javax.swing.border.Border bordePrevio = boton.getBorder();
        boton.setBorder(BorderFactory.createLineBorder(colorGenero, 4));

        // Reproducir sonido según género
        reproducirSonidoGenero();

        // Restaurar el borde después de un tiempo
        Timer timer = new Timer(800, e -> {
            boton.setBorder(bordePrevio);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void reproducirSonidoGenero() {
        try {
            String rutaAudio = "G:\\4toSemestre\\POO\\Practica-7\\src\\audios\\generos\\"
                    +genero.toLowerCase() + ".wav";

            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            File archivoAudio = new File(rutaAudio);
            if (!archivoAudio.exists()) {
                System.err.println("El archivo de audio no se encontró: " + rutaAudio);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            System.err.println("Error al reproducir sonido del género: "
                    + e.getMessage());
        }
    }

    private Color getColorGenero() {
        switch (genero.toLowerCase()) {
            case "romance": return new Color(255, 105, 180); // Rosa
            case "terror": return new Color(139, 0, 0);      // Rojo oscuro
            case "animada": return new Color(30, 144, 255);  // Azul brillante
            default: return Color.WHITE;
        }
    }

    @Override
    public void efectoAlFormarPar(VentanaJuego ventana, Tarjeta otraTarjeta,
                                  BotonTarjeta botonEsta, BotonTarjeta botonOtra) {
        // Desactivar botones
        botonEsta.setEnabled(false);
        botonOtra.setEnabled(false);

        // Efecto según género
        switch (genero.toLowerCase()) {
            case "romance":
                mostrarEfectoRomance(ventana);
                break;
            case "terror":
                mostrarEfectoTerror(ventana);
                break;
            case "animada":
                mostrarEfectoAnimada(ventana);
                break;
        }
    }

    // Muestra un efecto visual de corazón para películas de romance.
    private void mostrarEfectoRomance(VentanaJuego ventana) {
        // Crear un panel para el corazón utilizando Path2D
        JPanel panelEfecto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Configurar para mejor calidad de trazos
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Calcular tamaño y posición del corazón
                int anchoPanel = getWidth();
                int altoPanel = getHeight();
                int tamañoCorazon = Math.min(anchoPanel, altoPanel) / 2;
                float posX = (anchoPanel - tamañoCorazon) / 2.0f;
                float posY = (altoPanel - tamañoCorazon) / 2.0f;

                // Dibujar el corazón si hay espacio suficiente
                if (tamañoCorazon > 0) {
                    g2d.setColor(new Color(255, 20, 147, 200));
                    g2d.fill(crearFormaCorazon(posX, posY,
                            tamañoCorazon, tamañoCorazon));
                }
            }

            //Crea la forma de un corazón usando Path2D.
            private java.awt.geom.Path2D crearFormaCorazon(float x, float y,
                                                           float ancho, float alto) {
                float puntoBaseX = x + ancho / 2;      // Punto base X
                float puntoBaseY = y + alto;          // Punto base Y
                float ctrl1X = ancho * 0.968f;         // Control punto 1 X
                float ctrl1Y = alto * 0.672f;          // Control punto 1 Y
                float ctrl2X = ancho * 0.281f;         // Control punto 2 X
                float ctrl2Y = alto * 1.295f;          // Control punto 2 Y
                float puntoSuperiorY = alto * 0.850f;   // Punto superior Y

                java.awt.geom.Path2D.Float formaCorazon = new java.awt.geom.Path2D.Float();
                formaCorazon.moveTo(puntoBaseX, puntoBaseY);

                // Lado izquierdo del corazón
                formaCorazon.curveTo(
                        puntoBaseX - ctrl1X, puntoBaseY - ctrl1Y,
                        puntoBaseX - ctrl2X, puntoBaseY - ctrl2Y,
                        puntoBaseX, puntoBaseY - puntoSuperiorY);

                // Lado derecho del corazón
                formaCorazon.curveTo(
                        puntoBaseX + ctrl2X, puntoBaseY - ctrl2Y,
                        puntoBaseX + ctrl1X, puntoBaseY - ctrl1Y,
                        puntoBaseX, puntoBaseY);

                return formaCorazon;
            }
        };

        mostrarEfectoTemporal(ventana, panelEfecto, 1500);
    }

    // Muestra un efecto visual de terror
    private void mostrarEfectoTerror(VentanaJuego ventana) {
        // Crear un panel transparente para el efecto de parpadeo
        JPanel panelTerror = new JPanel() {
            private int contadorParpadeo = 0;
            private Timer temporizadorParpadeo;

            {
                // Configurar panel semitransparente
                setOpaque(true);
                setBackground(new Color(139, 0, 0, 180));

                // Iniciar temporizador para efecto de parpadeo
                temporizadorParpadeo = new Timer(50, e -> {
                    contadorParpadeo++;
                    if (contadorParpadeo > 10) {
                        temporizadorParpadeo.stop();
                    } else {
                        // Alternar entre rojo y negro
                        setBackground(contadorParpadeo % 2 == 0 ?
                                new Color(139, 0, 0, 200) :
                                new Color(0, 0, 0, 200));
                        repaint();
                    }
                });
                temporizadorParpadeo.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Dibujar manchas aleatorias en frames pares
                if (contadorParpadeo % 2 == 0) {
                    g2d.setColor(new Color(0, 0, 0, 150));
                    for (int i = 0; i < 20; i++) {
                        int posX = (int)(Math.random() * getWidth());
                        int posY = (int)(Math.random() * getHeight());
                        int tamaño = 10 + (int)(Math.random() * 30);
                        g2d.fillOval(posX, posY, tamaño, tamaño/3);
                    }
                }
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                if (temporizadorParpadeo != null && temporizadorParpadeo.isRunning()) {
                    temporizadorParpadeo.stop();
                }
            }
        };

        // Este método configura el panel y lo muestra durante el tiempo especificado
        mostrarEfectoTemporal(ventana, panelTerror, 1500);
    }

    // Muestra un efecto visual colorido para películas animadas
    private void mostrarEfectoAnimada(VentanaJuego ventana) {
        // Efecto de arcoíris
        JPanel panelAnimada = new JPanel() {
            private float tonoColor = 0; // Para efecto arcoíris
            private Timer temporizadorColor;

            {
                // Iniciar temporizador para cambio de colores
                temporizadorColor = new Timer(50, e -> {
                    tonoColor = (tonoColor + 0.02f) % 1.0f;
                    setBackground(new Color(Color.HSBtoRGB(tonoColor, 0.8f, 0.9f)));
                    repaint();
                });
                temporizadorColor.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Dibujar estrellas y círculos aleatorios
                g2d.setColor(new Color(255, 255, 255, 180));
                for (int i = 0; i < 20; i++) {
                    int posX = (int)(Math.random() * getWidth());
                    int posY = (int)(Math.random() * getHeight());
                    int tamaño = 10 + (int)(Math.random() * 20);

                    if (i % 2 == 0) {
                        dibujarEstrella(g2d, posX, posY, tamaño);
                    } else {
                        g2d.fillOval(posX, posY, tamaño, tamaño);
                    }
                }
            }

            //Dibuja una estrella en la posición especificada
            private void dibujarEstrella(Graphics2D g2d, int x, int y, int tamaño) {
                int[] puntosX = new int[10];
                int[] puntosY = new int[10];

                for (int i = 0; i < 10; i++) {
                    double angulo = Math.PI * i / 5;
                    int radio = (i % 2 == 0) ? tamaño : tamaño / 2;

                    puntosX[i] = x + (int)(radio * Math.cos(angulo));
                    puntosY[i] = y + (int)(radio * Math.sin(angulo));
                }

                g2d.fillPolygon(puntosX, puntosY, 10);
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                if (temporizadorColor != null && temporizadorColor.isRunning()) {
                    temporizadorColor.stop();
                }
            }
        };
        mostrarEfectoTemporal(ventana, panelAnimada, 1500);
    }

    private void mostrarEfectoTemporal(VentanaJuego ventana, JPanel panel, int duracion) {
        // Configurar el panel para ocupar toda la ventana
        panel.setOpaque(true);
        panel.setBounds(0, 0, ventana.getWidth(), ventana.getHeight());

        // Añadir panel a la capa superior de la ventana
        JLayeredPane capasVentana = ventana.getLayeredPane();
        capasVentana.add(panel, Integer.valueOf(JLayeredPane.POPUP_LAYER + 1));

        // Mostrar panel
        panel.setVisible(true);
        capasVentana.revalidate();
        capasVentana.repaint();

        // Temporizador para eliminar el panel después del tiempo establecido
        Timer temporizadorEliminacion = new Timer(duracion, e -> {
            capasVentana.remove(panel);
            capasVentana.revalidate();
            capasVentana.repaint();
        });
        temporizadorEliminacion.setRepeats(false);
        temporizadorEliminacion.start();
    }
}