package Main;

import Objetos.Obj_Vida;
import Objetos.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    KeyHandler keyH;
    Graphics2D g2;

    Font arial_40,arial_80B;

    BufferedImage corazon;

    //BufferedImage keyImage;
    public boolean messageOn=false;
    public String message="";
    int messageCounter=0;
    public boolean gameFinished=false;
    public int comandoNum=0;
    public int titleScreenState=0; // 0: la primera pantalla 1: segunda pantalla
    public int comandoNum1=0;// Índice de la opción seleccionada
    public int subState = 0;   // 0: Selección principal (Inventario), 1: Selección de Ataques

    double playTime;
    DecimalFormat dFormat=new DecimalFormat("0.00");


    public UI (GamePanel gp, KeyHandler keyH){
        this.gp=gp;
        this.keyH=keyH;
        arial_40=new Font("Arial",Font.BOLD,20);
        arial_80B=new Font("Arial",Font.BOLD,80);



        //CREAR HUD DE OBJETOS
        SuperObject vida=new Obj_Vida(gp);
        corazon=vida.imagen;



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
            if(gp.gameState == gp.escenaState1 || gp.gameState == gp.escenaState2 ||  gp.gameState == gp.escenaState3 ){
                drawJugadorVida();
                //Do playstate stuff

            }

            //ESTADO PAUSADO
            if(gp.gameState == gp.pauseState1 ||  gp.gameState == gp.pauseState2 ||  gp.gameState == gp.pauseState3){
                drawJugadorVida();
                drawPauseScreen();
            }


            //SI ESTA CERCA DE PUERTA
            if(gp.jugador.cercaPuerta || gp.jugador.cercaPelea && !keyH.ePressed){
                drawTextoGuia();
            }

            if(gp.gameState == gp.statePelea){
                drawJugadorVida();
                drawCombatMenu();
            }
    }


    public void drawJugadorVida(){
        // Cada punto de vida equivale a 16 píxeles en la barra
        int baseUnit=2;

        //Las posicion de la barra de vida
        int barraX=60;
        int barraY=525;

        //La posicion de la imagen de corazon
        int corazonX=28;
        int corazonY=518;

        //Maxima Vida Dibujar
            // Ancho actual de la vida , lo que le quede si le baja
        double hpBarValue = gp.jugador.life * baseUnit;
            // Ancho total de la barra de vida segun el nivel de vida
        double maxHpBarValue = gp.jugador.barraVida * baseUnit;

        //FONDO_BARRA MAXIMA
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barraX, barraY, (int) maxHpBarValue, 15);

        //VIDA ACTUAL
        g2.setColor(Color.RED);
        g2.fillRect(barraX, barraY, (int) hpBarValue, 15);

        //BORDE DE BARRA
        g2.setColor(Color.WHITE);
        g2.drawRect(barraX, barraY, (int) maxHpBarValue, 15);

        g2.drawImage(corazon,corazonX,corazonY,40,25,null);
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

    public void drawTextoGuia() {
        if (gp.jugador.cercaPuerta) {

            int x = 250;
            int y = 150;
            int ancho = 400;
            int alto = 100;

            // Fondo negro
            g2.setColor(new Color(0, 0, 0, 200)); // transparente
            g2.fillRect(x, y, ancho, alto);

            // Borde blanco
            g2.setColor(Color.white);
            g2.drawRect(x, y, ancho, alto);

            // Texto
            g2.setFont(g2.getFont().deriveFont(20F));
            g2.drawString("Pulsa E para entrar al castillo", x + 20, y + 55);
        }
        if (gp.jugador.cercaPelea) {

            int x = 250;
            int y = 150;
            int ancho = 400;
            int alto = 100;
            // Fondo negro
            g2.setColor(new Color(0, 0, 0, 200)); // transparente
            g2.fillRect(x, y, ancho, alto);

            // Borde blanco
            g2.setColor(Color.white);
            g2.drawRect(x, y, ancho, alto);

            // Texto
            g2.setFont(g2.getFont().deriveFont(20F));
            g2.drawString("Pulsa E para empezar la lucha", x + 20, y + 55);
        }
    }
    public void drawCombatMenu() {
        // Dibujamos el marco principal de fondo
        int x = 220;
        int y = 502;
        int width = 800;
        int height = 115;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height); // Tu método para hacer rectángulos
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);

        if (subState == 0) {
            // --- MENÚ PRINCIPAL DEL COMBATE ---
            g2.drawString("ATACAR", x + 60, y + 40);
            if (comandoNum1 == 0) g2.drawString(">", x + 30, y + 40);

            g2.drawString("INVENTARIO", x + 60, y + 90);
            if (comandoNum1 == 1) g2.drawString(">", x + 30, y + 90);

        } else if (subState == 1) {
            // --- MENÚ DE TIPOS DE ATAQUE ---
            g2.drawString("DEBIL", x + 50, y + 40);
            if (comandoNum1 == 0) g2.drawString(">", x + 30, y + 40);

            g2.drawString("EQUILIBRADO", x + 50, y + 80);
            if (comandoNum1 == 1) g2.drawString(">", x + 30, y + 80);

            g2.drawString("FUERTE", x + 50, y + 120);
            if (comandoNum1 == 2) g2.drawString(">", x + 30, y + 120);

            g2.setFont(g2.getFont().deriveFont(15F));
            g2.drawString("Presiona ESC para volver", x + 250, y + 130);
        }
    }
    public void abrirInventario() {

    }
}
