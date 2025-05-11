public class TarjetaHeroe extends Tarjeta {
    private String nombre; //Para mostrarlo nomás
    private String kwami;
    private boolean esHeroe;

    public TarjetaHeroe(String nombre,String kwami, boolean esHeroe) {
        super("héroe");
        this.nombre = nombre;
        this.kwami = kwami;
        this.esHeroe = esHeroe;
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

    @Override
    public boolean esParCon(Tarjeta otraTarjeta) {
        TarjetaHeroe otroHeroe= (TarjetaHeroe) otraTarjeta;
        //Si tienen el mismo kwami y uno es héroe y el otro no
        return this.kwami.equals(otroHeroe.kwami) && (this.esHeroe != otroHeroe.esHeroe);    }
}
