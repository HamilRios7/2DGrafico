package Entidad;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.clamp;

/**
 * Entidad del jugador. Gestiona su movimiento, animaciones, combate y muerte.
 *
 * Extiende Entidad para heredar los atributos base (vida, fuerza,
 * velocidad, sprites, etc.) y añade toda la lógica específica del personaje
 * controlado por el jugador a través de  KeyHandler.
 *
 *
 * El jugador tiene tres "instancias" de posición según la escena activa:
 *
 *    x1Jugador / y1Jugador — escena 1 (exterior).
 *    x2Jugador / y2Jugador — escena 2 y combate en este (interior del castillo).
 *    x3Jugador / y3Jugador — escena 3 y combate en este (piso final del castillo).
 *
 */
public class Jugador extends Entidad {

    /** Manejador de teclado para leer el input del jugador cada frame. */
    KeyHandler keyH;



    // -----Flags de la proximidad (detección de zonas de interaccion)-----


    /** true cuando el jugador está lo bastante cerca de la puerta del castillo. */
    public boolean cercaPuerta = false;

    /**  true cuando el jugador está cerca de la zona de inicio de combate. */
    public boolean cercaPelea = false;

    /** true cuando el jugador está cerca del acceso al siguiente piso final. */
    public boolean cercaIrPiso3 = false;


    /** true cuando el jugador está cerca de inicio a la pelea final. */
    public boolean cercaPeleaFinal = false;


    /** true cuando el jugador está cerca del acceso a la pantalla de felicitacion. */
    public boolean cercaCongratulations = false;

    // Posición en la escena 1 (exterior)
    int x1Jugador ;
    int y1Jugador ;

    // Posición en la escena 2 / combate (interior)
    int x2Jugador;
    int y2Jugador ;

    // Posición en la escena 3 / combate final (interior)
    int x3Jugador ;
    int y3Jugador ;


    // ----- Flags control combate y animacion -----


    /**
     *  true mientras la animación de ataque está en curso.
     * Se usa para bloquear nuevas acciones hasta que el ataque termine.
     */
    public boolean estoyAtacando = false;

    /** true cuando la vida del jugador llega a 0 y empieza la animación de muerte . */
    public boolean heMuerto = false;

    /** true cuando el último frame de la animación de muerte ha sido mostrado. */
    public boolean isAnimacionMuerteTerminada = false;


    // ----- COMBATE ------


    /**
     * Tipo de ataque seleccionado por el jugador en el menú de combate.
     *
     *   0 – Ataque débil (seguro)
     *   1 – Ataque equilibrado
     *   2 – Ataque fuerte (arriesgado)
     *   -1 – Sin ataque pendiente
     *
     */
    public int ataqueElegido = -1;


    // ----- CONSTRUCTOR ------


    /**
     * Crea el jugador, inicializa su área de colisión, sus valores por defecto
     * y carga todos los sprites desde los metodos de esta clase
     *
     * @param gp   panel principal del juego
     * @param keyH manejador de teclado
     */
    public Jugador(GamePanel gp, KeyHandler keyH) {
        this.gp   = gp;
        this.keyH = keyH;

        // Área de colisión: rectángulo de un mosaico de lado, desplazado para centrarlo
        solidArea = new Rectangle(48, 48, gp.tamañoMosaico, gp.tamañoMosaico);

        setDefaultValues();
        getPlayerImage();
    }


    // ----- INICIALIZACIÓN -------


    /**
     * Establece los valores iniciales del jugador al comenzar una nueva partida.
     *
     * Posición de inicio, velocidad, nivel, atributos de combate y vida máxima.
     * La barra de vida visual se escala con { barraVida = maxLife × 10}.
     *
     */
    public void setDefaultValues() {
        // Posición en la escena 1 (exterior)
        x1Jugador = 0;
        y1Jugador = 305;

        // Posición en la escena 2 / combate (interior)
        x2Jugador = 0;
        y2Jugador = 60;

        // Posición en la escena 3 / combate (interior)
        x3Jugador = 0;
        y3Jugador = 64;

        speed     = 5;

        //direccion a la que aparece mirando el jugador
        direction = "right";

        // ── Atributos de combate ──
        strenght         = 4;
        fuerzaPorcentaje = 1.5;
        maxLife          = 6;
        barraVida        = maxLife * 10; // 60
        life             = barraVida;
    }

