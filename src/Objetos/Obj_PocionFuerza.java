package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Obj_PocionFuerza  extends SuperObject {
    GamePanel gp;

    public Obj_PocionFuerza (GamePanel gp) {
        this.gp = gp;

        name="PocionFuerza";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionfuerza.png"));
            uTool.scaleImage(imagen,gp.tamañoMosaico, gp.tamañoMosaico);


        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void usar(GamePanel gp) {
        gp.jugador.strenght += 2;
        gp.fuerzaActiva = true;
    }
}
