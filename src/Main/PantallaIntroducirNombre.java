package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * PANTALLA DE INTRODUCIR NOMBRE
 * ─────────────────────────────────────────────────────────────────
 * Muestra una ventana emergente donde el jugador escribe su nombre
 * antes de empezar la partida.
 *
 * Usa JDialog, que es una ventana secundaria de Swing que aparece
 * encima del JFrame principal del juego.
 *
 * La ventana es MODAL, lo que significa que bloquea el juego
 * hasta que el jugador confirme su nombre.
 *
 * NIVEL DAM: aplica componentes Swing básicos (JDialog, JPanel,
 * JLabel, JTextField, JButton), layouts (GridBagLayout) y
 * eventos (ActionListener).
 * ─────────────────────────────────────────────────────────────────
 */
public class PantallaIntroducirNombre {

    // ── Campos privados ───────────────────────────────────────────────────

    /**
     * Nombre que el jugador ha escrito y confirmado.
     * Empieza vacío. Solo se rellena cuando pulsa CONTINUAR o ENTER.
     * Si sigue vacío al cerrar, getNombre() devuelve "Anónimo".
     */
    private String nombreJugador = "";

    /**
     * Referencia al JFrame principal del juego.
     * El JDialog lo necesita para saber encima de qué ventana centrarse
     * y para funcionar como ventana modal.
     *
     * Se obtiene en KeyHandler así:
     * JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(gp);
     */
    private final JFrame ventanaPadre;


    // ── Constructor ───────────────────────────────────────────────────────

    /**
     * Crea la pantalla vinculada a la ventana principal del juego.
     *
     * @param ventanaPadre el JFrame principal (la ventana del juego)
     */
    public PantallaIntroducirNombre(JFrame ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
    }


    // ── Método principal ──────────────────────────────────────────────────

    /**
     * Muestra el diálogo de introducir nombre.
     *
     * Este método es BLOQUEANTE: el código que viene después en
     * KeyHandler no se ejecuta hasta que el jugador confirme su nombre.
     *
     * Esto funciona porque JDialog modal congela el hilo de Swing
     * hasta que se llama a dialogo.dispose().
     */
    public void mostrar() {

        // ── Crear la ventana emergente ────────────────────────────────────
        // true = modal (bloquea hasta que se cierre)
        JDialog dialogo = new JDialog(ventanaPadre, "Introduce tu nombre", true);
        dialogo.setSize(420, 220);
        dialogo.setLocationRelativeTo(ventanaPadre); // centrar sobre el juego
        dialogo.setResizable(false);                 // no se puede redimensionar


        // ── Panel principal con fondo negro del juego ─────────────────────
        // GridBagLayout permite colocar componentes en filas y columnas
        // con control total del espaciado entre ellos
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(7, 10, 18));   // mismo negro que el menú
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // GridBagConstraints define la posición de cada componente
        // gbc.gridy = 0 → fila 0, gbc.gridy = 1 → fila 1, etc.
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(8, 8, 8, 8); // margen de 8px


        // ── Fila 0: Título ────────────────────────────────────────────────
        JLabel titulo = new JLabel("¿Cómo te llamas, aventurero?");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(209, 0, 28)); // mismo rojo del juego
        restricciones.gridy = 0;
        panel.add(titulo, restricciones);


        // ── Fila 1: Campo de texto ────────────────────────────────────────
        // 18 = anchura del campo en número de caracteres visibles
        JTextField campaNombre = new JTextField(18);
        campaNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        campaNombre.setBackground(new Color(30, 30, 40)); // fondo oscuro
        campaNombre.setForeground(Color.WHITE);           // texto blanco
        campaNombre.setCaretColor(Color.WHITE);           // cursor blanco
        campaNombre.setBorder(
                BorderFactory.createLineBorder(new Color(209, 0, 28), 2));
        restricciones.gridy = 1;
        panel.add(campaNombre, restricciones);


        // ── Fila 2: Botón de confirmación ─────────────────────────────────
        JButton botonContinuar = new JButton("CONTINUAR");
        botonContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        botonContinuar.setBackground(new Color(209, 0, 28));
        botonContinuar.setForeground(Color.WHITE);
        botonContinuar.setFocusPainted(false); // quita el borde feo de Swing
        restricciones.gridy = 2;
        panel.add(botonContinuar, restricciones);

        dialogo.add(panel);


        // ── Lógica de confirmación ────────────────────────────────────────
        // ActionListener es como KeyListener pero para botones y campos de texto.
        // Se activa al pulsar el botón O al pulsar ENTER en el campo de texto.
        // El mismo listener se asigna a los dos componentes para no repetir código.
        ActionListener alConfirmar = evento -> {
            // trim() elimina espacios en blanco al inicio y al final
            // evita que alguien confirme escribiendo solo espacios
            String textoIntroducido = campaNombre.getText().trim();

            if (!textoIntroducido.isEmpty()) {
                // Nombre válido → guardarlo y cerrar el diálogo
                nombreJugador = textoIntroducido;
                dialogo.dispose(); // cierra la ventana → desbloquea el hilo
            } else {
                // Nombre vacío → aviso visual con borde naranja
                campaNombre.setBorder(
                        BorderFactory.createLineBorder(Color.ORANGE, 2));
            }
        };

        // Asignar el mismo listener al botón y al campo de texto
        botonContinuar.addActionListener(alConfirmar);
        campaNombre.addActionListener(alConfirmar); // ENTER en el campo


        // ── Mostrar el diálogo (BLOQUEANTE) ───────────────────────────────
        // Esta línea congela el hilo hasta que se llame a dialogo.dispose()
        // Todo el código de KeyHandler después de mostrar() espera aquí
        dialogo.setVisible(true);
    }


    // ── Getter ────────────────────────────────────────────────────────────

    /**
     * Devuelve el nombre introducido por el jugador.
     * Si el jugador no escribió nada, devuelve "Anónimo" por defecto.
     *
     * Llamar DESPUÉS de mostrar(), nunca antes.
     *
     * @return nombre del jugador, nunca null ni vacío
     */
    public String getNombre() {
        return nombreJugador.isEmpty() ? "Anónimo" : nombreJugador;
    }
}