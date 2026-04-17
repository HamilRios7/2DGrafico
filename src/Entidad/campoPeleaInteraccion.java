package Entidad;

import Main.GamePanel;

import java.awt.*;

public class campoPeleaInteraccion extends Entidad {
    //clase que genera el area con el que puedes interactuar y entrar en combate
    public int spawnX = 200;
    public int spawnY = 300;
    GamePanel gp;
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 500);

    public campoPeleaInteraccion(GamePanel gp) {
        this.gp = gp;

        nombre = "ZonaPelea";
    }
    public Rectangle getBorde() {
        return area;
    }
}
