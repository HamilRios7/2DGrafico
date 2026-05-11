package Entidad;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Clase base para todos los enemigos del juego.
 * Extiende  Entidad y añade atributos y métodos específicos de los enemigos,
 * incluyendo su propia lógica de ataque y flags de estado de combate.
 */
public class Enemigo extends Entidad {

    public Enemigo() {}

    /** Nombre del enemigo mostrado en la UI */
    String nombreEnemigo;

    /** Vida máxima del enemigo */
    public int maxLifeEnemigo;

    /**
     * Valor total de la barra de vida del enemigo (maxLifeEnemigo * 10).
     * Se usa para calcular el ancho visual de la barra de vida.
     */
    public int barraVidaEnemigo;

    /** Vida actual del enemigo */
    public int lifeEnemigo;

    /** Ancho en píxeles del sprite del enemigo al dibujarse */
    public int enemyWidht;

    /** Posición del enemigo en pantalla */
    public int x1Enemigo, y1Enemigo;



    // ── Sprites del samurai ───────────────────────────────────────────────────
    // Idle (10 frames), ataque (7 frames), muerte (3 frames)
    public BufferedImage quieto_1,quieto_2,quieto_3,quieto_4,quieto_5, quieto_6,quieto_7,quieto_8,quieto_9,quieto_10,
            ata1,ata2,ata3,ata4,ata5,ata6,ata7,
            morir1,morir2,morir3;



    // ── Flags de estado de combate del enemigo ────────────────────────────────

    /** true si el enemigo falló su último ataque */
    public boolean haFalladoEnemigo = false;

    /** Daño que hizo el enemigo en su último ataque */
    public int dañoHechoEnemigo = 0;

    /** true si el turno que acaba de ocurrir fue un ataque del enemigo */
    public boolean fueEnemigoAtaque = false;

    /**
     * true cuando el samurai acaba de activar su habilidad única.
     * Se usa en  drawInformacionBatalla() para mostrar la pantalla
     * de habilidad con prioridad sobre cualquier otro mensaje ya que ocurren bugs sin esto.
     */
    public boolean seHaMostradoPantalla = false;



    // ── Contadores de animación del enemigo ───────────────────────────────────

    /** Contador de frames para la animación idle del enemigo */
    public int idleCounterEnemigo = 0;
    /** Frame actual de la animación idle del enemigo (1-10) */
    public int idleNumEnemigo = 1;

    /** Contador de frames para la animación de ataque del enemigo */
    public int atacarCounterEnemigo = 0;
    /** Frame actual de la animación de ataque del enemigo (1-7) */
    public int atacarNumEnemigo = 1;

    /**
     * Contador de ciclos completados de la animación del enemigo.
     * Cuando llega a 1, la animación ha terminado.
     */
    int contadorMaxFramesEnemigo = 0;



    // ── Métodos de ataque del enemigo ─────────────────────────────────────────

    private void ejecutarAtaqueEnemigo(Entidad jugador,int ataque, int probabilidadAcierto) {
        fueEnemigoAtaque = true;
        int dañoFinal = (int)(ataque + (strenght * fuerzaPorcentaje));

        Random rand = new Random();
        if (rand.nextInt(100) < probabilidadAcierto) {
           jugador.recibirDaño(jugador,dañoFinal);
            dañoHechoEnemigo = dañoFinal;
            haFalladoEnemigo = false;
        } else {
            haFalladoEnemigo = true;
        }
        gp.isSituacionPelea = false;
    }

    public void ataqueSeguro()      { ejecutarAtaqueEnemigo(gp.jugador,5,  90); }
    public void ataqueEquilibrado() { ejecutarAtaqueEnemigo(gp.jugador,8,  68); }
    public void ataqueArriesgado()  { ejecutarAtaqueEnemigo(gp.jugador, 12, 44); }



    public String getNombre() { return nombre; }
    public int getBarraVidaEnemigo() { return barraVidaEnemigo; }
    public int getLifeEnemigo() { return lifeEnemigo; }
    public void setLifeEnemigo(int lifeEnemigo) { this.lifeEnemigo = lifeEnemigo; }

    public void recibirDaño(Enemigo enemigo, int dañoFinal){
        int vida = enemigo.getLifeEnemigo() - dañoFinal;
        if (vida < 0) vida = 0;
        enemigo.setLifeEnemigo(vida);
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



