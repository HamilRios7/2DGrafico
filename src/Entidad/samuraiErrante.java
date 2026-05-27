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

    //----CONSTRUCTOR-----

    /**
     *
     * Crea el samurai, inicializa su área de colisión, sus valores por defecto
     * y carga todos los sprites desde los metodos de esta clase
     *
     * @param gp referencia al panel principal del juego.
     */
    public samuraiErrante(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(48, 48, gp.tamañoMosaico, gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }

    // ------Defult Valores de Samurai-----


    /**
     * Establece los valores iniciales del samurai al comenzar una nueva partida.
     *
     * Posición de inicio, velocidad, nivel, atributos de combate y vida máxima.
     * La barra de vida visual se escala con { barraVida = maxLife × 10}.
     *
     */
    public void setDefaultValuesEnemigo() {
        enemyWidth = 340;
        xEnemigo = 710;
        yEnemigo = 120;

        nombre = "Samurai Errante";
        maxLife   = 5;
        barraVida = maxLife * 10; // 50
        life      = barraVida;
        strenght         = 3;
        fuerzaPorcentaje = 1.8;
    }


    /**
     * Carga a las variables BufferedImage  todos los sprites del samurai:
     *  ataque (7 frames),idle izquierda (10 frames cada uno) y muerte (3 frames).
     *
     */
    public void getEnemyImage() {
        try {


            //getResourceAsStream busca un archivo dentro de tu proyecto y lo abre como un flujo de datos para poder  leerlo.
            // getClass().getClassLoader() sirve para acceder a archivos dentro del proyecto, en este caso, res
            // ImageIO.read(...) lee el objeto y lo convierte en BufferedImage

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

    // -----  Update de escenas (es llamado cada frame por GamePanel) ------


    /**
     * Lógica de actualización del jugador en la escena 2 y combate.
     *
     * Si el jugador ha muerto, delega en  #animacionMuerte() y corta
     * el resto de la lógica. En la escena 2 aplica movimiento con velocidad 6
     * (ligeramente mayor que en escena 1) y sus propios límites de pantalla.
     * En el estado de pelea solo se actualiza la animación idle.
     *
     */
    @Override
    public void updateEnemigo() {
        if (!estoyAtacando && !heMuertoEnemigo) {
            animacionQuieto();
        }
    }


    // ----- DRAW ----- (llamado cada frame desde GamePanel)

    /**
     * Dibuja el jugador en la escena 1 con su animación de movimiento o idle.
     *
     * @param gd2 contexto gráfico 2D del frame actual
     */
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
            gd2.drawImage(image, xEnemigo, yEnemigo, enemyWidth, enemyWidth, null);
        }
    }

    // ------ CONTROLADORES DE ANIMACIÓN ------

    /**
     * Avanza el contador de la animación de ataque.
     * Cambia de fotograma cada 6 frames; cicla entre sprite  1 y  sprite 6.
     * Al completar un ciclo completo incrementa  contadorMaxFrames,
     * que sirve como señal de que la animación ha terminado.
     */
    @Override
    public void animacionAtacar() {
        atacarCounter++;
        if (atacarCounter > 5) {
            atacarNum++;
            atacarCounter = 0;

            if(atacarNum==5){
                gp.playSE(7);
            }
            if (atacarNum > 7) {
                atacarNum = 1;
                contadorMaxFrames++;
            }
        }
    }

    /**
     * Avanza el contador de la animación de muerte.
     * Cambia de fotograma cada 14 frames ; se detiene en el sprite 7
     * (no es bucle, el último frame queda congelado).
     */
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
                contadorMaxFrames++;
            }
        }
    }


    /**
     * Avanza el contador de la animación idle (en reposo).
     * Cambia de fotograma cada 10 frames; cicla entre sprite 1 y  sprite 5.
     */

    public void animacionQuieto() {
        idleCounter++;
        if (idleCounter > 10) {
            idleNum++;
            idleCounter = 0;
            if (idleNum > 10) idleNum = 1;
        }
    }

    // -----  Seleccion de Sprite segun numero animacion -----


    /**
     * Devuelve el sprite de idle correspondiente a  idleNum
     * y a la dirección actual del jugador.
     *
     * @return sprite idle actual, o  null si  idleNum está fuera de rango
     */
    public Image dibujarQuieto() {
        switch (idleNum) {
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

    /**
     * Devuelve el sprite de idle correspondiente a  idleNum
     * y a la dirección actual del jugador.
     *
     * @return sprite idle actual, o  null si  idleNum está fuera de rango
     */
    public Image dibujarMuerte() {
        switch (muerteNum) {
            case 1: return morir1;
            case 2: return morir2;
            case 3: return morir3;
            default: return null;
        }
    }


    /**
     * Devuelve el sprite de idle correspondiente a  idleNum
     * y a la dirección actual del jugador.
     *
     * @return sprite idle actual, o  null si  idleNum está fuera de rango
     */
    public Image dibujarAtacar() {
        switch (atacarNum) {
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



    // COMBATE
    /**
     * Controla el ataque del gigante, este se ejecutara cada vez que se llame a este metodo, el cual
     * se llama en el update de la pelea, este metodo elige aleatoriamente
     * entre 3 tipos de ataque, cada uno con diferente fuerza y probabilidad de fallo
     */
    @Override
    public void actuar() {
        contadorMaxFrames = 0;
        fueAtaque = true;

        int opcion = (int)(Math.random() * 3);
        switch (opcion) {
            case 0: ataqueSeguro();      break;
            case 1: ataqueEquilibrado(); break;
            case 2: ataqueArriesgado();  break;
        }
    }

    /**
     *Una vez que es llamada , deja isHabilidadActivada en true, sube la fuerza permanentemente y permite mostrarlo con prioridad parando la pelea
     */
    @Override
    public void activarHabilidadUnica() {
        isHabilidadActivada = true;
        strenght = strenght + 2;
        seHaMostradoPantalla = true;
        gp.isSituacionPelea = false;
    }

    /** Ataque seguro: daño bajo base de ataque , acierto medio/alto debido al calculo medio de daño final (94 %). */
    public void ataqueSeguro()      { ejecutarAtaqueEnemigo(gp.jugador, 2,  94); }

    /** Ataque equilibrado: daño medio base  de ataque , acierto medio/bajo debido al calculo medio de daño final (78 %). */
    public void ataqueEquilibrado() { ejecutarAtaqueEnemigo(gp.jugador, 5,  78); }

    /** Ataque arriesgado: daño alto base de ataque , acierto bajo debido al calculo medio de daño final (55 %). */
    public void ataqueArriesgado()  { ejecutarAtaqueEnemigo(gp.jugador, 9,  55); }



    /**
     * Ejecuta el contrataque cuando el jugador falla un ataque.
     * Marca fueContrataque=true para que KeyHandler sepa que al cerrar
     * esta pantalla debe arrancar el turno normal del enemigo.
     */
    public void contratacar() {
        fueContrataque = true;
        fueAtaque = true;
        heFallado = false;
        int daño = 5;
        int vidaRestante = gp.jugador.getLife() - daño;
        if (vidaRestante < 0) vidaRestante = 0;
        gp.jugador.setLife(vidaRestante);
        dañoHecho = daño;
        gp.isSituacionPelea = false;
    }


    @Override
    public boolean fueContrataque() {
        return fueContrataque;
    }

    @Override
    public void resetContrataque() {
        fueContrataque = false;
    }


    // ------ COMPROBACIONES DE FIN DE ANIMACIÓN ----------


    /**
     * Indica si la animación de ataque ha completado al menos un ciclo completo.
     *
     * @return true si contadorMaxFrames ≥ 1
     */
    @Override
    public boolean animacionAtaqueTerminada() {
        return contadorMaxFrames >= 1;
    }



    /**
     * Indica si la animación de muerte ha llegado al frame final (frame 7).
     *
     * @return  true si muerteNum == 7
     */
    @Override
    public boolean animacionMuerteTerminada() {
        return muerteNum == 3;
    }

}