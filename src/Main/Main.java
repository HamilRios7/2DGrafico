package Main;


import javax.swing.*;
import java.awt.*;

public class

Main {
    public static void main(String[] args) {



        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//permitira que se cierre cuando se de a la "x"
        frame.setResizable(false);
        frame.setTitle("El Viaje del Héroe");

        try {
            Image icono = new ImageIcon(Main.class.getClassLoader().getResource("iconoJuego/icono.png")).getImage();
            frame.setIconImage(icono);

        }catch (Exception e) {
            System.out.println("No se pudo cargar el icono del juego: " + e.getMessage());
        }

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        frame.setLocationRelativeTo(null); //al no especificar donde aparecera, entonces aparecera siempre en el centro
        frame.setVisible(true); //para que veamos la ventana
        gamePanel.setupGame();
        gamePanel.startGame();
    }
}
