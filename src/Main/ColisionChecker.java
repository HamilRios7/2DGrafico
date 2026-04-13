package Main;


import Entidad.Entidad;

import static java.lang.Math.clamp;

public class ColisionChecker {
    GamePanel gp;
  ;
    public ColisionChecker(GamePanel gp){
        this.gp=gp;
    }

    public int checkTile(Entidad entity){
        int x=entity.getX();
        x = clamp(x, 0, gp.pantallaAnchura - gp.tamañoMosaico);
        return x;
    }


}
