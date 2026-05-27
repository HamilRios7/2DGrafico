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
     * Si llegamos a cerrar el JDialog sin introducir nombre ,nos pondra Anónimo
     * pero solo si cierras esa ventana ,
     */
    private String nombreJugador = "";

    /**
     * Referencia al JFrame principal del juego.
     * El JDialog lo necesita para saber encima de qué ventana centrarse
     *
     * Se obtiene en KeyHandler así:
     * JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(gp)-> esto nos servira para saber en que ventana centrarse ya que escalara un arbol siendo raiz el JFrame
     * y el nodo hoja el GamePanel, asi que al pedir el ancestro del GamePanel nos devuelve el JFrame;
     *
     * Le das el GamePanel, sube hasta la raíz, y te devuelve el JFrame.
     * Luego ese JFrame se le pasa al JDialog para que sepa dónde centrarse y a quién bloquear cuando es modal.
     */
    private final JFrame ventanaPadre;


    //--------- Constructor---------

    /**
     * Crea la pantalla vinculada a la ventana principal del juego.
     *
     * @param ventanaPadre el JFrame principal (la ventana del juego)
     */
    public PantallaIntroducirNombre(JFrame ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
    }


    // ------ Método principal ---------

    /**
     * Muestra el diálogo de introducir nombre.
     *
     * Este método es como un bloqueador , es decir , el código que viene después en
     * KeyHandler no se ejecuta hasta que el jugador confirme su nombre.
     *
     * Esto funciona porque JDialog modal congela el hilo de Swing, es decir, no ejecuta  el codigo que tiene debajo
     *
     * hasta que se llama a dialogo.dispose(), es decir, escreibe su nombre y confirma que llama a dialogo.dispose()
     *
     *
     * El JDialog se consigue dandole la ventana padre que hemos obtenido en KeyHandler con -> JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(gp)
     */
    public void mostrar() {

        // ------ Crear la ventana emergente---------
        // true = modal (bloquea hasta que se cierre)
        JDialog dialogo = new JDialog(ventanaPadre, "Introduce tu nombre", true);//Le damos la ventana padre para saber donde abrirse, y el titulo de la ventana
        dialogo.setSize(420, 220);//Ponemos tamaño
        dialogo.setLocationRelativeTo(ventanaPadre); // centrar sobre el juego
        dialogo.setResizable(false); // no se puede redimensionar


        // -----Panel principal con fondo negro del juego--
       // Crea el panel y le dice que use GridBagLayout como sistema de posicionamiento.
        // Esto permite colocar componentes en filas y columnas con control preciso del espaciado.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(7, 10, 18));   // mismo negro que el menú
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));// Creamos  un borde invisible alrededor del panel entero que actúa como padding interior.

        // GridBagConstraints define la posición de cada componente , sin esto , el GridBagLayout no sabe donde colocar cosas
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(8, 8, 8, 8); // margen de 8px para el exterior de cosas


        // ------- Fila 0: Título --------
        JLabel titulo = new JLabel("¿Cómo te llamas, aventurero?");//Creamos un titulo no editable
        titulo.setFont(new Font("Arial", Font.BOLD, 18));//Tipo de letra
        titulo.setForeground(new Color(209, 0, 28)); // color mismo rojo del juego
        restricciones.gridy = 0; //lo coloca en la fila 0 del panel
        panel.add(titulo, restricciones);


        //------- Fila 1: Campo de texto --------
        // 18 = anchura del campo en número de caracteres visibles
        JTextField campaNombre = new JTextField(18);//Creamos un rectangulo que nos deja  introducir texto
        campaNombre.setFont(new Font("Arial", Font.PLAIN, 16)); //colocamos tipo de letra, tamaño y estilo
        campaNombre.setBackground(new Color(30, 30, 40)); // fondo oscuro
        campaNombre.setForeground(Color.WHITE);           // texto blanco
        campaNombre.setCaretColor(Color.WHITE);           // cursor blanco
        campaNombre.setBorder(
                BorderFactory.createLineBorder(new Color(209, 0, 28), 2));
        restricciones.gridy = 1;  //lo coloca en la fila 1 del panel
        panel.add(campaNombre, restricciones);


        // -------- Fila 2: Botón de confirmación--------
        JButton botonContinuar = new JButton("CONTINUAR");// añadimos el boton que nos deja continuar
        botonContinuar.setFont(new Font("Arial", Font.BOLD, 14));//colocamos tipo de letra, tamaño y estilo
        botonContinuar.setBackground(new Color(209, 0, 28)); //color de fondo del cuadro boton
        botonContinuar.setForeground(Color.WHITE);//colocmos el color de texto
        botonContinuar.setFocusPainted(false); // quita el borde feo de Swing
        restricciones.gridy = 2;//lo colocamos en la fila 2 del panel
        panel.add(botonContinuar, restricciones);

        dialogo.add(panel);// al JDialog , le añadimos el JPanel que ya tiene el boton , cuadro de texto y titulo


        // ------- Lógica de confirmación -------
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


        // ----- Mostrar el diálogo (BLOQUEANTE) --------
        // Esta línea congela el hilo hasta que se llame a dialogo.dispose()
        // Todo el código de KeyHandler después de mostrar() espera aquí
        dialogo.setVisible(true);
    }


    //------ Getter ---------

    /**
     * Devuelve el nombre introducido por el jugador.
     * Si el jugador no escribió nada, devuelve "Anónimo" por defecto.
     *
     * Llamar DESPUÉS de mostrar(), nunca antes.
     *
     * @return nombre del jugador, nunca null ni vacío
     */
    public String getNombre() {
        if(nombreJugador.isEmpty()){
            return "Anónimo";
        }else
            return nombreJugador;
    }
}