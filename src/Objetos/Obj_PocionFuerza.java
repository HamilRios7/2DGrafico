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
        descripcion = "Aumenta fuerza +5 durante 1 ataque";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionfuerza.png"));

        }catch (IOException e){
            System.out.println("Imagen de pocion no encontrada : ");
            e.printStackTrace();
        }
    }
    @Override
    public void usar() {
        int strenght= gp.jugador.getStrenght() + 5;
        gp.jugador.setStrenght(strenght);
        gp.fuerzaActiva = true;
    }
}
