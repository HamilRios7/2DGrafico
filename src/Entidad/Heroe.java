/*package Entidad;//se ha añadido metodos para usar ambas pociones, un inventario (ArrayList)

import java.util.ArrayList;
import java.util.Scanner;
public class Heroe extends Personaje {
    public int oro;
    public int vidaMaxima;
    public ArrayList<Item> pocionesInventario;
    public Heroe() {
        super("heroe",125,35,0.8);//stats de prueba.
        this.oro = 0;
        this.vidaMaxima = vida;
        this.pocionesInventario = new ArrayList<>();
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
    //aún sin limitar a 100.
    public void usarPocionVida(int aumentoVida){
        vida=vida+aumentoVida;
    }

    //sin limitar a turnos.
    public void usarPocionFuerza(int aumentoFuerza){
        ataque=ataque+aumentoFuerza;
    }
    //inventario de pociones
    public void obtenerPocion(Item pocion){
        pocionesInventario.add(pocion);
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

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }
}*/






