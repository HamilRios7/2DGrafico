package Entidad;

import Main.GamePanel;

import java.awt.*;

/**
 *Esta clase representa la hitbox de interaccion de la escena de enhorabuena, que se activa al derrotar al jefe final.
*/
public class campoEnhorabuenaInteraccion  {

    //----Atributos----

    //posiciones donde estara ubicada el rectangulo
    private final int spawnX = 140;
    private final int spawnY = 60;


    GamePanel gp;

    //creamos el cuadro con el que podra interactuar
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 400);

    //----Constructor-----
    public campoEnhorabuenaInteraccion(GamePanel gp) {
        this.gp = gp;
    }

    //Getter
    public Rectangle getBorde() {
        return area;
    }
}
