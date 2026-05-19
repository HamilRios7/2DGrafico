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

    // ── Inicialización ────────────────────────────────────────────────────────

    public void setDefaultValuesEnemigo() {
        enemyWidht = 500;
        x1Enemigo = 588;
        y1Enemigo = 46;

        nombreEnemigo = "Gigante";
        maxLifeEnemigo = 9;
        barraVidaEnemigo = maxLifeEnemigo * 10;
        lifeEnemigo = barraVidaEnemigo;

        strenght = 5;
        fuerzaPorcentaje = 0.4;
    }

    public void getEnemyImage() {
        try {
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
        if (atacarCounterEnemigo > 6) {
            atacarNumEnemigo++;
            atacarCounterEnemigo = 0;
            if(atacarNumEnemigo==10){
                gp.playSE(11);
            }
            if (atacarNumEnemigo > 14) {
                atacarNumEnemigo = 1;
                contadorMaxFramesEnemigo++;
            }
        }
    }

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
                contadorMaxFramesEnemigo++;
            }
        }
    }



    @Override
    public void activarHabilidadUnica() {
        isHabilidadActivada = true;
        strenght = strenght + 3;
        seHaMostradoPantalla = true;
        gp.isSituacionPelea = false;
    }



    @Override
    public boolean animacionAtaqueTerminada() {
        return contadorMaxFramesEnemigo >= 1;
    }

    @Override
    public boolean animacionMuerteTerminada() {
        return muerteNum == 16;
    }

    // contratacar() NO se sobreescribe → hereda el no-op de Enemigo (Gigante no contrataca)

    // ── Animación idle ────────────────────────────────────────────────────────

    public void animacionQuieto() {
        idleCounterEnemigo++;
        if (idleCounterEnemigo > 10) {
            idleNumEnemigo++;
            idleCounterEnemigo = 0;
            if (idleNumEnemigo > 6) idleNumEnemigo = 1;
        }
    }



    // ── Dibujo de frames ──────────────────────────────────────────────────────

    public Image dibujarQuieto() {
        switch (idleNumEnemigo) {
            case 1: return quieto_1Gig;
            case 2: return quieto_2Gig;
            case 3: return quieto_3Gig;
            case 4: return quieto_4Gig;
            case 5: return quieto_5Gig;
            case 6: return quieto_6Gig;
            default: return null;
        }
    }

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

    public Image dibujarAtacar() {
        switch (atacarNumEnemigo) {
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

    // ── Tipos de ataque ───────────────────────────────────────────────────────

    @Override
    public void actuar() {
        contadorMaxFramesEnemigo = 0;
        fueEnemigoAtaque = true;
        resetStun();

        int opcion = (int)(Math.random() * 3);
        switch (opcion) {
            case 0: ataqueSeguro();      break;
            case 1: ataqueEquilibrado(); break;
            case 2: ataqueArriesgado();  break;
        }
        if(!haFalladoEnemigo) {
            activarStun();
        }
    }

    public void ataqueSeguro()      { ejecutarAtaqueEnemigo(gp.jugador, 6,  79); }
    public void ataqueEquilibrado() { ejecutarAtaqueEnemigo(gp.jugador, 10, 55); }
    public void ataqueArriesgado()  { ejecutarAtaqueEnemigo(gp.jugador, 14, 34); }

    @Override
    public boolean fueStuneado() {
        return fueStun;
    }

    @Override
    public void resetStun() {
        fueStun = false;
    }

    public void activarStun(){
        int rand = new java.util.Random().nextInt(100);
        if (rand < 25) {
            System.out.println("me active");
            fueStun=true;
        }
    }
}
    //posible boss
    //public Gigante(){
      //  super("Gigante",120,50,0.5);
   // }
    //golpe medio.
    //public void golpeBajo(Heroe heroe){
        //ejecutarAtaque(heroe,"gole bajo",1.2,0.2);//65% de acierto, 20 de daño
    //}
    //golpe fuerte
    //public void pisoton(Heroe heroe){
        //ejecutarAtaque(heroe,"pisotón",0.7,0.6);//35% de acierto, 30 de daño
   // }
    //golpe débil
   // public void empujon(Heroe heroe){
        //ejecutarAtaque(heroe,"empujón",1.7,0.1);//85% de acertar, 10 de daño
   // }
    //método para que ejecute una ataque al azar.
    //public void ataqueAleatorio(Heroe heroe){//ataque aleatorio
        //int opcion=(int) (Math.random()*3);// opciones 0,1,2.
       // switch (opcion){
           // case 0:
             //   golpeBajo(heroe);
             //   break;
           // case 1:
             //   pisoton(heroe);
             //   break;
          //  case 2:
            //    empujon(heroe);
            //    break;
      //  }
    //}

   // public void mostrarDatos(){
        //super.mostrarDatos();



