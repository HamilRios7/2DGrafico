package Entidad;

public class BerserkerCarmesi extends Enemigo{
    public BerserkerCarmesi(){
        super("Berserker Carmesí",250,50,0.4);
    }
    //golpe débil
    public void corteEscarata(Heroe heroe){
        ejecutarAtaque(heroe,"corte escarlata",2,0.5);//80% de precision, 25 daño (30 daño al llegar al 50% de la vida)
    }
    //golpe medio
    public void cargaCarmesi(Heroe heroe){
        ejecutarAtaque(heroe,"carga carmesi",1.1,0.5);//44% de precision 35 daño (50% de vida se multiplica 1.2 y hará 42)
    }
}
