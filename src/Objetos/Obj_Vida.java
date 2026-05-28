package Objetos;


import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Obj_Vida  extends  SuperObject{

    GamePanel gp;

    public Obj_Vida(GamePanel gp) {
        this.gp = gp;

        name="Heart";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/corazon.png"));



        }catch (IOException e){
            System.out.println("Imagen de vida no encontrada : ");
            e.printStackTrace();
        }
    }

    public void usar(){}
}
