package Entidad;

import Main.GamePanel;

import java.awt.*;

public class campoEnhorabuenaInteraccion extends Entidad {
    public int spawnX = 140;
    public int spawnY = 60;
    GamePanel gp;
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 400);

    public campoEnhorabuenaInteraccion(GamePanel gp) {
        this.gp = gp;

        nombre = "Enhorabuena";
    }
    public Rectangle getBorde() {
        return area;
    }
}
