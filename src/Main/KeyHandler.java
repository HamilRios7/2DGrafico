package Main;

import Entidad.Enemigo;
import HallOfFame.GestorXml;
import HallOfFame.RegistroJugador;
import Objetos.SuperObject;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manejador de eventos de teclado del juego.
 *
 * Implementa KeyListener para capturar las pulsaciones del jugador
 * y traducirlas en acciones según el estado actual del juego
 * (título, combate, pausa, exploración, etc.).
 */
public class KeyHandler implements KeyListener {

    /** true mientras el jugador mantiene pulsada la tecla de movimiento derecha (D). */
    public boolean rightPressed;

    /** true mientras el jugador mantiene pulsada la tecla de movimiento izquierda (A). */
    public boolean leftPressed;

    /** Referencia al panel principal del juego para leer y modificar el estado global. */
    GamePanel gp;

    /**
     * true durante el frame en que el jugador pulsa E.
     * Se usa para activar interacciones (entrar por puertas, iniciar peleas, etc.).
     */
    public boolean ePressed;


    public boolean oPressed =false;

    /**
     * Construye el manejador de teclado vinculado al panel de juego.
     *
     * @param gp panel principal del juego
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    /** No se usa en este juego; requerido por la interfaz KeyListener. */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Se invoca cada vez que el jugador presiona una tecla.
     * Gestiona la lógica de entrada para todos los estados del juego.
     *
     * @param e evento de tecla presionada
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // ── Movimiento del jugador (disponible en cualquier estado) ──────────
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_A) leftPressed  = true;

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: TÍTULO
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.titleState) {
            // ── Sub-pantalla 0: Menú principal ───────────────────────────────
            if (gp.ui.titleScreenState == 0) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.comandoNum--;
                    if (gp.ui.comandoNum < 0) gp.ui.comandoNum = 2;
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.comandoNum++;
                    if (gp.ui.comandoNum > 2) gp.ui.comandoNum = 0;
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.comandoNum == 0) gp.ui.titleScreenState = 1;
                    if (gp.ui.comandoNum == 1) { /* créditos */ }
                    if (gp.ui.comandoNum == 2) System.exit(0);
                }

                // ── Sub-pantalla 1: Historia / introducción ──────────────────────
            } else if (gp.ui.titleScreenState == 1) {

                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    // Obtener la ventana principal del juego
                    JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(gp);

                    // Mostrar pantalla de nombre (bloqueante hasta confirmar)
                    PantallaIntroducirNombre pantallaIntroducirNombre =
                            new PantallaIntroducirNombre(ventana);
                    pantallaIntroducirNombre.mostrar();

                    // Guardar nombre y arrancar cronómetro
                    gp.nombreJugador = pantallaIntroducirNombre.getNombre();
                    gp.stopMusic();
                    gp.gameState = gp.escenaState1;
                    gp.playMusic(5);
                    gp.cronometro.arrancar();
                }

                // ── Sub-pantalla 2: Pantalla de muerte ──────────────────────────
            } else if (gp.ui.titleScreenState == 2) {

                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    System.exit(0);
                }
            }
        }

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: COMBATE — turno del jugador (menú de acciones)
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.statePelea || gp.gameState == gp.statePelea2) {
// ── INVENTARIO (prioridad máxima, antes que cualquier otro bloque) ──
            if (gp.inventarioAbierto) {
                if (code == KeyEvent.VK_D && !gp.inventario.objetos.isEmpty()) {
                    gp.inventarioSlot++;
                    if (gp.inventarioSlot >= gp.inventario.objetos.size())
                        gp.inventarioSlot = 0;
                }
                if (code == KeyEvent.VK_A && !gp.inventario.objetos.isEmpty()) {
                    gp.inventarioSlot--;
                    if (gp.inventarioSlot < 0)
                        gp.inventarioSlot = gp.inventario.objetos.size() - 1;
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (!gp.inventario.objetos.isEmpty()) {
                        SuperObject obj = gp.inventario.objetos.get(gp.inventarioSlot);
                        obj.usar(gp);
                        gp.inventario.eliminarObjeto(gp.inventarioSlot);
                        gp.inventarioSlot = 0;
                    }
                    gp.inventarioAbierto = false;
                    gp.jugadorTurno = true; // sigue siendo turno del jugador
                    gp.isSituacionPelea = true; // vuelve al menú de combate
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    gp.inventarioAbierto = false;
                    gp.jugadorTurno = true; // sigue siendo turno del jugador
                    gp.isSituacionPelea = true; // vuelve al menú de combate
                }
                return;
            }
            if (code == KeyEvent.VK_ENTER && gp.jugadorTurno && gp.isSituacionPelea) {

                if (gp.ui.subState == 0) {
                    if (gp.ui.comandoNum1 == 0) {
                        gp.ui.subState = 1;
                    } else if (gp.ui.comandoNum1 == 1) {
                        gp.inventarioAbierto = true;
                        gp.jugadorTurno = false;
                        gp.ui.comandoNum1 = 0;
                    }
                } else if (gp.ui.subState == 1) {
                    if (!gp.jugador.estoyAtacando) {
                        gp.jugador.ejecutarAtaque(gp.ui.comandoNum1);
                        gp.ui.subState    = 0;
                        gp.ui.comandoNum1 = 0;
                    }
                }
            }

            // Volver atrás desde el submenú de ataques con ESC
            if (code == KeyEvent.VK_ESCAPE && gp.ui.subState == 1) {
                gp.ui.subState = 0;
            }

            // Navegación vertical dentro del menú de combate
            int maxCommand = (gp.ui.subState == 0) ? 1 : 2;
            if (code == KeyEvent.VK_W && gp.ui.comandoNum1 > 0) {
                gp.ui.comandoNum1--;
            }
            if (code == KeyEvent.VK_S && gp.ui.comandoNum1 < maxCommand) {
                gp.ui.comandoNum1++;
            }
        }



        // ════════════════════════════════════════════════════════════════════
        // ESTADO: COMBATE — confirmación de resultado
        // Usa gp.enemigoActual para ser compatible con cualquier enemigo futuro.
        // ════════════════════════════════════════════════════════════════════
        if ((gp.gameState == gp.statePelea || gp.gameState == gp.statePelea2) && !gp.isSituacionPelea) {

            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum2 == 1) {

                // Alias genérico: funciona con cualquier subclase de Enemigo
                Enemigo enemigo = gp.enemigoActual;

                // Guardamos flags antes de resetearlos
                boolean eraAtaqueJugador = gp.jugador.fuejugadorAtaque;
                boolean hayContrataque   = gp.jugador.contrataquePendiente;

                // Resetear flags de turno para el siguiente ciclo
                gp.jugador.fuejugadorAtaque  = false;
                enemigo.fueEnemigoAtaque     = false;
                gp.isSituacionPelea          = true;

                if (eraAtaqueJugador) {
                    // ── Fue el turno del jugador ──────────────────────────────
                    if (hayContrataque) {
                        // El jugador falló → el enemigo contrataca.
                        // contratacar() está definido en Enemigo base (no-op por defecto),
                        // cada subclase puede sobreescribirlo si lo necesita.
                        gp.jugador.contrataquePendiente = false;
                        enemigo.contratacar();
                        gp.jugadorTurno        = false;
                        enemigo.enemigoYaAtaco  = false;
                        enemigo.estoyAtacando   = false;
                    } else {
                        // Ataque del jugador resuelto → turno del enemigo
                        gp.jugadorTurno        = false;
                        enemigo.enemigoYaAtaco  = false;
                        enemigo.estoyAtacando   = false;
                    }

                } else {
                    // ── Fue el turno del enemigo (o habilidad especial) ───────
                    if (enemigo.seHaMostradoPantalla) {
                        // El jugador confirmó la pantalla de habilidad única
                        enemigo.seHaMostradoPantalla = false;
                        enemigo.fueEnemigoAtaque     = false;
                        gp.jugadorTurno              = false;
                        enemigo.enemigoYaAtaco        = false;
                        enemigo.estoyAtacando         = false;
                        gp.isSituacionPelea          = true;

                    } else if (enemigo.fueContrataque()) {
                        // Venimos del contrataque → el enemigo hace su turno normal
                        enemigo.resetContrataque();
                        enemigo.fueEnemigoAtaque  = false;
                        gp.jugadorTurno           = false;
                        enemigo.enemigoYaAtaco     = false;
                        enemigo.estoyAtacando      = false;
                        gp.isSituacionPelea       = true;

                    } else if(enemigo.fueStuneado()) {
                        gp.jugadorTurno        = false;
                        enemigo.enemigoYaAtaco  = false;
                        enemigo.estoyAtacando   = false;
                        enemigo.resetStun();
                    } else {
                        // Ataque del enemigo resuelto → devolver turno al jugador
                        gp.jugadorTurno        = true;
                        enemigo.enemigoYaAtaco  = false;
                        enemigo.estoyAtacando   = false;
                    }
                }
            }
        }

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: PAUSA / REANUDAR (tecla P)
        // ════════════════════════════════════════════════════════════════════
        if (code == KeyEvent.VK_P) {

            if (gp.gameState == gp.pauseState1) {
                gp.gameState = gp.escenaState1;
                gp.playMusic(0);
                gp.playMusic(5);
            } else if (gp.gameState == gp.escenaState1) {
                gp.gameState = gp.pauseState1;
                gp.stopMusic();
            }

            if (gp.gameState == gp.pauseState2) {
                gp.gameState = gp.escenaState2;
            } else if (gp.gameState == gp.escenaState2) {
                gp.gameState = gp.pauseState2;
            }

            if (gp.gameState == gp.pauseState3) {
                gp.gameState = gp.escenaState3;
            } else if (gp.gameState == gp.escenaState3) {
                gp.gameState = gp.pauseState3;
            }
        }

        // ── Interacción con el entorno ────────────────────────────────────────
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: ENHORABUENA (fin del juego)
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.congratulationsState) {
            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                long finalTime = gp.cronometro.detener();
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                RegistroJugador registro = new RegistroJugador(gp.nombreJugador, finalTime, fecha);
                GestorXml.guardar(registro);
                gp.gameState = gp.hallOfFameState;
                return; //  evita que el bloque de hallOfFameState procese este mismo ENTER
            }
        }

        if (gp.gameState == gp.hallOfFameState) {
            if (code == KeyEvent.VK_ENTER) {
                System.exit(0);
            }
        }



        // ════════════════════════════════════════════════════════════════════
