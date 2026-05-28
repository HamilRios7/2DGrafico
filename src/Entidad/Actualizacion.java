package Entidad;

import Main.GamePanel;
import Objetos.Obj_PocionFuerza;
import Objetos.Obj_PocionVida;

/**
 *
 *
 * Clase que se encargara de gestionar toda la lógica de actualización del combate, así como las transiciones entre escenas y las transiciones entre escena y estado combate.
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
     *   Jugador elige ataque -> solo se guarda ataqueElegido
     *   Animación del jugador se reproduce
     *   Al terminar la animación -> se calcula el daño -> isSituacionPelea=false
     *   Si situacion pelea =false -> UI muestra resultado → jugador pulsa Enter (gestionado en KeyHandler)
     *   Si fallé ataque -> contrataque Enemigo -> muestra pantalla → Enter -> situacion pelea=true -> turno normal del enemigo
     *   Si acerté -> turno del enemigo directamente
     *   Posible pantalla de habilidad única antes de turno enemigo si se cumple condicion -> Enter -> animación de enemigo de ataque
     *   -> cálculo de daño y muestra pantalla -> Enter -> situacion pelea = true ->turno jugador
     *
     * NOTA: gp.enemigoActual debe estar asignado ANTES de entrar al estado statePelea.
     *   gp.enemigoActual = gp.samuraiErrante;  // combate normal
     *   gp.enemigoActual = gp.gigante;          // combate boss
     */
    public void actualizacionSistemaCombate() {
        // gp.enemigoActual ya viene asignado desde Actualizacion.actualizacionMoverEscena*
        // NO se sobreescribe aquí para que funcione con cualquier enemigo.

        Enemigo e = gp.enemigoActual; // alias local para no repetir

        //dejamos que se actualice para que se mueva cuando esta quieto
        e.updateEnemigo();

        //si estamos en escena pelea samurai o escena pelea boss se actualizara con ubicacion distinta
        if(gp.gameState == gp.statePelea) gp.jugador.update2();
        if(gp.gameState == gp.statePelea2) gp.jugador.update3();

        // --- Comprobar muerte del jugador ---
        if (gp.jugador.getLife() <= 0) {
            gp.jugador.heMuerto = true;
        }
        //Si es true, empezamos animacion
        if (gp.jugador.heMuerto) {
            gp.jugador.animacionMuerte();
            //Comprobamoso cuando la animacion termina ,si termina  entonces mostramos pantalla de muerte  ,paramos musica y cambiamos pantalla
            if (gp.jugador.animacionMuerteTerminada()) {
                gp.stopMusic();
                gp.jugador.isAnimacionMuerteTerminada = true;
                gp.gameState = gp.titleState;
            }
        }

        //---Comprobar muerte del enemigo -----
        if (e.getLife() <= 0) {
            e.heMuertoEnemigo = true;
            //aseguramos que el jugador no pueda hacer nada cuando el enemigo muera
            gp.jugadorTurno = false;
        }
        //si ha muerto , reiniciamos flags y empezamos muerte animacion
        if (e.heMuertoEnemigo) {
            e.estoyAtacando = false;
            e.enemigoYaAtaco = false;
            e.animacionMuerte();
            //si termina entonces dejamos ambas flags en true, isPeleaFinalizada nos servira para controlar futuras interacciones
            if (e.animacionMuerteTerminada()) {
                gp.ispeleaFinalizada = true;
                e.isAnimacionMuerteTerminadaEnemigo = true;

                // ------Drop aleatorio -----
                // 20% no dropea nada, objetoDropeado queda null
                if (e instanceof samuraiErrante) {
                    // hacemos el drop solo para samurai , generamos un numero del 0 al 99 y dependiendo del resultado dropeamos un objeto u otro
                     int rand = new java.util.Random().nextInt(100);
                    if (rand < 60) {
                        // si es menor que 60, dropeamos vida y ya no entra al else if
                        gp.objetoDropeado = new Obj_PocionVida(gp);
                    } else if (rand < 85) {
                        //si ha pasado primer if y es menor que 85, dropeamos fuerza
                        gp.objetoDropeado = new Obj_PocionFuerza(gp);
                    }
                    //si es mayor que 85 , entonces dropea nada ,es null

                    //colocamos lugar de drop, paramos musica  pelea, nos colocamos en la escena  de antes y iniciamos la otra cancion
                    gp.dropX = 500;
                    gp.dropY = 420;
                    gp.stopMusic();
                    gp.gameState = gp.escenaState2;
                    gp.playMusic(0);

                } else if (e instanceof Gigante) {
                    // Gigante no dropea nada , si muere para la musica anterior, cambia a escena antes de la pelea y iniciamos nueva musica
                    gp.stopMusic();
                    gp.gameState = gp.escenaState3;
                    gp.playMusic(4);
                }
            }
        }

        // -------- Turno del jugador --------

        //si no ha muerto y es su turno
        if (gp.jugadorTurno && !gp.jugador.heMuerto) {
            //cuando seleccionamos ataque , estoyatacando se vuelve true
            if (gp.jugador.estoyAtacando) {
                //iniciamos ataque animacion y una vez que termina ejecutamos la logica de calculo de vida y daño
                gp.jugador.animacionAtaque();

                if (gp.jugador.animacionAtaqueTerminada()) {
                    gp.jugador.estoyAtacando = false;
                    if (gp.jugador.ataqueElegido == 0) gp.jugador.ataqueSeguro();
                    if (gp.jugador.ataqueElegido == 1) gp.jugador.ataqueEquilibrado();
                    if (gp.jugador.ataqueElegido == 2) gp.jugador.ataqueArriesgado();
                    gp.jugador.ataqueElegido = -1;
                    //reiniciamos ambas flechas del menu de elegir ataque y elegir tipo ataque o inventario
                    gp.ui.comandoNum1 = 0; //
                    gp.ui.subState    = 0; //

                    //si nos hemos tomado una pocion ,al no consumir turno , solo durara ese primer ataque
                    if (gp.fuerzaActiva) {
                        gp.jugador.strenght -= 5;
                        gp.fuerzaActiva = false;
                    }
                }
            }
        }

        // ------- Turno del enemigo --------
        if (!gp.jugadorTurno && !e.heMuertoEnemigo && gp.isSituacionPelea && !gp.inventarioAbierto) {

            //ponemos una proteccion para que no pueda realizar multiples ataques en un turno
            if (!e.enemigoYaAtaco) {

                // Comprobamos habilidad única antes de arrancar la animación , asi se activa antes de hacer nada
                if (!e.isHabilidadActivada && e.getLife() <= (e.getBarraVida() / 2)) {


                    //situacion pelea =false al activar la habilidad , seHaMostradoPantalla asi puede mostrar en pantalla en concreto la informacion de habilidad
                    e.activarHabilidadUnica();

                    //le colocamos que ya ataco
                    e.enemigoYaAtaco = true;

                    //cuando hace el return , se muestra la pantalla de la habilidad unica, que colocara enemigoYaAtaco a false de nuevo, de esta manera no ataca
                    //hasta que el jugador pase la informacion
                    return;
                }

                // Sin habilidad , entonces arrancar animación de ataque normal, colocamos la animacion de ataque a 1 para que empiece
                // desde el principio y colocamos enemigoYaAtaco a true para que no
                // pueda volver a atacar hasta que termine la animacion
                e.enemigoYaAtaco = true;
                e.estoyAtacando = true;
                e.contadorMaxFrames = 0;
                e.atacarNum = 1;
            }

            //activamos al animacion
            if (e.estoyAtacando) {
                e.animacionAtacar();


                if (e.animacionAtaqueTerminada()) {
                    //cuando termina , lo ponemos en false y calcula el daño con el tipo de ataque que quiere
                    e.estoyAtacando = false;
                    e.actuar();
                    //nos aseguramos que cuando vuelva a su turno este todo en su sitio
                    gp.ui.comandoNum1 = 0;
                    gp.ui.subState    = 0;
                }
            }
        }
    }

    /**
     * Comprueba si el jugador está en la zona de la puerta de la escena 1
     * y gestiona la transición a la escena 2 al pulsar E.
     */
    public void actualizacionIrEscena2() {

        //revisa si ambas hitboxs se tocan
        if (gp.cChecker.checkerCambioEscena(gp.jugador, new campoPuerta(gp))) {
            //colocamos que esta en zona para interacctuar
            gp.jugador.cercaPuerta = true;
            if ( gp.keyH.ePressed) {

                //cuando interactura, colocamos al samurai errante como enemigo actual en game panel y cambios escena
                gp.stopMusic();
                gp.enemigoActual = gp.samuraiErrante;
                gp.gameState = gp.escenaState2;

                //lo colocamos en false e iniciamos musica de la otra escena
                gp.jugador.cercaPuerta = false;
                gp.playMusic(0);

            }
        } else {
            //si se va de la zona o no esta cerca, entonces no podra interactuar
            gp.jugador.cercaPuerta = false;
        }
    }

    /**
     * Comprueba si el jugador está en la zona de interacción de pelea
     * y gestiona la transición al estado de combate al pulsar E.
     * Solo funciona si la pelea no ha sido ya finalizada al morir samurai
     */
    public void actualizacionEmpezarPelea1() {
        //revisa si ambas hitboxs se tocan
        if (gp.cChecker.checkerEstadoPelea(gp.jugador, new campoPeleaInteraccion(gp))) {
            //colocamos que esta en zona para interacctuar
            gp.jugador.cercaPelea = true;
            if ( gp.keyH.ePressed && !gp.ispeleaFinalizada) {

                //si inicia, paramos musica , quitamos el false , colocamos que puede empezar a pelear y el turno del jugador e inicimos el update del enemigo
                //ademas lo movemos a una posicion concreta para que no se quede bloqueado en la puerta y se vea bien la animacion de pelea
                gp.stopMusic();
                gp.jugador.cercaPelea = false;
                gp.jugadorTurno = true;
                gp.isSituacionPelea = true;
                gp.jugador.moverPelea1();
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
            if ( gp.keyH.ePressed && !gp.ispeleaFinalizada) {

                //como precaucion , reiniciamos flags del enemigo pelea a su base
                gp.stopMusic();
                gp.jugador.cercaPeleaFinal = false;
                gp.jugadorTurno = true;
                gp.isSituacionPelea = true;
                gp.enemigoActual = gp.gigante;
                gp.gigante.heMuertoEnemigo = false;
                gp.gigante.enemigoYaAtaco = false;
                gp.gigante.estoyAtacando = false;
                gp.gigante.contadorMaxFrames = 0;
                gp.jugador.moverPelea2();
                gp.playMusic(3);
            }
        } else {
            gp.jugador.cercaPeleaFinal = false;
        }
    }


    /**
     * Comprueba si el jugador está en la zona para ir a escena 3
     * Solo funciona si la pelea ha sido finalizada.
     */
    public void actualizacionIrEscena3() {
        if (gp.cChecker.checkerCambioPantallaEscena3(gp.jugador, new campoIntercaccionEscena3(gp))) {
            gp.jugador.cercaIrPiso3 = true;
            if ( gp.keyH.ePressed && gp.ispeleaFinalizada) {
                //actualizamos variables cuando interactuamos para la siguiente escena
                gp.jugador.cercaIrPiso3 = false;
                gp.enemigoActual = gp.gigante;
                gp.gameState = gp.escenaState3;
                gp.ispeleaFinalizada = false;
            }
        } else {
            gp.jugador.cercaIrPiso3 = false;
        }
    }


    /**
     * Comprueba si el jugador está en la zona para ir a la pantalla de enhorabuena
     * Solo funciona si la pelea ha sido finalizada.
     */
    public void actualizacionMostrarEscenaCongratulations() {
        if (gp.cChecker.checkerCambioPantallaEnhorabuena(gp.jugador, new campoEnhorabuenaInteraccion(gp))) {
            gp.jugador.cercaCongratulations = true;
            if (gp.keyH.ePressed && gp.ispeleaFinalizada) {
                //cambiamos la pantalla a la de felicitaciones
                gp.jugador.cercaCongratulations = false;
                //al terminar la pelea , colocamos que el juego ha terminado para mostrar mensaje de salir en pantalla de hall of fame
                gp.juegoTerminado = true;

                //al terminar la pelea , colocamos que el juego ha terminado para mostrar mensaje de salir en pantalla de hall of fame
                gp.gameState = gp.congratulationsState;
            }
        } else {
            gp.jugador.cercaCongratulations = false;
        }
    }

    /**
     * Comprueba si el jugador pasa por las pociones y si lo hace , las pilla y las guarda en el inventario
     * Solo funciona si la pelea ha sido finalizada.
     */
    public void actualizacionRecogerDrop() {
        if (gp.objetoDropeado == null) return;

        int jugadorX=0;

        //calculamos donde se va a colocar en cada escena
        if (gp.gameState == gp.escenaState2) {
            jugadorX = gp.jugador.x2Jugador + 200; // centro del sprite 400px
        }


        //calculamos la diferencia entre  la posicion del jugador y el drop
        int distX = Math.abs(jugadorX - gp.dropX);

        //si la diferencia llega a ser menor que 60 , entonces se recoge el objeto , se añade al inventario y se reinician variables
        if (distX < 60) {
            gp.inventario.añadirObjeto(gp.objetoDropeado);
            gp.objetoDropeado = null;
            gp.inventarioSlot = 0;
        }
    }
}