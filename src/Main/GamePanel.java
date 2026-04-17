package Main;

import Entidad.Jugador;
import Entidad.campoPeleaInteraccion;
import Entidad.campoPuerta;
import Fondo.TileManager;
import Objetos.SuperObject;

import javax.swing.JPanel;
import java.awt.*;
import java.sql.SQLOutput;

//extends hace que heredes los metodos y atributos sin obligarte a usarlo , a diferencia implements te obliga a usarlos
public class GamePanel extends JPanel implements Runnable {

    //AJUSTES DE PANTALLLA
    int originalTamañoMosaico=16; //16x16 del mosaico
    int escala=3;
 // para ajustar a la resolucion de pantalla
  public  int tamañoMosaico=originalTamañoMosaico * escala; //48x48 del mosaico
  int maxPantallaCol=23;
  int maxPantallaRow=13;
  public int pantallaAnchura=tamañoMosaico*maxPantallaCol; //1104 pixeles
  public int pantallaAltura=tamañoMosaico*maxPantallaRow; //912 pixeles

    //FPS
   int  fps =60;


   //SISTEMA
    TileManager fondoM = new TileManager(this);
    KeyHandler keyH =new KeyHandler(this);;
    Thread gameThread; //esto hara que el juego este como un bucle , funcionando el codigo sin parar hasta que se cierre el juego
    public ColisionChecker cChecker =new ColisionChecker(this);
    Sonido sound=new Sonido();
    public UI ui= new UI(this,keyH);
    public SuperObject obj[]=new SuperObject[10];
    public UtilityTool uTool=new UtilityTool();



    //ENTIIDAD Y OBJETO
    Jugador jugador=new Jugador(this,keyH);


    //ESTADO DEL JUEGO
    public int gameState;
    public int titleState=0;
    public int escenaState1 =1;
    public int pauseState1 =4;
    public int pauseState2 =5;
    public int pauseState3 =6;
    public int escenaState2=2;
    public int escenaState3=3;
    public int statePelea=10;



    public GamePanel() {
        this.setPreferredSize(new Dimension(pantallaAnchura, pantallaAltura));// este constructor sera el que ajusta el tamaño
                                                                              // de la clase o diriamos pantalla al crear el objeto
        this.setBackground(Color.black);//hacemos que el fondo de la ventana sea blanco
        this.setDoubleBuffered(true); //en verdadero,el dibujar se hara en un buffer pintador que no aparecera en pantalla
        // el double buffered hara que mejore el renderizado de del juego al usar jpanel

        this.addKeyListener(keyH);
        this.setFocusable(true);//esta clase se "concentrara" en recibir la entrada de una tecla
    }

    public void setupGame(){



        gameState = titleState;

    }


    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();//esto llamara directamente a nuestro metodo run
    }

    //este metodo sera el nucleo de nuestro juego, hara como un gameloop
    @Override
    public void run() {

        double drawInterval= 1000000000/fps; //dibujara cada 0,166 segundos , por lo que hara 60 por segundo
        double delta=0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer=0;
        int drawCounter=0;

        //mientras que gameThread exista , repetira lo que este dentro del while
        while(gameThread!=null){

            currentTime = System.nanoTime();//Devuelve el tiempo que actua en nanoseconds

            delta=delta+((currentTime-lastTime)/drawInterval);
            timer=timer+(currentTime-lastTime);
            lastTime=currentTime;

            if(delta >= 1){
                //1 UPDATE : actualizara la posicion del personaje de manera constante
                update();


                //2 DRAWN: DIBUJARA LA PANTALLA CON LA INFORMACION ACTUALIZADA
                repaint();//se llama de esta manera aunque parezca confuso

                delta--;
                drawCounter++;
            }

            if (timer >=1000000000){
                System.out.println("FPS : " + drawCounter);
                drawCounter=0;
                timer=0;
            }
        }
    }


    public void update(){

        if(gameState == escenaState1){
            jugador.update1();
            if (cChecker.checkerCambioEscena(jugador,new campoPuerta(this))) {
                jugador.cercaPuerta = true;

                if (jugador.cercaPuerta && keyH.ePressed) {
                    gameState = escenaState2;
                    jugador.cercaPuerta = false;
                }
            } else {
                jugador.cercaPuerta = false;
            }
        }else if(gameState == escenaState2){
            jugador.update2();
            if(cChecker.checkerEstadoPelea(jugador,new campoPeleaInteraccion(this))){
                jugador.cercaPelea = true;
                if (jugador.cercaPelea && keyH.ePressed) {
                    jugador.cercaPelea = false;
                    jugador.moverPelea();

                }
            }else {
                jugador.cercaPelea = false;
            }
        }else if(gameState == escenaState3){
            jugador.update2();
        }

        if(gameState == statePelea){
                //aqui se haria el update de la pelea
        }
        if(gameState == pauseState1 || gameState == pauseState2 || gameState == pauseState3){
            //nada
        }







    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //cambiamos los graphics g a graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //Titulo Screen
        if(gameState == titleState){
            ui.draw(g2d);
        }
        //escena1
        else if(gameState == escenaState1){
            fondoM.draw1(g2d); //dibuja el fondo pedido
            //Jugador
            jugador.draw1(g2d); // dibuja el jugador
            //UI
            ui.draw(g2d);
            g2d.dispose();
        }else if(gameState == escenaState2 || gameState== statePelea){
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            ui.draw(g2d);
            g2d.dispose();
        }

        if(gameState == pauseState1 ){
            fondoM.draw1(g2d);
            jugador.draw1(g2d);
            ui.draw(g2d);
            g2d.dispose();
        }else if(gameState == pauseState2){
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            ui.draw(g2d);
            g2d.dispose();
        }



    }

    public void playMusic(int i){

        sound.setFile(i);
        sound.play();
        sound.loop();

    }

    public void stopMusic(){

        sound.stop();
    }

    public void playSE(int i){

    }



}
