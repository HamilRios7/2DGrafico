package Entidad;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.clamp;


public class Jugador  extends Entidad{
    GamePanel gp;
    KeyHandler keyH;
    int playerWidth = 70;
    public boolean cercaPuerta = false;
    public boolean cercaPelea = false;

    public Jugador(GamePanel gp,KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;


        solidArea = new Rectangle(48,48,gp.tamañoMosaico,gp.tamañoMosaico);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        x1Jugador =0;
        y1Jugador =380;
        x2Jugador=0;
        y2Jugador=300;
        speed=4;
        direction="right";
        System.out.println("Se ejecuta setDefaultValues");

        //JUGADOR ATRIBUTOS

        level =1;
        strenght=1;
        defense=1;
        exp=0;
        nextLevelExp=100;
        coin=0;
        maxLife=3;
        barraVida=maxLife*10;
        life=barraVida;

    }




    public void getPlayerImage(){
        try{
            rgt1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_1.png"));
            rgt2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_2.png"));
            rgt3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_3.png"));
            rgt4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_4.png"));
            rgt5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_5.png"));
            rgt6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_6.png"));
            rgt7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_7.png"));
            rgt8= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingright_8.png"));
            lft1= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_1.png"));
            lft2= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_2.png"));
            lft3= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_3.png"));
            lft4= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_4.png"));
            lft5= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_5.png"));
            lft6= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_6.png"));
            lft7= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_7.png"));
            lft8= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/walkingleft_8.png"));




        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void update1(){
        if(keyH.rightPressed==true || keyH.leftPressed==true){
            if(keyH.rightPressed ==true){
                direction="right";
                x1Jugador = x1Jugador +speed;
            }
            else if(keyH.leftPressed ==true){
                direction="left";
                x1Jugador = x1Jugador - speed;
            }

            System.out.println("ANTES1: " + x1Jugador);
            x1Jugador = clamp(x1Jugador, -24, gp.pantallaAnchura - gp.tamañoMosaico);
            System.out.println("DESPUES1: " + x1Jugador);


            System.out.println("antes"+speed);
            spriteCounter++;
            if(spriteCounter>10){
                if(spriteNum==1){
                    spriteNum=2;
                }
                else if(spriteNum==2){
                    spriteNum=3;
                }else if(spriteNum==3){
                    spriteNum=4;
                }else if(spriteNum==4){
                    spriteNum=5;
                }else if(spriteNum==5){
                    spriteNum=6;
                }else if(spriteNum==6){
                    spriteNum=7;
                }else if(spriteNum==7){
                    spriteNum=8;
                }else if(spriteNum==8){
                    spriteNum=1;
                }
                spriteCounter=0;
            }
        }
    }

    public void draw1(Graphics2D gd2){
        BufferedImage Image= null;
        switch (direction){
            case "right":
                if(spriteNum==1){
                    Image=rgt1;
                }
               if(spriteNum==2){
                   Image=rgt2;
               }
               if (spriteNum==3){
                   Image=rgt3;
               }
                if (spriteNum==4){
                    Image=rgt4;
                }
                if (spriteNum==5){
                    Image=rgt5;
                }
                if (spriteNum==6){
                    Image=rgt6;
                }
                if (spriteNum==7){
                    Image=rgt7;
                }
                if (spriteNum==8){
                    Image=rgt8;
                }

                break;
            case "left":
                if(spriteNum==1){
                    Image=lft1;
                }
                if(spriteNum==2){
                    Image=lft2;
                }
                if (spriteNum==3){
                    Image=lft3;
                }
                if (spriteNum==4){
                    Image=lft4;
                }
                if (spriteNum==5){
                    Image=lft5;
                }
                if (spriteNum==6){
                    Image=lft6;
                }
                if (spriteNum==7){
                    Image=lft7;
                }
                if (spriteNum==8){
                    Image=lft8;
                }
                break;
        }

            gd2.drawImage(Image, x1Jugador, y1Jugador, playerWidth, playerWidth, null);


    }
    public void draw2(Graphics2D gd2){
        BufferedImage Image= null;
        switch (direction){
            case "right":
                if(spriteNum==1){
                    Image=rgt1;
                }
                if(spriteNum==2){
                    Image=rgt2;
                }
                if (spriteNum==3){
                    Image=rgt3;
                }
                if (spriteNum==4){
                    Image=rgt4;
                }
                if (spriteNum==5){
                    Image=rgt5;
                }
                if (spriteNum==6){
                    Image=rgt6;
                }
                if (spriteNum==7){
                    Image=rgt7;
                }
                if (spriteNum==8){
                    Image=rgt8;
                }

                break;
            case "left":
                if(spriteNum==1){
                    Image=lft1;
                }
                if(spriteNum==2){
                    Image=lft2;
                }
                if (spriteNum==3){
                    Image=lft3;
                }
                if (spriteNum==4){
                    Image=lft4;
                }
                if (spriteNum==5){
                    Image=lft5;
                }
                if (spriteNum==6){
                    Image=lft6;
                }
                if (spriteNum==7){
                    Image=lft7;
                }
                if (spriteNum==8){
                    Image=lft8;
                }
                break;
        }



        gd2.drawImage(Image, x2Jugador, y2Jugador, 200, 200, null);

    }
    public void update2(){
        if(keyH.rightPressed==true || keyH.leftPressed==true){
            if(keyH.rightPressed ==true){
                direction="right";
                x2Jugador = x2Jugador +speed;
            }
            else if(keyH.leftPressed ==true){
                direction="left";
                x2Jugador = x2Jugador - speed;
            }


                if(!cercaPelea) {
                    System.out.println("ANTES2: " + x2Jugador);
                    x2Jugador = clamp(x2Jugador, -40, gp.pantallaAnchura - gp.tamañoMosaico);
                    System.out.println("DESPUES2: " + x2Jugador);
                }else if(cercaPelea){
                    x2Jugador = clamp(x2Jugador, -40, 310);
                }


            spriteCounter++;
            if(spriteCounter>10){
                if(spriteNum==1){
                    spriteNum=2;
                }
                else if(spriteNum==2){
                    spriteNum=3;
                }else if(spriteNum==3){
                    spriteNum=4;
                }else if(spriteNum==4){
                    spriteNum=5;
                }else if(spriteNum==5){
                    spriteNum=6;
                }else if(spriteNum==6){
                    spriteNum=7;
                }else if(spriteNum==7){
                    spriteNum=8;
                }else if(spriteNum==8){
                    spriteNum=1;
                }
                spriteCounter=0;
            }
        }
    }

    public void ejecutarAtaque(int comando){

    }
    public Rectangle getBorde1() {
        return new Rectangle(x1Jugador, y1Jugador,playerWidth,playerWidth);
    }

    public Rectangle getBorde2() {
        return new Rectangle(x2Jugador, y2Jugador,playerWidth,playerWidth);
    }

    public void moverPelea(){
        gp.gameState = gp.statePelea;
        spriteNum=1;
        direction="right";
        cercaPuerta = false;
        for(int x=x2Jugador;!keyH.rightPressed;x++){
            keyH.rightPressed=true;
            x2Jugador=x;
            if(keyH.rightPressed ==true){
                direction="right";
                x2Jugador = x2Jugador +speed;
            }
            if(x==354){
                keyH.rightPressed=false;
                direction="right";
                spriteNum=1;
            }
        }
    }
}
