package Main;

import java.awt.*;

public class AccionesJugador {
    boolean turnOn;
    GamePanel gp;
    public AccionesJugador(GamePanel gp) {
      this.gp=gp;
    }

    public boolean ejecutarAccion(int opcion){
        boolean turnOn=true;
        if(opcion==1) {
            turnOn=abrirInventario();
            return turnOn;
        }
        if(opcion==2) {
        return turnOn;
        }else {

            return false;
        }

    }


    public boolean abrirInventario(){
    return  true;
    }
}
