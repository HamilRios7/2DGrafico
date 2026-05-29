package Entidad;

import Main.GamePanel;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Clase base abstracta para todas las entidades del juego (Jugador y Enemigos).
 *
 * Contiene solo los atributos y comportamientos que son comunes a todas
 * las entidades. Lo específico del jugador vive en Jugador; lo específico
 * de los enemigos vive en Enemigo o en los enemigos en concreto.
 */
public abstract class Entidad {

    //----Referencia al panel principal------

    protected GamePanel gp;


    // ---Velocidad y direccion hacia donde mira ----

    protected int speed;
    protected String direction;

    // -----Identidad------

    protected String nombre;

    // ---- Atributos de combate -----

    /** Vida máxima. */
    protected int maxLife;

    /** Valor total de la barra de vida (maxLife * 10). */
    protected int barraVida;

    /** Vida actual que se actualiza segun acontecimientos. */
    protected int life;

    /** Fuerza base que se suma al daño final. */
    protected int strenght;

    /** Fracción de fuerza que se aplica al daño. */
    protected double fuerzaPorcentaje;


    // ----- Flags de combate ------

    /** true si esta entidad falló su último ataque. */
    protected boolean heFallado = false;

    /** Daño causado en el último ataque (0 si falló). */
    protected int dañoHecho = 0;

    /** true si el turno que acaba de ocurrir fue un ataque de esta entidad. */
    protected boolean fueAtaque = false;

    /**
     * true si una entidad falló y hay un contrataque pendiente.
     * Se ejecuta cuando el jugador pulsa Enter en KeyHandler.
     */
    protected boolean contrataquePendiente = false;


    // ------ Sprites comunes a todas las entidades (Jugador en este caso) -----

    // Caminar derecha / izquierda (8 frames cada uno)
    protected BufferedImage lft1, lft2, lft3, lft4, lft5, lft6, lft7, lft8;
    protected BufferedImage rgt1, rgt2, rgt3, rgt4, rgt5, rgt6, rgt7, rgt8;

    // Idle derecha / izquierda (5 frames cada uno)
    protected BufferedImage idle1Right, idle2Right, idle3Right, idle4Right, idle5Right;
    protected BufferedImage idle1Left,  idle2Left,  idle3Left,  idle4Left,  idle5Left;

    // Muerte (7 frames)
    protected BufferedImage muerto1, muerto2, muerto3, muerto4,
            muerto5, muerto6, muerto7;

    protected BufferedImage ataJuga1,ataJuga2,ataJuga3,ataJuga4,ataJuga5,ataJuga6;
    // ----- Contadores de animación --------

    protected int moveCounter  = 0;
    protected int moveNum      = 1;

    protected int muerteCounter = 0;
    protected int muerteNum     = 1;

    protected int idleCounter  = 0;
    protected int idleNum      = 1;

    protected int atacarCounter = 0;
    protected int atacarNum     = 1;

    /**
     * Contador de ciclos completados de la animación de ataque.
     * Cuando llega a 1 la animación ha terminado.
     */
    protected int contadorMaxFrames = 0;


    // -------- Colisiones --------

    protected Rectangle solidArea;


    //------- Combate -------

    /**
     * Aplica daño a ESTA entidad (reduce su propia vida).
     * Nunca deja la vida por debajo de 0.
     *
     * @param dañoFinal puntos de daño a recibir
     */
    public void recibirDaño(int dañoFinal) {
        life = Math.max(0, life - dañoFinal);
    }

    /**
     * Lógica central del ataque del jugador: calcula el daño, aplica
     * probabilidad de acierto y actualiza los flags de combate.
     *
     * @param ataque              daño base del tipo de ataque
     * @param probabilidadAcierto porcentaje de acierto (0–100)
     * @param enemigo             objetivo que recibirá el daño
     */
    protected void ejecutarAtaque(int ataque, int probabilidadAcierto, Enemigo enemigo) {
        // evita doble ejecución en el mismo turno
        if (fueAtaque) return;

        fueAtaque = true;

        //Calculo sobre daño final a aplicar a una entidad si se acierta
        int dañoFinal = (int) (ataque + (strenght * fuerzaPorcentaje));

        Random rand = new Random();

        if (rand.nextInt(100) < probabilidadAcierto) {

            //Aplicamos recibir daño para el enemigo a atacar
            enemigo.recibirDaño(dañoFinal);

            //Guardamos variables al acertar para mostrar
            heFallado = false;
            dañoHecho = dañoFinal;
        } else {
            //Guardamos variables y contrataque nos sirve para Samurai
            heFallado            = true;
            dañoHecho            = 0;
            contrataquePendiente = true;
        }

        //Para mostrar por pantalla la informacion del ataque
        gp.isSituacionPelea = false;
    }

    /** Ataque seguro: daño bajo, acierto muy alto (92 %). */
    public void ataqueSeguro(Enemigo enemigo)      { ejecutarAtaque(5, 92, enemigo); }

    /** Ataque equilibrado: daño medio, acierto medio (68 %). */
    public void ataqueEquilibrado(Enemigo enemigo) { ejecutarAtaque(10,  68, enemigo); }

    /** Ataque arriesgado: daño alto, acierto bajo (40 %). */
    public void ataqueArriesgado(Enemigo enemigo)  { ejecutarAtaque(16,  40, enemigo); }


    // ----- Getters / setters ------

    public String getNombre()       { return nombre; }

    public int  getLife()           { return life; }
    public void setLife(int life)   { this.life = life; }
    public int  getBarraVida()      { return barraVida; }


    public boolean isHeFallado()            { return heFallado; }
    public int     getDañoHecho()           { return dañoHecho; }
    public boolean isFueAtaque()            { return fueAtaque; }
    public boolean isContrataquePendiente() { return contrataquePendiente; }


    public void setFueAtaque(boolean fueAtaque) {
        this.fueAtaque = fueAtaque;
    }

    public void setContrataquePendiente(boolean contrataquePendiente) {
        this.contrataquePendiente = contrataquePendiente;
    }


    public String getNombreEnemigo() {
        return nombre;
    }

    public int getStrenght() {
        return strenght;
    }

    public void setStrenght(int strenght) {
        this.strenght = strenght;
    }
}