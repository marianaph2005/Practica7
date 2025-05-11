public abstract class Tarjeta {
    protected String tipo;
    protected boolean descubierta;
    protected boolean estaVolteada;

    public Tarjeta(String tipo) {
        this.tipo = tipo;
        descubierta = false;
        estaVolteada = false;
    }

    public boolean estaDescubierta() {
        return descubierta;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean estaVolteada() {
        return estaVolteada;
    }

    // Méttodo abstracto que las subclases deben de implementar
    //para compararse entre sí y verificar si pueden ser par
    public abstract boolean esParCon(Tarjeta otraTarjeta);

    //Se usaría cuando un jugador está volteando las cartas
    // y no encontró par por lo que se vuelven a voltear
    public void voltear() {
        estaVolteada = !estaVolteada;
    }

    //Para cuando se encuentre un par se quede descubierta
    public void descubir() {
        descubierta = true;
    }

    @Override
    public String toString() {
        return "Tarjeta tipo: " + tipo + ", descubierta: " + descubierta;
    }
}
