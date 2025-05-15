import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;

public class TarjetaCancion extends Tarjeta {
    private String nombreCancion; //Solo para mostrarlo
    private String album; // Álbums: Lover, TTPD, Speak Now
    private String rutaAudio; // Ruta al archivo de audio de la canción
    private String rutaAudioAlbum; // Ruta al audio de la canción del álbum
    private static Clip clip;
    private static boolean reproduciendo = false;

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

    public String getAlbum() {
        return album;
    }

    @Override
    public boolean esParCon(Tarjeta otraTarjeta) {
        TarjetaCancion otraCancion = (TarjetaCancion) otraTarjeta;
        return this.nombreCancion.equals(otraCancion.getNombreCancion());
    }

    @Override
    public void efectoAlFormarPar(VentanaJuego ventana, Tarjeta otraTarjeta, BotonTarjeta botonEsta, BotonTarjeta botonOtra) {
        detenerAudioActual();
        reproducirAudio(rutaAudioAlbum);
    }

    private void detenerAudioActual() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    private void reproducirAudio(String rutaAudio) {
        try {
            detenerAudioActual();
            File archivoAudio = new File(rutaAudio);
            if (!archivoAudio.exists()) {
                System.err.println("El archivo no se encontró" + rutaAudio);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip != null) {
                reproduciendo = true;
                clip.setFramePosition(0);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        reproduciendo = false;
                    }
                });
            }

        } catch (Exception e) {
            System.err.println("Error en el reproducir el audio" + e.getMessage());
            reproduciendo = false;
        }
    }

    public void efectoAlVoltear(VentanaJuego ventana, BotonTarjeta boton) {
        // Solo reproducir si no va a formar par
        if (!reproduciendo) {
            reproducirAudio(rutaAudio);
        }
    }

    private String modificarNombre(String original) {
        return original.toLowerCase().replace(" ", "_").replace("'", "").replace("?", "");
    }
}
