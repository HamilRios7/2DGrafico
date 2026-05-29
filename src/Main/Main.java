package Main;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal del juego "El Viaje del Héroe".
 * Se encarga de inicializar la ventana principal (JFrame),
 * configurar el modo de pantalla y arrancar el GamePanel.
 */
public class Main {

    /**
     * GraphicsDevice estático para permitir acceso global
     * (por ejemplo, desde KeyHandler u otras clases).
     */
    public static GraphicsDevice gd;

    /**
     * Punto de entrada del programa.
     * Inicializa la ventana del juego y arranca el bucle principal.
     */
    public static void main(String[] args) {


        // Obtener el dispositivo gráfico principal (pantalla actual)
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Crear la ventana principal del juego
        JFrame frame = new JFrame();
        //Asignamos que cuando cierre ventana, apague o cierre programa (System.exit(0))
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Ponemos titulo de la ventana
        frame.setTitle("El Viaje del Héroe");

        try {
            //Creamos una imagen para el icono del juego, cargándola desde los recursos
            Image icono = new ImageIcon(Main.class.getClassLoader().getResource("iconoJuego/icono.png")).getImage();
            //Le asignamos el icono del juego por defecto , la imagen creada
            frame.setIconImage(icono);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono del juego: " + e.getMessage());
        }

        // Crear el panel principal del juego
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        // Ajustar el tamaño del JFrame al preferredSize del panel
        frame.pack();

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);
        // Hacer visible la ventana
        frame.setVisible(true);

        // Inicializar y arrancar el juego
        gamePanel.setupGame();
        gamePanel.startGame();
    }
}