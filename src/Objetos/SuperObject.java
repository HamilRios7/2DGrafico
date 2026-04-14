package Objetos;

import Main.GamePanel;
import Main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage imagen;
    public String name;
    public boolean collision = false;
    public int x, y;
    UtilityTool uTool=new UtilityTool();

    public void draw(Graphics2D g, GamePanel gp) {

        g.drawImage(imagen,x,y,gp.tamañoMosaico,gp.tamañoMosaico,null);
    }
}

