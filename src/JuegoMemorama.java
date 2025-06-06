import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoMemorama {
    private List<Tarjeta> tarjetas;
    private List<Jugador> jugadores;
    private int jugadorActual;
    private Tarjeta primeraSeleccion;
    private String tipoTarjetas;
    public static String[] GENEROS = {"Romance","Terror","Animada"};
    public static String[] ALBUMES = {"Speak Now","TTPD","Lover"};

    public JuegoMemorama(String tipoTarjetas, List<String> nombresJugadores) {
        this.tarjetas = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.tipoTarjetas = tipoTarjetas;

        for (String nombre : nombresJugadores) {
            jugadores.add(new Jugador(nombre));
        }

        this.jugadorActual = 0;
        this.primeraSeleccion = null;
        inicializarTarjetas();
    }

    private void inicializarTarjetas() {
        tarjetas.clear(); // Limpiar las tarjetas antes de inicializarlas nuevamente

        switch (tipoTarjetas) {
            case "miraculous":
                // Pares: Héroe + Kwami
                agregarParMiraculous("Ladybug", "Tikki");
                agregarParMiraculous("Chat Noir", "Plagg");
                agregarParMiraculous("Viperion", "Sass");
                agregarParMiraculous("Flairmidable", "Barkk");
                agregarParMiraculous("Tigresa", "Roarr");
                agregarParMiraculous("Bunnix", "Fluff");
                break;

            case "canción":
                // Pares: Mismo álbum
                agregarParCancion("Enchanted", ALBUMES[0]);
                agregarParCancion("Foolish One", ALBUMES[0]);
                //agregarParCancion("I Can See You", ALBUMES[0]);

                agregarParCancion("The Prophecy", ALBUMES[1]);
                agregarParCancion("So High School", ALBUMES[1]);
                //agregarParCancion("Guilty as Sin?", ALBUMES[1]);

                agregarParCancion("London Boy", ALBUMES[2]);
                agregarParCancion("Paper Rings", ALBUMES[2]);
                // agregarParCancion("I Think He Knows", ALBUMES[2]);
                break;

            case "película":
                // Pares: Mismo género
                agregarParPelicula("Flipped",GENEROS[0] );
                agregarParPelicula("How to Lose a Guy in 10 Days", GENEROS[0]);
                agregarParPelicula("To All the Boys I've Loved Before", GENEROS[0]);
                agregarParPelicula("The Notebook", GENEROS[0]);

                agregarParPelicula("Orphan",GENEROS[1] );
                agregarParPelicula("Insidious",GENEROS[1] );
                agregarParPelicula("El Conjuro",GENEROS[1] );
                agregarParPelicula("Scream",GENEROS[1] );

                agregarParPelicula("Enredados",GENEROS[2] );
                agregarParPelicula("Coco",GENEROS[2] );
                agregarParPelicula("Coraline",GENEROS[2] );
                agregarParPelicula("Shrek",GENEROS[2] );
                break;
        }
        Collections.shuffle(tarjetas);
    }

    private void agregarParCancion(String nombre, String album) {
        // Agregar 2 veces para que formar un par
        tarjetas.add(new TarjetaCancion(nombre, album));
        tarjetas.add(new TarjetaCancion(nombre, album));
    }

    private void agregarParPelicula(String nombre,String genero) {
        tarjetas.add(new TarjetaPelicula(nombre,genero));
    }

    private void agregarParMiraculous(String heroe, String kwami) {
        tarjetas.add(new TarjetaMiraculous(heroe, kwami, true));  // Héroe
        tarjetas.add(new TarjetaMiraculous(heroe, kwami, false)); // Kwami
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(jugadorActual);
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual + 1) % jugadores.size();
    }

    public boolean seleccionarTarjeta(int indice) {
        Tarjeta tarjeta = tarjetas.get(indice);

        // Verificar si la tarjeta ya está descubierta o volteada
        if (tarjeta.estaDescubierta() || tarjeta.estaVolteada()) {
            return false;
        }

        // Voltear la tarjeta
        tarjeta.voltear();

        // Si es la primera selección, guardarla y esperar
        if (primeraSeleccion == null) {
            primeraSeleccion = tarjeta;
            return false; // No se forma par aún
        } else {
            boolean esPar = primeraSeleccion.esParCon(tarjeta);
            if (esPar) {
                // Marcar ambas tarjetas como descubiertas
                primeraSeleccion.descubir();
                tarjeta.descubir();

                // Dar puntos al jugador actual
                getJugadorActual().sumarPunto();
                getJugadorActual().sumarParesEncontrados();
                if(tarjeta instanceof TarjetaMiraculous){
                    // Si las tarjetas son de tipo miraculous se suman 2 puntos
                    getJugadorActual().sumarPunto();
                }
            } else {
                // Solo cambiamos el turno
                cambiarTurno();
            }

            // Resetear la primera selección
            primeraSeleccion = null;
            return esPar;
        }
    }

    public void voltearTarjetasNoDescubiertas() {
        for (Tarjeta t : tarjetas) {
            if (t.estaVolteada() && !t.estaDescubierta()) {
                t.voltear();
            }
        }
    }

    public String getTipoTarjetas(){
        return tipoTarjetas;
    }
}