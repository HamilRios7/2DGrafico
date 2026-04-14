package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//el que recibe las eventos en el teclado
public class KeyHandler implements KeyListener {
    public boolean leftPressed,rightPressed;
    GamePanel gp;
    public boolean iniciarTexto;
    public boolean ePressed;


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
            }
            else if(gp.ui.titleScreenState==1){

                if(code == KeyEvent.VK_ENTER){
                    if(gp.ui.comandoNum==0){
                        gp.gameState=gp.escenaState1;
                        gp.playMusic(0);
                    }

                }
            }

        }


        //ESTADO PAUSE Y JUGAR
        if(code == KeyEvent.VK_P){
            if(gp.gameState == gp.pauseState1)
            {
                gp.gameState =gp.escenaState1;
                gp.playMusic(0);
            }else  if(gp.gameState == gp.escenaState1)
            {
                gp.gameState = gp.pauseState1;
                gp.stopMusic();
            }

            if(gp.gameState == gp.pauseState2)
            {
                gp.gameState =gp.escenaState2;

            }else  if( gp.gameState == gp.escenaState2)
            {
                gp.gameState = gp.pauseState2;
            }

            if(gp.gameState == gp.pauseState3)
            {
                gp.gameState =gp.escenaState3;

            } else  if(gp.gameState == gp.escenaState3){
                gp.gameState = gp.pauseState3;

            }


        }

        //TELEPORT ZONAS
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = true;

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
