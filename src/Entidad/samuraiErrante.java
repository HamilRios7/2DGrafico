package Entidad;

import Main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Primer enemigo del juego: el Samurai Errante.
 * Extiende  Enemigo y añade su comportamiento específico:
 * una habilidad única que se activa al bajar de la mitad de vida,
 * y un contrataque cuando el jugador falla.
 *
 * Flujo de su turno en combate:
 *   Actualizacion detecta que es turno del enemigo
 *   Comprueba si debe activar la habilidad (sin animación)
 *   Si no, arranca la animación de ataque
 *   Al terminar la animación, llama a actuarSamurai() para calcular el daño
 */
public class samuraiErrante extends Enemigo {

    /** true mientras el samurai está reproduciendo su animación de ataque */
    public boolean estoyAtacandoErrante = false;

    /**
     * true cuando el samurai ya ha tomado su decisión de ataque este turno.
     * Evita que  Actualizacion lo llame varias veces en el mismo turno.
     */
    public boolean enemigoYaAtaco = false;

    /** true cuando el samurai ha llegado a 0 de vida y está muriendo */
    public boolean heMuertoEnemigo = false;

    /** true cuando la animación de muerte ha terminado completamente */
    public boolean isAnimacionMuerteTerminadaEnemigo = false;

    /** true cuando la habilidad única ya ha sido activada (solo se activa una vez) */
    boolean isHabilidadActivada = false;

    /**
     * true cuando el samurai acaba de ejecutar un contrataque.
     * Se usa en  KeyHandler para saber que, al cerrar la pantalla
     * del contrataque que aparece de UI, el siguiente paso es el turno normal del enemigo.
     */
    public boolean fueContrataque = false;