    /**
     * Carga a las variables BufferedImage  todos los sprites del jugador:
     * caminar a la derecha e izquierda (8 frames cada uno), ataque (6 frames),
     * idle derecha e izquierda (5 frames cada uno) y muerte (7 frames).
     *
     */
    public void getPlayerImage() {
        try {


            //getResourceAsStream busca un archivo dentro de tu proyecto y lo abre como un flujo de datos para poder  leerlo.
            // getClass().getClassLoader() sirve para acceder a archivos dentro del proyecto, en este caso, res
            // ImageIO.read(...) lee el objeto y lo convierte en BufferedImage

            // ----- Animación caminar -> derecha ------
            rgt1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_1.png"));
            rgt2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_2.png"));
            rgt3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_3.png"));
            rgt4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_4.png"));
            rgt5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_5.png"));
            rgt6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_6.png"));
            rgt7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_7.png"));
            rgt8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_8.png"));

            // ------ Animación caminar -> izquierda -------
            lft1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_1.png"));
            lft2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_2.png"));
            lft3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_3.png"));
            lft4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_4.png"));
            lft5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_5.png"));
            lft6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_6.png"));
            lft7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_7.png"));
            lft8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_8.png"));

            // -------Animación de ataque (6 frames)------
            ataJuga1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_1.png"));
            ataJuga2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_2.png"));
            ataJuga3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_3.png"));
            ataJuga4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_4.png"));
            ataJuga5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_5.png"));
            ataJuga6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_6.png"));

            // ------- Animación idle → mirando a la derecha (5 frames) -------
            idle1Right= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1Jg.png"));
            idle2Right = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2Jg.png"));
            idle3Right = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3Jg.png"));
            idle4Right= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4Jg.png"));
            idle5Right  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5Jg.png"));

            // ------ Animación idle → mirando a la izquierda (5 frames) ------
            idle1Left= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1JgLeft.png"));
            idle2Left = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2JgLeft.png"));
            idle3Left = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3JgLeft.png"));
            idle4Left= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4JgLeft.png"));
            idle5Left= ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5JgLeft.png"));

            // -------- Animación de muerte (7 frames, no en bucle) -------
            muerto1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_1.png"));
            muerto2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_2.png"));
            muerto3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_3.png"));
            muerto4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_4.png"));
            muerto5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_5.png"));
            muerto6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_6.png"));
            muerto7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_7.png"));

        } catch (IOException e) {
            System.err.println("[Jugador] Error cargando sprites del jugador:");
            e.printStackTrace();
        }
    }


    // -----  Update de escenas (es llamado cada frame por GamePanel) ------


    /**
     * Lógica de actualización del jugador en la escena 1 (exterior).
     *
     * Mueve al jugador horizontalmente según el input de teclado,
     * lo mantiene dentro de los límites de pantalla con  clamp
     * y selecciona la animación correcta (movimiento o idle).
     */
    public void update1() {

        //es true si el jugador está pulsando alguna de las teclas de movimiento horizontal
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        //para que en combate no puedas moverte
            if (moviendo) {

                //si es presionado derecha , la velocidad por frame se le suma a la posicion del jugador
                if (keyH.rightPressed) {
                    direction   = "right";
                    x1Jugador  += speed;
                }
                //si es presionado izquierda , la velocidad por frame se le resta a la posicion del jugador
                if (keyH.leftPressed) {
                    direction   = "left";
                    x1Jugador  -= speed;
                }

                // Evita que el jugador salga de los bordes de la pantalla
                //clamp fuerza un valor a estar dentro de un rango, si sale del minimo -> coloca minimo , si pasa maximo -> coloca maximo
                x1Jugador = clamp(x1Jugador, -54, gp.pantallaAnchura - 70);

                //iniciamos la animacion movimiento al moviendo=true
                animacionMoviendome();
            } else {
                animacionQuieto();
            }
    }

