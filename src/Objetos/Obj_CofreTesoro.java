package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Obj_CofreTesoro  extends SuperObject{

    GamePanel gp;

    public Obj_CofreTesoro (GamePanel gp) {
        this.gp = gp;

        name="Cofre";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/cofre.png"));
            uTool.scaleImage(imagen,gp.tamañoMosaico, gp.tamañoMosaico);


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
