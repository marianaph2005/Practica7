import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VentanaJuego extends JFrame {
    private JuegoMemorama juegoActual;
    private int puntosParaGanar;
    private String tipoTarjetasActual;

    // Componentes de la pantalla de inicio
    private JButton botonJugar;
    private JButton botonSalir;

    // Componentes del juego
    private PanelJuego panelJuego;

    public VentanaJuego() {
        setTitle("Memorama");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Iniciar el menú principal
        iniciarMenuPrincipal();
    }

    public void iniciarMenuPrincipal() {
        // Limpiar el contenido anterior si existe
        getContentPane().removeAll();

        // Configurar el panel principal
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(230, 220, 250));

        // Título
        JLabel tituloLabel = new JLabel("✧ MEMORAMA ✧");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 48));
        tituloLabel.setForeground(new Color(100, 50, 150));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(40));
        panelMenu.add(tituloLabel);

        // Imagen
        ImageIcon icono = new ImageIcon("ruta/a/tu/imagen.png");
        try {
            Image imagen = icono.getImage();
            Image nuevaImagen = imagen.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            icono = new ImageIcon(nuevaImagen);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
            // Crear una imagen vacía si no se encuentra
            icono = new ImageIcon(new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB));
        }

        JLabel etiquetaImagen = new JLabel(icono);
        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(etiquetaImagen);

        // Botón de jugar
        botonJugar = new JButton("Jugar");
        botonJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonJugar.setMaximumSize(new Dimension(200, 40));
        botonJugar.setFont(new Font("Arial", Font.BOLD, 16));
        panelMenu.add(Box.createVerticalStrut(30));
        panelMenu.add(botonJugar);

        // Botón de salir
        botonSalir = new JButton("Salir");
        botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonSalir.setMaximumSize(new Dimension(200, 40));
        botonSalir.setFont(new Font("Arial", Font.BOLD, 16));
        panelMenu.add(Box.createVerticalStrut(10));
        panelMenu.add(botonSalir);

        // Configurar acciones de botones
        botonSalir.addActionListener(e -> System.exit(0));
        botonJugar.addActionListener(e -> inicializarConfiguracionJuego());

        // Agregar el panel al frame
        getContentPane().add(panelMenu, BorderLayout.CENTER);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void inicializarConfiguracionJuego() {
        //Seleccionar número de jugadores
        int numeroDeJugadores = 0;
        while (numeroDeJugadores < 2 || numeroDeJugadores > 4) {
            String input = JOptionPane.showInputDialog(this,
                    "Ingrese el número de jugadores (2 a 4):",
                    "Número de Jugadores", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return; // Se canceló
            }

            try {
                numeroDeJugadores = Integer.parseInt(input);
                if (numeroDeJugadores < 2 || numeroDeJugadores > 4) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor ingrese un número entre 2 y 4",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un número válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Seleccionar puntos para ganar
        int puntosParaGanar = 0;
        while (puntosParaGanar < 1) {
            String input = JOptionPane.showInputDialog(this,
                    "¿Cuántos puntos para ganar? (Mínimo 1):",
                    "Puntos para Ganar", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return; // Se canceló
            }

            try {
                puntosParaGanar = Integer.parseInt(input);
                if (puntosParaGanar < 1) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor ingrese un número mayor a 0",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un número válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.puntosParaGanar = puntosParaGanar;

        // Seleccionar tipo de tarjetas
        Object[] opciones = {"Canción", "Película", "Miraculous"};
        int opcion = JOptionPane.showOptionDialog(this,
                "Seleccione el tipo de tarjetas:",
                "Tipo de Tarjetas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == JOptionPane.CLOSED_OPTION) {
            return; // Se canceló
        }

        // Convertir la opción a los valores que espera JuegoMemorama
        String[] tiposTarjetas = {"canción", "película", "miraculous"};
        this.tipoTarjetasActual = tiposTarjetas[opcion];

        //Pedir nombres de los jugadores
        List<String> nombresJugadores = new ArrayList<>();
        for (int i = 1; i <= numeroDeJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(this,
                    "Nombre del jugador " + i + ":",
                    "Nombre del Jugador",
                    JOptionPane.QUESTION_MESSAGE);

            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Jugador " + i;
            }

            nombresJugadores.add(nombre);
        }

        // Iniciar el juego
        iniciarJuego(puntosParaGanar, tipoTarjetasActual, nombresJugadores);
    }

    public void iniciarJuego(int puntosParaGanar, String tipoTarjetas, List<String> nombresJugadores) {
        // Limpiar pantalla anterior
        getContentPane().removeAll();

        // Crear el juego
        juegoActual = new JuegoMemorama(tipoTarjetas, nombresJugadores);

        // Crear panel de juego
        panelJuego = new PanelJuego(juegoActual, this);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelJuego, BorderLayout.CENTER);

        // Refrescar la pantalla
        revalidate();
        repaint();
    }

    public void finalizarJuego(Jugador ganador) {
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar mensaje de ganador
                JOptionPane.showMessageDialog(VentanaJuego.this,
                        "¡" + ganador.getNombre() + " ha ganado con " + ganador.getPuntos() + " puntos!",
                        "¡Juego Terminado!",
                        JOptionPane.INFORMATION_MESSAGE);

                // Preguntar si quieren jugar de nuevo
                int opcion = JOptionPane.showConfirmDialog(VentanaJuego.this,
                        "¿Quieren jugar de nuevo?",
                        "Nueva Partida",
                        JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    iniciarMenuPrincipal();
                } else {
                    System.exit(0);
                }
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    public void continuarJuegoNuevasTarjetas(String nuevoTipoTarjetas) {
        this.tipoTarjetasActual = nuevoTipoTarjetas;

        // Guardar los puntajes actuales
        List<Integer> puntosActuales = new ArrayList<>();
        List<Integer> paresActuales = new ArrayList<>();
        List<String> nombresJugadores = new ArrayList<>();

        for (Jugador j : juegoActual.getJugadores()) {
            puntosActuales.add(j.getPuntos());
            paresActuales.add(j.getParesEncontrados());
            nombresJugadores.add(j.getNombre());
        }

        // Crear un nuevo juego con el nuevo tipo de tarjetas
        juegoActual = new JuegoMemorama(nuevoTipoTarjetas, nombresJugadores);

        // Restaurar los puntajes
        for (int i = 0; i < juegoActual.getJugadores().size(); i++) {
            Jugador jugador = juegoActual.getJugadores().get(i);
            jugador.setPuntos(puntosActuales.get(i));
            jugador.setParesEncontrados(paresActuales.get(i));
        }

        // Actualizar la interfaz
        getContentPane().removeAll();
        panelJuego = new PanelJuego(juegoActual, this);
        getContentPane().add(panelJuego, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public int getPuntosParaGanar() {
        return puntosParaGanar;
    }

    public JuegoMemorama getJuegoActual() {
        return juegoActual;
    }
}