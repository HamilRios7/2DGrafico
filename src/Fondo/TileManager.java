package Fondo;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {

    GamePanel gp;
    Fondo[] fondos;


    public TileManager(GamePanel gp) {
        this.gp=gp;
        fondos=new Fondo[15];
        getTileImage();
    }

    public void getTileImage(){
        try{
            fondos[0]=new Fondo ();
            fondos[0].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_0.png")));
            fondos[1]=new Fondo ();
            fondos[1].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_1.png")));
            fondos[2]=new Fondo ();
            fondos[2].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_2.png")));
            fondos[3]=new Fondo ();
            fondos[3].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_3.png")));
            fondos[4]=new Fondo ();
            fondos[4].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_4.png")));
            fondos[5]=new Fondo ();
            fondos[5].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_5.png")));
            fondos[6]=new Fondo ();
            fondos[6].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_6.png")));
            fondos[7]=new Fondo ();
            fondos[7].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_7.png")));
            fondos[8]=new Fondo ();
            fondos[8].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_8.png")));
            fondos[9]=new Fondo ();
            fondos[9].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_9.png")));
            fondos[10]=new Fondo ();
            fondos[10].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_10.png")));
            fondos[11]=new Fondo ();
            fondos[11].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/capa_11.png")));
            fondos[12]=new Fondo ();
            fondos[12].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/castillete.png")));
            fondos[13]=new Fondo ();
            fondos[13].imagen= ImageIO.read((getClass().getClassLoader().getResourceAsStream("fondo/fondoEscritura.png")));


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void draw (Graphics2D g){
        g.drawImage(fondos[0].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[1].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[2].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[3].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[4].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[5].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[6].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[7].imagen,0,-210,gp.pantallaAnchura,70,null);
        g.drawImage(fondos[8].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[9].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[10].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[11].imagen,0,-210,gp.pantallaAnchura,700,null);
        g.drawImage(fondos[12].imagen,780,180,360,275,null);
        g.drawImage(fondos[13].imagen,0,490, gp.pantallaAnchura,130,null);
    }

}
