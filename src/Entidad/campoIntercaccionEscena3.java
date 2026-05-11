package Entidad;

import Main.GamePanel;

import java.awt.*;

public class campoIntercaccionEscena3  extends  Entidad{
    public int spawnX = 874;
    public int spawnY = 60;
    GamePanel gp;
    public Rectangle area = new Rectangle(spawnX, spawnY, 1, 200);

    public  campoIntercaccionEscena3(GamePanel gp) {
        this.gp = gp;

        nombre = "Escena 3";
    }
    public Rectangle getBorde() {
        return area;
    }
}