    /**
     * Lógica de actualización del jugador en la escena 2 y combate.
     *
     * Si el jugador ha muerto, delega en  #animacionMuerte() y corta
     * el resto de la lógica. En la escena 2 aplica movimiento con velocidad 6
     * (ligeramente mayor que en escena 1) y sus propios límites de pantalla.
     * En el estado de pelea solo se actualiza la animación idle.
     *
     */
    public void update2() {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        // Si ha muerto, solo reproducimos la animación y bloqueamos todo lo demás
        if (heMuerto) {
            animacionMuerte();
            return;
        }

        if (gp.gameState == gp.escenaState2) {

            if (moviendo) {

                if (keyH.rightPressed) {
                    direction   = "right";
                    x2Jugador  += 6;
                }
                if (keyH.leftPressed) {
                    direction   = "left";
                    x2Jugador  -= 6;
                }

                if (!gp.samuraiErrante.heMuertoEnemigo){
                    // Límites de la escena 2 (distintos a los de escena 1 por el diseño y componentes de la escena)
                    x2Jugador = clamp(x2Jugador, -170, gp.pantallaAnchura - 800);
                }
                if(gp.samuraiErrante.heMuertoEnemigo){
                    x2Jugador = clamp(x2Jugador, -170, gp.pantallaAnchura - 195);
                }


                animacionMoviendome();
            } else {
                animacionQuieto();
            }

        }

        if (gp.gameState == gp.statePelea && !heMuerto) {
            // Durante el combate el jugador está quieto esperando su turno , entonces hace animacion
            animacionQuieto();
        }
    }


    /**
     * Lógica de actualización del jugador en la escena 2 y combate.
     *
     * Si el jugador ha muerto, delega en  #animacionMuerte() y corta
     * el resto de la lógica. En la escena 2 aplica movimiento con velocidad 6
     * (ligeramente mayor que en escena 1) y sus propios límites de pantalla.
     * En el estado de pelea solo se actualiza la animación idle.
     *
     */
    public void update3() {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        // Si ha muerto, solo reproducimos la animación y bloqueamos todo lo demás
        if (heMuerto) {
            animacionMuerte();
            return;
        }


        if (gp.gameState == gp.escenaState3) {
            if (moviendo) {
                if (keyH.rightPressed) {
                    direction = "right";
                    x3Jugador += 6;
                }

                if (keyH.leftPressed) {
                    direction = "left";
                    x3Jugador -= 6;
                }

                if (!gp.cofreAparecido){
                    // Límites de la escena 3 (distintos a los de escena 1 por el layout)
                    x3Jugador = clamp(x3Jugador, -170, gp.pantallaAnchura - 800);
                }
                if(gp.cofreAparecido){
                    x3Jugador = clamp(x3Jugador, -170, gp.pantallaAnchura - 970);
                }
                animacionMoviendome();
            } else {
                animacionQuieto();
            }

        } else if (gp.gameState == gp.statePelea2 && !heMuerto) {
            // Durante el combate el jugador está quieto esperando su turno
            animacionQuieto();
        }
    }


    // ----- DRAW ----- (llamado cada frame desde GamePanel)


    /**
     * Dibuja el jugador en la escena 1 con su animación de movimiento o idle.
     *
     * @param gd2 contexto gráfico 2D del frame actual
     */
    public void draw1(Graphics2D gd2) {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        BufferedImage image;
        if (moviendo) {
            image = (BufferedImage) dibujarMoviendome();
        } else {
            image = (BufferedImage) dibujarQuieto();
        }
        gd2.drawImage(image, x1Jugador, y1Jugador, 130,130, null);
    }

