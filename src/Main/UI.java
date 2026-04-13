package Main;

import java.awt.*;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;

    Graphics2D g2;

    Font arial_40,arial_80B;

    //BufferedImage keyImage;
    public boolean messageOn=false;
    public String message="";
    int messageCounter=0;
    public boolean gameFinished=false;
    public int comandoNum=0;
    public int titleScreenState=0; // 0: la primera pantalla 1: segunda pantalla


    double playTime;
    DecimalFormat dFormat=new DecimalFormat("0.00");


    public UI (GamePanel gp){
        this.gp=gp;

        arial_40=new Font("Arial",Font.BOLD,20);
        arial_80B=new Font("Arial",Font.BOLD,80);

    }


    public void showMessage(String text){

        message=text;
        messageOn=true;

    }

    public void draw(Graphics2D g2){
            this.g2=g2;

            g2.setFont(arial_40);
            g2.setColor(Color.white);

            //TITULO JUGANDO
            if(gp.gameState==gp.titleState){
                drawTitleScreen();
            }
            //ESTADO JUGANDO
            if(gp.gameState == gp.playState){
                //Do playstate stuff

            }
            //ESTADO PAUSADO
            if(gp.gameState == gp.pauseState){
                drawPauseScreen();

            }
    }

    public void drawPauseScreen(){
        String text="PAUSADO";
        int x=getXforCenteredText(text);
        int y=gp.pantallaAltura/2;

        g2.drawString("PAUSADO",x,y);
    }

    public void drawTitleScreen(){

        if(titleScreenState==0){
            g2.setColor(new Color(7 ,10,18));
            g2.fillRect(0,0,gp.pantallaAnchura,gp.pantallaAltura);

            //TITILO NOMBRE
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
            String text = "Stairgrave";
            int x=getXforCenteredText(text);
            int y=gp.tamañoMosaico*3;

            //SOMBRAS
            g2.setColor(Color.black);
            g2.drawString(text,x+5,y+5);

            //MAIN COLOR
            g2.setColor(new Color(209,0,28));
            g2.drawString(text,x,y);


            //GUERRERO IMAGEN
            x = (gp.pantallaAnchura/2)-90;
            y = y+gp.tamañoMosaico*2;
            g2.drawImage(gp.jugador.rgt1,x,y,gp.tamañoMosaico*4,gp.tamañoMosaico*4,null);


            //MENU

            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
            text= "NUEVA PARTIDA";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico*5;
            g2.drawString(text,x,y);
            if(comandoNum==0){
                g2.drawString(">",x-gp.tamañoMosaico,y);
            }


            text= "CARGAR PARTIDA";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);
            if(comandoNum==1){
                g2.drawString(">",x-gp.tamañoMosaico,y);
            }

            text= "SALIR";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);
            if(comandoNum==2){
                g2.drawString(">",x-gp.tamañoMosaico,y);
            }


        }else if(titleScreenState==1){
            //TEXTO HISTORIA
            g2.setColor(new Color(209,0,28));
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20F));

            String text ="Nadie sabe de dónde salió el Castillo." ;
            int x=getXforCenteredText(text);
            int y=gp.tamañoMosaico*3;
            g2.drawString(text,x,y);

            text = "Nadie llega a la cima.";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);

            text ="No porque no puedan, sino porque el Castillo no lo permite.";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);


            text = "Y aun así, tú sigues subiendo.";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);


            text = "Desgarra el camino. O conviértete en él.";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico;
            g2.drawString(text,x,y);


            text= "DESGARRAR";
            x=getXforCenteredText(text);
            y=y+gp.tamañoMosaico*2;
            g2.drawString(text,x,y);
            if(comandoNum==0){
                g2.drawString(">",x-gp.tamañoMosaico,y);
            }
        }



    }

    public int getXforCenteredText(String text){
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x=gp.pantallaAnchura/2-lenght/2;
        return x;
    }



}
