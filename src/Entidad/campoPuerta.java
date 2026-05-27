package Entidad;

import Main.GamePanel;

import java.awt.*;


/**
 *Esta clase representa la hitbox de interaccion para entrar dentro del castillo
 */
public class campoPuerta  {

    //----Atributos----

    //posiciones donde estara ubicada el rectangulo
    private final int spawnX = 960;
    private final  int spawnY = 180;

    GamePanel gp;

    //creamos el cuadro con el que podra interactuar
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 300);


    //----Constructor---
    public campoPuerta(GamePanel gp) {
        this.gp = gp;
    }


    //---Getter---
    public Rectangle getBorde() {
        return area;
    }
}

