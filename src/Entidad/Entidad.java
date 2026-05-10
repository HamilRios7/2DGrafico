package Entidad;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Clase base de la que heredan todas las entidades del juego (Jugador y Enemigos).
 * Contiene los atributos comunes, la lógica de ataque del jugador y los contadores
 * de animación compartidos.
 */
public class Entidad {

    /** Referencia al panel principal del juego, necesario para acceder al estado global */
    public GamePanel gp;

    // Posiciones del jugador en cada escena
    public int x1Jugador, y1Jugador; // Posición en escena 1
    public int x2Jugador, y2Jugador; // Posición en escena 2

    /** Velocidad de movimiento de la entidad */
    public int speed;

    // ── Sprites del jugador ──────────────────────────────────────────────────
    // Caminar derecha (8 frames) e izquierda (8 frames)
    public BufferedImage lft1,lft2,lft3,lft4,lft5,lft6,lft7,lft8,
            rgt1,rgt2,rgt3,rgt4,rgt5,rgt6,rgt7,rgt8;

    // Idle derecha e izquierda (5 frames cada uno)
    public BufferedImage quieto1JugaRight,quieto2JugaRight,quieto3JugaRight,
            quieto4JugaRight,quieto5JugaRight,
            quieto1JugaLeft,quieto2JugaLeft,quieto3JugaLeft,
            quieto4JugaLeft,quieto5JugaLeft;

    // Ataque (6 frames) y muerte (7 frames)
    public BufferedImage ataJuga1,ataJuga2,ataJuga3,ataJuga4,ataJuga5,ataJuga6,
            muerto_1,muerto_2,muerto_3,muerto_4,muerto_5,muerto_6,muerto_7;

    /** Dirección actual de la entidad: "right" o "left" */
    public String direction;
    public String nombre;

    // ── Atributos de combate ─────────────────────────────────────────────────

    /** Vida máxima de la entidad */
    protected int maxLife;

    /** Valor total de la barra de vida (maxLife * 10) */
    public int barraVida;

    /** Vida actual */
    public int life;

    /** Nivel de la entidad */
    public int level;

    /** Fuerza base que se suma al daño final */
    protected int strenght;

    /** Porcentaje de fuerza que se aplica al daño */
    public double fuerzaPorcentaje;


    // ── Flags de combate del jugador ─────────────────────────────────────────

    /** true si el jugador falló su último ataque */
    public boolean heFalladoJugador = false;

    /** Daño que hizo el jugador en su último ataque */
    public int dañoHechoJugador = 0;

    /** true si el turno que acaba de ocurrir fue un ataque del jugador */
    public boolean fuejugadorAtaque = false;

    /**
     * true si el jugador falló y hay un contrataque pendiente de ejecutar.
     * Se marca aquí pero se ejecuta cuando el jugador pulsa Enter en KeyHandler,
     * para respetar como funciona de UI por turnos.
     */
    public boolean contrataquePendiente = false;


    // ── Contadores de animación ───────────────────────────────────────────────

    /** Contador de frames para la animación de movimiento */
    public int moveCounter = 0;
    /** Frame actual de la animación de movimiento (1-8) */
    public int moveNum = 1;

    /** Contador de frames para la animación de muerte */
    public int muerteCounter = 0;
    /** Frame actual de la animación de muerte (1-7) */
    public int muerteNum = 1;

    /** Contador de frames para la animación de idle */
    public int idleCounter = 0;
    /** Frame actual de la animación de idle (1-5) */
    public int idleNum = 1;

    /** Contador de frames para la animación de ataque */
    public int atacarCounter = 0;
    /** Frame actual de la animación de ataque (1-6) */
    public int atacarNum = 1;
    /**
     * Contador que se incrementa cada vez que la animación de ataque completa un ciclo.
     * Cuando llega a 1, significa que la animación terminó.
     */
    int contadorMaxFrames = 0;


    /** Área sólida usada para detección de colisiones */
    public Rectangle solidArea;



    // ── Métodos de ataque del jugador ─────────────────────────────────────────

    private void ejecutarAtaqueJugador(int ataque, int probabilidadAcierto, Enemigo enemigo) {
        fuejugadorAtaque = true;
        int dañoFinal = (int)(ataque + (strenght * fuerzaPorcentaje));

        Random rand = new Random();
        if (rand.nextInt(100) < probabilidadAcierto) {
            int vida = enemigo.getLifeEnemigo() - dañoFinal;
            if (vida < 0) vida = 0;
            enemigo.setLifeEnemigo(vida);
            heFalladoJugador = false;
            dañoHechoJugador = dañoFinal;
        } else {
            heFalladoJugador = true;
            contrataquePendiente = true;
        }
        gp.isSituacionPelea = false;
    }

    public void ataqueSeguro(Enemigo enemigo)      { ejecutarAtaqueJugador(6,  90, enemigo); }
    public void ataqueEquilibrado(Enemigo enemigo) { ejecutarAtaqueJugador(10, 68, enemigo); }
    public void ataqueArriesgado(Enemigo enemigo)  { ejecutarAtaqueJugador(14, 40, enemigo); }

    public String getNombre() { return nombre; }
    public int getLife() { return life; }
    public void setLife(int life) { this.life = life; }
}