package Main;

import javax.swing.*;
import java.awt.*;

public class Main {

    // Campo estático para que KeyHandler pueda acceder al GraphicsDevice
    public static GraphicsDevice gd;

    public static void main(String[] args) {

        gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("El Viaje del Héroe");

        try {
            Image icono = new ImageIcon(Main.class.getClassLoader().getResource("iconoJuego/icono.png")).getImage();
            frame.setIconImage(icono);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGame();
    }
}