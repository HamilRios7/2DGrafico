package Main;

import Entidad.Jugador;
import Fondo.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyListener;

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
    public UI ui= new UI(this);
    public AccionesJugador ac=new AccionesJugador(this);


    //ENTIIDAD Y OBJETO
    Jugador jugador=new Jugador(this,keyH);


    //ESTADO DEL JUEGO
    public int gameState;
    public int playState=1;
    public int pauseState=2;



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


        playMusic(0);

        gameState = playState;

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

        if(gameState == playState){
            jugador.update();
        }
        if(gameState == pauseState){
            //nada
        }



//esto actualizara la posicion del personaje

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //cambiamos los graphics g a graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //extiende la clase grafica para dar un control mas sofisticado en la geometria, coordinacion,
        // etc.
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);


        //Fondo
        fondoM.draw(g2d); //dibuja el fondo pedido
        //Jugador
        jugador.draw(g2d); // dibuja el jugador
        //UI
        ui.draw(g2d);
        g2d.dispose(); //sirve como para borrar y tirar lo viejo antes de seguir con algo nuevo
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

    public boolean ejecutarAccion(int opcion){
    boolean turnOn=ac.ejecutarAccion(opcion);

    return turnOn;
    }


    public boolean abrirInventario(){
       boolean turnOn=ac.abrirInventario();
       return turnOn;
    }

}
