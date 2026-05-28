package Objetos;

import java.awt.image.BufferedImage;



/**
 * Clase abstracta base para todos los objetos del juego.
 *
 * Define las propiedades comunes que tendrán todos los objetos,
 * como su imagen, nombre y descripción.
 *
 * Las subclases deberán implementar el método usar()
 * para definir el comportamiento específico del objeto
 * (por ejemplo, una poción que cura o un objeto que aumenta estadísticas).
 *
 * Al ser una clase abstracta, no puede instanciarse directamente.
 */
public  abstract class SuperObject {



    /** Imagen que representa el objeto en el juego */
    protected BufferedImage imagen;


    /** Nombre del objeto */
    protected String name;

    /** Descripción del objeto (opcional) */
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

