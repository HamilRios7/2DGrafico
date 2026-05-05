package Entidad;

import Main.GamePanel;

public class Actualizacion {

    GamePanel gp;

    public Actualizacion(GamePanel gp) {
        this.gp = gp;

    }

    public void actualizacionCombate1() {
        gp.enemigoActual = gp.samuraiErrante;
        gp.samuraiErrante.updateSamurai();
        gp.jugador.update2();
        if (gp.jugador.getLife() <= 0) {
            gp.jugador.heMuerto = true;
        }
        if (gp.jugador.heMuerto) {
            gp.jugador.animacionMuerte();
            if (gp.jugador.animacionMuerteTerminada()) {
                gp.jugador.isAnimacionMuerteTerminada = true;

                gp.gameState = gp.titleState;
            }
        }

        if (gp.samuraiErrante.getLifeEnemigo() <= 0) {
            gp.samuraiErrante.heMuertoEnemigo = true;
            gp.jugadorTurno = false;
        }
        if (gp.samuraiErrante.heMuertoEnemigo) {
            gp.samuraiErrante.estoyAtacandoErrante = false;
            gp.samuraiErrante.enemigoYaAtaco = false;
            gp.samuraiErrante.animacionMuerte();
            if (gp.samuraiErrante.animacionMuerteTerminadaErrante()) {
                gp.ispeleaFinalizada = true;
                gp.samuraiErrante.isAnimacionMuerteTerminadaEnemigo = true;
                gp.gameState = gp.escenaState2;
            }
        }

        if (gp.jugadorTurno && !gp.jugador.heMuerto) {
            if (gp.jugador.estoyAtacando) {
                gp.jugador.animacionAtaque();
                if (gp.jugador.haFallado) {
                    gp.samuraiErrante.contratacar();
                }
                if (gp.jugador.animacionAtaqueTerminada()) {
                    gp.jugadorTurno = false;
                    gp.jugador.estoyAtacando = false;

                }
            }
        }
        if (!gp.jugadorTurno && !gp.samuraiErrante.heMuertoEnemigo) {

            if (!gp.samuraiErrante.enemigoYaAtaco) {
                gp.samuraiErrante.actuarSamurai();
                gp.samuraiErrante.enemigoYaAtaco = true;
            }
            if (gp.samuraiErrante.estoyAtacandoErrante) {
                gp.samuraiErrante.animacionAtacar();
                if (gp.samuraiErrante.animacionAtaqueTerminadaErrante()) {
                    gp.jugadorTurno = true;
                    gp.samuraiErrante.estoyAtacandoErrante = false;
                    gp.samuraiErrante.enemigoYaAtaco = false;
                }
            }

        }
    }


    public void actualizacionMoverEscena2(){

        if (gp.cChecker.checkerCambioEscena(gp.jugador,new campoPuerta(gp))) {
            gp.jugador.cercaPuerta = true;

            if (gp.jugador.cercaPuerta && gp.keyH.ePressed) {
                gp.gameState = gp.escenaState2;
                gp.jugador.cercaPuerta = false;
            }
        } else {
            gp.jugador.cercaPuerta = false;
        }

    }


    public void actualizacionMoverEstadoPelea1(){

        if(gp.cChecker.checkerEstadoPelea(gp.jugador,new campoPeleaInteraccion(gp))){
            gp.jugador.cercaPelea = true;
            if (gp.jugador.cercaPelea && gp.keyH.ePressed && !gp.ispeleaFinalizada) {
                gp.jugador.cercaPelea = false;
                gp.jugador.moverPelea();
                gp.samuraiErrante.updateSamurai();
            }

        }else {
            gp.jugador.cercaPelea = false;
        }

    }

    public void actualizacionMoverEscenaCongratulatons(){

        if(gp.cChecker.checkerCambioPantallaEnhorabuena(gp.jugador,new campoEnhorabuenaInteraccion(gp))){
            gp.jugador.cercaIrPiso3 = true;
            if (gp.jugador.cercaIrPiso3 && gp.keyH.ePressed && gp.ispeleaFinalizada) {
                gp.jugador.cercaIrPiso3 = false;
                gp.gameState=gp.congratulationsState;
            }
        }else {
            gp.jugador.cercaIrPiso3 = false;
        }

    }
}
