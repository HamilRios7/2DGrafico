package Entidad;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.clamp;


public class Jugador  extends Entidad{
    GamePanel gp;
    KeyHandler keyH;
    int playerWidth = 130;

    //Comprobar cambios de escenas
    public boolean cercaPuerta = false;
    public boolean cercaPelea = false;
    public boolean cercaIrPiso3=false;

    //Comprobar comienzos y finalizaciones  de animacion y muerte
    public boolean estoyAtacando=false;
    public boolean heMuerto=false;
    public boolean isAnimacionMuerteTerminada=false;



    public Jugador(GamePanel gp,KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;


        solidArea = new Rectangle(48,48,gp.tamañoMosaico,gp.tamañoMosaico);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        x1Jugador =0;
        y1Jugador =305;
        x2Jugador=0;
        y2Jugador=60;
        speed=5;
        direction="right";


        //JUGADOR ATRIBUTOS

        level =1;
        strenght=3;
        fuerzaPorcentaje=0.4;
        precision=1;
        precisionPorcentaje=0.3;
        exp=0;
        nextLevelExp=100;
        oro=0;
        maxLife=3;
        barraVida=maxLife*10;
        life=barraVida;

    }




    public void getPlayerImage(){
        try{

            //Animaciones Caminar
            //Animacion Caminar Derecha
            rgt1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_1.png"));
            rgt2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_2.png"));
            rgt3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_3.png"));
            rgt4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_4.png"));
            rgt5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_5.png"));
            rgt6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_6.png"));
            rgt7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_7.png"));
            rgt8= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_8.png"));

            //Animacion Caminar Izquierda
            lft1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_1.png"));
            lft2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_2.png"));
            lft3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_3.png"));
            lft4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_4.png"));
            lft5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_5.png"));
            lft6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_6.png"));
            lft7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_7.png"));
            lft8= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_8.png"));

            //Animacion Ataques
            ataJuga1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_1.png"));
            ataJuga2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_2.png"));
            ataJuga3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_3.png"));
            ataJuga4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_4.png"));
            ataJuga5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_5.png"));
            ataJuga6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_6.png"));


            //Animacion Quieto
            quieto1JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1Jg.png"));
            quieto2JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2Jg.png"));
            quieto3JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3Jg.png"));
            quieto4JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4Jg.png"));
            quieto5JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5Jg.png"));

            quieto1JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1JgLeft.png"));
            quieto2JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2JgLeft.png"));
            quieto3JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3JgLeft.png"));
            quieto4JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4JgLeft.png"));
            quieto5JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5JgLeft.png"));

            //Animacion Muerto

            muerto_1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_1.png"));
            muerto_2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_2.png"));
            muerto_3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_3.png"));
            muerto_4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_4.png"));
            muerto_5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_5.png"));
            muerto_6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_6.png"));
            muerto_7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_7.png"));





        }catch(IOException e){
            e.printStackTrace();
        }

    }

    //ESCENA 1
    public void update1(){
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        if(gp.gameState==gp.escenaState1){
            if(moviendo) {

                if (keyH.rightPressed) {
                    direction = "right";
                    x1Jugador += speed;
                }

                if (keyH.leftPressed) {
                    direction = "left";
                    x1Jugador -= speed;
                }

                x1Jugador = clamp(x1Jugador, -54, gp.pantallaAnchura - 70);

                animacionMoviendome();
            }else if (!moviendo) {
                animacionQuieto();
            }
        }
    }


    //ESCENA 1
    public void draw1(Graphics2D gd2){
        BufferedImage Image= null;

        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        if (moviendo) {
            Image = (BufferedImage) dibujarMoviendome();
        } else if (!moviendo) {
            Image = (BufferedImage) dibujarQuieto();
        }


        gd2.drawImage(Image, x1Jugador, y1Jugador, playerWidth, playerWidth, null);
    }

    //ESCENA 2
    public void draw2(Graphics2D gd2){
        BufferedImage Image= null;

        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        if(gp.gameState==gp.escenaState2) {
            if (moviendo) {
                Image = (BufferedImage) dibujarMoviendome();
            }else if (!moviendo) {
                Image = (BufferedImage) dibujarQuieto();
            }
        }else if (gp.gameState==gp.statePelea && estoyAtacando) {
            Image= (BufferedImage) dibujarAtaque();
        }else if( gp.gameState==gp.statePelea  && !heMuerto){
            Image = (BufferedImage) dibujarQuieto();
        }else if ( heMuerto ) {
            Image = (BufferedImage) dibujarMuerte();
        }
        gd2.drawImage(Image, x2Jugador, y2Jugador, 400, 400, null);

    }



