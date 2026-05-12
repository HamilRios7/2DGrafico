package Entidad;

import Main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Primer enemigo del juego: el Samurai Errante.
 * Implementa todos los métodos abstractos de Enemigo.
 */
public class samuraiErrante extends Enemigo {


    /**
     * true cuando el samurai acaba de ejecutar un contrataque.
     * Se usa en KeyHandler para saber que al cerrar la pantalla,
     * el siguiente paso es el turno normal del enemigo.
     */
    public boolean fueContrataque = false;


    public samuraiErrante(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(48, 48, gp.tamañoMosaico, gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }

    // ── Inicialización ────────────────────────────────────────────────────────

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
            System.err.println("[Jugador] Error cargando sprites del jugador:");
            e.printStackTrace();
        }
    }

    // ── Métodos abstractos implementados ──────────────────────────────────────

    @Override
    public void updateEnemigo() {
        if (!estoyAtacando && !heMuertoEnemigo) {
            animacionQuieto();
        }
    }

    @Override
    public void drawEnemigo(Graphics2D gd2) {
        BufferedImage image = null;

        if (heMuertoEnemigo) {
            image = (BufferedImage) dibujarMuerte();
        } else if (estoyAtacando) {
            image = (BufferedImage) dibujarAtacar();
        } else {
            image = (BufferedImage) dibujarQuieto();
        }

        if (image != null) {
            gd2.drawImage(image, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
        }
    }

    @Override
    public void animacionAtacar() {
        atacarCounterEnemigo++;
        if (atacarCounterEnemigo > 5) {
            atacarNumEnemigo++;
            atacarCounterEnemigo = 0;

            if(atacarNumEnemigo==5){
                gp.playSE(7);
            }
            if (atacarNumEnemigo > 7) {
                atacarNumEnemigo = 1;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    @Override
    public void animacionMuerte() {
        muerteCounter++;
        if (muerteCounter > 20) {
            muerteNum++;
            muerteCounter = 0;
            if(muerteNum==2){
                gp.playSE(9);
            }
            if (muerteNum > 3) {
                muerteNum = 3;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    @Override
    public void actuar() {
        contadorMaxFramesEnemigo = 0;
        fueEnemigoAtaque = true;

        int opcion = (int)(Math.random() * 3);
        switch (opcion) {
            case 0: ataqueSeguro();      break;
            case 1: ataqueEquilibrado(); break;
            case 2: ataqueArriesgado();  break;
        }
    }

    @Override
    public void activarHabilidadUnica() {
        isHabilidadActivada = true;
        strenght = strenght + 2;
        seHaMostradoPantalla = true;
        gp.isSituacionPelea = false;
    }

    @Override
    public boolean animacionAtaqueTerminada() {
        return contadorMaxFramesEnemigo >= 1;
    }

    @Override
    public boolean animacionMuerteTerminada() {
        return muerteNum == 3;
    }

    // ── Animación idle ────────────────────────────────────────────────────────

    public void animacionQuieto() {
        idleCounterEnemigo++;
        if (idleCounterEnemigo > 10) {
            idleNumEnemigo++;
            idleCounterEnemigo = 0;
            if (idleNumEnemigo > 10) idleNumEnemigo = 1;
        }
    }

    // ── Dibujo de frames ──────────────────────────────────────────────────────

    public Image dibujarQuieto() {
        switch (idleNumEnemigo) {
            case 1:  return quieto_1;
            case 2:  return quieto_2;
            case 3:  return quieto_3;
            case 4:  return quieto_4;
            case 5:  return quieto_5;
            case 6:  return quieto_6;
            case 7:  return quieto_7;
            case 8:  return quieto_8;
            case 9:  return quieto_9;
            case 10: return quieto_10;
            default: return null;
        }
    }

    public Image dibujarMuerte() {
        switch (muerteNum) {
            case 1: return morir1;
            case 2: return morir2;
            case 3: return morir3;
            default: return null;
        }
    }

    public Image dibujarAtacar() {
        switch (atacarNumEnemigo) {
            case 1: return ata1;
            case 2: return ata2;
            case 3: return ata3;
            case 4: return ata4;
            case 5: return ata5;
            case 6: return ata6;
            case 7: return ata7;
            default: return null;
        }
    }

    // ── Tipos de ataque ───────────────────────────────────────────────────────

    public void ataqueSeguro()      { ejecutarAtaqueEnemigo(gp.jugador, 5,  0); }
    public void ataqueEquilibrado() { ejecutarAtaqueEnemigo(gp.jugador, 8,  0); }
    public void ataqueArriesgado()  { ejecutarAtaqueEnemigo(gp.jugador, 12, 0); }

    // ── Contrataque ───────────────────────────────────────────────────────────

    /**
     * Ejecuta el contrataque cuando el jugador falla un ataque.
     * Marca fueContrataque=true para que KeyHandler sepa que al cerrar
     * esta pantalla debe arrancar el turno normal del enemigo.
     */
    public void contratacar() {
        fueContrataque = true;
        fueEnemigoAtaque = true;
        haFalladoEnemigo = false;
        int daño = 5;
        int vidaRestante = gp.jugador.getLife() - daño;
        if (vidaRestante < 0) vidaRestante = 0;
        gp.jugador.setLife(vidaRestante);
        dañoHechoEnemigo = daño;
        gp.isSituacionPelea = false;
    }

    @Override
    public boolean fueContrataque() {
        return fueContrataque;  // usa el campo que ya tienes
    }

    @Override
    public void resetContrataque() {
        fueContrataque = false;
    }
}