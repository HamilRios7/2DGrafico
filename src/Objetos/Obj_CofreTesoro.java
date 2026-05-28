package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * Objeto que representa un cofre del juego.
 */
public class Obj_CofreTesoro  extends SuperObject{

    /**
     * Referencia al GamePanel para acceder al estado del juego.
     */
    GamePanel gp;



    /**
     * Constructor del cofre.
     *
     * @param gp referencia al GamePanel principal
     */
    public Obj_CofreTesoro (GamePanel gp) {

        this.gp = gp;
        name="Cofre";
        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/cofre.png"));
        }catch (IOException e){
            System.out.println("Imagen de cofre no encontrada : ");
            e.printStackTrace();
        }


    }

    public void usar(){}
}
