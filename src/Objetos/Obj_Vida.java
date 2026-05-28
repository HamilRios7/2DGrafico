package Objetos;


import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;


/**
 * Objeto visual que representa un corazón de vida.
 *
 * Este objeto no es utilizable, únicamente se usa para mostrar
 * la vida del jugador en la interfaz .
 */
public class Obj_Vida  extends  SuperObject{

    /**
     * Constructor del objeto vida (corazón).
     */
    public Obj_Vida() {


        name="Corazón";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/corazon.png"));



        }catch (IOException e){
            System.out.println("Imagen de vida no encontrada : ");
            e.printStackTrace();
        }
    }


    /**
     * Este objeto no tiene uso.
     *
     * Se implementa vacío porque la clase padre lo requiere,
     * pero no realiza ninguna acción.
     */
    public void usar(){}
}
