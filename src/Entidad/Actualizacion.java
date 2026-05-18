package Entidad;

import Main.GamePanel;
import Objetos.Obj_PocionFuerza;
import Objetos.Obj_PocionVida;

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
    public void actualizacionSistemaCombate() {
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

                // ── Drop aleatorio ──
                int rand = new java.util.Random().nextInt(100);
                if (rand < 50) {
                    gp.objetoDropeado = new Obj_PocionVida(gp);
                } else if (rand < 80) {
                    gp.objetoDropeado = new Obj_PocionFuerza(gp);
                }
                // 20% no dropea nada, objetoDropeado queda null




                if (e instanceof samuraiErrante) {
                    gp.dropX = 500; // centro de la escena 2
                    gp.dropY = 420; // justo encima del suelo
                    gp.stopMusic();
                    gp.gameState = gp.escenaState2;
                    gp.playMusic(0);

                } else if (e instanceof Gigante) {
                    gp.dropX = 500; // centro de la escena 3
                    gp.dropY = 420;
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

// Resetear fuerza temporal después del ataque
                    if (gp.fuerzaActiva) {
                        gp.jugador.strenght -= 2;
                        gp.fuerzaActiva = false;
                    }
                }
            }
        }

        // ── Turno del enemigo ─────────────────────────────────────────────────
        if (!gp.jugadorTurno && !e.heMuertoEnemigo && gp.isSituacionPelea && !gp.inventarioAbierto) {
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
    public void actualizacionIrEscena2() {
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
    public void actualizacionEmpezarPelea1() {
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
    public void actualizacionEmpezarPeleaFinal() {
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
    public void actualizacionIrEscena3() {
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

    public void actualizacionMostrarEscenaCongratulations() {
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

    public void actualizacionInventario() {
        if (gp.gameState == gp.statePelea || gp.gameState == gp.statePelea2) {
            if (!gp.jugadorTurno && gp.inventarioAbierto) {
                // El jugador está navegando el inventario, no hacer nada más
                return;
            }
        }
    }
    public void actualizacionRecogerDrop() {
        if (gp.objetoDropeado == null) return;

        int jugadorX;

        if (gp.gameState == gp.escenaState2) {
            jugadorX = gp.jugador.x2Jugador + 200; // centro del sprite 400px
        } else if (gp.gameState == gp.escenaState3) {
            jugadorX = gp.jugador.x3Jugador + 200;
        } else {
            return;
        }

        int distX = Math.abs(jugadorX - gp.dropX);

        if (distX < 60) {
            gp.inventario.añadirObjeto(gp.objetoDropeado);
            gp.objetoDropeado = null;
            gp.inventarioSlot = 0;
            System.out.println("Pocion recogida!");
        }
    }
}