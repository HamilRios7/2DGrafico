package Objetos;

import Main.GamePanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el inventario del jugador.
 * Permite almacenar, eliminar y acceder a objetos del juego
 * con un límite máximo de capacidad.
 */
public class Inventario {
    /**
     * Referencia al GamePanel principal del juego.
     */
    GamePanel gp;

    /**
     * Número máximo de objetos que puede contener el inventario.
     */
    private final int MAX_OBJETOS = 20;

    /**
     * Lista de objetos almacenados en el inventario.
     */
    public List<SuperObject> objetos = new ArrayList<>();

    /**
     * Constructor del inventario.
     *
     * @param gp referencia al GamePanel del juego
     */
    public Inventario(GamePanel gp) {
        this.gp = gp;
    }


    /**
     * Añade un objeto al inventario si hay espacio disponible.
     *
     * @param obj objeto a añadir
     * @return true si el objeto fue añadido, false si el inventario está lleno
     */
    public boolean añadirObjeto(SuperObject obj) {
        if (objetos.size() < MAX_OBJETOS) {
            objetos.add(obj);
            return true;
        }
        return false;
    }

    /**
     * Elimina un objeto del inventario según su índice.
     *
     * @param index posición del objeto a eliminar
     */
    public void eliminarObjeto(int index) {

        //Comprueba que el índice sea válido,si lo es, elimina el objeto en esa posición
        if (index >= 0 && index < objetos.size())
            objetos.remove(index);
    }


    /**
     * Obtiene un objeto del inventario según su índice.
     *
     * @param index posición del objeto
     * @return el objeto si existe, o null si el índice no es válido
     */
    public SuperObject getObjeto(int index) {
        if (index >= 0 && index < objetos.size())
            return objetos.get(index);
        return null;
    }


    /**
     * Devuelve la cantidad actual de objetos en el inventario.
     *
     * @return número de objetos almacenados
     */
    public int getSize() {
        return objetos.size();
    }
}