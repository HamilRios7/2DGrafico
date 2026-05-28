package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;


/**
 * Objeto tipo poción que aumenta la fuerza del jugador.
 *
 * Al usar esta poción, la fuerza del jugador aumenta en +5
 * durante un ataque. También activa un estado temporal en el juego
 * indicando que el efecto está activo.
 */
public class Obj_PocionFuerza  extends SuperObject {


    /** Referencia al GamePanel para acceder al jugador y estado del juego */
    GamePanel gp;




    /**
     * Constructor de la poción de fuerza.
     *
     * @param gp referencia al panel principal del juego
     */
    public Obj_PocionFuerza (GamePanel gp) {
        this.gp = gp;

        name="PocionFuerza";
        descripcion = "Aumenta fuerza +5 durante 1 ataque";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionfuerza.png"));

        }catch (IOException e){
            System.out.println("Imagen de pocion no encontrada : ");
            e.printStackTrace();
        }
    }




    /**
     * Aplica el efecto de la poción al jugador.
     *
     * Aumenta la fuerza del jugador en +5 y activa el estado
     * de fuerza temporal en el GamePanel.
     */
    @Override
    public void usar() {
        int strenght= gp.jugador.getStrenght() + 5;
        gp.jugador.setStrenght(strenght);
        gp.fuerzaActiva = true;
    }
}
