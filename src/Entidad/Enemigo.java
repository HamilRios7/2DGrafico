package Entidad;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Clase base abstracta para todos los enemigos del juego.
 * Define la interfaz común que GamePanel y Actualizacion usan,
 * sin importar con qué enemigo concreto están trabajando.
 */
public abstract class Enemigo extends Entidad {

    public Enemigo() {}

    // ── Datos del enemigo ─────────────────────────────────────────────────────

    String nombreEnemigo;
    public int maxLifeEnemigo;
    public int barraVidaEnemigo;
    public int lifeEnemigo;
    public int enemyWidht;
    public int x1Enemigo, y1Enemigo;

    // ── Sprites compartidos (samurai y gigante los usan desde la subclase) ────

    public BufferedImage quieto_1,quieto_2,quieto_3,quieto_4,quieto_5,
            quieto_6,quieto_7,quieto_8,quieto_9,quieto_10,
            ata1,ata2,ata3,ata4,ata5,ata6,ata7,
            morir1,morir2,morir3;

    public BufferedImage quieto_1Gig,quieto_2Gig,quieto_3Gig,quieto_4Gig,quieto_5Gig,quieto_6Gig,
            ata1Gig,ata2Gig,ata3Gig,ata4Gig,ata5Gig,ata6Gig,ata7Gig,ata8Gig,ata9Gig,ata10Gig,ata11Gig,ata12Gig,ata13Gig,ata14Gig,
            morir1Gig,morir2Gig,morir3Gig,morir4Gig,morir5Gig,morir6Gig,morir7Gig,morir8Gig,morir9Gig,morir10Gig,morir11Gig,morir12Gig,morir13Gig,morir14Gig,morir15Gig,morir16Gig;

    // ── Flags de estado de combate ────────────────────────────────────────────
    /** true cuando el enemigo ha ejecutado un contrataque este turno */
    protected boolean fueContrataqueBase = false;  // ← añadir aquí



    /** true si el enemigo falló su último ataque */
    public boolean haFalladoEnemigo = false;

    /** Daño que hizo el enemigo en su último ataque */
    public int dañoHechoEnemigo = 0;

    /** true si el turno que acaba de ocurrir fue un ataque del enemigo */
    public boolean fueEnemigoAtaque = false;

    /** true cuando el enemigo ha llegado a 0 de vida y está muriendo */
    public boolean heMuertoEnemigo = false;

    /** true cuando la animación de muerte ha terminado completamente */
    public boolean isAnimacionMuerteTerminadaEnemigo = false;

    /**
     * true mientras el enemigo está reproduciendo su animación de ataque.
     * Unificado aquí para que Actualizacion no necesite saber el tipo concreto.
     */
    public boolean estoyAtacando = false;

    /**
     * true cuando el enemigo ya ha tomado su decisión de ataque este turno.
     * Evita que Actualizacion lo llame varias veces en el mismo turno.
     */
    public boolean enemigoYaAtaco = false;

    /**
     * true cuando el enemigo acaba de activar su habilidad única.
     * Se usa en drawInformacionBatalla() para mostrar la pantalla con prioridad.
     */
    public boolean seHaMostradoPantalla = false;

    /**
     * true cuando la habilidad única ya ha sido activada (solo se activa una vez).
     * Declarado aquí para que Actualizacion pueda comprobarlo sin conocer
     * el tipo concreto del enemigo.
     */
    public boolean isHabilidadActivada = false;

    // ── Contadores de animación ───────────────────────────────────────────────

    public int idleCounterEnemigo = 0;
    public int idleNumEnemigo = 1;

    public int atacarCounterEnemigo = 0;
    public int atacarNumEnemigo = 1;

    /** Contador de ciclos completados. Cuando llega a 1, la animación terminó. */
    public int contadorMaxFramesEnemigo = 0;

    public int muerteCounter = 0;
    public int muerteNum = 1;

    // ── Métodos abstractos que cada enemigo debe implementar ──────────────────

    /** Actualiza la lógica del enemigo cada frame (idle, animaciones, etc.) */
    public abstract void updateEnemigo();

    /** Dibuja el enemigo en pantalla según su estado actual */
    public abstract void drawEnemigo(Graphics2D g2d);

    /** Avanza la animación de ataque del enemigo */
    public abstract void animacionAtacar();

    /** Avanza la animación de muerte del enemigo */
    public abstract void animacionMuerte();

    /**
     * Calcula y aplica el ataque del enemigo al jugador.
     * Se llama solo cuando la animación de ataque ha terminado.
     */
    public abstract void actuar();

    /**
     * Activa la habilidad única del enemigo (solo una vez por combate).
     * Cada subclase define qué hace su habilidad.
     */
    public abstract void activarHabilidadUnica();

    /** @return true si la animación de ataque ha completado al menos un ciclo */
    public abstract boolean animacionAtaqueTerminada();

    /** @return true si la animación de muerte ha llegado al último frame */
    public abstract boolean animacionMuerteTerminada();

    // ── Contrataque ───────────────────────────────────────────────────────────

    /**
     * Ejecuta el contrataque cuando el jugador falla un ataque.
     * Por defecto no hace nada (enemigos como el Gigante no contratacán).
     * Las subclases que quieran contrataque deben sobreescribir este método.
     */
    public void contratacar() {

    }

    public void activarStun(){

    }

    // ── Lógica de ataque compartida ───────────────────────────────────────────

    protected void ejecutarAtaqueEnemigo(Entidad jugador, int ataque, int probabilidadAcierto) {
        fueEnemigoAtaque = true;
        int dañoFinal = (int)(ataque + (strenght * fuerzaPorcentaje));

        Random rand = new Random();
        if (rand.nextInt(100) < probabilidadAcierto) {
            jugador.recibirDaño(jugador, dañoFinal);
            dañoHechoEnemigo = dañoFinal;
            haFalladoEnemigo = false;
        } else {
            haFalladoEnemigo = true;
        }
        gp.isSituacionPelea = false;
    }

    // ── Getters y setters ─────────────────────────────────────────────────────

    public String getNombre()               { return nombre; }
    public String getNombreEnemigo()        { return nombreEnemigo; }
    public int getBarraVidaEnemigo()        { return barraVidaEnemigo; }
    public int getLifeEnemigo()             { return lifeEnemigo; }
    public void setLifeEnemigo(int v)       { this.lifeEnemigo = v; }

    // ── Contrataque ───────────────────────────────────────────────────────────



    // Samurai
    public boolean fueContrataque() {
        return false;
    }

    public void resetContrataque() {}

    //Gigante
    public boolean fueStuneado() {
        return false;
    }

    public void resetStun() {}

    public void recibirDaño(Enemigo enemigo, int dañoFinal) {
        int vida = enemigo.getLifeEnemigo() - dañoFinal;
        if (vida < 0) {vida = 0;}
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



