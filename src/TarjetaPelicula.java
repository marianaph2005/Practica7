public class TarjetaPelicula extends Tarjeta{
    private String nombre; //Para mostrarlo
    private String genero; //Romance, Terror, Animada

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
        TarjetaPelicula otraPelicula = (TarjetaPelicula) otraTarjeta;
        return otraPelicula.getGenero().equals(this.genero);
    }
}
