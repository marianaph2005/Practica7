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
            });
            botones.add(boton);
            panelTarjetas.add(boton);
        }

        // Agregar todo al panel principal
        add(panelInfo, BorderLayout.NORTH);
        add(panelTarjetas, BorderLayout.CENTER);
    }

    private void manejarClickTarjeta(int indice) {
        if (esperando) return;

        Tarjeta tarjeta = juego.getTarjetas().get(indice);

        // Si ya está descubierta o volteada, no se hace nada
        if (tarjeta.estaDescubierta() || tarjeta.estaVolteada()) {
            return;
        }

        // Seleccionar tarjeta en el juego
        boolean esPar = juego.seleccionarTarjeta(indice);

        // Actualizar visualmente todas las tarjetas
        actualizarBotones();

        // Actualizar información de jugadores
        actualizarInformacion();

        // Si no formó par, esperar y voltear las tarjetas
        if (!esPar && contarTarjetasVolteadas() == 2) {
            esperando = true;

            Timer timer = new Timer(1000, new ActionListener() {
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
        // Verificar si alguien alcanzó los puntos para ganar
        for (Jugador jugador : juego.getJugadores()) {
            if (jugador.getPuntos() >= ventana.getPuntosParaGanar()) {
                ventana.finalizarJuego(jugador);
                return;
            }
        }

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
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¡Se acabaron las tarjetas! ¿Quieren continuar con otro tipo de tarjetas?",
                "Continuar Juego",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            //Elegir nuevo tipo de tarjetas
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
        } else {
            ventana.finalizarJuego(ganador);
        }
    }
}