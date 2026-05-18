package Objetos;

import Main.GamePanel;
import java.util.ArrayList;
import java.util.List;

public class Inventario {

    GamePanel gp;
    private final int MAX_OBJETOS = 20;
    public List<SuperObject> objetos = new ArrayList<>();

    public Inventario(GamePanel gp) {
        this.gp = gp;
    }

    public boolean añadirObjeto(SuperObject obj) {
        if (objetos.size() < MAX_OBJETOS) {
            objetos.add(obj);
            return true;
        }
        return false;
    }

    public void eliminarObjeto(int index) {
        if (index >= 0 && index < objetos.size())
            objetos.remove(index);
    }

    public SuperObject getObjeto(int index) {
        if (index >= 0 && index < objetos.size())
            return objetos.get(index);
        return null;
    }

    public int getSize() {
        return objetos.size();
    }
}