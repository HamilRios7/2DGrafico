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
     *  gameState == statePelea.
     *
     * El flujo de turnos es:
     *
     *   Jugador elige ataque → solo se guarda  ataqueElegido
     *   Animación del jugador se reproduce
     *   Al terminar la animación → se calcula el daño →  isSituacionPelea=false
     *   UI muestra resultado → jugador pulsa Enter (gestionado en KeyHandler)
     *   Si falló: contrataque Enemigo  → muestra pantalla →  Enter para salir de la informacion de UI→ turno normal del enemigo
     *   Si acertó: turno del enemigo directamente
     *   Posible pantalla de habilidad unica→ Enter para salir de la informacion de UI → animación de enemigo de ataque
     *   → calculo de daño y muestra pantalla → Enter para salir de la informacion de UI → turno jugador
     *
     */
    public void actualizacionCombate1() {
        gp.enemigoActual = gp.samuraiErrante;
        gp.samuraiErrante.updateSamurai();
        gp.jugador.update2();

        // ── Comprobar muerte del jugador ──────────────────────────────────────
        if (gp.jugador.getLife() <= 0) {
            gp.jugador.heMuerto = true;
        }
        if (gp.jugador.heMuerto) {
            gp.jugador.animacionMuerte();
            if (gp.jugador.animacionMuerteTerminada()) {
                gp.jugador.isAnimacionMuerteTerminada = true;
                gp.gameState = gp.titleState; // volver al título con pantalla de muerte
            }
        }

        // ── Comprobar muerte del enemigo ──────────────────────────────────────
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
                gp.gameState = gp.escenaState2; // volver a la escena 2
            }
            return; // no procesar más lógica si el enemigo está muriendo
        }

        // ── Turno del jugador ─────────────────────────────────────────────────

        // Solo gestiona la animación. El daño se calcula al terminar la animación,
        // no al elegir el ataque, para sincronizarlo visualmente.
        if (gp.jugadorTurno && !gp.jugador.heMuerto) {
            if (gp.jugador.estoyAtacando) {
                gp.jugador.animacionAtaque();

                if (gp.jugador.animacionAtaqueTerminada()) {
                    gp.jugador.estoyAtacando = false;

                    // Calcular daño según el ataque elegido por el jugador
                    if (gp.jugador.ataqueElegido == 0) gp.jugador.ataqueSeguro();
                    if (gp.jugador.ataqueElegido == 1) gp.jugador.ataqueEquilibrado();
                    if (gp.jugador.ataqueElegido == 2) gp.jugador.ataqueArriesgado();
                    gp.jugador.ataqueElegido = -1; // reset para el siguiente turno

                    // isSituacionPelea=false ya lo activa el método de ataque
                    // jugadorTurno NO cambia aquí, cambia en KeyHandler al pulsar Enter
                }
            }
        }

        // ── Turno del enemigo ─────────────────────────────────────────────────

        // Solo entra cuando isSituacionPelea=true (no hay pantalla de UI abierta)
        if (!gp.jugadorTurno && !gp.samuraiErrante.heMuertoEnemigo && gp.isSituacionPelea) {

            if (!gp.samuraiErrante.enemigoYaAtaco) {

                // Se comprueba la habilidad ANTES de arrancar la animación.
                // Si se activa, no hay animación este turno, solo pantalla de UI.
                if (!gp.samuraiErrante.isHabilidadActivada &&
                        gp.samuraiErrante.getLifeEnemigo() <= (gp.samuraiErrante.getBarraVidaEnemigo() / 2)) {

                    gp.samuraiErrante.activarHabilidadUnica();
                    gp.samuraiErrante.enemigoYaAtaco = true;
                    // isSituacionPelea=false → UI muestra pantalla de habilidad
                    // NO ponemos estoyAtacandoErrante=true → sin animación
                    return;
                }

                // Sin habilidad → arrancar animación de ataque normal
                gp.samuraiErrante.enemigoYaAtaco = true;
                gp.samuraiErrante.estoyAtacandoErrante = true;
                gp.samuraiErrante.contadorMaxFramesEnemigo = 0; // reset contador
                gp.samuraiErrante.atacarNumEnemigo = 1;          // reset frame animación
            }

            if (gp.samuraiErrante.estoyAtacandoErrante) {
                gp.samuraiErrante.animacionAtacar();

                if (gp.samuraiErrante.animacionAtaqueTerminadaErrante()) {
                    gp.samuraiErrante.estoyAtacandoErrante = false;
                    // Calcular daño UNA SOLA VEZ al terminar la animación
                    gp.samuraiErrante.actuarSamurai();
                    // actuarSamurai pone isSituacionPelea=false → pantalla de resultado
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
                gp.gameState = gp.escenaState2;
                gp.jugador.cercaPuerta = false;
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
                gp.jugador.cercaPelea = false;
                gp.jugador.moverPelea();
                gp.samuraiErrante.updateSamurai();
            }
        } else {
            gp.jugador.cercaPelea = false;
        }
    }

    /**
     * Comprueba si el jugador está en la zona de enhorabuena
     * y gestiona la transición a la pantalla de congratulaciones al pulsar E.
     * Solo funciona si la pelea ha sido finalizada.
     */
    public void actualizacionMoverEscenaCongratulatons() {
        if (gp.cChecker.checkerCambioPantallaEnhorabuena(gp.jugador, new campoEnhorabuenaInteraccion(gp))) {
            gp.jugador.cercaIrPiso3 = true;
            if (gp.jugador.cercaIrPiso3 && gp.keyH.ePressed && gp.ispeleaFinalizada) {
                gp.jugador.cercaIrPiso3 = false;
                gp.gameState = gp.congratulationsState;
            }
        } else {
            gp.jugador.cercaIrPiso3 = false;
        }
    }
}