package Main;
import Main.UI;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//el que recibe las eventos en el teclado
public class KeyHandler implements KeyListener {
    public boolean leftPressed,rightPressed;
    GamePanel gp;
    public boolean iniciarTexto;
    public boolean ePressed;
    ;

    public KeyHandler(GamePanel gp){
        this.gp=gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();//devuelve el numero de tecla que fue presionada

        //ESTADO JUGANDO
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }

        //ESTADO TITULO

        if(gp.gameState==gp.titleState){

            if(gp.ui.titleScreenState==0){

                if(code == KeyEvent.VK_W){
                    gp.ui.comandoNum--;
                    if(gp.ui.comandoNum<0) {
                        gp.ui.comandoNum=2;
                    }
                }
                if(code == KeyEvent.VK_S){
                    gp.ui.comandoNum++;
                    if(gp.ui.comandoNum>2) {
                        gp.ui.comandoNum=0;
                    }
                }
                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.comandoNum==0){
                        gp.ui.titleScreenState=1;
                    }
                    if(gp.ui.comandoNum==1){
                        //cargar partida luego
                    }
                    if(gp.ui.comandoNum==2){
                        System.exit(0);
                    }
                }
            } else if(gp.ui.titleScreenState==1){

                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.comandoNum==0){
                        gp.gameState=gp.escenaState1;
                        gp.playMusic(0);
                    }
                }
            }else if(gp.ui.titleScreenState==2){
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.comandoNum == 0) {
                        System.exit(0);
                        //gp.playMusic(0);
                    }
                }
            }
        }
        //ESTADO PELEA
        if (gp.gameState == gp.statePelea) {
            if (code == KeyEvent.VK_ENTER) {
                if(gp.jugadorTurno) {
                    if (gp.ui.subState == 0) {
                        // Lógica Menú Principal
                        if (gp.ui.comandoNum1 == 0) {
                            gp.ui.subState = 1; // Vamos a elegir ataque
                        }
                        if (gp.ui.comandoNum1 == 1) {
                            gp.ui.abrirInventario();
                        }
                        gp.ui.comandoNum1 = 0; // Reset del cursor para el siguiente menú
                    } else if (gp.ui.subState == 1) {
                        // Lógica de Ejecución de Ataque
                        if(!gp.jugador.estoyAtacando){ //Una proteccion en caso de que haya una animacion en el medio

                            gp.jugador.ejecutarAtaque(gp.ui.comandoNum1);
                            gp.ui.subState = 0;

                        }
                    }
                }
            }
            //Ir hacia atras si estas en el submenu de ataques
            if (code == KeyEvent.VK_ESCAPE && gp.ui.subState == 1) {
                gp.ui.subState = 0; // Volver atrás
            }

            // Movimiento arriba y abajo
            int maxCommand;
            if (gp.ui.subState == 0) {
                maxCommand = 1;
            } else {
                maxCommand = 2;
            }
            if (code == KeyEvent.VK_W && gp.ui.comandoNum1 > 0){ gp.ui.comandoNum1--;}
            if (code == KeyEvent.VK_S && gp.ui.comandoNum1 < maxCommand) {gp.ui.comandoNum1++;}
        }

        //ESTADO PAUSE Y JUGAR
        if(code == KeyEvent.VK_P){

            //PAUSE Y DESPAUSE ESCENA 1
            if(gp.gameState == gp.pauseState1)
            {
                gp.gameState =gp.escenaState1;
                gp.playMusic(0);
            }else  if(gp.gameState == gp.escenaState1)
            {
                gp.gameState = gp.pauseState1;
                gp.stopMusic();
            }

            //PAUSE Y DESPAUSE ESCENA 2
            if(gp.gameState == gp.pauseState2)
            {
                gp.gameState =gp.escenaState2;

            }else  if( gp.gameState == gp.escenaState2)
            {
                gp.gameState = gp.pauseState2;
            }

            //PAUSE Y DESPAUSE ESCENA 3 (SI EXISTIERA)
            if(gp.gameState == gp.pauseState3)
            {
                gp.gameState =gp.escenaState3;

            } else  if(gp.gameState == gp.escenaState3){
                gp.gameState = gp.pauseState3;
                if (code == KeyEvent.VK_ENTER) {
                    System.exit(0);
                    //gp.stopMusic(0);
                }
            }


        }

        //TELEPORT ZONAS
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = true;

        }

        //Enhorabuna estado
        if(gp.gameState==gp.congratulationsState){

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();
        if(code == KeyEvent.VK_D){
            rightPressed =false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = false;
        }

    }
}
