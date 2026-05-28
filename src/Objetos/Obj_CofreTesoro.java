package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;


/**
 * Objeto decorativo que representa un cofre.
 *
 * Este cofre no es interactivo, únicamente se dibuja en el mapa
 * como elemento visual.
 */
public class Obj_CofreTesoro  extends SuperObject{


    /**
     * Constructor del cofre.
     */
    public Obj_CofreTesoro () {


        name="Cofre";
        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/cofre.png"));
        }catch (IOException e){
            System.out.println("Imagen de cofre no encontrada : ");
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
