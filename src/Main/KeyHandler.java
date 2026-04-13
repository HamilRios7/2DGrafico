package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//el que recibe las eventos en el teclado
public class KeyHandler implements KeyListener {
    public boolean leftPressed,rightPressed;
    GamePanel gp;
    public boolean iniciarTexto;


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
                        gp.gameState=gp.playState;
                        gp.playMusic(0);
                    }

                }
            }
        }


        //ESTADO PAUSE Y JUGAR
        if(code == KeyEvent.VK_P){
            if(gp.gameState == gp.playState){
                gp.gameState = gp.pauseState;
                gp.stopMusic();
            }
            else if(gp.gameState == gp.pauseState){
                gp.gameState =gp.playState;
                gp.playMusic(0);
            }
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
    }
}
