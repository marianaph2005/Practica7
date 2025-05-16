import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VentanaJuego extends JFrame {
    private JuegoMemorama juegoActual;
    private String tipoTarjetasActual;

    // Componentes de la pantalla de inicio
    private JButton botonJugar;
    private JButton botonSalir;

    // Componentes del juego
    private PanelJuego panelJuego;
    private JLabel fondoLabel;

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

        // Usar JLayeredPane para manejar capas
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1000, 800));

        // 1. Configurar el fondo (capa más baja)
        try {
            ImageIcon fondoIcon = new ImageIcon("G:\\4toSemestre\\POO\\Practica-7\\src\\imagenMenu.jpg");
            Image imgRedimensionada = fondoIcon.getImage().getScaledInstance(1000, 800, Image.SCALE_SMOOTH);
            fondoIcon = new ImageIcon(imgRedimensionada);
            fondoLabel = new JLabel(fondoIcon);
            fondoLabel.setBounds(0, 0, 1000, 800);
            layeredPane.add(fondoLabel, JLayeredPane.DEFAULT_LAYER);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            fondoLabel = new JLabel();
            fondoLabel.setBackground(new Color(230, 220, 250));
            fondoLabel.setOpaque(true);
            fondoLabel.setBounds(0, 0, 1000, 800);
            layeredPane.add(fondoLabel, JLayeredPane.DEFAULT_LAYER);
        }

        // 2. Panel de elementos (capa superior)
        JPanel panelElementos = new JPanel();
        panelElementos.setLayout(new BoxLayout(panelElementos, BoxLayout.Y_AXIS));
        panelElementos.setOpaque(false);
        panelElementos.setBounds(0, 100, 1000, 800);

        // Título
        JLabel tituloLabel = new JLabel("✧ MEMORAMA ✧");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 48));
        tituloLabel.setForeground(new Color(255, 255, 255));
        tituloLabel.setBackground(new Color(100, 50, 150));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setOpaque(true);
        panelElementos.add(Box.createVerticalStrut(100));
        panelElementos.add(tituloLabel);
        panelElementos.add(Box.createVerticalStrut(40));

        // Botón de jugar
        botonJugar = new JButton("Jugar");
        estiloBoton(botonJugar);
        panelElementos.add(botonJugar);
        panelElementos.add(Box.createVerticalStrut(20));

        // Botón de salir
        botonSalir = new JButton("Salir");
        estiloBoton(botonSalir);
        panelElementos.add(botonSalir);

        // Configurar acciones de botones
        botonSalir.addActionListener(e -> System.exit(0));
        botonJugar.addActionListener(e -> inicializarConfiguracionJuego());

        // Agregar panel de elementos a la capa superior
        layeredPane.add(panelElementos, JLayeredPane.PALETTE_LAYER);

        // Centrar los elementos verticalmente
        panelElementos.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Configurar el contenido
        getContentPane().add(layeredPane, BorderLayout.CENTER);

        // Actualizar la ventana
        revalidate();
        repaint();
        setVisible(true);
    }

    private void estiloBoton(JButton boton) {
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 60));
        boton.setFont(new Font("Arial", Font.BOLD, 20));
        boton.setBackground(new Color(100, 50, 150));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(140, 80, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(100, 50, 150));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boton.setBackground(new Color(80, 40, 120));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                boton.setBackground(new Color(100, 50, 150));
            }
        });
    }

    public void inicializarConfiguracionJuego() {
        // Seleccionar número de jugadores
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

        // Pedir nombres de los jugadores
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

        // Iniciar el juego - Establecemos el puntaje para ganar a un número alto que no se alcanzará
        iniciarJuego(1000, tipoTarjetasActual, nombresJugadores); // 1000 es un valor que nunca se alcanzará
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
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar mensaje de ganador con efecto visual
                JOptionPane pane = new JOptionPane(
                        "¡" + ganador.getNombre() + " ha ganado con " + ganador.getPuntos() + " puntos!",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null,
                        new Object[]{"Jugar de nuevo", "Salir"}
                );

                JDialog dialog = pane.createDialog(VentanaJuego.this, "¡Juego Terminado!");
                dialog.setVisible(true);

                Object seleccionado = pane.getValue();
                if (seleccionado != null && seleccionado.equals("Jugar de nuevo")) {
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
}