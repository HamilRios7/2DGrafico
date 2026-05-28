package Objetos;

import java.awt.image.BufferedImage;

public  abstract class SuperObject {

    protected BufferedImage imagen;
    protected String name;
    protected String descripcion = "";





    public  abstract void usar();


    public BufferedImage getImagen() {
        return imagen;
    }

    public String getName() {
        return name;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

