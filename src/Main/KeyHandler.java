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

    /**
     * true durante el frame en que el jugador pulsa O
     * Se usa para activar opciones .
     */
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

        // -------- Movimiento del jugador (disponible en cualquier estado menos pause) ------
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_A) leftPressed  = true;


        //  ------- --ESTADO: TÍTULO ----------
        if (gp.gameState == gp.titleState) {
            // --------- Sub-pantalla 0: Menú principal ---------
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
                    //Te lleva a pantalla de introduccion
                    if (gp.ui.comandoNum == 0) gp.ui.titleScreenState = 1;

                    //Te pinta hall of fame
                    if (gp.ui.comandoNum == 1) {
                        gp.mostrarHallofFame=true;
                    }

                    //Te saca del juego
                    if (gp.ui.comandoNum == 2) System.exit(0);
                }

                // ------- Sub-pantalla 1: Historia / introducción --------
            } else if (gp.ui.titleScreenState == 1) {

                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    // Obtener la ventana principal del juego , la padre para mostrar el diálogo de introducción de nombre
                    JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(gp);

                    // Mostrar pantalla de nombre (bloqueante hasta confirmar)
                    PantallaIntroducirNombre pantallaIntroducirNombre = new PantallaIntroducirNombre(ventana);
                    pantallaIntroducirNombre.mostrar();

                    // Guardar nombre
                    gp.nombreJugador = pantallaIntroducirNombre.getNombre();
                    //Paramos musica de inicio
                    gp.stopMusic();
                    //Pasamos a escena 1
                    gp.gameState = gp.escenaState1;
                    //Empezamos musica de escena 1
                    gp.playMusic(5);
                    //Arrancar cronómetro
                    gp.cronometro.arrancar();
                }

                // -------- Sub-pantalla 2: Pantalla de muerte -----------
            } else if (gp.ui.titleScreenState == 2) {
                        //buscar cancion
                        //gp.startMusic();
                if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                    //El num no puede cambir
                    System.exit(0);
                }
            }
        }


        // ------- ESTADO: COMBATE — turno del jugador (menú de acciones) -----------

        if (gp.gameState == gp.statePelea || gp.gameState == gp.statePelea2) {
            // --------- INVENTARIO (prioridad máxima, antes que cualquier otro bloque) -------
            if (gp.inventarioAbierto) {
                if (code == KeyEvent.VK_D && !gp.inventario.objetos.isEmpty()) {
                    //Si vamos a la derecha y no esta vacio, inventario suma y ui lo recibe para cambiar el color grafico
                    gp.inventarioSlot++;
                    if (gp.inventarioSlot >= gp.inventario.objetos.size())
                        //Si pasa a un siguiente objeto y no hay nada , simplemente vuelve al principio
                        gp.inventarioSlot = 0;
                }
                if (code == KeyEvent.VK_A && !gp.inventario.objetos.isEmpty()) {
                    //Si vamos a la izquierdaa y no esta vacio, inventario resta y ui lo recibe para cambiar el color grafico
                    gp.inventarioSlot--;
                    if (gp.inventarioSlot < 0)
                        //Si pasa a un siguiente objeto , como no puede ir a negativos en el size, simplemente vuelve al final de este
                        gp.inventarioSlot = gp.inventario.objetos.size() - 1;
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (!gp.inventario.objetos.isEmpty()) {
                        //pillamos el objeto al que apunta inventarioSlot que al final es un indice
                        SuperObject obj = gp.inventario.objetos.get(gp.inventarioSlot);
                        //el objeto pillado lo usamos
                        obj.usar();
                        //luego lo eliminamos
                        gp.inventario.eliminarObjeto(gp.inventarioSlot);
                        //y reiniciamos el "cursos" que mira inventario
                        gp.inventarioSlot = 0;
                    }

                    gp.inventarioAbierto = false;
                    gp.isSituacionPelea = true; // vuelve al menú de combate
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    //Si hacemos escape una vez abierto
                    gp.inventarioAbierto = false;
                    // vuelve al menú de combate
                    gp.isSituacionPelea = true;
                }
                //hacemos que comience de nuevo por completo
                return;
            }
            //----- AQUI CONTROLAMOS SI ENTRA AL INVENTARIO O SI EJECUTA UN ATAQUE , PERO SOLO SI ESTA EN TURNO DE JUGADOR Y EN SITUACION DE PELEA (MENU DE COMBATE)  -----
            if (code == KeyEvent.VK_ENTER && gp.jugadorTurno && gp.isSituacionPelea) {

                //ATACAR/INVENTARIO O SEGURO/EQUILIBRADO/ARRIESGADO,
                if (gp.ui.subState == 0) {
                    //ATACAR
                    if (gp.ui.comandoNum1 == 0) {
                        //SEGURO/EQUILIBRADO/ARRIESGADO,
                        gp.ui.subState = 1;
                    //INVENTARIO
                    } else if (gp.ui.comandoNum1 == 1) {
                        //abrimos
                        gp.inventarioAbierto = true;
                        //Colocamos ATACAR
                        gp.ui.comandoNum1 = 0;
                    }
                //SEGURO/EQUILIBRADO/ARRIESGADO
                } else if (gp.ui.subState == 1) {
                    //Proteccion por si petamos con ataques
                    if (!gp.jugador.estoyAtacando) {
                        //le damos el comando para moverse y elegir el ataque
                        gp.jugador.ejecutarAtaque(gp.ui.comandoNum1);
                        // nos colocamos en ATAQUE/INVENTARIO
                        gp.ui.subState    = 0;

                        //reiniciamos el de movernos a ATAQUE
                        gp.ui.comandoNum1 = 0;
                    }
                }
            }

            // Volver atrás desde el submenú de ataques con ESC
            if (code == KeyEvent.VK_ESCAPE && gp.ui.subState == 1) {
                gp.ui.subState = 0;
            }

            // Navegación vertical dentro del menú de combate
            //En el menu ATAQUE/INVENTARIO tiene hasta 1 como numero maximo (0, 1) , y (SEGURO / EQUILIBRADO / ARRIESGADO) tiene hasta 2 como numero maximo (0,1 y 2 )
            int maxCommand = (gp.ui.subState == 0) ? 1 : 2;
            //Solo subes si no estás en la primera opción (0)
            if (code == KeyEvent.VK_W && gp.ui.comandoNum1 > 0) {
                gp.ui.comandoNum1--;
            }
            //Solo bajas si NO te pasas del máximo permitido
            if (code == KeyEvent.VK_S && gp.ui.comandoNum1 < maxCommand) {
                gp.ui.comandoNum1++;
            }
        }



        // ESTADO: COMBATE — confirmación de resultado
        // Usa gp.enemigoActual para ser compatible con cualquier enemigo futuro.

        if ((gp.gameState == gp.statePelea || gp.gameState == gp.statePelea2) && !gp.isSituacionPelea) {

                //Continuamos pantalla de informacion
            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum2 == 1) {

                // Alias genérico: funciona con cualquier subclase de Enemigo
                Enemigo enemigo = gp.enemigoActual;

                // Guardamos flags antes de resetearlos
                boolean eraAtaqueJugador = gp.jugador.isFueAtaque();
                boolean hayContrataque   = gp.jugador.isContrataquePendiente();

                // Resetear flags de turno para el siguiente ciclo
                gp.jugador.setFueAtaque(false);
                enemigo.setFueAtaque(false);
                gp.isSituacionPelea          = true;

                if (eraAtaqueJugador) {
                    // ── Fue el turno del jugador ──────────────────────────────
                    if (hayContrataque) {
                        // El jugador falló → el enemigo contrataca.
                        // contratacar() está definido en Enemigo base ,
                        // cada subclase puede sobreescribirlo si lo necesita.
                        gp.jugador.setContrataquePendiente(false);
                        enemigo.contratacar();
                        gp.jugadorTurno        = false;

                    } else {
                        // Ataque del jugador resuelto → turno del enemigo
                        gp.jugadorTurno        = false;
                    }

                } else {
                    // ------- Fue el turno del enemigo (o habilidad especial) -------
                    if (enemigo.seHaMostradoPantalla) {
                        // El jugador confirmó la pantalla de habilidad única , colocamos todo para que empiece su turno el enemigo despues de habilidad
                        enemigo.seHaMostradoPantalla = false;
                        enemigo.setFueAtaque(false);
                        gp.jugadorTurno              = false;
                        enemigo.enemigoYaAtaco        = false;
                        enemigo.estoyAtacando         = false;
                        gp.isSituacionPelea          = true;

                    } else if (enemigo.fueContrataque()) {
                        // Venimos del contrataque → el enemigo hace su turno normal
                        enemigo.resetContrataque();
                        enemigo.setFueAtaque(false);
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


        // ----- ESTADO: PAUSA / REANUDAR (tecla P) --------

        if (code == KeyEvent.VK_P) {
            //si le damos en pause, inciamos de la escena que es

            //si estaba jugando, paramos la musica

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

        // ------- Interacción con el entorno ---------
        //si le da, entonces se vuelve true e interactuamos
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }


        //------ ESTADO: ENHORABUENA (fin del juego)-------

        if (gp.gameState == gp.congratulationsState) {
            //Cuando llegamos a este estado, paramos el conometro que nos devuelve el calculo hecho de tiempo jugado
            long finalTime = gp.cronometro.detener();
            //Pillo la fecha de hoy con la hora
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            //Guardamos el registro con el nombre pillado al principio, el tiempo en milisegundos y la fecha formateada
            RegistroJugador registro = new RegistroJugador(gp.nombreJugador, finalTime, fecha);
            //Empezamos a guardar o sustituir en GestorXML
            GestorXml.guardar(registro);
            if (code == KeyEvent.VK_ENTER && gp.ui.comandoNum == 0) {
                gp.gameState = gp.hallOfFameState;
                return; //  evita que el bloque de hallOfFameState procese este mismo ENTER
            }
        }

        //Si hemos terminado el juego y estamos en el hall of fame donde tambien dibujamos esta pantalla, con enter salimos del juego
        if (gp.gameState == gp.hallOfFameState) {
            if (code == KeyEvent.VK_ENTER) {
                System.exit(0);
            }
        }

        //Si hemos mostrado la pantalla (dibujado) y estamos en el hall of fame, con ESC quitamos el dibujo
        if(gp.mostrarHallofFame && code==KeyEvent.VK_ESCAPE) {
            gp.mostrarHallofFame=false;
        }




        // MENÚ DE OPCIONES (tecla O)

        //Si las hemos dibujado
        if (gp.ui.dibujadoOpciones) {
            if (code == KeyEvent.VK_W) {
                //Si subimos le restamos, entonces el cursor sube , si le damos demas , entonces va al que esta mas abajo
                gp.ui.opcionesComando--;
                if (gp.ui.opcionesComando < 0) gp.ui.opcionesComando = 1;
            }
            if (code == KeyEvent.VK_S) {
                //Si restamos le sumamos, entonces el cursor baja , si le damos demas , entonces va al que esta mas arriba
                gp.ui.opcionesComando++;
                if (gp.ui.opcionesComando > 1) gp.ui.opcionesComando = 0;
            }
            //Si damos enter
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.opcionesComando == 0) {
                    // Togglear pantalla completa , es decir, lo que esta se vuelve lo contrario como un interruptor
                    gp.pantallaCompleta = !gp.pantallaCompleta;
                    if (gp.pantallaCompleta) {
                        Main.gd.setFullScreenWindow(SwingUtilities.getWindowAncestor(gp)); //lo hacemos pantalla completa y en paintcomponent, hacemos que las imagenes se adapten a esta nueva resolucion
                    } else {
                        Main.gd.setFullScreenWindow(null);//salimos del pantalla completa
                    }
                }
                //Salimos
                if (gp.ui.opcionesComando == 1) {
                    gp.ui.dibujadoOpciones = false;
                    oPressed = false;
                }
            }

            return; // bloquea el resto del input mientras opciones está abierto
        }

        //Si es true ,colocamos el dibujo true
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
        if (code == KeyEvent.VK_0) oPressed     = false;


    }
}