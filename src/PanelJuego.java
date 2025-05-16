import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PanelJuego extends JPanel {
    private JuegoMemorama juego;
    private VentanaJuego ventana;
    private List<BotonTarjeta> botones;
    private JPanel panelTarjetas;
    private JPanel panelInfo;
    private JLabel lblJugadorActual;
    private List<JLabel> etiquetasPuntos;
    private boolean esperando = false;
    private Tarjeta ultimaTarjetaSeleccionada = null;
    private BotonTarjeta ultimoBotonSeleccionado = null;

    public PanelJuego(JuegoMemorama juego, VentanaJuego ventana) {
        this.juego = juego;
        this.ventana = ventana;
        this.botones = new ArrayList<>();
        this.etiquetasPuntos = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 255)); // Color de fondo suave

        // Panel de información
        panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 250), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelInfo.setBackground(new Color(230, 230, 250));

        // Etiqueta del jugador actual
        lblJugadorActual = new JLabel("Turno de: " + juego.getJugadorActual().getNombre());
        lblJugadorActual.setFont(new Font("Arial", Font.BOLD, 18));
        lblJugadorActual.setForeground(new Color(75, 0, 130)); // Índigo
        lblJugadorActual.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInfo.add(lblJugadorActual);
        panelInfo.add(Box.createVerticalStrut(10));

        // Panel para mostrar puntuaciones
        JPanel panelPuntuaciones = new JPanel();
        panelPuntuaciones.setLayout(new GridLayout(0, 2, 10, 5));
        panelPuntuaciones.setBackground(new Color(230, 230, 250));
        panelPuntuaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPuntuaciones.setMaximumSize(new Dimension(400, 100));

        // Etiquetas de puntuación
        for (Jugador jugador : juego.getJugadores()) {
            JLabel lblNombre = new JLabel(jugador.getNombre() + ":");
            lblNombre.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel lblPuntos = new JLabel(jugador.getPuntos() + " pts (" + jugador.getParesEncontrados() + " pares)");
            lblPuntos.setFont(new Font("Arial", Font.PLAIN, 14));

            etiquetasPuntos.add(lblPuntos);
            panelPuntuaciones.add(lblNombre);
            panelPuntuaciones.add(lblPuntos);
        }

        panelInfo.add(panelPuntuaciones);
        // Panel inferior con botones de control
        JPanel panelBotonesInferiores = new JPanel();
        panelBotonesInferiores.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotonesInferiores.setBackground(new Color(230, 230, 250));

        JButton btnMenu = new JButton("Menú Principal");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 14));
        btnMenu.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que quieres regresar al menú?\nSe perderá el progreso actual.",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                ventana.iniciarMenuPrincipal();
            }
        });

        JButton btnInstrucciones = new JButton("Instrucciones");
        btnInstrucciones.setFont(new Font("Arial", Font.BOLD, 14));
        btnInstrucciones.addActionListener(e -> mostrarInstrucciones());

        panelBotonesInferiores.add(btnMenu);
        panelBotonesInferiores.add(btnInstrucciones);

        add(panelBotonesInferiores, BorderLayout.SOUTH);

        // Panel de tarjetas
        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new GridLayout(3, 4, 10, 10));
        panelTarjetas.setBackground(new Color(240, 240, 255));
        panelTarjetas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Creamos los botones de las tarjetas
        for (int i = 0; i < juego.getTarjetas().size(); i++) {
            final int indice = i;
            Tarjeta tarjeta = juego.getTarjetas().get(i);
            BotonTarjeta boton = new BotonTarjeta(tarjeta, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    manejarClickTarjeta(indice);
                }
            }, ventana);
            botones.add(boton);
            panelTarjetas.add(boton);
        }

        // Agregar todo al panel principal
        add(panelInfo, BorderLayout.NORTH);
        add(panelTarjetas, BorderLayout.CENTER);
    }
    private void mostrarInstrucciones() {
        String tipo = juego.getTipoTarjetas().toLowerCase();
        String mensaje = "";

        switch (tipo) {
            case "canción":
                mensaje = "🎵 Encuentra las tarjetas con la misma canción para formar un par!!\n"
                        + "Al voltear una tarjeta escucharás un pequeño fragmento de esta canción\n"
                        +"para ayudarte a recordarlas, y al formar un par escucharás el nombre " +
                        "del álbum.\n" + "¡¡Junta la mayor cantidad de pares posibles para ganar!!";
                break;
            case "película":
                mensaje = "🎬 Une las películas del mismo género (romance, terror, animada).\n"
                        + "Al voltear la tarjeta escucharás los efectos de sonido y se observará\n" +
                        "una pequeña guía de colores para ayudarte a encontrar los pares, y al formar " +
                        "un par\nte sorpenderás con los efectos visuales personalizados para cada género" +
                        "\n ¡¡Junta la mayor cantidad de pares posibles para ganar!!";
                break;
            case "miraculous":
                mensaje = "🐞 Encuentra pares de tarjeta relacionando al superhéroe con su kwami.\n"
                        + "Por ejemplo: LadyBug y Tikki, Chat noir y plagg.\n Al voltear una tarjeta" +
                        "se revelará la identidad de cada héroe y el miraculous(objeto)\n de cada kwami," +
                        " debido a esto, en este modo de juego se otorgan puntos dobles!!\n Y al formar" +
                        " un par, verás cómo se conectan las tarjetas entre sí. \n ¡¡Junta la mayor cantidad " +
                        "de pares posibles para ganar!!";
                break;
            default:
                mensaje = "🤔 Las instrucciones específicas no están disponibles para este tipo de tarjetas.";
                break;
        }

        JOptionPane.showMessageDialog(this, mensaje, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
    }


    private void manejarClickTarjeta(int indice) {
        if (esperando) return;

        Tarjeta tarjeta = juego.getTarjetas().get(indice);
        BotonTarjeta boton = botones.get(indice);

        // Si ya está descubierta o volteada, no se hace nada
        if (tarjeta.estaDescubierta() || tarjeta.estaVolteada()) {
            return;
        }

        // Guardar referencia a la última tarjeta seleccionada
        if (ultimaTarjetaSeleccionada == null) {
            ultimaTarjetaSeleccionada = tarjeta;
            ultimoBotonSeleccionado = boton;
        } else {
            // Reiniciar para la próxima selección
            ultimaTarjetaSeleccionada = null;
            ultimoBotonSeleccionado = null;
        }

        // Seleccionar tarjeta en el juego
        boolean esPar = juego.seleccionarTarjeta(indice);

        // Actualizar visualmente todas las tarjetas
        actualizarBotones();

        // Después de actualizar visualmente, ejecutar el efecto al voltear
        if (tarjeta.estaVolteada()) {
            tarjeta.efectoAlVoltear(ventana, boton);
        }

        // Actualizar información de jugadores
        actualizarInformacion();

        // Si formó un par se activa el efecto especial
        if (esPar) {
            // Buscar la otra tarjeta del par (la que ya estaba volteada)
            Tarjeta otraTarjeta = null;
            BotonTarjeta otroBoton = null;

            for (int i = 0; i < juego.getTarjetas().size(); i++) {
                Tarjeta t = juego.getTarjetas().get(i);
                if (t != tarjeta && t.estaDescubierta() && tarjeta.esParCon(t)) {
                    otraTarjeta = t;
                    otroBoton = botones.get(i);
                    break;
                }
            }

            // Activar el efecto especial al formar par
            if (otraTarjeta != null && otroBoton != null) {
                final Tarjeta tarjetaFinal = tarjeta;
                final Tarjeta otraTarjetaFinal = otraTarjeta;
                final BotonTarjeta botonFinal = boton;
                final BotonTarjeta otroBotonFinal = otroBoton;

                // Desactivar botones inmediatamente
                botonFinal.setEnabled(false);
                otroBotonFinal.setEnabled(false);

                // Usar un pequeño delay para que los efectos se muestren correctamente
                Timer timerEfecto = new Timer(200, e -> {
                    tarjetaFinal.efectoAlFormarPar(ventana, otraTarjetaFinal, botonFinal, otroBotonFinal);
                });
                timerEfecto.setRepeats(false);
                timerEfecto.start();
            }
        }

        // Si no formó par, esperar y voltear las tarjetas
        if (!esPar && contarTarjetasVolteadas() == 2) {
            esperando = true;

            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Usar el método de JuegoMemorama para voltear tarjetas
                    juego.voltearTarjetasNoDescubiertas();

                    // Actualizar la visualización
                    actualizarBotones();
                    actualizarInformacion();

                    esperando = false;
                }
            });

            timer.setRepeats(false);
            timer.start();
        }

        // Verificar si el juego ha terminado
        verificarFinJuego();
    }

    private void actualizarBotones() {
        for (int i = 0; i < botones.size(); i++) {
            botones.get(i).actualizarImagen();
        }
    }

    private int contarTarjetasVolteadas() {
        int count = 0;
        for (Tarjeta tarjeta : juego.getTarjetas()) {
            if (tarjeta.estaVolteada() && !tarjeta.estaDescubierta()) {
                count++;
            }
        }
        return count;
    }

    private void actualizarInformacion() {
        lblJugadorActual.setText("Turno de: " + juego.getJugadorActual().getNombre());

        for (int i = 0; i < juego.getJugadores().size(); i++) {
            Jugador jugador = juego.getJugadores().get(i);
            etiquetasPuntos.get(i).setText(jugador.getPuntos() + " pts (" + jugador.getParesEncontrados() + " pares)");
        }
    }

    private void verificarFinJuego() {
        // Verificar si se descubrieron todas las tarjetas
        boolean todasDescubiertas = true;
        for (Tarjeta tarjeta : juego.getTarjetas()) {
            if (!tarjeta.estaDescubierta()) {
                todasDescubiertas = false;
                break;
            }
        }

        if (todasDescubiertas) {
            preguntarContinuarJuego();
        }
    }

    private void preguntarContinuarJuego() {
        // Pequeño retraso para que los efectos visuales terminen
        Timer timer = new Timer(500, e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¡Se acabaron las tarjetas! ¿Quieren continuar con otro tipo de tarjetas?",
                    "Continuar Juego",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                // Elegir nuevo tipo de tarjetas
                Object[] tiposDisponibles = {"Canción", "Película", "Miraculous"};
                int seleccion = JOptionPane.showOptionDialog(
                        this,
                        "Seleccione el nuevo tipo de tarjetas:",
                        "Nuevas Tarjetas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        tiposDisponibles,
                        tiposDisponibles[0]
                );

                if (seleccion != JOptionPane.CLOSED_OPTION) {
                    String[] tiposTarjetas = {"canción", "película", "miraculous"};
                    String nuevoTipo = tiposTarjetas[seleccion];
                    ventana.continuarJuegoNuevasTarjetas(nuevoTipo);
                } else {
                    determinarGanador();
                }
            } else {
                // No continuar, se determina el ganador
                determinarGanador();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void determinarGanador() {
        // Encontrar el jugador con más puntos
        Jugador ganador = juego.getJugadores().get(0);
        for (Jugador jugador : juego.getJugadores()) {
            if (jugador.getPuntos() > ganador.getPuntos()) {
                ganador = jugador;
            }
        }

        // Verificar si hay empate
        boolean hayEmpate = false;
        for (Jugador jugador : juego.getJugadores()) {
            if (jugador != ganador && jugador.getPuntos() == ganador.getPuntos()) {
                hayEmpate = true;
                break;
            }
        }

        if (hayEmpate) {
            JOptionPane.showMessageDialog(this,
                    "¡El juego terminó en empate!",
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);
            ventana.iniciarMenuPrincipal();
        } else {
            ventana.finalizarJuego(ganador);
        }
    }
}