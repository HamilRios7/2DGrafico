package Entidad;

public class PocionVida extends Pocion {
    public PocionVida() {
        super("Poción de vida");
    }
    //debería funcionar cuando se habilite la clase Heroe.
    //método para curar al héroe mediante la poción de cura.
    /*public void usarPocion(Heroe heroe) {
        int curacion=(int)(heroe.getVidaMaxima()*0.05);
        heroe.usarPocionVida(curacion);
    }*/
}
