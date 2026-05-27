package Entidad;

import Main.GamePanel;
import Objetos.Obj_PocionVida;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Enemigo jefe del juego: el Gigante.
 * Implementa todos los métodos abstractos de Enemigo.
 */
public class Gigante extends Enemigo {

  public boolean fueStun=false;

    public Gigante(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(48, 48, gp.tamañoMosaico, gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }

    // ----- Inicialización ------

    public void setDefaultValuesEnemigo() {
        enemyWidth = 500;
        xEnemigo = 588;
        yEnemigo = 46;

        nombre = "Gigante";
        maxLife  = 13;
        barraVida = maxLife * 10; // 200
        life      = barraVida;
        strenght         = 8;
        fuerzaPorcentaje = 2.0;
    }


    /**
     * Carga a las variables BufferedImage  todos los sprites del gigante:
     *  ataque (14 frames), idle  izquierda (6 frames cada uno) y muerte (16 frames).
     */
    public void getEnemyImage() {
        try {

            //getResourceAsStream busca un archivo dentro de tu proyecto y lo abre como un flujo de datos para poder  leerlo.
            // getClass().getClassLoader() sirve para acceder a archivos dentro del proyecto, en este caso, res
            // ImageIO.read(...) lee el objeto y lo convierte en BufferedImage

            quieto_1Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_1.png"));
            quieto_2Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_2.png"));
            quieto_3Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_3.png"));
            quieto_4Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_4.png"));
            quieto_5Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_5.png"));
            quieto_6Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/idle_6.png"));

            ata1Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_1.png"));
            ata2Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_2.png"));
            ata3Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_3.png"));
            ata4Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_4.png"));
            ata5Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_5.png"));
            ata6Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_6.png"));
            ata7Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_7.png"));
            ata8Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_8.png"));
            ata9Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_9.png"));
            ata10Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_10.png"));
            ata11Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_11.png"));
            ata12Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_12.png"));
            ata13Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_13.png"));
            ata14Gig= ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/1_atk_14.png"));

            morir1Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_1.png"));
            morir2Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_2.png"));
            morir3Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_3.png"));
            morir4Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_4.png"));
            morir5Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_5.png"));
            morir6Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_6.png"));
            morir7Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_7.png"));
            morir8Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_8.png"));
            morir9Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_9.png"));
            morir10Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_10.png"));
            morir11Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_11.png"));
            morir12Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_12.png"));
            morir13Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_13.png"));
            morir14Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_14.png"));
            morir15Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_15.png"));
            morir16Gig = ImageIO.read(getClass().getClassLoader().getResourceAsStream("gigantejefe/death_16.png"));
        } catch (IOException e) {
            System.err.println("[Gigante] Error cargando sprites del gigante:");
            e.printStackTrace();
        }
    }

    // ---Animaciones y dibujar estas -----

    /**
     * Actualizamos solo para la animacion quieta, ya que las otras se actualizan en sus propios métodos (animacionAtacar y animacionMuerte) para mayor control en su activacion
     */
    @Override
    public void updateEnemigo() {
        if (!estoyAtacando && !heMuertoEnemigo) {
            animacionQuieto();
        }
    }
    /**
     * Dibujar que atraviesa los ifs y segun estos , carga la imagen y dibuja
     *  @param gd2   Dibuja los graficos 2d que obtiene de PaintComponent
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


    /**
     * Animacion  de ataque de gigante, esta permite que se repita una vez que llega al final
     *  a partir que llega a un frame de sprite, este genera un sonido de ataque
     */
    @Override
    public void animacionAtacar() {
        atacarCounter++;
        if (atacarCounter > 6) {
            atacarNum++;
            atacarCounter = 0;
            if(atacarNum==10){
                gp.playSE(11);
            }
            if (atacarNum > 14) {
                atacarNum= 1;
                contadorMaxFrames++;
            }
        }
    }


    /**
     * Animacion  de muerte de gigante, esta se quedara igual y no desaperecera una vez que llegue al final,
     * a partir que llega a un frame de sprite, este genera un sonido de muerte
     *
     */
    @Override
    public void animacionMuerte() {
        muerteCounter++;
        if (muerteCounter > 8) {
            muerteNum++;
            muerteCounter = 0;
            if(muerteNum==9){
                gp.playSE(10);
            }
            if (muerteNum > 16) {
                muerteNum = 16;
                contadorMaxFrames++;
            }
        }
    }


    /**
     *Controla cada cuantos frames  cambia de sprite la animacion quieta, esta se repetira una vez que llegue al final
     */

    public void animacionQuieto() {
        idleCounter++;
        if (idleCounter > 10) {
            idleNum++;
            idleCounter = 0;
            if (idleNum > 6) idleNum = 1;
        }
    }


    /**
     *Dibuja el sprite correspondiente a cada frame de la animacion quieta, segun el numero del frame que se le asigna a cada sprite
     */
    public Image dibujarQuieto() {
        switch (idleNum) {
            case 1: return quieto_1Gig;
            case 2: return quieto_2Gig;
            case 3: return quieto_3Gig;
            case 4: return quieto_4Gig;
            case 5: return quieto_5Gig;
            case 6: return quieto_6Gig;
            default: return null;
        }
    }

    /**
     *Dibuja el sprite correspondiente a cada frame de la animacion muerte, segun el numero del frame que se le asigna a cada sprite
     */
    public Image dibujarMuerte() {
        switch (muerteNum) {
            case 1: return morir1Gig;
            case 2: return morir2Gig;
            case 3: return morir3Gig;
            case 4: return morir4Gig;
            case 5: return morir5Gig;
            case 6: return morir6Gig;
            case 7: return morir7Gig;
            case 8: return morir8Gig;
            case 9: return morir9Gig;
            case 10: return morir10Gig;
            case 11: return morir11Gig;
            case 12: return morir12Gig;
            case 13: return morir13Gig;
            case 14: return morir14Gig;
            case 15: return morir15Gig;
            case 16: return morir16Gig;



            default: return null;
        }
    }


    /**
     *Dibuja el sprite correspondiente a cada frame de la animacion ataque, segun el numero del frame que se le asigna a cada sprite
     */
    public Image dibujarAtacar() {
        switch (atacarNum) {
            case 1: return ata1Gig;
            case 2: return ata2Gig;
            case 3: return ata3Gig;
            case 4: return ata4Gig;
            case 5: return ata5Gig;
            case 6: return ata6Gig;
            case 7: return ata7Gig;
            case 8: return ata8Gig;
            case 9: return ata9Gig;
            case 10: return ata10Gig;
            case 11: return ata11Gig;
            case 12: return ata12Gig;
            case 13: return ata13Gig;
            case 14: return ata14Gig;


            default: return null;
        }
    }


    //----- Logica tipo combate  ------

    /**
     *Una vez que es llamada , deja isHabilidadActivada en true, sube la fuerza permanentemente y permite mostrarlo con prioridad parando la pelea
     */
    @Override
    public void activarHabilidadUnica() {
        isHabilidadActivada = true;
        strenght = strenght + 3;
        seHaMostradoPantalla = true;
        gp.isSituacionPelea = false;
    }


    /**
     * Controla el ataque del gigante, este se ejecutara cada vez que se llame a este metodo, el cual
     * se llama en el update de la pelea, este metodo elige aleatoriamente
     * entre 3 tipos de ataque, cada uno con diferente fuerza y probabilidad de fallo
     */
    @Override
    public void actuar() {


        contadorMaxFrames = 0;
        //Colocamos las flags para la ActualizacionCombate
        fueAtaque = true;

        //Hacemos un random y lo multiplicamos por 3  , segun el numero , elegimos un caso de ataque
        int opcion = (int)(Math.random() * 3);
        switch (opcion) {
            case 0: ataqueSeguro();      break;
            case 1: ataqueEquilibrado(); break;
            case 2: ataqueArriesgado();  break;
        }

        //Si no llegamos a fallar, activamos la probabilidad de existencia de stun
        if(!heFallado) {
            activarStun();
        }
    }


    /** Ataque seguro: daño bajo base de ataque , acierto medio/alto debido al calculo medio de daño final (68 %). */
    public void ataqueSeguro()      { ejecutarAtaqueEnemigo(gp.jugador, 4,  68); }

    /** Ataque equilibrado: daño medio base  de ataque , acierto medio/bajo debido al calculo medio de daño final (48 %). */
    public void ataqueEquilibrado() { ejecutarAtaqueEnemigo(gp.jugador, 9,  48); }

    /** Ataque arriesgado: daño alto base de ataque , acierto bajo debido al calculo medio de daño final (28 %). */
    public void ataqueArriesgado()  { ejecutarAtaqueEnemigo(gp.jugador, 16, 28); }


    /** Recogemos si el enemigo fue stuneado. */
    @Override
    public boolean fueStuneado() {
        return fueStun;
    }


    /** Reseteamos si hubo un stun */
    @Override
    public void resetStun() {
        fueStun = false;
    }

    /** Si acierta el ataque , activamos una probabilidad de 25 para que el Gigante stunee y quite turno de jugador */
    public void activarStun(){
        int rand = new java.util.Random().nextInt(100);
        if (rand < 25) {
            System.out.println("me active");
            fueStun=true;
        }
    }


    // ------ COMPROBACIONES DE FIN DE ANIMACIÓN ----------

    /**
     *Devuelve un boolean que sera true cuando al llamar la animacion de ataque ,  sera true. Nos sirve para saber cuando aplicar el daño y no actue el daño logico antes que la animacion
     */
    @Override
    public boolean animacionAtaqueTerminada() {
        return contadorMaxFrames == 1;
    }

    /**
     *Devuelve un boolean que sera true cuando al llamar la animacion de muerte ,  sera true. Nos sirve para saber que ha muerto y terminar la pelea
     */
    @Override
    public boolean animacionMuerteTerminada() {
        return muerteNum == 16;
    }

    // contratacar() nO se sobreescribe ya que este tiupo de enemigo no heredara Enemigo para hacer algo




}
