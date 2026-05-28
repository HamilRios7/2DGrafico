package Objetos;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Obj_PocionVida  extends  SuperObject{
    GamePanel gp;

    public Obj_PocionVida (GamePanel gp) {
        this.gp = gp;

        name="PocionVida";
        descripcion = "Recupera 30 puntos de vida";

        try {
            imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionvida.png"));



        }catch (IOException e){
            System.out.println("Imagen de pocion no encontrada : ");
            e.printStackTrace();
        }
    }


    @Override
    public void usar() {
        // Cura siempre la mitad de la vida máxima, sin pasarse del tope
        int vida =Math.min(gp.jugador.getLife() + (gp.jugador.getBarraVida() / 2), gp.jugador.getBarraVida());
        gp.jugador.setLife(vida);
    }
}
