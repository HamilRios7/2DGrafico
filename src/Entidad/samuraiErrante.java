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
    public boolean isAnimacionMuerteTerminadaEnemigo=false;

    boolean isHabilidadActivada=false;

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

        nombreEnemigo ="Samurai Errante";

        maxLifeEnemigo=4;
        barraVidaEnemigo=maxLifeEnemigo*10;
        lifeEnemigo=barraVidaEnemigo;

        strenght=2;
        fuerzaPorcentaje=0.4;


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
           morir1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_1.png"));
            morir2=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_2.png"));
            morir3=ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/muerte_3.png"));


            //Recibiendo daño animacion




        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void updateSamurai(){
        if(!estoyAtacandoErrante && !heMuertoEnemigo){
            animacionQuieto();
        }
    }
    public void drawSamurai(Graphics2D gd2){

        BufferedImage image = null;

        if(heMuertoEnemigo){
            image = (BufferedImage) dibujarMuerte();
        }
        else if(estoyAtacandoErrante){
            image = (BufferedImage) dibujarAtacar();
        }
        else{
            image = (BufferedImage) dibujarQuieto();
        }

        if(image != null){
            gd2.drawImage(image, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
        }
    }






    public void animacionQuieto(){
        idleCounterEnemigo++;

        if(idleCounterEnemigo > 10){
            idleNumEnemigo++;
            idleCounterEnemigo = 0;
            if(idleNumEnemigo > 10) idleNumEnemigo = 1;
        }
    }

    public void animacionAtacar(){
        atacarCounterEnemigo++;
        if(atacarCounterEnemigo > 5){

            atacarNumEnemigo++;
            atacarCounterEnemigo = 0;
            if(atacarNumEnemigo > 7) {
                atacarNumEnemigo = 1;
                contadorMaxFramesEnemigo++;
            }
        }
    }

    public void animacionMuerte(){

        muerteCounter++;
        if(muerteCounter > 20){
            muerteNum++;
            muerteCounter = 0;
            if(muerteNum > 3) {
                muerteNum = 3;
                contadorMaxFramesEnemigo++;
            }
        }
    }





    public Image dibujarQuieto(){
        BufferedImage Image= null;
        if (idleNumEnemigo == 1) { Image= quieto_1; }
        if (idleNumEnemigo == 2) { Image= quieto_2; }
        if (idleNumEnemigo == 3) { Image= quieto_3; }
        if (idleNumEnemigo == 4) { Image= quieto_4; }
        if (idleNumEnemigo == 5) { Image= quieto_5;}
        if (idleNumEnemigo == 6) { Image= quieto_6; }
        if (idleNumEnemigo == 7) { Image= quieto_7; }
        if (idleNumEnemigo == 8) { Image= quieto_8; }
        if (idleNumEnemigo == 9) { Image= quieto_9; }
        if (idleNumEnemigo == 10) { Image= quieto_10; }
        return Image;
    }

    public Image dibujarMuerte(){
        BufferedImage Image= null;
        if (muerteNum == 1) { Image= morir1; }
        if (muerteNum == 2) { Image= morir2; }
        if (muerteNum == 3) { Image= morir3; }
        return Image;
    }

    public Image dibujarAtacar(){
        BufferedImage Image= null;

        if (atacarNumEnemigo == 1) { Image= ata1; }
        if (atacarNumEnemigo == 2) { Image= ata2; }
        if (atacarNumEnemigo == 3) { Image= ata3; }
        if (atacarNumEnemigo == 4) { Image= ata4; }
        if (atacarNumEnemigo == 5) { Image= ata5;}
        if (atacarNumEnemigo == 6) { Image= ata6; }
        if (atacarNumEnemigo == 7) { Image= ata7; }

        return Image;
    }



    public void actuarSamurai(){
        contadorMaxFramesEnemigo=0;

        //ACTIVA LA HABILIDAD SI BAJA LA VIDA A LA MITAD
        if(!isHabilidadActivada && getLifeEnemigo()<=(getBarraVidaEnemigo()/2)){
            activarHabilidadUnica();
        }

        //SACAMOS EL TIPO DE ATAQUE QUE HARA EL SAMURAI
        Random rand = new Random();
        int numero = rand.nextInt(3) + 1;
        if(numero==1){
            ataqueSeguroSamurai();
        }else if(numero==2){
            ataqueEquilibradoSamurai();
        }else{
            ataqueArriesgadoSamurai();
        }


        estoyAtacandoErrante=true;


    }


    public void ataqueSeguroSamurai(){super.ataqueSeguroSamurai();}

    public void ataqueEquilibradoSamurai(){super.ataqueEquilibradoSamurai();}

    public void ataqueArriesgadoSamurai(){super.ataqueArriesgadoSamurai();}


    public void activarHabilidadUnica(){
        System.out.println("Ha activado su habilidad");
        isHabilidadActivada=true;
        strenght=strenght+2;
    }


    public void contratacar(){
        gp.jugador.haFallado=false;
        int daño=5;
        int vidaRestanteJugador=gp.jugador.getLife()-daño;
        gp.jugador.setLife(vidaRestanteJugador);
    }


    public boolean animacionAtaqueTerminadaErrante(){
        return contadorMaxFramesEnemigo >=1;
    }

    public boolean animacionMuerteTerminadaErrante(){
        if (muerteNum  ==3) {return true;}
        else {return false;}
    }

    public int getBarraVidaEnemigo(){
        return super.getBarraVidaEnemigo();
    }

    public void setBarraVidaEnemigo(int barraVidaEnemigo){
        this.barraVidaEnemigo = barraVidaEnemigo;
    }

    public void setLifeEnemigo(int lifeEnemigo){
        super.setLifeEnemigo(lifeEnemigo);
    }

    public int getLifeEnemigo(){
        return super.getLifeEnemigo();
    }


}
