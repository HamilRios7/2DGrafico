package Entidad;

import Main.GamePanel;

import java.awt.*;

public class campoPeleaInteraccion extends Entidad {
    //clase que genera el area con el que puedes interactuar y entrar en combate
    public int spawnX = 90;
    public int spawnY = 60;
    GamePanel gp;
    public Rectangle area = new Rectangle(spawnX, spawnY, 3, 150);

    public campoPeleaInteraccion(GamePanel gp) {
        this.gp = gp;

        nombre = "ZonaPelea";
    }
    public Rectangle getBorde() {
        return area;
    }
}