    /**
     * Constructor: inicializa la referencia al GamePanel, el área sólida,
     * los valores por defecto y carga las imágenes.
     *
     * @param gp referencia al panel principal del juego
     */
    public samuraiErrante(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(48, 48, gp.tamañoMosaico, gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }

    /**
     * Establece los valores iniciales del samurai: posición, vida, fuerza, etc.
     */
    public void setDefaultValuesEnemigo() {
        enemyWidht = 340;
        x1Enemigo = 710;
        y1Enemigo = 120;

        nombreEnemigo = "Samurai Errante";
        maxLifeEnemigo = 4;
        barraVidaEnemigo = maxLifeEnemigo * 10;
        lifeEnemigo = barraVidaEnemigo;

        strenght = 2;
        fuerzaPorcentaje = 0.4;
    }

    /**
     * Carga todos los sprites del samurai desde los recursos del proyecto.
     * Incluye animaciones de idle (10 frames), ataque (7 frames) y muerte (3 frames).
     */
    public void getEnemyImage() {
        try {
            quieto_1  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_1.png"));
            quieto_2  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_2.png"));
            quieto_3  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_3.png"));
            quieto_4  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_4.png"));
            quieto_5  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_5.png"));
            quieto_6  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_6.png"));
            quieto_7  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_7.png"));
            quieto_8  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_8.png"));
            quieto_9  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_9.png"));
            quieto_10 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_10.png"));

            ata1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_1S.png"));
            ata2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_2S.png"));
            ata3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_3S.png"));
            ata4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_4S.png"));
            ata5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_5S.png"));
            ata6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_6S.png"));
            ata7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_7S.png"));

            morir1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_1.png"));
            morir2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_2.png"));
            morir3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el estado del samurai cada frame.
     * Solo avanza la animación de idle cuando no está atacando ni muerto.
     */
    public void updateSamurai() {
        if (!estoyAtacandoErrante && !heMuertoEnemigo) {
            animacionQuieto();
        }
    }

    /**
     * Dibuja el samurai en pantalla según su estado actual:
     * muerto → animación muerte, atacando → animación ataque, idle → animación quieto.
     *
     * @param gd2 contexto gráfico 2D
     */
    public void drawSamurai(Graphics2D gd2) {
        BufferedImage image = null;

        if (heMuertoEnemigo) {
            image = (BufferedImage) dibujarMuerte();
        } else if (estoyAtacandoErrante) {
            image = (BufferedImage) dibujarAtacar();
        } else {
            image = (BufferedImage) dibujarQuieto();
        }

        if (image != null) {
            gd2.drawImage(image, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
        }
    }

    // ── Animaciones ───────────────────────────────────────────────────────────

    /** Avanza la animación de idle del samurai (ciclo de 10 frames) */
    public void animacionQuieto() {
        idleCounterEnemigo++;
        if (idleCounterEnemigo > 10) {
            idleNumEnemigo++;
            idleCounterEnemigo = 0;
            if (idleNumEnemigo > 10) idleNumEnemigo = 1;
        }
    }

    /**
     * Avanza la animación de ataque del samurai (ciclo de 7 frames).
     * Cuando completa un ciclo, incrementa  contadorMaxFramesEnemigo
     * para señalar que la animación terminó.
     */
    public void animacionAtacar() {
        atacarCounterEnemigo++;
        if (atacarCounterEnemigo > 5) {
            atacarNumEnemigo++;
            atacarCounterEnemigo = 0;
            if (atacarNumEnemigo > 7) {
                atacarNumEnemigo = 1;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    /**
     * Avanza la animación de muerte del samurai (ciclo de 3 frames).
     * Se queda congelado en el último frame al terminar.
     */
    public void animacionMuerte() {
        muerteCounter++;
        if (muerteCounter > 20) {
            muerteNum++;
            muerteCounter = 0;
            if (muerteNum > 3) {
                muerteNum = 3;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    // ── Dibujo de frames ─────────────────────────────────────────────────────

    /** @return el frame de idle correspondiente al contador actual */
    public Image dibujarQuieto() {
        BufferedImage image = null;
        switch (idleNumEnemigo) {
            case 1:  image = quieto_1;  break;
            case 2:  image = quieto_2;  break;
            case 3:  image = quieto_3;  break;
            case 4:  image = quieto_4;  break;
            case 5:  image = quieto_5;  break;
            case 6:  image = quieto_6;  break;
            case 7:  image = quieto_7;  break;
            case 8:  image = quieto_8;  break;
            case 9:  image = quieto_9;  break;
            case 10: image = quieto_10; break;
        }
        return image;
    }
    /** @return el frame de muerte correspondiente al contador actual */
    public Image dibujarMuerte() {
        BufferedImage image = null;
        switch (muerteNum) {
            case 1: image = morir1; break;
            case 2: image = morir2; break;
            case 3: image = morir3; break;
        }
        return image;
    }

    /** @return el frame de ataque correspondiente al contador actual */
    public Image dibujarAtacar() {
        BufferedImage image = null;
        switch (atacarNumEnemigo) {
            case 1: image = ata1; break;
            case 2: image = ata2; break;
            case 3: image = ata3; break;
            case 4: image = ata4; break;
            case 5: image = ata5; break;
            case 6: image = ata6; break;
            case 7: image = ata7; break;
        }
        return image;
    }

    // ── Lógica de combate ────────────────────────────────────────────────────

    /**
     * Calcula y aplica el ataque del samurai al jugador.
     * Este método se llama SOLO cuando la animación de ataque ha terminado,
     * para que el daño ocurra en el momento visual correcto.
     * NO activa la habilidad aquí — eso se gestiona en  Actualizacion
     * antes de arrancar la animación.
     */
    public void actuarSamurai() {
        contadorMaxFramesEnemigo = 0;
        fueEnemigoAtaque = true;

        Random rand = new Random();
        int numero = rand.nextInt(3) + 1;
        if      (numero == 1) ataqueSeguro();      // heredado de Enemigo
        else if (numero == 2) ataqueEquilibrado(); // heredado de Enemigo
        else                  ataqueArriesgado();  // heredado de Enemigo
    }

    /** Delega al método de la clase padre  Enemigo ataqueSeguro() */
    public void ataqueSeguro() { super.ataqueSeguro(); }

    /** Delega al método de la clase padre  Enemigo ataqueEquilibrado() */
    public void ataqueEquilibrado() { super.ataqueEquilibrado(); }

    /** Delega al método de la clase padre Enemigo ataqueArriesgado() */
    public void ataqueArriesgado() { super.ataqueArriesgado(); }

    /**
     * Activa la habilidad única del samurai cuando baja de la mitad de vida.
     * Solo se puede activar una vez por combate.
     * Sube la fuerza del samurai en 2 puntos y muestra la pantalla de habilidad.
     * NO hace animación de ataque — solo muestra el mensaje en la UI.
     */
    public void activarHabilidadUnica() {
        isHabilidadActivada = true;
        strenght = strenght + 2;
        seHaMostradoPantalla = true;  // flag para que UI muestre el mensaje
        gp.isSituacionPelea = false;  // activa la pantalla de resultado
    }

    /**
     * Ejecuta el contrataque del samurai cuando el jugador falla un ataque.
     * Hace 5 puntos de daño fijo al jugador sin animación propia.
     * Marca fueContrataque=true para que  KeyHandler sepa
     * que al cerrar esta pantalla sobre contrataque, debe arrancar el turno normal del enemigo.
     */
    public void contratacar() {
        fueContrataque = true;       // señal para KeyHandler
        fueEnemigoAtaque = true;     // para que UI muestre el resultado
        haFalladoEnemigo = false;
        int daño = 5;
        int vidaRestanteJugador = gp.jugador.getLife() - daño;
        if (vidaRestanteJugador < 0) vidaRestanteJugador = 0;
        gp.jugador.setLife(vidaRestanteJugador);
        dañoHechoEnemigo = daño;
        gp.isSituacionPelea = false; // muestra pantalla del contrataque
    }

    // ── Comprobaciones de fin de animación ───────────────────────────────────

    /**
     * @return true si la animación de ataque del samurai ha completado al menos un ciclo
     */
    public boolean animacionAtaqueTerminadaErrante() {
        return contadorMaxFramesEnemigo >= 1;
    }

    /**
     * @return true si la animación de muerte ha llegado al último frame
     */
    public boolean animacionMuerteTerminadaErrante() {
        return muerteNum == 3;
    }


}