public class Jugador {
    private String nombre;
    private int puntos;
    private int paresEncontrados;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntos = 0;
        this.paresEncontrados = 0;
    }

    // Actualizar puntos y pares
    public void sumarPunto() {
        puntos++;
    }
    public void sumarParesEncontrados() {
        paresEncontrados++;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setParesEncontrados(int pares) {
        this.paresEncontrados = pares;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getParesEncontrados() {
        return paresEncontrados;
    }
}