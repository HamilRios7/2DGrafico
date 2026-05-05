package Entidad;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
            ataJuga1,ataJuga2,ataJuga3,ataJuga4,ataJuga5,ataJuga6,ataJuga7,
            muerto_1,muerto_2,muerto_3,muerto_4,muerto_5,muerto_6,muerto_7;




    public String direction;
    public String nombre;

    //PERSONAJE ATRIBUTOS

    protected int maxLife;
    public int barraVida;
    public int life;

    public int level;

    protected int strenght;
    public double fuerzaPorcentaje;







    public Entidad(){}


    //ESTO NOS SIRVE PARA LA ANIMACION AL CAMINAR DE NUESTRO PERSONAJE
    public int moveCounter =0;
    public int moveNum =1;
    public int muerteCounter =0;
    public int muerteNum =1;
    public int idleCounter=0;
    public int idleNum=1;
    public int atacarCounter=0;
    public int atacarNum=1;
    int contadorMaxFrames=0;

    //
    public Rectangle solidArea;





    public void ataqueSeguro(Enemigo enemigo){

        int ataque=6;
        int probabilidadAcierto=90;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));

        Random rand = new Random();

        if(rand.nextInt(100) < probabilidadAcierto){
            int vida = enemigo.getLifeEnemigo() - dañoFinal;
            if(vida < 0) vida = 0;
            enemigo.setLifeEnemigo(vida);
        }
    }

    public void ataqueEquilibrado(Enemigo enemigo) {


        int ataque=10;
        int probabilidadAcierto=68;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));


        Random rand = new Random();




        if(rand.nextInt(100) < probabilidadAcierto){
            int vida = enemigo.getLifeEnemigo() - dañoFinal;
            if(vida < 0) vida = 0;
            enemigo.setLifeEnemigo(vida);

        }else{

        }


    }


    public void ataqueArriesgado(Enemigo enemigo) {
        int ataque=14;
        int probabilidadAcierto=40;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));


        Random rand = new Random();




        if(rand.nextInt(100) < probabilidadAcierto){
            int vida = enemigo.getLifeEnemigo() - dañoFinal;
            if(vida < 0) vida = 0;
            enemigo.setLifeEnemigo(vida);
        }else{

        }


    }



    public String getNombre() {
        return nombre;
    }
    public int getLife() {
        return life;
    }
    public void setLife(int life){
        this.life =life;
    }




}
