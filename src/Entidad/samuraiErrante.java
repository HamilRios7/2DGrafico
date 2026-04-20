package Entidad;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class samuraiErrante extends Enemigo{

    long ultimoCambio = 0;
    int spriteActual = 0; // 0 = sprite1, 1 = sprite2
    long intervalo = 300; // milisegundos (0.3   segundos)
    public samuraiErrante(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle(48,48,gp.tamañoMosaico,gp.tamañoMosaico);
        setDefaultValuesEnemigo();
        getEnemyImage();
    }


    public void setDefaultValuesEnemigo(){

        //AJUSTES NORMALES
        enemyWidht=240;
        x1Enemigo=800;
        y1Enemigo=260;


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
            quieto_1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_1.png"));
            quieto_2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("samuraierrante/quieto_2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void updateSamurai(){
        long now = System.currentTimeMillis();

        if (now - ultimoCambio >= intervalo) {
            if (spriteActual == 0) {
                spriteActual = 1;
            } else {
               spriteActual = 0;
            }
            ultimoCambio= now;
        }
    }
    public void drawSamurai(Graphics2D gd2){
        if (spriteActual == 0) {
            gd2.drawImage(quieto_1, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
        } else {
            gd2.drawImage(quieto_2, x1Enemigo, y1Enemigo, enemyWidht, enemyWidht, null);
        }
    }


    public void actuarSamurai(){
        if(gp.jugadorTurno==false){
            ataqueEquilibradoSamurai();
            gp.jugadorTurno=true;
        }
    }
    public void ataqueEquilibradoSamurai(){
        int ataque=15;
        int porcentaje=10;
        int dañoFinal=(int)(ataque+(strenght*fuerzaPorcentaje));
        int aciertoFinal=(int)(porcentaje+((precision*precisionPorcentaje)*100));
        Random rand = new Random();

        int value;

        if (rand.nextInt(100) < aciertoFinal) {
            value = 1; // acierto
        } else {
            value = 0; //fallo
        }

        if(value==1){
            int jugadorVidaRestante=gp.jugador.getBarraVida()-dañoFinal;
            gp.jugador.setBarraVida(jugadorVidaRestante);
            gp.ui.drawInformacionBatalla();
            if(gp.jugador.getBarraVida()<=0){
                gp.ui.drawInformacionBatalla();
                gp.gameState=gp.escenaState2;
            }

        }else if(value==0){
            gp.ui.drawInformacionBatalla();
        }

    }

    public void setBarraVidaEnemigo(int barraVidaEnemigo){
        this.barraVidaEnemigo = barraVidaEnemigo;
    }

    public int getBarraVidaEnemigo(){
        return barraVidaEnemigo;
    }
}