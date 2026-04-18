package Entidad;
//modificada para que se extienda de la clase Personaje
public class Enemigo extends Personaje {
    public Enemigo(String nombre, int vida, int ataque, double precision) {
        super(nombre,vida, ataque, precision);
    }

    public int dropOro(){//Dropea entre 2 a 5 de oro si ha muerto.
        int oro=0;
        if(this.vida<=0){
            oro= (int) (Math.random() * (5 - 2 + 1)) + 2;
        }
        return oro;
    }

    public void mostrarDatos(){
        System.out.println("Nombre: "+nombre);
        super.mostrarDatos();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
