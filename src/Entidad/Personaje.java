package Entidad;

public class Personaje {
    protected String nombre;
    protected int vida;
    protected int ataque;
    protected double presicion;
    public Personaje (String nombre,int vida, int ataque, double presicion) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.presicion = presicion;
    }
    //metodo para que todos los personajes ataquen.
    //el resultado de la multiplicación de presición base por la del multiplicador nunca puede ser >1 ya que sino el porcentaje de acierto será 100%
    public void ejecutarAtaque(Personaje objetivo, String nombreAtaque, double multiplicadorPresicion, double multiplicadorAtaque) {
        double probabilidad = presicion * multiplicadorPresicion;// calcular probabilidad de acierto
        probabilidad = Math.max(0.0, Math.min(1.0, probabilidad)); // limitar entre 0% y 100% (evita errores de balance)
        if (Math.random() <= probabilidad) {
            int daño = (int) Math.round(ataque * multiplicadorAtaque);
            objetivo.recibirDaño(daño);
            System.out.println(this.nombre + " ha usado " + nombreAtaque + " causando " + daño + " puntos de daño");
        } else {
            System.out.println(this.nombre + " ha usado " + nombreAtaque + " pero ha fallado");
        }
    }

    public void mostrarDatos(){
        System.out.println("Nombre: "+nombre);
        System.out.println("Vida: " + vida);
        System.out.println("Ataque: " + ataque);
        System.out.println("Presicion: " + presicion);
    }

    public void recibirDaño(int daño){
        vida=vida-daño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getVida() {
        return vida;
    }


    public void setVida(int vida) {
        this.vida = vida;
    }


    public int getAtaque() {
        return ataque;
    }


    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public double getPrecision() {
        return presicion;
    }


    public void setPrecision(double precision) {
        this.presicion = precision;
    }
}
