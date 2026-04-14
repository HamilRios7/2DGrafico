package Main;

import Entidad.Jugador;
import Entidad.Entidad;
import Entidad.campoPuerta;

import static java.lang.Math.clamp;

public class ColisionChecker {
    GamePanel gp;
  ;
    public ColisionChecker(GamePanel gp){
        this.gp=gp;
    }

    public int checkTile(Entidad entity){
        int x=entity.getX();
        x = clamp(-4, 0, gp.pantallaAnchura - gp.tamañoMosaico);
        return x;
    }

    public boolean checkerCambioEscena(Jugador jugador, campoPuerta escena){
       if( jugador.getBorde().intersects(escena.getBorde())){
           return true;
       }else {
           return false;
       }
    }
}