// MENÚ DE OPCIONES (tecla O)
// ════════════════════════════════════════════════════════════════════
        if (gp.ui.dibujadoOpciones) {
            if (code == KeyEvent.VK_W) {
                gp.ui.opcionesComando--;
                if (gp.ui.opcionesComando < 0) gp.ui.opcionesComando = 2;
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.opcionesComando++;
                if (gp.ui.opcionesComando > 2) gp.ui.opcionesComando = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.opcionesComando == 1) {
                    // Togglear pantalla completa
                    gp.pantallaCompleta = !gp.pantallaCompleta;
                    if (gp.pantallaCompleta) {
                        Main.gd.setFullScreenWindow(
                                SwingUtilities.getWindowAncestor(gp));
                    } else {
                        Main.gd.setFullScreenWindow(null);
                    }
                }
                if (gp.ui.opcionesComando == 2) {
                    gp.ui.dibujadoOpciones = false;
                    oPressed = false;
                }
            }
            if (code == KeyEvent.VK_O || code == KeyEvent.VK_ESCAPE) {
                gp.ui.dibujadoOpciones = false;
                oPressed = false;
            }
            return; // bloquea el resto del input mientras opciones está abierto
        }

        if (code == KeyEvent.VK_O) {
            oPressed = true;
            gp.ui.dibujadoOpciones = true;
        }
    }

    /**
     * Se invoca cuando el jugador suelta una tecla.
     * Desactiva los flags de movimiento continuo y de interacción puntual.
     *
     * @param e evento de tecla soltada
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_A) leftPressed  = false;
        if (code == KeyEvent.VK_E) ePressed     = false;


    }
}