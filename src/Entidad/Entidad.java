package Entidad;

import Main.GamePanel;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entidad  {
// de este campo heredan los enemigos y jugador
// dejamos las estadisticas ya dichas y sin añadir armas como variedad, el calculo se volveria mas facil
    public GamePanel gp;
    public int x1Jugador, y1Jugador, x2Jugador, y2Jugador;
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



    //ATRIBUTOS DEL LO SUPUESTOS ATAQUES SIN EL CALCULO CON LA FUERZA
    public int debilAtaque;
    public int medianoAtaque;
    public int fuerteAtaque;


    public Entidad(GamePanel gp) {
        this.gp = gp;
    }
    public Entidad(){}


    //ESTO NOS SIRVE PARA LA ANIMACION AL CAMINAR DE NUESTRO PERSONAJE
    public int spriteCounter=0;
    public int spriteNum=1;


    //
    public Rectangle solidArea;
    public boolean colisionOn=false;


    public int getX1Jugador() {
        return x1Jugador;
    }
}
