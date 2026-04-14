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
        getTileImage1();
        fondos2 =new Fondo2[15];
    }

    
    public void getTileImage1(){
        if(gp.gameState==gp.escenaState1) {
            try {
                fondos1[0] = new Fondo1();
                fondos1[0].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_0.png")));
                fondos1[1] = new Fondo1();
                fondos1[1].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_1.png")));
                fondos1[2] = new Fondo1();
                fondos1[2].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_2.png")));
                fondos1[3] = new Fondo1();
                fondos1[3].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_3.png")));
                fondos1[4] = new Fondo1();
                fondos1[4].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_4.png")));
                fondos1[5] = new Fondo1();
                fondos1[5].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_5.png")));
                fondos1[6] = new Fondo1();
                fondos1[6].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_6.png")));
                fondos1[7] = new Fondo1();
                fondos1[7].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_7.png")));
                fondos1[8] = new Fondo1();
                fondos1[8].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_8.png")));
                fondos1[9] = new Fondo1();
                fondos1[9].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_9.png")));
                fondos1[10] = new Fondo1();
                fondos1[10].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_10.png")));
                fondos1[11] = new Fondo1();
                fondos1[11].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_11.png")));
                fondos1[12] = new Fondo1();
                fondos1[12].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/castillete.png")));
                fondos1[13] = new Fondo1();
                fondos1[13].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f1/fondoEscritura.png")));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(gp.gameState==gp.escenaState2) {
            try{
            fondos2[0] = new Fondo2();
            fondos2[0].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_0.png")));
            fondos2[1] = new Fondo2();
            fondos2[1].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_1.png")));
            fondos2[2] = new Fondo2();
            fondos2[2].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_2.png")));
            fondos2[3] = new Fondo2();
            fondos2[3].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_3.png")));
            fondos2[4] = new Fondo2();
            fondos2[4].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_4.png")));
            fondos2[5] = new Fondo2();
            fondos2[5].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_5.png")));
            fondos2[6] = new Fondo2();
            fondos2[6].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_6.png")));
            fondos2[7] = new Fondo2();
            fondos2[7].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_7.png")));
            fondos2[8] = new Fondo2();
            fondos2[8].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_8.png")));
            fondos2[9] = new Fondo2();
            fondos2[9].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_9.png")));
            fondos2[10] = new Fondo2();
            fondos2[10].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_10.png")));
            fondos2[11] = new Fondo2();
            fondos2[11].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/capa_11.png")));
            fondos2[12] = new Fondo2();
            fondos2[12].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/castillete.png")));
            fondos2[13] = new Fondo2();
            fondos2[13].imagen = ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/f2/fondoEscritura.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        }else if(gp.gameState==gp.escenaState3) {
            
        }

    }

    public void draw1(Graphics2D g){
        if(gp.gameState==gp.escenaState1 || gp.gameState==gp.pauseState1) {
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
        } else if (gp.gameState==gp.escenaState2) {
            
        } else if (gp.gameState==gp.escenaState3) {
            
        }
    }

}
