public abstract class Tarjeta {
    protected String tipo;
    protected boolean descubierta;

    public Tarjeta(String tipo){
        this.tipo = tipo;
        descubierta = false;
    }

    public boolean estaDescubierta(){
        return descubierta;
    }

    public String getTipo(){
        return tipo;
    }

    public abstract boolean esParCon(Tarjeta otraTarjeta);

    public void voltear(){
        descubierta = !descubierta;
    }

    @Override
    public String toString() {
        return "Tarjeta tipo: " + tipo + ", descubierta: " + descubierta;
    }

}