    // ESCENA 2
    public void update2(){
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        if(heMuerto){
            animacionMuerte();
            return;
        }

        if(gp.gameState==gp.escenaState2) {
            if (moviendo) {
                if (keyH.rightPressed == true) {
                    direction = "right";
                    x2Jugador = x2Jugador + 6;
                } else if (keyH.leftPressed == true) {
                    direction = "left";
                    x2Jugador = x2Jugador - 6;
                }

                System.out.println("x: " + x2Jugador);
                System.out.println("y: " + y2Jugador);
                x2Jugador = clamp(x2Jugador, -170, gp.pantallaAnchura -195);


                animacionMoviendome();
            } else if(!moviendo ) {
                animacionQuieto();
            }
        }else if( gp.gameState==gp.statePelea && !heMuerto ) {
            animacionQuieto();
        }
    }

    public void animacionMoviendome(){
        moveCounter++;
        if(moveCounter > 10){
            moveNum++;
            moveCounter = 0;
            if(moveNum > 8) moveNum = 1;

        }
    }



    public void animacionQuieto(){

        idleCounter++;

        if(idleCounter > 10){
            idleNum++;
            idleCounter = 0;
            if(idleNum > 5) idleNum= 1;

        }
    }

    public void animacionAtaque(){
        atacarCounter++;
        if(atacarCounter > 6){
            atacarNum++;
            atacarCounter = 0;
            if(atacarNum > 6) {
                atacarNum = 1;
                contadorMaxFrames++;
            }
        }
    }

    public void animacionMuerte(){
        muerteCounter++;
        if(muerteCounter > 14){
            muerteNum++;
            muerteCounter = 0;
            if(muerteNum > 7) {
                muerteNum = 7;
            }
        }
    }

    public Image dibujarQuieto(){
        BufferedImage Image = null;
        if(direction.equals("right")){
            if(idleNum==1) Image=quieto1JugaRight;
            if(idleNum==2) Image=quieto2JugaRight;
            if(idleNum==3) Image=quieto3JugaRight;
            if(idleNum==4) Image=quieto4JugaRight;
            if(idleNum==5) Image=quieto5JugaRight;
        }

        if(direction.equals("left")){
            if(idleNum==1) Image=quieto1JugaLeft;   // 👈 tus sprites nuevos
            if(idleNum==2) Image=quieto2JugaLeft;
            if(idleNum==3) Image=quieto3JugaLeft;
            if(idleNum==4) Image=quieto4JugaLeft;
            if(idleNum==5) Image=quieto5JugaLeft;
        }
        return Image;
    }

    public Image dibujarMoviendome(){
        BufferedImage Image=null;
        switch (direction) {
            case "right":
                if (moveNum == 1) Image = rgt1;
                if (moveNum == 2) Image = rgt2;
                if (moveNum == 3) Image = rgt3;
                if (moveNum == 4) Image = rgt4;
                if (moveNum == 5) Image = rgt5;
                if (moveNum == 6) Image = rgt6;
                if (moveNum == 7) Image = rgt7;
                if (moveNum == 8) Image = rgt8;
                break;

            case "left":
                if (moveNum == 1) Image = lft1;
                if (moveNum == 2) Image = lft2;
                if (moveNum == 3) Image = lft3;
                if (moveNum == 4) Image = lft4;
                if (moveNum == 5) Image = lft5;
                if (moveNum == 6) Image = lft6;
                if (moveNum == 7) Image = lft7;
                if (moveNum == 8) Image = lft8;
                break;
        }
        return Image;
    }

    public Image dibujarAtaque(){
        BufferedImage Image=null;
        if (atacarNum == 1) Image = ataJuga1;
        if (atacarNum == 2) Image = ataJuga2;
        if (atacarNum == 3) Image = ataJuga3;
        if (atacarNum == 4) Image = ataJuga4;
        if (atacarNum == 5) Image = ataJuga5;
        if (atacarNum == 6) Image =ataJuga6;



        return Image;
    }

