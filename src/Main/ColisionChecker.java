package Main;

import Entidad.Jugador;
import Entidad.campoPuerta;
import Entidad.campoPeleaInteraccion;

import static java.lang.Math.clamp;

public class ColisionChecker {
    GamePanel gp;
  ;
    public ColisionChecker(GamePanel gp){
        this.gp=gp;
    }


    public boolean checkerCambioEscena(Jugador jugador, campoPuerta escena){
       if( jugador.getBorde1().intersects(escena.getBorde())){
           return true;
       }else {
           return false;
       }
    }

    public boolean checkerEstadoPelea(Jugador jugador, campoPeleaInteraccion escena){

        if( jugador.getBorde2().intersects(escena.getBorde())){
            return true;
        }else {
            return false;
        }

    }
}
