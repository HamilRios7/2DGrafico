package Entidad;

import java.util.Scanner;

public class Heroe extends Personaje {
    public int oro;
    public Heroe() {
        super("heroe",125,35,0.8);//stats de prueba.
        this.oro = 0;

    }

    public String añadirNombre(Scanner teclado){
        System.out.println("Ingrese el nombre: ");
        nombre = teclado.nextLine();
        return nombre;
    }

    public void golpeSeguro(Enemigo enemigo){
        ejecutarAtaque(enemigo,"golpe seguro",0.7,0.4);// 56% de acierto 14 de daño
    }

    public void golpeEquilibrado(Enemigo enemigo){
        ejecutarAtaque(enemigo,"golpe equilibrado",0.5,0.48);//40% de acierto y 16 de daño
    }

    public void obtenerOro(int oroDrop){//se le suma el oro que ha dropeado el enemigo
        oro=oro+oroDrop;
    }

    public void mostrarDatos(){
        super.mostrarDatos();
        System.out.println("oro: "+this.oro);
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }
}
