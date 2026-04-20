package Entidad;

import Main.GamePanel;

import javax.imageio.ImageIO;
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











