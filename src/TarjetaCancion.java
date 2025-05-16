import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import javax.swing.Timer;

public class TarjetaCancion extends Tarjeta {
    private String nombreCancion; // Solo para mostrarlo
    private String album; // Álbums: Lover, TTPD, Speak Now
    private String rutaAudio; // Ruta al archivo de audio de la canción
    private String rutaAudioAlbum; // Ruta al audio de la canción del álbum
    private static Clip clipActual;
    private static boolean reproduciendo = false;
    private static Timer timerAudio;

    public TarjetaCancion(String nombreCancion, String album) {
        super("canción");
        this.nombreCancion = nombreCancion;
        this.album = album;
        this.rutaAudio = "G:\\4toSemestre\\POO\\Practica-7\\src\\audios\\canciones\\" + modificarNombre(nombreCancion) + ".wav";
        this.rutaAudioAlbum = "G:\\4toSemestre\\POO\\Practica-7\\src\\audios\\albums\\" + modificarNombre(album) + ".wav";
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    @Override
    public boolean esParCon(Tarjeta otraTarjeta) {
        if (!(otraTarjeta instanceof TarjetaCancion)) return false;
        TarjetaCancion otraCancion = (TarjetaCancion) otraTarjeta;
        return this.nombreCancion.equals(otraCancion.getNombreCancion());
    }

    @Override
    public void efectoAlFormarPar(VentanaJuego ventana, Tarjeta otraTarjeta,
                                  BotonTarjeta botonEsta, BotonTarjeta botonOtra) {
        // Desactivar botones
        botonEsta.setEnabled(false);
        botonOtra.setEnabled(false);

        // Detener cualquier audio actual
        detenerAudioActual();

        // Reproducir el sonido del álbum
        reproducirAudio(rutaAudioAlbum);
    }

    private void detenerAudioActual() {
        // Cancela cualquier Timer que esté ejecutándose
        if (timerAudio != null && timerAudio.isRunning()) {
            timerAudio.stop();
        }

        // Detener y cerrar cualquier clip actual
        if (clipActual != null) {
            try {
                if (clipActual.isRunning()) {
                    clipActual.stop();
                }
                clipActual.close();
                clipActual = null;
                reproduciendo = false;
            } catch (Exception e) {
                System.err.println("Error al detener el audio: " + e.getMessage());
            }
        }
    }

    private void reproducirAudio(String rutaAudio) {
        try {
            // Asegurarnos de detener cualquier audio actual primero
            detenerAudioActual();

            File archivoAudio = new File(rutaAudio);
            if (!archivoAudio.exists()) {
                System.err.println("Archivo de audio no encontrado: " + rutaAudio);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
            clipActual = AudioSystem.getClip();
            clipActual.open(audioStream);

            reproduciendo = true;
            clipActual.setFramePosition(0);
            clipActual.start();

            // Añadir un listener para saber cuándo termina el audio
            clipActual.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    reproduciendo = false;
                    clipActual.close();
                    clipActual = null;
                }
            });

            // Timer para cerrar el audio si no se procesa el evento STOP
            timerAudio = new Timer(10000, e -> {
                if (clipActual != null && clipActual.isRunning()) {
                    clipActual.stop();
                    clipActual.close();
                    clipActual = null;
                }
                reproduciendo = false;
            });
            timerAudio.setRepeats(false);
            timerAudio.start();

        } catch (Exception e) {
            System.err.println("Error al reproducir el audio: " + e.getMessage());
            e.printStackTrace();
            reproduciendo = false;
        }
    }

    @Override
    public void efectoAlVoltear(VentanaJuego ventana, BotonTarjeta boton) {
        // Solo reproducir si no se está reproduciendo ya otro audio
        if (!reproduciendo) {
            reproducirAudio(rutaAudio);
        }
    }

    private String modificarNombre(String original) {
        return original.toLowerCase().replace(" ", "_").replace("'", "");
    }
}