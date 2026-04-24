package Entidad;

public abstract class Item {
    //clase para crear las pociones
    protected String nombrePocion;

    public Item(String nombrePocion) {
        this.nombrePocion = nombrePocion;
    }

    public String getNombrePocion() {
        return nombrePocion;
    }
}

