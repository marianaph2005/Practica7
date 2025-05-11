public class TarjetaCancion extends Tarjeta {
    private String nombreCancion; //Solo para mostrarlo
    private String album; // Álbums: Lover, TTPD, Speak Now

    public TarjetaCancion(String nombreCancion, String album) {
        super("canción");
        this.nombreCancion = nombreCancion;
        this.album = album;
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
        return this.album.equals(otraCancion.getAlbum());
    }
}