    /**
     * Dibuja el jugador en la escena 2 y durante el combate.
     *
     * Selecciona el sprite según el estado actual:
     *
     *   Escena 2 con movimiento -> animación de caminar.
     *   Escena 2 quieto -> animación idle.
     *   Combate atacando -> animación de ataque.
     *   Combate quieto (vivo) -> animación idle.
     *   Muerto -> animación de muerte (no en bucle).
     *
     *
     * @param gd2 contexto gráfico 2D del frame actual
     */
    public void draw2(Graphics2D gd2) {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        BufferedImage image = null;

        //si estamos en escena 2 o en pausa de escena 2, se dibuja el sprite de movimiento o idle según el input del jugador
        //al estar en pause, nunca se podra mover entonces sera un idle el dibujo sin update
        if (gp.gameState == gp.escenaState2 || gp.gameState== gp.pauseState2 ) {
            if(moviendo){
                //cargo en image que esta inicializado
                image=(BufferedImage)dibujarMoviendome();
            }else {
                image=(BufferedImage) dibujarQuieto();
            }
        } else if (gp.gameState == gp.statePelea && estoyAtacando) {
            image = (BufferedImage) dibujarAtaque();

        } else if (gp.gameState == gp.statePelea && !heMuerto) {
            image = (BufferedImage) dibujarQuieto();

        } else if (heMuerto) {
            image = (BufferedImage) dibujarMuerte();
        }

        // En la escena 2 y combate el sprite se dibuja más grande (400×400)
        gd2.drawImage(image, x2Jugador, y2Jugador, 400, 400, null);
    }

    /**
     * Dibuja el jugador en la escena 3 y durante el combate.
     *
     * Selecciona el sprite según el estado actual:
     *
     *   Escena 3 con movimiento -> animación de caminar.
     *   Escena 3 quieto -> animación idle.
     *   Combate atacando -> animación de ataque.
     *   Combate quieto (vivo) -> animación idle.
     *   Muerto -> animación de muerte (no en bucle).
     *
     *
     * @param gd2 contexto gráfico 2D del frame actual
     */
    public void draw3(Graphics2D gd2) {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        BufferedImage image = null;


        //si estamos en escena 3 o en pausa de escena 3, se dibuja el sprite de movimiento o idle según el input del jugador
        //al estar en pause, nunca se podra mover entonces sera un idle el dibujo sin update
        if (gp.gameState == gp.escenaState3 || gp.gameState== gp.pauseState3) {
            if(moviendo){
                image=(BufferedImage)dibujarMoviendome();
            }else {
                image=(BufferedImage) dibujarQuieto();
            }
        } else if (gp.gameState == gp.statePelea2 && estoyAtacando) {
            image = (BufferedImage) dibujarAtaque();

        } else if (gp.gameState == gp.statePelea2 && !heMuerto) {
            image = (BufferedImage) dibujarQuieto();

        } else if (heMuerto) {
            image = (BufferedImage) dibujarMuerte();
        }

        // En la escena 3 y combate el sprite se dibuja más grande (400×400)
        gd2.drawImage(image, x3Jugador, y3Jugador, 400, 400, null);
    }


    // ------ CONTROLADORES DE ANIMACIÓN ------


    /**
     * Avanza el contador de la animación de movimiento.
     * Cambia de fotograma cada 10 frames ; cicla entre sprite 1  y 8.
     */
    public void animacionMoviendome() {
        moveCounter++;
        if (moveCounter > 10) {
            moveNum++;
            moveCounter = 0;
            if (moveNum > 8) moveNum = 1;
        }
    }

    /**
     * Avanza el contador de la animación idle (en reposo).
     * Cambia de fotograma cada 10 frames; cicla entre sprite 1 y  sprite 5.
     */
    public void animacionQuieto() {
        idleCounter++;
        if (idleCounter > 10) {
            idleNum++;
            idleCounter = 0;
            if (idleNum > 5) idleNum = 1;
        }
    }

    /**
     * Avanza el contador de la animación de ataque.
     * Cambia de fotograma cada 6 frames; cicla entre sprite  1 y  sprite 6.
     * Al completar un ciclo completo incrementa  contadorMaxFrames,
     * que sirve como señal de que la animación ha terminado.
     */
    public void animacionAtaque() {
        atacarCounter++;
        if (atacarCounter > 6) {
            atacarNum++;
            atacarCounter = 0;
            if(atacarNum==4){
                gp.playSE(6);
            }
            if (atacarNum > 6) {
                atacarNum = 1;
                contadorMaxFrames++; // señal de fin de animación de ataque
            }
        }
    }

