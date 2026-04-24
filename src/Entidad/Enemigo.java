package Entidad;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    public int moveCounter =0;
    public int moveNum =1;
    public int idleCounter=0;
    public int idleNum=1;
    public int atacarCounter=0;
    public int atacarNum=1;
    int contadorMaxFramesEnemigo=0;

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



