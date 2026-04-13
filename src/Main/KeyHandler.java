package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//el que recibe las eventos en el teclado
public class KeyHandler implements KeyListener {
    public boolean leftPressed,rightPressed;
    GamePanel gp;



    public KeyHandler(GamePanel gp){
        this.gp=gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int opcionSeleccionada=0;
        boolean turnOn=false;
        //devuelve el numero de tecla que fue presionada

        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
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
        while(turnOn) {
            if (code == KeyEvent.VK_1) {
                opcionSeleccionada = 1;
                gp.ejecutarAccion(opcionSeleccionada);
            }
            if (code == KeyEvent.VK_2) {
                opcionSeleccionada = 2;
                gp.ejecutarAccion(opcionSeleccionada);
            }
            if (code == KeyEvent.VK_3) {
                opcionSeleccionada = 3;
                gp.ejecutarAccion(opcionSeleccionada);

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
