package Entidad;

import Main.GamePanel;

import java.awt.*;

public class campoPuerta  extends Entidad {

    //clase que genera el campo de interaccion para entrar al castillo
    public int spawnX = 960;
    public int spawnY = 180;
    GamePanel gp;
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 300);

    public campoPuerta(GamePanel gp) {
        this.gp = gp;

        nombre = "PuertaCastillo";


    }



    public Rectangle getBorde() {
        return area;
    }
}

