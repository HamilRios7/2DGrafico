package Objetos;


import Main.GamePanel;
import Main.UtilityTool;

import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage imagen;
    public String name;

    UtilityTool uTool=new UtilityTool();

    public void usar(GamePanel gp) {
        // cada subclase sobreescribe esto
    }
}

