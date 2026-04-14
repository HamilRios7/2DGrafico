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

    public Jugador(GamePanel gp,KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;


        solidArea = new Rectangle(48,48,gp.tamañoMosaico,gp.tamañoMosaico);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        x=0;
        y=380;
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


    public void update(){
        if(keyH.rightPressed==true || keyH.leftPressed==true){
            if(keyH.rightPressed ==true){
                direction="right";
                x=x +speed;
            }
            else if(keyH.leftPressed ==true){
                direction="left";
                x=x - speed;
            }

            System.out.println("ANTES: " + x);
            x = clamp(x, -24, gp.pantallaAnchura -gp.tamañoMosaico);
            System.out.println("DESPUES: " + x);

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

    public void draw(Graphics2D gd2){
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
        gd2.drawImage(Image,x,y,playerWidth,playerWidth,null);
    }

    public Rectangle getBorde() {
        return new Rectangle(x,y,playerWidth,playerWidth);
    }
}
