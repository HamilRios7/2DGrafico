package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Obj_PocionVida  extends  SuperObject{
    GamePanel gp;

    public Obj_PocionVida (GamePanel gp) {
        this.gp = gp;

        name="PocionVida";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionvida.png"));
            uTool.scaleImage(imagen,gp.tamañoMosaico, gp.tamañoMosaico);


        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void usar(GamePanel gp) {
        // Cura siempre la mitad de la vida máxima, sin pasarse del tope
        gp.jugador.life = Math.min(gp.jugador.life + (gp.jugador.barraVida / 2), gp.jugador.barraVida);
    }
}
