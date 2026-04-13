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

            if(gp.gameState == gp.playState){
                //Do playstate stuff

            }
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

    public int getXforCenteredText(String text){
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x=gp.pantallaAnchura/2-lenght/2;
        return x;
    }
}