    /**
     * Avanza el contador de la animación de muerte.
     * Cambia de fotograma cada 14 frames ; se detiene en el sprite 7
     * (no es bucle, el último frame queda congelado).
     */
    public void animacionMuerte() {
        muerteCounter++;
        if (muerteCounter > 14) {
            muerteNum++;
            muerteCounter = 0;
            if(atacarNum==4){
                gp.playSE(8);
            }
            if (muerteNum > 7) muerteNum = 7; // congela en el último frame
        }
    }


    // -----  Seleccion de Sprite segun numero animacion -----


    /**
     * Devuelve el sprite de idle correspondiente a  idleNum
     * y a la dirección actual del jugador.
     *
     * @return sprite idle actual, o  null si  idleNum está fuera de rango
     */
    public Image dibujarQuieto() {
        BufferedImage image = null;
        switch (direction) {
            case "right":
                if (idleNum == 1) image = idle1Right;
                if (idleNum == 2) image = idle2Right;
                if (idleNum == 3) image = idle3Right;
                if (idleNum == 4) image = idle4Right;
                if (idleNum == 5) image = idle5Right;
                break;
            case "left":
                if (idleNum == 1) image = idle1Left;
                if (idleNum == 2) image = idle2Left;
                if (idleNum == 3) image = idle3Left;
                if (idleNum == 4) image = idle4Left;
                if (idleNum == 5) image = idle5Left;
                break;
        }
        return image;
    }

    /**
     * Devuelve el sprite de movimiento correspondiente a  moveNum
     * y a la dirección actual del jugador.
     *
     * @return sprite de caminar actual, o  null si  moveNum está fuera de rango
     */
    public Image dibujarMoviendome() {
        BufferedImage image = null;

        switch (direction) {
            case "right":
                if (moveNum == 1) image = rgt1;
                if (moveNum == 2) image = rgt2;
                if (moveNum == 3) image = rgt3;
                if (moveNum == 4) image = rgt4;
                if (moveNum == 5) image = rgt5;
                if (moveNum == 6) image = rgt6;
                if (moveNum == 7) image = rgt7;
                if (moveNum == 8) image = rgt8;
                break;

            case "left":
                if (moveNum == 1) image = lft1;
                if (moveNum == 2) image = lft2;
                if (moveNum == 3) image = lft3;
                if (moveNum == 4) image = lft4;
                if (moveNum == 5) image = lft5;
                if (moveNum == 6) image = lft6;
                if (moveNum == 7) image = lft7;
                if (moveNum == 8) image = lft8;
                break;
        }

        return image;
    }

    /**
     * Devuelve el sprite  de ataque correspondiente a  atacarNum.
     *
     * @return sprite de ataque actual, o null si  atacarNum está fuera de rango
     */
    public Image dibujarAtaque() {
        BufferedImage image = null;
        switch (atacarNum) {
            case 1: image = ataJuga1; break;
            case 2: image = ataJuga2; break;
            case 3: image = ataJuga3; break;
            case 4: image = ataJuga4; break;
            case 5: image = ataJuga5; break;
            case 6: image = ataJuga6; break;
        }
        return image;
    }

    /**
     * Devuelve el sprite de muerte correspondiente a muerteNum.
     * El frame 7 queda congelado hasta que la escena cambie.
     *
     * @return sprite de muerte actual, o  null si  muerteNum está fuera de rango
     */
    public Image dibujarMuerte() {
        BufferedImage image = null;
        switch (muerteNum) {
            case 1: image = muerto1; break;
            case 2: image = muerto2; break;
            case 3: image = muerto3; break;
            case 4: image = muerto4; break;
            case 5: image = muerto5; break;
            case 6: image = muerto6; break;
            case 7: image = muerto7; break;
        }
        return image;
    }



    // COMBATE


