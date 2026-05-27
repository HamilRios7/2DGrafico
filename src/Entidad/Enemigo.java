package Entidad;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Clase base abstracta para todos los enemigos del juego.
 *
 * Hereda de Entidad los atributos comunes (life, maxLife, barraVida,
 * strenght, fuerzaPorcentaje, nombre, contadores de animación, etc.) y
 * añade únicamente lo que es exclusivo de los enemigos: sprites propios,
 * flags de estado de combate del enemigo y los métodos abstractos que
 * cada enemigo concreto debe implementar.
 *
 */
public abstract class Enemigo extends Entidad {

    // Posición y tamaño del enemigo en pantalla

    /** Tamaño en píxeles con la que se dibuja el sprite del enemigo. */
    public int enemyWidth;

    /** Posición horizontal del enemigo en la escena. */
    public int xEnemigo;

    /** Posición vertical del enemigo en la escena. */
    public int yEnemigo;


    // ----- Sprites exclusivos de los enemigos ------
    //    Cada subclase carga los suyos en getEnemyImage().

    //    Se declaran aquí para que Entidad no los tenga (el jugador
    //    tiene sus propios sprites declarados en Jugador).

    //---- Samurai (idle 10 frames, ataque 7, muerte 3)---
    protected BufferedImage quieto_1,  quieto_2,  quieto_3,  quieto_4,  quieto_5,
            quieto_6,  quieto_7,  quieto_8,  quieto_9,  quieto_10;
    protected BufferedImage ata1, ata2, ata3, ata4, ata5, ata6, ata7;
    protected BufferedImage morir1, morir2, morir3;

    //---- Gigante (idle 6 frames, ataque 14, muerte 16)----
    protected BufferedImage quieto_1Gig, quieto_2Gig, quieto_3Gig,
            quieto_4Gig, quieto_5Gig, quieto_6Gig;
    protected BufferedImage ata1Gig,  ata2Gig,  ata3Gig,  ata4Gig,  ata5Gig,
            ata6Gig,  ata7Gig,  ata8Gig,  ata9Gig,  ata10Gig,
            ata11Gig, ata12Gig, ata13Gig, ata14Gig;
    protected BufferedImage morir1Gig,  morir2Gig,  morir3Gig,  morir4Gig,
            morir5Gig,  morir6Gig,  morir7Gig,  morir8Gig,
            morir9Gig,  morir10Gig, morir11Gig, morir12Gig,
            morir13Gig, morir14Gig, morir15Gig, morir16Gig;


    // ----- Flags de estado exclusivos de enemigos----

    /** true mientras el enemigo está reproduciendo su animación de ataque. */
    public boolean estoyAtacando = false;

    /**
     * true cuando el enemigo ya ha tomado su decisión de ataque este turno.
     * Consigue evitar que Actualizacion lo llame varias veces en el mismo turno.
     */
    public boolean enemigoYaAtaco = false;

    /** true cuando la vida llega a 0 y empieza la animación de muerte. */
    public boolean heMuertoEnemigo = false;

    /** true cuando la animación de muerte ha terminado completamente. */
    public boolean isAnimacionMuerteTerminadaEnemigo = false;

    /**
     * true cuando el enemigo acaba de activar su habilidad única.
     * Se usa en drawInformacionBatalla() para mostrar la pantalla de habilidad con prioridad.
     */
    public boolean seHaMostradoPantalla = false;

    /**
     * true cuando la habilidad única ya ha sido activada (solo una vez por combate).
     * Declarado aquí para que Actualizacion pueda comprobarlo sin conocer
     * el tipo concreto del enemigo.
     */
    public boolean isHabilidadActivada = false;


    // ---- Métodos abstractos que cada enemigo concreto debe implementar ----

    /** Actualiza la lógica del enemigo cada frame (idle). */
    public abstract void updateEnemigo();

    /** Dibuja el enemigo en pantalla según su estado actual. */
    public abstract void drawEnemigo(Graphics2D g2d);

    /** Hace la animación de ataque. */
    public abstract void animacionAtacar();

    /** Hace la animación de muerte. */
    public abstract void animacionMuerte();

    /**
     * Elige ataque.
     * Calcula y aplica el ataque del enemigo al jugador.
     * Se llama cuando la animación de ataque ha terminado.
     */
    public abstract void actuar();

    /**
     * Activa la habilidad única (solo una vez por combate).
     * Cada subclase define qué hace.
     */
    public abstract void activarHabilidadUnica();

    /** @return true si la animación de ataque ha completado al menos un ciclo. */
    public abstract boolean animacionAtaqueTerminada();

    /** @return true si la animación de muerte ha llegado al último frame. */
    public abstract boolean animacionMuerteTerminada();


    // ----- Métodos con comportamiento por defecto-----

    /**
     * Ejecuta el contrataque cuando el jugador falla.
     * Por defecto no hace nada; las subclases que contratacan lo sobreescriben.
     * Solo podria Samurai
     */
    public void contratacar() {}

    /**
     * Activa el stun del enemigo.
     * Por defecto no hace nada; las subclases que stuneean lo sobreescriben.
     * Solo podria Gigante
     */
    public void activarStun() {}

    /** @return true si el enemigo fue stuneado este turno , por defecto es false. */
    public boolean fueStuneado()   { return false; }

    /** Reinicia el flag de stun , es decir la coloca en false de nuevo directamente , en esta clase no hace nada. */
    public void resetStun()        {}

    /** @return true si el enemigo ejecutó un contrataque este turno, por defecto false. */
    public boolean fueContrataque() { return false; }

    /** Reinicia el flag de contrataque , asi vuelve de nuevo a false una vez que lo hace , por defecto no hace nada. */
    public void resetContrataque()  {}




    // ------- Lógica de ataque del enemigo-------

    /**
     * Calcula y aplica el ataque del enemigo al jugador usando los atributos
     * heredados de Entidad (strenght, fuerzaPorcentaje) y aplicandolo con un metodo de Entidad

     * @param entidad          entidad objetivo (solo el jugador)
     * @param ataque              daño base del tipo de ataque
     * @param probabilidadAcierto porcentaje de acierto (0–100)
     */
    protected void ejecutarAtaqueEnemigo(Entidad entidad, int ataque, int probabilidadAcierto) {
        //heredado de entidad
        fueAtaque = true;

        //calculamos daño , poniendo la fuerza y su fraccion mas importante que el daño base de ataque en todas las entidades
        int dañoFinal = (int) (ataque + (strenght * fuerzaPorcentaje));

        Random rand = new Random();

        //Si el numero generado es menor la probabilidad , entonces acertamos si es mayor, fallamos
        //50<90 -> Acierto
        // 95<90 -> Fallo
        if (rand.nextInt(100) < probabilidadAcierto) {
            //aplicamos daño al jugador usando el método recibirDaño() de Entidad
            entidad.recibirDaño(dañoFinal);

            //metemos el daño y si ha fallado en variables heredadas , que serviran para mostrar la informacion en pantalla
            dañoHecho = dañoFinal;
            heFallado = false;
        } else {
            heFallado = true;
            dañoHecho = 0;
        }

        //Lo colocamos una vez que ejecuta el ataque en false para que pueda mostrar en pantalla informacion y no continue la logica de combate
        gp.isSituacionPelea = false;
    }


    // ------Getters de enemigo -------
    // Ya están definidos en Entidad

    public int  getLife()       {return super.getLife(); }
    public void setLife(int v)  { super.setLife(v); }
    public int  getBarraVida()  { return super.getBarraVida(); }
}