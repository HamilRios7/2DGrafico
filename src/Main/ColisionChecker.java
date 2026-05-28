package Main;



import Entidad.*;


/**
 *Clase que nos comprobara si hay un choque entre la hitboxs del jugador y
 * un recuadro de interaccion
 *
 */
public class ColisionChecker {
    GamePanel gp;

    //----Constructor-----
    public ColisionChecker(GamePanel gp){
        this.gp=gp;
    }


    /**
     * Metodo que comprueba si el jugador ha chocado con la hitbox de la puerta
     * para entrar al castillo cada frame
     *
     * @param jugador Recibimos el jugador del cual sacaremos el rectangulo de la hitbox
     * @param escena Recibe el objeto campoPuerta que ya tiene un rectangulo creado como hitbox
     */
    public boolean checkerCambioEscena(Jugador jugador, campoPuerta escena){
        //Revisa si dos rectangulos chocan o se tocan entre si
        if( jugador.getBorde1().intersects(escena.getBorde())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * Metodo que comprueba si el jugador esta en la zona para empezar la pelea con el samurai
     * dentro del castillo cada frame
     *
     * @param jugador Recibimos el jugador del cual sacaremos el rectangulo de la hitbox
     * @param escena Recibe el objeto campo de interracion que ya tiene un rectangulo creado como hitbox
     */
    public boolean checkerEstadoPelea(Jugador jugador, campoPeleaInteraccion escena){

        if( jugador.getBorde2().intersects(escena.getBorde())){
            return true;
        }else {
            return false;
        }

    }

    /**
     * Metodo que comprueba si el jugador esta en la zona para empezar la pelea con el gigante
     * dentro del castillo cada frame
     *
     * @param jugador Recibimos el jugador del cual sacaremos el rectangulo de la hitbox
     * @param escena Recibe el objeto campo de interracion que ya tiene un rectangulo creado como hitbox
     */
    public boolean checkerEstadoPeleaFinal(Jugador jugador, campoPeleaInteraccion escena){

        if( jugador.getBorde3().intersects(escena.getBorde())){
            return true;
        }else {
            return false;
        }

    }


    /**
     * Metodo que comprueba si el jugador esta en la zona para ir a la escena 3 donde se encuentra con el gigante , una vez ya matado al samurai,
     * dentro del castillo cada frame
     *
     * @param jugador Recibimos el jugador del cual sacaremos el rectangulo de la hitbox
     * @param campoIntercaccionEscena3 Recibe el objeto campo de interracion que ya tiene un rectangulo creado como hitbox
     */
    public boolean checkerCambioPantallaEscena3(Jugador jugador, campoIntercaccionEscena3 campoIntercaccionEscena3) {
        if( jugador.getBorde2().intersects(campoIntercaccionEscena3.getBorde())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * Metodo que comprueba si el jugador esta en la zona para finalizar el juego , una vez matado el gigante ,
     * dentro del castillo cada frame
     *
     * @param jugador Recibimos el jugador del cual sacaremos el rectangulo de la hitbox
     * @param campoEnhorabuenaInteraccion Recibe el objeto campo de interracion que ya tiene un rectangulo creado como hitbox
     */
    public boolean checkerCambioPantallaEnhorabuena(Jugador jugador, campoEnhorabuenaInteraccion campoEnhorabuenaInteraccion) {
        if( jugador.getBorde3().intersects(campoEnhorabuenaInteraccion.getBorde())){
            return true;
        }else {
            return false;
        }
    }
}