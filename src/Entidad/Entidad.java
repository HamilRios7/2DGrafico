package Entidad;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entidad  {
// de este campo heredan los enemigos y jugador
// dejamos las estadisticas ya dichas y sin añadir armas como variedad, el calculo se volveria mas facil
    public GamePanel gp;
    public int x1Jugador, y1Jugador, x2Jugador, y2Jugador;
    public int speed;

//lo usamos para guardar nuestras imagenes
        //jugador
    public BufferedImage lft1,lft2,lft3,lft4,lft5,lft6,lft7,lft8,
                        rgt1,rgt2,rgt3,rgt4,rgt5,rgt6,rgt7,rgt8,
        quieto1JugaRight, quieto2JugaRight, quieto3JugaRight, quieto4JugaRight, quieto5JugaRight,
        quieto1JugaLeft,quieto2JugaLeft,quieto3JugaLeft,quieto4JugaLeft,quieto5JugaLeft,
                        ataJuga1,ataJuga2,ataJuga3,ataJuga4,ataJuga5,ataJuga6,ataJuga7,ataJuga8;

    //Enemigo1=BerserkerCarmesi
    public BufferedImage quieto_1,quieto_2,ata1,ata2,ata3,ata4,ata5;

    public String direction;


    public String nombre;

    //PERSONAJE ATRIBUTOS
    public int maxLife;
    public int barraVida;
    public int life;
    public int level;
    public int strenght;
    public double fuerzaPorcentaje;
    public double precision;
    public double precisionPorcentaje;
    public int oro;
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



}
