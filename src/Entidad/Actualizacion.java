package Entidad;

import Main.GamePanel;

/**
 * Clase que gestiona toda la lógica de actualización del juego cada frame.
 * Controla el flujo de turnos en combate, las animaciones, las transiciones
 * entre escenas y la detección de zonas de interacción.
 */
public class Actualizacion {

    /** Referencia al panel principal del juego */
    GamePanel gp;

    /**
     * @param gp referencia al panel principal del juego
     */
    public Actualizacion(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Actualización principal del combate, llamada cada frame cuando
     * gameState == statePelea.
     *
     * El flujo de turnos es:
     *   Jugador elige ataque → solo se guarda ataqueElegido
     *   Animación del jugador se reproduce
     *   Al terminar la animación → se calcula el daño → isSituacionPelea=false
     *   UI muestra resultado → jugador pulsa Enter (gestionado en KeyHandler)
     *   Si falló: contrataque Enemigo → muestra pantalla → Enter → turno normal del enemigo
     *   Si acertó: turno del enemigo directamente
     *   Posible pantalla de habilidad única → Enter → animación de enemigo de ataque
     *   → cálculo de daño y muestra pantalla → Enter → turno jugador
     *
     * NOTA: gp.enemigoActual debe estar asignado ANTES de entrar al estado statePelea.
     *   gp.enemigoActual = gp.samuraiErrante;  // combate 1
     *   gp.enemigoActual = gp.gigante;          // combate boss
     */
    public void actualizacionCombate1() {
        // gp.enemigoActual ya viene asignado desde Actualizacion.actualizacionMoverEscena*
        // NO se sobreescribe aquí para que funcione con cualquier enemigo.

        Enemigo e = gp.enemigoActual; // alias local para no repetir

        e.updateEnemigo();
        if(gp.gameState == gp.statePelea) gp.jugador.update2();
        if(gp.gameState == gp.statePelea2) gp.jugador.update3();

        // ── Comprobar muerte del jugador ──────────────────────────────────────
        if (gp.jugador.getLife() <= 0) {
            gp.jugador.heMuerto = true;
        }
        if (gp.jugador.heMuerto) {
            gp.jugador.animacionMuerte();
            if (gp.jugador.animacionMuerteTerminada()) {
                gp.stopMusic();
                gp.jugador.isAnimacionMuerteTerminada = true;
                gp.gameState = gp.titleState;
            }
        }

        // ── Comprobar muerte del enemigo ──────────────────────────────────────
        if (e.getLifeEnemigo() <= 0) {
            e.heMuertoEnemigo = true;
            gp.jugadorTurno = false;
        }
        if (e.heMuertoEnemigo) {
            e.estoyAtacando = false;
            e.enemigoYaAtaco = false;
            e.animacionMuerte();
            if (e.animacionMuerteTerminada()) {
                gp.ispeleaFinalizada = true;
                e.isAnimacionMuerteTerminadaEnemigo = true;
                if(e instanceof samuraiErrante) {
                    gp.stopMusic();
                    gp.gameState = gp.escenaState2;
                    gp.playMusic(0);
                }else if(e instanceof Gigante) {
                    gp.stopMusic();
                    gp.gameState = gp.escenaState3;
                    gp.playMusic(4);
                }
            }
        }

        // ── Turno del jugador ─────────────────────────────────────────────────
        if (gp.jugadorTurno && !gp.jugador.heMuerto) {
            if (gp.jugador.estoyAtacando) {
                gp.jugador.animacionAtaque();

                if (gp.jugador.animacionAtaqueTerminada()) {
                    gp.jugador.estoyAtacando = false;

                    if (gp.jugador.ataqueElegido == 0) gp.jugador.ataqueSeguro();
                    if (gp.jugador.ataqueElegido == 1) gp.jugador.ataqueEquilibrado();
                    if (gp.jugador.ataqueElegido == 2) gp.jugador.ataqueArriesgado();
                    gp.jugador.ataqueElegido = -1;
                }
            }
        }

        // ── Turno del enemigo ─────────────────────────────────────────────────
        if (!gp.jugadorTurno && !e.heMuertoEnemigo && gp.isSituacionPelea) {

            if (!e.enemigoYaAtaco) {

                // Comprobar habilidad única ANTES de arrancar la animación
                if (!e.isHabilidadActivada &&
                        e.getLifeEnemigo() <= (e.getBarraVidaEnemigo() / 2)) {

                    e.activarHabilidadUnica();
                    e.enemigoYaAtaco = true;
                    // isSituacionPelea=false → UI muestra pantalla de habilidad
                    // NO ponemos estoyAtacando=true → sin animación este turno
                    return;
                }

                // Sin habilidad → arrancar animación de ataque normal
                e.enemigoYaAtaco = true;
                e.estoyAtacando = true;
                e.contadorMaxFramesEnemigo = 0;
                e.atacarNumEnemigo = 1;
            }

            if (e.estoyAtacando) {
                e.animacionAtacar();

                if (e.animacionAtaqueTerminada()) {
                    e.estoyAtacando = false;
                    // Calcular daño UNA SOLA VEZ al terminar la animación
                    e.actuar();
                    // actuar() pone isSituacionPelea=false → pantalla de resultado
                }
            }
        }
    }

    /**
     * Comprueba si el jugador está en la zona de la puerta de la escena 1
     * y gestiona la transición a la escena 2 al pulsar E.
     */
    public void actualizacionMoverEscena2() {
        if (gp.cChecker.checkerCambioEscena(gp.jugador, new campoPuerta(gp))) {
            gp.jugador.cercaPuerta = true;
            if (gp.jugador.cercaPuerta && gp.keyH.ePressed) {
                gp.stopMusic();
                gp.enemigoActual = gp.samuraiErrante;
                gp.gameState = gp.escenaState2;

                gp.jugador.cercaPuerta = false;
                gp.playMusic(0);

            }
        } else {
            gp.jugador.cercaPuerta = false;
        }
    }

    /**
     * Comprueba si el jugador está en la zona de interacción de pelea
     * y gestiona la transición al estado de combate al pulsar E.
     * Solo funciona si la pelea no ha sido ya finalizada.
     */
    public void actualizacionMoverEstadoPelea1() {
        if (gp.cChecker.checkerEstadoPelea(gp.jugador, new campoPeleaInteraccion(gp))) {
            gp.jugador.cercaPelea = true;
            if (gp.jugador.cercaPelea && gp.keyH.ePressed && !gp.ispeleaFinalizada) {
                gp.stopMusic();
                gp.jugador.cercaPelea = false;
                gp.jugadorTurno = true;        // ← aquí
                gp.isSituacionPelea = true;    // ← aquí
                gp.jugador.moverPelea1();
                gp.enemigoActual.updateEnemigo();
                gp.playMusic(2);
            }
        } else {
            gp.jugador.cercaPelea = false;
        }
    }

    /**
     * Comprueba si el jugador está en la zona de interacción de pelea
     * y gestiona la transición al estado de combate al pulsar E.
     * Solo funciona si la pelea no ha sido ya finalizada.
     */
    public void actualizacionMoverEstadoPeleaFinal() {
        if (gp.cChecker.checkerEstadoPeleaFinal(gp.jugador, new campoPeleaInteraccion(gp))) {
            gp.jugador.cercaPeleaFinal = true;
            if (gp.jugador.cercaPeleaFinal && gp.keyH.ePressed && !gp.ispeleaFinalizada) {
                gp.stopMusic();
                gp.jugador.cercaPeleaFinal = false;
                gp.jugadorTurno = true;

                // ── Resetear estado de combate antes de empezar ──
                gp.isSituacionPelea = true;

                gp.jugador.moverPelea2();
                gp.enemigoActual.updateEnemigo();
                gp.playMusic(3);
            }
        } else {
            gp.jugador.cercaPeleaFinal = false;
        }
    }


    /**
     * Comprueba si el jugador está en la zona de enhorabuena
     * y gestiona la transición a la escena 3 al pulsar E.
     * Solo funciona si la pelea ha sido finalizada.
     */
    public void actualizacionMoverEscena3() {
        if (gp.cChecker.checkerCambioPantallaEscena3(gp.jugador, new campoIntercaccionEscena3(gp))) {
            gp.jugador.cercaIrPiso3 = true;
            if (gp.jugador.cercaIrPiso3 && gp.keyH.ePressed && gp.ispeleaFinalizada) {

                gp.jugador.cercaIrPiso3 = false;
                gp.enemigoActual = gp.gigante;
                gp.gameState = gp.escenaState3;
                gp.ispeleaFinalizada = false;
            }
        } else {
            gp.jugador.cercaIrPiso3 = false;
        }
    }

    public void actualizacionMoverEscenaCongratulations() {
        if (gp.cChecker.checkerCambioPantallaEnhorabuena(gp.jugador, new campoEnhorabuenaInteraccion(gp))) {
            gp.jugador.cercaCongratulations = true;
            if (gp.jugador.cercaCongratulations && gp.keyH.ePressed && gp.ispeleaFinalizada) {
                gp.jugador.cercaCongratulations = false;
                gp.gameState = gp.congratulationsState;
            }
        } else {
            gp.jugador.cercaCongratulations = false;
        }
    }
}