package Entidad;

import Main.GamePanel;

import java.awt.*;

/**
 *Esta clase representa la hitbox de interaccion en la escena 2 para ir a la escena 3, que se activa al derrotar a samurai
 */
public class campoIntercaccionEscena3 {

    //----Atributos----

    //posiciones donde estara ubicada el rectangulo
    private final int spawnX = 874;
    private final int spawnY = 60;
    GamePanel gp;

    //creamos el cuadro con el que podra interactuar
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 200);

    //-----Constructor----
    public  campoIntercaccionEscena3(GamePanel gp) {
        this.gp = gp;

    }
    //-----Getter-----
    public Rectangle getBorde() {
        return area;
    }
}
