package Entidad;

import Main.GamePanel;

import java.awt.*;

/**
 *Esta clase representa la hitbox de interaccion para entrar en modo combate contra samurai , solo esta antes de derrotar a samurai
 */
public class campoPeleaInteraccion {

    //----Atributos----

    //posiciones donde estara ubicada el rectangulo
    private final int spawnX = 90;
    private final int spawnY = 60;
    GamePanel gp;

    //creamos el cuadro con el que podra interactuar
    public Rectangle area = new Rectangle(spawnX, spawnY, 3, 150);

    //----Constructor----
    public campoPeleaInteraccion(GamePanel gp) {this.gp=gp;}

    //------Getter----
    public Rectangle getBorde() {
        return area;
    }
}
