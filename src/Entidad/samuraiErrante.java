package Entidad;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class samuraiErrante extends Enemigo{

    public boolean estoyAtacandoErrante=false;
    public boolean enemigoYaAtaco = false;
    public boolean heMuertoEnemigo=false;



    public samuraiErrante(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle(48,48,gp.tamañoMosaico,gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }


    public void setDefaultValuesEnemigo(){

        //AJUSTES NORMALES
        enemyWidht=340;
        x1Enemigo=710;
        y1Enemigo=120;


        //JUGADOR ATRIBUTOS

        nombreEnemigo ="Berserker  Carmesi";
        maxLifeEnemigo=4;
        barraVidaEnemigo=maxLifeEnemigo*10;
        lifeEnemigo=barraVidaEnemigo;
        strenght=2;
        fuerzaPorcentaje=0.4;
        precision=1;
        precisionPorcentaje=0.3;

    }

    public void getEnemyImage(){
        try{

            // Quieto animacion
            quieto_1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_1.png"));
            quieto_2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_2.png"));
            quieto_3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_3.png"));
            quieto_4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_4.png"));
            quieto_5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_5.png"));
            quieto_6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_6.png"));
            quieto_7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_7.png"));
            quieto_8= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_8.png"));
            quieto_9= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_9.png"));
            quieto_10= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_10.png"));


            //Atacando animacion
            ata1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_1S.png"));
            ata2=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_2S.png"));
            ata3=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_3S.png"));
            ata4=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_4S.png"));
            ata5=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_5S.png"));
            ata6=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_6S.png"));
            ata7=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/ataque_7S.png"));



            //Muriendo animacion




            //Recibiendo daño animacion




        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void updateSamurai(){
        if(!estoyAtacandoErrante){
            animacionQuieto();
        }
    }
    public void drawSamurai(Graphics2D gd2){
        BufferedImage Image=null;
        if(estoyAtacandoErrante ){
            Image=(BufferedImage) dibujarAtacar();
        }else if(!estoyAtacandoErrante && !heMuertoEnemigo){
            Image=(BufferedImage) dibujarQuieto();
        }else if(heMuertoEnemigo){
            dibujarMuerte();
        }


        gd2.drawImage(Image, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
    }



    public void animacionQuieto(){
        idleCounter++;

        if(idleCounter > 10){
            idleNum++;
            idleCounter = 0;
            if(idleNum > 10) idleNum= 1;
        }
    }

    public void animacionAtacar(){
        atacarCounter++;
        if(atacarCounter > 3){
            System.out.println("contador atacar: "+atacarCounter);
            System.out.println("contador contadorMaxFramesEnemigo: "+contadorMaxFramesEnemigo);
            atacarNum++;
            atacarCounter = 0;
            if(atacarNum > 7) {
                atacarNum = 1;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    public void animacionMuerte(){

    }

    public void animacionRecibirDaño(){

    }



    public Image dibujarQuieto(){
        BufferedImage Image= null;
        if (idleNum == 1) { Image= quieto_1; }
        if (idleNum == 2) { Image= quieto_2; }
        if (idleNum == 3) { Image= quieto_3; }
        if (idleNum == 4) { Image= quieto_4; }
        if (idleNum == 5) { Image= quieto_5;}
        if (idleNum == 6) { Image= quieto_6; }
        if (idleNum == 7) { Image= quieto_7; }
        if (idleNum == 8) { Image= quieto_8; }
        if (idleNum == 9) { Image= quieto_9; }
        if (idleNum == 10) { Image= quieto_10; }
        return Image;
    }

    public Image dibujarMuerte(){
        BufferedImage Image= null;
        return Image;
    }

    public Image dibujarAtacar(){
        BufferedImage Image= null;

        if (atacarNum == 1) { Image= ata1; }
        if (atacarNum == 2) { Image= ata2; }
        if (atacarNum == 3) { Image= ata3; }
        if (atacarNum == 4) { Image= ata4; }
        if (atacarNum == 5) { Image= ata5;}
        if (atacarNum == 6) { Image= ata6; }
        if (atacarNum == 7) { Image= ata7; }

        return Image;
    }

    public Image dibujarRecibirDaño(){
        BufferedImage Image= null;
        return Image;
    }


    public void actuarSamurai(){
        contadorMaxFramesEnemigo=0;
        ataqueEquilibradoSamurai();
        estoyAtacandoErrante=true;
    }
    public void ataqueEquilibradoSamurai(){
        int ataque=20;
        int porcentaje=10;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));
        int aciertoFinal = (int) ((porcentaje + gp.jugador.precision * gp.jugador.precisionPorcentaje) * 100);
        Random rand = new Random();


        boolean acierta = rand.nextInt(100) < aciertoFinal;


        if(acierta){
            int jugadorVidaRestante=gp.jugador.getLife()-dañoFinal;
            if(jugadorVidaRestante<0){
                jugadorVidaRestante=0;
            }
            gp.jugador.setLife(jugadorVidaRestante);
            gp.ui.drawInformacionBatalla();

        }else if(!acierta){
            gp.ui.drawInformacionBatalla();
        }
    }

    public boolean animacionAtaqueTerminadaErrante(){
        return contadorMaxFramesEnemigo >=1;
    }

    public boolean animacionMuerteTerminada(){

        if (muerteNum   ==5) {return true;}
        else {return false;}
    }


    public void setLifeEnemigo(int lifeEnemigo){
        this.lifeEnemigo = lifeEnemigo;
    }

    public int getLifeEnemigo(){
        return lifeEnemigo;
    }
}