    /**
     * Inicia la secuencia de ataque del jugador.
     *
     * Solo tiene efecto si no hay ya un ataque en curso ( estoyAtacando == false).
     * Guarda el tipo de ataque elegido en ataqueElegido sin ejecutarlo todavía,
     * de modo que el daño se calcula cuando la animación arranca en el siguiente frame,
     * evitando saltos visuales.
     *
     *
     * @param comando tipo de ataque seleccionado (0 = débil, 1 = equilibrado, 2 = fuerte)
     */
    public void ejecutarAtaque(int comando) {
        if (!estoyAtacando) {
            contadorMaxFrames = 0;
            estoyAtacando     = true;
            ataqueElegido     = comando; // el daño se aplica cuando arranque la animación
        }
    }

    /**
     * A traves de herencia , llama al metodo de Entidad ataqueSeguro,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueSeguro() {
        super.ataqueSeguro(gp.enemigoActual);
    }

    /**
     A traves de herencia , llama al metodo de Entidad ataqueEquilibrado,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueEquilibrado() {
        super.ataqueEquilibrado(gp.enemigoActual);
    }

    /**
     A traves de herencia , llama al metodo de Entidad ataqueArriesgado,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueArriesgado() {
        super.ataqueArriesgado(gp.enemigoActual);
    }


    // ------ COMPROBACIONES DE FIN DE ANIMACIÓN ----------


    /**
     * Indica si la animación de ataque ha completado al menos un ciclo completo.
     *
     * @return true si contadorMaxFrames ≥ 1
     */
    public boolean animacionAtaqueTerminada() {
        return contadorMaxFrames >= 1;
    }

    /**
     * Indica si la animación de muerte ha llegado al frame final (frame 7).
     *
     * @return  true si muerteNum == 7
     */
    public boolean animacionMuerteTerminada() {
        return muerteNum == 7;
    }



    // ÁREAS DE INTERACCIÓN (hitboxes de jugador )

    /**
     * Devuelve el hitbox frontal del jugador en la escena 1.
     *
     * Es una línea horizontal de 75 px de ancho y 1 px de alto,
     * usada para detectar proximidad a puertas u objetos en el eje X.
     *
     *
     *  Como solo usamos realmente el eje x, no importa la altura que tenga el rectangulo
     *
     * @return rectángulo de detección en la escena 1
     *
     */
    public Rectangle getBorde1() {
        return new Rectangle(x1Jugador, y1Jugador, 75, 1);
    }

    /**
     * Devuelve el hitbox frontal del jugador en la escena 2.
     *
     * Es una línea horizontal de 70 px de ancho y 1 px de alto,
     * usada para detectar proximidad a zonas de pelea o escaleras.
     *
     *  Como solo usamos realmente el eje x, no importa la altura que tenga el rectangulo
     *
     * @return rectángulo de detección en la escena 2
     */
    public Rectangle getBorde2() {
        return new Rectangle(x2Jugador, y2Jugador, 70, 1);
    }

    /**
     * Devuelve el hitbox frontal del jugador en la escena 2.
     *
     * Es una línea horizontal de 20 px de ancho y 1 px de alto,
     * usada para detectar proximidad a zonas de pelea o escaleras.
     *
     *  Como solo usamos realmente el eje x, no importa la altura que tenga el rectangulo
     *
     * @return rectángulo de detección en la escena 3
     */
    public Rectangle getBorde3() {
        return new Rectangle(x3Jugador, y3Jugador, 20, 1);
    }


    //MOVIMIENTO ESTADOS

    /**
     * Mueve al jugador a la posición de inicio del combate en la escena 2 , dejamos que se quede viendo a la derecha aunque este mirando
     * a la izquierda e interactue.
     *
     * @return rectángulo de detección en la escena 3
     */
    public void moverPelea1(){
        gp.gameState = gp.statePelea;
        direction = "right";
        cercaPuerta = false;
        x2Jugador = 130;
    }

    /**
     * Mueve al jugador a la posición de inicio del combate en la escena 3 ,  dejamos que se quede viendo a la derecha aunque este mirando
     * a la izquierda e interactue.
     * @return rectángulo de detección en la escena 3
     */
    public void moverPelea2(){
        gp.gameState = gp.statePelea2;
        direction = "right";
        cercaPuerta = false;
        x3Jugador = 130;
    }
}