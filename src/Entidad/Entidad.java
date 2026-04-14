package Entidad;

import Main.GamePanel;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entidad  {

    public GamePanel gp;
    public int x,y;
    public int speed;

//lo usamos para guardar nuestras imagenes
    public BufferedImage lft1,lft2,lft3,lft4,lft5,lft6,lft7,lft8,rgt1,rgt2,rgt3,rgt4,rgt5,rgt6,rgt7,rgt8;
    public String direction;


    public String nombre;

    //PERSONAJE ATRIBUTOS
    public int maxLife;
    public int barraVida;
    public int life;
    public int level;
    public int strenght;
    public int attack;
    public int defense;
    public int coin;
    public Entity currentWeapon;
    public int exp;
    public int nextLevelExp;



    //ATRIBUTOS ITEMS
    public int debilAtaque;
    public int medianoAtaque;
    public int fuerteAtaque;


    public Entidad(GamePanel gp) {
        this.gp = gp;
    }
    public Entidad(){}

    public void setAction(){}
    public void damageReaction(){}
    public void speal(){}

    public int spriteCounter=0;
    public int spriteNum=1;

    public Rectangle solidArea;
    public boolean colisionOn=false;


    public int getX() {
        return x;
    }
}