    public Image dibujarMuerte(){
        BufferedImage Image=null;
        if(muerteNum== 1) Image = muerto_1;
        if (muerteNum == 2) Image = muerto_2;
        if (muerteNum == 3) Image = muerto_3;
        if(muerteNum== 4) Image = muerto_4;
        if (muerteNum == 5) Image = muerto_5;
        if (muerteNum == 6) Image = muerto_6;
        if (muerteNum == 7) Image = muerto_7;
        return Image;
    }

    //CONSEGUIR LA ZONA DE INTERACCION DEL JUGADOR EN LA ESCENA 1



    //LO MUEVE A LA ESCENA DE PELEA EN LA POSICION QUE QUEREMOS
    public void moverPelea(){
        gp.gameState = gp.statePelea;
        moveNum =1;
        direction="right";
        cercaPuerta = false;
        x2Jugador=130;
    }

    //ELECCION CON EL KEYHANDLER DE ATAQUE Y LOS TIPOS DE ATAQUE
    public void ejecutarAtaque(int comando){

        if(!estoyAtacando){

            contadorMaxFrames = 0;

            estoyAtacando = true;

            if(comando==0) ataqueSeguro();
            if(comando==1) ataqueEquilibrado();
            if(comando==2) ataqueArriesgado();
        }
    }



    public void ataqueSeguro(){

        int ataque=10;
        double porcentaje=0.40;
        int dañoFinal=(int)(ataque+(gp.jugador.strenght*gp.jugador.fuerzaPorcentaje));
        int aciertoFinal=(int)((porcentaje+gp.jugador.precision*gp.jugador.precisionPorcentaje)*100);
        Random rand = new Random();

        boolean acierta = rand.nextInt(100) < aciertoFinal;

        if (acierta) {
            int enemigoVidaRestante=gp.samuraiErrante.getLifeEnemigo()-dañoFinal;
            gp.samuraiErrante.setLifeEnemigo(enemigoVidaRestante); // acierto
        } else {
            gp.ui.drawInformacionBatalla(); //fallo
        }


    }
    public void ataqueEquilibrado() {
        int ataque = 10;
        double porcentaje = 0.40;
        int dañoFinal = (int) (ataque + (gp.jugador.strenght * gp.jugador.fuerzaPorcentaje));
        int aciertoFinal = (int) ((porcentaje + gp.jugador.precision * gp.jugador.precisionPorcentaje) * 100);
        Random rand = new Random();


        boolean acierta = rand.nextInt(100) < aciertoFinal;


        if (acierta) {
            int enemigoVidaRestante = gp.samuraiErrante.getLifeEnemigo() - dañoFinal;
            gp.samuraiErrante.setLifeEnemigo(enemigoVidaRestante); // acierto
        } else {
            gp.ui.drawInformacionBatalla(); //fallo
        }

    }

    public void ataqueArriesgado() {
        int ataque = 10;
        double porcentaje = 0;
        int dañoFinal = (int) (ataque + (gp.jugador.strenght * gp.jugador.fuerzaPorcentaje));
        int aciertoFinal = (int) ((porcentaje + gp.jugador.precision * gp.jugador.precisionPorcentaje) * 100);
        Random rand = new Random();


        boolean acierta = rand.nextInt(100) < aciertoFinal;


        if (acierta) {
            int enemigoVidaRestante = gp.samuraiErrante.getLifeEnemigo() - dañoFinal;
            gp.samuraiErrante.setLifeEnemigo(enemigoVidaRestante); // acierto
        } else {
            gp.ui.drawInformacionBatalla(); //fallo
        }

    }

    public boolean animacionAtaqueTerminada(){

        if (contadorMaxFrames>=1) {return true;}
        else {return false;}
    }

    public boolean animacionMuerteTerminada(){

        if (muerteNum   ==7) {return true;}
        else {return false;}
    }

    public void obtenerOro(int oroDrop){//se le suma el oro que ha dropeado el enemigo
        oro=oro+oroDrop;
    }

    public Rectangle getBorde1() {
        return new Rectangle(x1Jugador, y1Jugador,75,1);
    }

    //CONSEGUIR LA ZONA DE INTERACCION DEL JUGADOR EN LA ESCENA 2
    public Rectangle getBorde2() {
        return new Rectangle(x2Jugador, y2Jugador,64,1);
    }

    public void setLife(int life){
        this.life =life;
    }

    public int getLife(){
        return life;
    }


    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }
}
