package Entidad;

import java.awt.image.BufferedImage;
import java.util.Random;

//modificada para que se extienda de la clase Personaje
public class Enemigo extends Entidad {
    public Enemigo() {

    }
    String nombreEnemigo;
    public int maxLifeEnemigo;
    public int barraVidaEnemigo;
    public int lifeEnemigo;
    public int enemyWidht;

    public int x1Enemigo, y1Enemigo;


    //Samurai
    public BufferedImage quieto_1,quieto_2,quieto_3,quieto_4,quieto_5,quieto_6,quieto_7,quieto_8,quieto_9,quieto_10,
            ata1,ata2,ata3,ata4,ata5,ata6,ata7,

            morir1,morir2,morir3;





    //DE ENEMIGOS
    public int idleCounterEnemigo =0;
    public int idleNumEnemigo =1;
    public int atacarCounterEnemigo =0;
    public int atacarNumEnemigo =1;
    int contadorMaxFramesEnemigo=0;

    public void ataqueSeguroSamurai(){
        int ataque=5;
        int probabilidadAcierto=90;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));


        Random rand = new Random();




        if(rand.nextInt(100) < probabilidadAcierto){
            int jugadorVidaRestante=gp.jugador.getLife()-dañoFinal;
            if(jugadorVidaRestante<0){
                jugadorVidaRestante=0;
            }


            gp.jugador.setLife(jugadorVidaRestante);

        }else{

        }


    }




    public void ataqueEquilibradoSamurai(){
        int ataque=8;
        int probabilidadAcierto=68;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));


        Random rand = new Random();




        if(rand.nextInt(100) < probabilidadAcierto){
            int jugadorVidaRestante=gp.jugador.getLife()-dañoFinal;
            if(jugadorVidaRestante<0){
                jugadorVidaRestante=0;
            }
            gp.jugador.setLife(jugadorVidaRestante);

        }else{

        }
    }



    public void ataqueArriesgadoSamurai(){
        int ataque=12;
        int probabilidadAcierto=44;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));


        Random rand = new Random();


        if(rand.nextInt(100) < probabilidadAcierto){
            int jugadorVidaRestante=gp.jugador.getLife()-dañoFinal;
            if(jugadorVidaRestante<0){
                jugadorVidaRestante=0;
            }
            gp.jugador.setLife(jugadorVidaRestante);

        }else{

        }
    }


    public String getNombre() {
        return nombre;
    }

    public int getMaxLifeEnemigo() {
        return maxLifeEnemigo;
    }

    public int getBarraVidaEnemigo() {
        return barraVidaEnemigo;
    }

    public int getLifeEnemigo() {
        return lifeEnemigo;
    }

    public void setLifeEnemigo(int lifeEnemigo) {
        this.lifeEnemigo=lifeEnemigo;
    }


}




//Antes de poner esto en marcha, necesitaos ser capaces de general a nuestro enemigo
//Piensa que la clase madre completa es Entidad , entonces de esa tienen que heredar las demas

// public int dropOro(){//Dropea entre 2 a 5 de oro si ha muerto.
//int oro=0;
// if(this.vida<=0){
//   oro= (int) (Math.random() * (5 - 2 + 1)) + 2;
// }
// return oro;
// }

        //método para dropear una poción (NO es random)
       /* public void dropPocino(Heroe heroe,Item pocion){
            heroe.obtenerPocion(pocion);
        }*/


// public void mostrarDatos(){
//      System.out.println("Nombre: "+nombre);
//     super.mostrarDatos();
// }


//  public String getNombre() {
// return nombre;
// }

// public void setNombre(String nombre) {
//    this.nombre = nombre;
// }



