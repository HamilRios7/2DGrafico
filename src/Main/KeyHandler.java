package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Manejador de eventos de teclado del juego.
 *
 * Implementa  KeyListener para capturar las pulsaciones del jugador
 * y traducirlas en acciones según el estado actual del juego
 * (título, combate, pausa, exploración, etc.).
 *
 */
public class KeyHandler implements KeyListener {

    /**  true mientras el jugador mantiene pulsada la tecla de movimiento derecha (D). */
    public boolean rightPressed;

    /** true mientras el jugador mantiene pulsada la tecla de movimiento izquierda (A). */
    public boolean leftPressed;

    /** Referencia al panel principal del juego para leer y modificar el estado global. */
    GamePanel gp;


    /**
     *  true durante el frame en que el jugador pulsa E.
     * Se usa para activar interacciones (entrar por puertas, iniciar peleas, etc.).
     */
    public boolean ePressed;

    /**
     * Construye el manejador de teclado vinculado al panel de juego.
     *
     * @param gp panel principal del juego
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * No se usa en este juego; requerido por la interfaz  KeyListener.
     *
     * @param e evento de tecla tipada
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Se invoca cada vez que el jugador presiona una tecla.
     *
     * Gestiona la lógica de entrada para todos los estados del juego:
     *
     *   Movimiento horizontal (A / D) en cualquier estado.
     *   Navegación y selección en los menús de título.
     *   Menú de combate y confirmación de resultados de ataque.
     *   Pausa y reanudación del juego (P).
     *   Interacción con el entorno (E).
     *   Pantalla de enhorabuena al terminar el juego.
     *
     *
     * @param e evento de tecla presionada
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); // código numérico de la tecla pulsada


        // ── Movimiento del jugador (disponible en cualquier estado) ──────────
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_A) leftPressed  = true;


        // ════════════════════════════════════════════════════════════════════
        // ESTADO: TÍTULO
        // ════════════════════════════════════════════════════════════════════

        if (gp.gameState == gp.titleState) {

            // ── Sub-pantalla 0: Menú principal (NUEVA PARTIDA / CRÉDITOS / SALIR) ──
            if (gp.ui.titleScreenState == 0) {

                // Navegar hacia arriba
                if (code == KeyEvent.VK_W) {
                    gp.ui.comandoNum--;
                    if (gp.ui.comandoNum < 0) gp.ui.comandoNum = 2; // wrap al final
                }
                // Navegar hacia abajo
                if (code == KeyEvent.VK_S) {
                    gp.ui.comandoNum++;
                    if (gp.ui.comandoNum > 2) gp.ui.comandoNum = 0; // wrap al inicio
                }
                // Confirmar selección
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.comandoNum == 0) gp.ui.titleScreenState = 1; // ir a historia
                    if (gp.ui.comandoNum == 1) { /* créditos: solo se muestra overlay */ }
                    if (gp.ui.comandoNum == 2) System.exit(0);              // salir del juego
                }

                // ── Sub-pantalla 1: Historia / introducción ──────────────────────
            } else if (gp.ui.titleScreenState == 1) {

                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    gp.gameState = gp.escenaState1; // comenzar la partida
                    gp.playMusic(0);
                }

                // ── Sub-pantalla 2: Pantalla de muerte ──────────────────────────
            } else if (gp.ui.titleScreenState == 2) {

                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    System.exit(0); // salir tras la muerte
                }
            }
        }


        // ════════════════════════════════════════════════════════════════════
        // ESTADO: COMBATE — turno del jugador (menú de acciones)
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.statePelea) {

            if (code == KeyEvent.VK_ENTER && gp.jugadorTurno) {

                if (gp.ui.subState == 0) {
                    // ── Menú principal: ATACAR o INVENTARIO ──
                    if (gp.ui.comandoNum1 == 0) {
                        gp.ui.subState = 1;          // entrar al submenú de ataques
                    }
                    if (gp.ui.comandoNum1 == 1) {
                        gp.ui.abrirInventario();      // abrir inventario (cede el turno)
                    }
                    gp.ui.comandoNum1 = 0;            // reset del cursor para el submenú

                } else if (gp.ui.subState == 1) {
                    // ── Submenú de ataques: ejecutar el tipo seleccionado ──
                    if (!gp.jugador.estoyAtacando) {  // protección contra doble input durante animación
                        gp.jugador.ejecutarAtaque(gp.ui.comandoNum1);
                        gp.ui.subState = 0;           // volver al menú principal tras atacar
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
        // ESTADO: COMBATE — confirmación de resultado de ataque
        // (se muestra el panel de información y el jugador pulsa "Continuar")
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.statePelea && !gp.isSituacionPelea) {

            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum2 == 1) {

                // Guardamos flags antes de resetearlos
                boolean eraAtaqueJugador = gp.jugador.fuejugadorAtaque;
                boolean hayContrataque   = gp.jugador.contrataquePendiente;

                // Resetear flags de turno para el siguiente ciclo
                gp.jugador.fuejugadorAtaque      = false;
                gp.samuraiErrante.fueEnemigoAtaque = false;
                gp.isSituacionPelea              = true;

                if (eraAtaqueJugador) {
                    // ── Fue el turno del jugador ──
                    if (hayContrataque) {
                        // El jugador falló → el enemigo contrataca
                        gp.jugador.contrataquePendiente          = false;
                        gp.samuraiErrante.contratacar();          // pone isSituacionPelea=false
                        gp.jugadorTurno                          = false;
                        gp.samuraiErrante.enemigoYaAtaco         = false;
                        gp.samuraiErrante.estoyAtacandoErrante   = false;
                    } else {
                        // Ataque del jugador resuelto → turno del enemigo
                        gp.jugadorTurno                          = false;
                        gp.samuraiErrante.enemigoYaAtaco         = false;
                        gp.samuraiErrante.estoyAtacandoErrante   = false;
                    }

                } else {
                    // ── Fue el turno del enemigo (o habilidad especial) ──
                    if (gp.samuraiErrante.seHaMostradoPantalla) {
                        // El jugador confirmó la pantalla de furia del samurái
                        gp.samuraiErrante.seHaMostradoPantalla   = false;
                        gp.samuraiErrante.fueEnemigoAtaque       = false;
                        gp.jugadorTurno                          = false;
                        gp.samuraiErrante.enemigoYaAtaco         = false;
                        gp.samuraiErrante.estoyAtacandoErrante   = false;
                        gp.isSituacionPelea                      = true;

                    } else if (gp.samuraiErrante.fueContrataque) {
                        // Venimos del contrataque → el enemigo hace su turno normal
                        gp.samuraiErrante.fueContrataque         = false;
                        gp.samuraiErrante.fueEnemigoAtaque       = false;
                        gp.jugadorTurno                          = false;
                        gp.samuraiErrante.enemigoYaAtaco         = false;
                        gp.samuraiErrante.estoyAtacandoErrante   = false;
                        gp.isSituacionPelea                      = true;

                    } else {
                        // Ataque del enemigo resuelto → devolver turno al jugador
                        gp.jugadorTurno                          = true;
                        gp.samuraiErrante.enemigoYaAtaco         = false;
                        gp.samuraiErrante.estoyAtacandoErrante   = false;
                    }
                }
            }
        }

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: PAUSA / REANUDAR (tecla P)
        // ════════════════════════════════════════════════════════════════════
        if (code == KeyEvent.VK_P) {

            // Escena 1
            if (gp.gameState == gp.pauseState1) {
                gp.gameState = gp.escenaState1;
                gp.playMusic(0);                    // reanudar música
            } else if (gp.gameState == gp.escenaState1) {
                gp.gameState = gp.pauseState1;
                gp.stopMusic();
            }

            // Escena 2
            if (gp.gameState == gp.pauseState2) {
                gp.gameState = gp.escenaState2;
            } else if (gp.gameState == gp.escenaState2) {
                gp.gameState = gp.pauseState2;
            }

            // Escena 3
            if (gp.gameState == gp.pauseState3) {
                gp.gameState = gp.escenaState3;
            } else if (gp.gameState == gp.escenaState3) {
                gp.gameState = gp.pauseState3;
            }
        }

        // ── Interacción con el entorno (entrar puertas, iniciar pelea, etc.) ──
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }

        // ════════════════════════════════════════════════════════════════════
        // ESTADO: ENHORABUENA (fin del juego)
        // ════════════════════════════════════════════════════════════════════
        if (gp.gameState == gp.congratulationsState) {
            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                System.exit(0);
            }
        }
    }

    /**
     * Se invoca cuando el jugador suelta una tecla.
     *
     * Desactiva los flags de movimiento continuo y de interacción puntual.
     *
     *
     * @param e evento de tecla soltada
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_A) leftPressed  = false;
        if (code == KeyEvent.VK_E) ePressed     = false; // la interacción es puntual, no continua
    }
}