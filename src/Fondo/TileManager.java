package Fondo;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {

    GamePanel gp;
    Fondo1[] fondos1;
    Fondo2[] fondos2;


    public TileManager(GamePanel gp) {
        this.gp=gp;
        fondos1 =new Fondo1[15];
        for(int i=0;i<fondos1.length;i++){
            fondos1[i] = new Fondo1();
        }
        fondos2 =new Fondo2[5];
        for(int i=0;i<fondos2.length;i++){
            fondos2[i] = new Fondo2();
        }
        getTileImage1();
    }

    
    public void getTileImage1(){

            try {

                fondos1[0].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_0.png")));
                fondos1[1].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_1.png")));
                fondos1[2].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_2.png")));
                fondos1[3].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_3.png")));
                fondos1[4].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_4.png")));
                fondos1[5].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_5.png")));
                fondos1[6].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_6.png")));
                fondos1[7].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_7.png")));
                fondos1[8].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_8.png")));
                fondos1[9].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_9.png")));
                fondos1[10].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_10.png")));
                fondos1[11].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_11.png")));
                fondos1[12].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/castillete.png")));
                fondos1[13].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/fondoEscritura.png")));

                fondos2[0].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_0.png")));
                fondos2[1].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_2.png")));
                fondos2[2].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_3.png")));
                fondos2[3].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/fondoEscritura.png")));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void draw1(Graphics2D g){

            g.drawImage(fondos1[0].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[1].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[2].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[3].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[4].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[5].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[6].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[7].imagen, 0, -210, gp.pantallaAnchura, 70, null);
            g.drawImage(fondos1[8].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[9].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[10].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[11].imagen, 0, -210, gp.pantallaAnchura, 700, null);
            g.drawImage(fondos1[12].imagen, 780, 180, 360, 275, null);
            g.drawImage(fondos1[13].imagen, -350, 490, 1800, 140, null);


    }

    public void draw2(Graphics2D g){
        g.drawImage(fondos2[0].imagen, 0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[1].imagen, 0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[2].imagen, 0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[3].imagen, -350, 490, 1800, 140, null);
    }

}
