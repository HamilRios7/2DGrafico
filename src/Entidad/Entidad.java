package Entidad;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entidad  {

    public int x,y;
    public int speed;

//lo usamos para guardar nuestras imagenes
    public BufferedImage lft1,lft2,lft3,lft4,lft5,lft6,lft7,lft8,rgt1,rgt2,rgt3,rgt4,rgt5,rgt6,rgt7,rgt8;
    public String direction;


    public int spriteCounter=0;
    public int spriteNum=1;

    public Rectangle solidArea;
    public boolean colisionOn=false;


    public int getX() {
        return x;
    }
}
