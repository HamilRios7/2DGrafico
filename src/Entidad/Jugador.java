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
 * El jugador tiene dos "instancias" de posición según la escena activa:
 *
 *    x1Jugador / y1Jugador — escena 1 (exterior, scroll lateral).
 *    x2Jugador / y2Jugador — escena 2 y combate (interior del castillo).
 *
 */
public class Jugador extends Entidad {

    /** Manejador de teclado para leer el input del jugador cada frame. */
    KeyHandler keyH;

    /** Ancho en píxeles con el que se dibuja el sprite del jugador en la escena 1. */
    int playerWidth = 130;

    // ════════════════════════════════════════════════════════════════════════
    // FLAGS DE PROXIMIDAD (detección de zonas de interés)
    // ════════════════════════════════════════════════════════════════════════

    /** true cuando el jugador está lo bastante cerca de la puerta del castillo. */
    public boolean cercaPuerta = false;

    /**  true cuando el jugador está cerca de la zona de inicio de combate. */
    public boolean cercaPelea = false;

    /** true cuando el jugador está cerca del acceso al siguiente piso. */
    public boolean cercaIrPiso3 = false;

    // ════════════════════════════════════════════════════════════════════════
    // FLAGS DE ESTADO DE ANIMACIÓN
    // ════════════════════════════════════════════════════════════════════════

    /**
     *  true mientras la animación de ataque está en curso.
     * Se usa para bloquear nuevas acciones hasta que el ataque termine.
     */
    public boolean estoyAtacando = false;

    /** true cuando la vida del jugador llega a 0 y empieza la animación de muerte. */
    public boolean heMuerto = false;

    /** true cuando el último frame de la animación de muerte ha sido mostrado. */
    public boolean isAnimacionMuerteTerminada = false;

    // ════════════════════════════════════════════════════════════════════════
    // COMBATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Tipo de ataque seleccionado por el jugador en el menú de combate.
     *
     *   0 – Ataque débil (seguro)</li>
     *   1 – Ataque equilibrado</li>
     *   2 – Ataque fuerte (arriesgado)</li>
     *   -1 – Sin ataque pendiente</li>
     *
     */
    public int ataqueElegido = -1;

    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Crea el jugador, inicializa su área de colisión, sus valores por defecto
     * y carga todos los sprites desde los recursos del proyecto.
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

    // ════════════════════════════════════════════════════════════════════════
    // INICIALIZACIÓN
    // ════════════════════════════════════════════════════════════════════════

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
        direction = "right";

        // ── Atributos de combate ──
        level            = 1;
        strenght         = 3;    // fuerza base
        fuerzaPorcentaje = 0.4;  // multiplicador aplicado al daño

        // ── Vida ──
        maxLife  = 3;
        barraVida = maxLife * 10; // escala visual de la barra (30 px de barra = vida llena)
        life      = barraVida;    // comienza con la vida al máximo
    }

    /**
     * Carga desde el classpath todos los sprites del jugador:
     * caminar a la derecha e izquierda (8 frames cada uno), ataque (6 frames),
     * idle derecha e izquierda (5 frames cada uno) y muerte (7 frames).
     *
     * Se usa  getClassLoader().getResourceAsStream() para compatibilidad
     * tanto al ejecutar desde el IDE como desde un JAR empaquetado.</p>
     */
    public void getPlayerImage() {
        try {
            // ── Animación caminar → derecha ──────────────────────────────────
            rgt1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_1.png"));
            rgt2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_2.png"));
            rgt3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_3.png"));
            rgt4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_4.png"));
            rgt5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_5.png"));
            rgt6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_6.png"));
            rgt7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_7.png"));
            rgt8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/right_8.png"));

            // ── Animación caminar → izquierda ────────────────────────────────
            lft1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_1.png"));
            lft2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_2.png"));
            lft3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_3.png"));
            lft4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_4.png"));
            lft5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_5.png"));
            lft6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_6.png"));
            lft7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_7.png"));
            lft8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/left_8.png"));

            // ── Animación de ataque (6 frames) ──────────────────────────────
            ataJuga1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_1.png"));
            ataJuga2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_2.png"));
            ataJuga3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_3.png"));
            ataJuga4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_4.png"));
            ataJuga5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_5.png"));
            ataJuga6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/atacar_6.png"));

            // ── Animación idle → mirando a la derecha (5 frames) ────────────
            quieto1JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1Jg.png"));
            quieto2JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2Jg.png"));
            quieto3JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3Jg.png"));
            quieto4JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4Jg.png"));
            quieto5JugaRight = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5Jg.png"));

            // ── Animación idle → mirando a la izquierda (5 frames) ──────────
            quieto1JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_1JgLeft.png"));
            quieto2JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_2JgLeft.png"));
            quieto3JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_3JgLeft.png"));
            quieto4JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_4JgLeft.png"));
            quieto5JugaLeft = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/quieto_5JgLeft.png"));

            // ── Animación de muerte (7 frames, no en bucle) ─────────────────
            muerto_1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_1.png"));
            muerto_2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_2.png"));
            muerto_3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_3.png"));
            muerto_4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_4.png"));
            muerto_5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_5.png"));
            muerto_6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_6.png"));
            muerto_7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("jugador/muerte_7.png"));

        } catch (IOException e) {
            System.err.println("[Jugador] Error cargando sprites del jugador:");
            e.printStackTrace();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // ACTUALIZACIÓN DE LÓGICA (llamada cada frame desde GamePanel)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Lógica de actualización del jugador en la escena 1 (exterior).
     *
     * Mueve al jugador horizontalmente según el input de teclado,
     * lo mantiene dentro de los límites de pantalla con {@code clamp}
     * y selecciona la animación correcta (movimiento o idle).
     *
     */
    public void update1() {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;

        if (gp.gameState == gp.escenaState1) {
            if (moviendo) {
                if (keyH.rightPressed) {
                    direction   = "right";
                    x1Jugador  += speed;
                }
                if (keyH.leftPressed) {
                    direction   = "left";
                    x1Jugador  -= speed;
                }

                // Evita que el jugador salga de los bordes de la pantalla
                x1Jugador = clamp(x1Jugador, -54, gp.pantallaAnchura - 70);

                animacionMoviendome();
            } else {
                animacionQuieto();
            }
        }
    }

    /**
     * Lógica de actualización del jugador en la escena 2 y durante el combate.
     *
     * Si el jugador ha muerto, delega en  #animacionMuerte() y corta
     * el resto de la lógica. En la escena 2 aplica movimiento con velocidad 6
     * (ligeramente mayor que en escena 1) y sus propios límites de pantalla.
     * En el estado de pelea solo actualiza la animación idle.
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
                } else if (keyH.leftPressed) {
                    direction   = "left";
                    x2Jugador  -= 6;
                }

                // Límites de la escena 2 (distintos a los de escena 1 por el layout)
                x2Jugador = clamp(x2Jugador, -170, gp.pantallaAnchura - 195);

                animacionMoviendome();
            } else {
                animacionQuieto();
            }

        } else if (gp.gameState == gp.statePelea && !heMuerto) {
            // Durante el combate el jugador está quieto esperando su turno
            animacionQuieto();
        }
    }

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
                    direction   = "right";
                    x3Jugador  += 6;
                } else if (keyH.leftPressed) {
                    direction   = "left";
                    x3Jugador  -= 6;
                }

                // Límites de la escena 2 (distintos a los de escena 1 por el layout)
                x3Jugador = clamp(x3Jugador, -170, gp.pantallaAnchura - 195);

                animacionMoviendome();
            } else {
                animacionQuieto();
            }

        } else if (gp.gameState == gp.statePelea && !heMuerto) {
            // Durante el combate el jugador está quieto esperando su turno
            animacionQuieto();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // RENDERIZADO (llamado cada frame desde GamePanel)
    // ════════════════════════════════════════════════════════════════════════

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
        gd2.drawImage(image, x1Jugador, y1Jugador, playerWidth, playerWidth, null);
    }

    /**
     * Dibuja el jugador en la escena 2 y durante el combate.
     *
     * Selecciona el sprite según el estado actual:
     *
     *   Escena 2 con movimiento → animación de caminar.</li>
     *   Escena 2 quieto → animación idle.</li>
     *   Combate atacando → animación de ataque.</li>
     *   Combate quieto (vivo) → animación idle.</li>
     *   Muerto → animación de muerte (no en bucle).</li>
     *
     *
     * @param gd2 contexto gráfico 2D del frame actual
     */
    public void draw2(Graphics2D gd2) {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        BufferedImage image = null;

        if (gp.gameState == gp.escenaState2) {
            image = moviendo
                    ? (BufferedImage) dibujarMoviendome()
                    : (BufferedImage) dibujarQuieto();

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

    public void draw3(Graphics2D gd2) {
        boolean moviendo = keyH.rightPressed || keyH.leftPressed;
        BufferedImage image = null;

        if (gp.gameState == gp.escenaState3) {
            image = moviendo
                    ? (BufferedImage) dibujarMoviendome()
                    : (BufferedImage) dibujarQuieto();

        } else if (gp.gameState == gp.statePelea && estoyAtacando) {
            image = (BufferedImage) dibujarAtaque();

        } else if (gp.gameState == gp.statePelea && !heMuerto) {
            image = (BufferedImage) dibujarQuieto();

        } else if (heMuerto) {
            image = (BufferedImage) dibujarMuerte();
        }

        // En la escena 2 y combate el sprite se dibuja más grande (400×400)
        gd2.drawImage(image, x3Jugador, y3Jugador, 400, 400, null);
    }

    // ════════════════════════════════════════════════════════════════════════
    // CONTROLADORES DE ANIMACIÓN
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Avanza el contador de la animación de movimiento.
     * Cambia de fotograma cada 10 frames lógicos; cicla entre 1 y 8.
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
     * Cambia de fotograma cada 10 frames lógicos; cicla entre 1 y 5.
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
     * Cambia de fotograma cada 6 frames lógicos; cicla entre 1 y 6.
     * Al completar un ciclo completo incrementa  contadorMaxFrames,
     * que sirve como señal de que la animación ha terminado.
     */
    public void animacionAtaque() {
        atacarCounter++;
        if (atacarCounter > 6) {
            atacarNum++;
            atacarCounter = 0;
            if (atacarNum > 6) {
                atacarNum = 1;
                contadorMaxFrames++; // señal de fin de animación de ataque
            }
        }
    }

    /**
     * Avanza el contador de la animación de muerte.
     * Cambia de fotograma cada 14 frames lógicos; se detiene en el frame 7
     * (no es cíclica, el último frame queda congelado).
     */
    public void animacionMuerte() {
        muerteCounter++;
        if (muerteCounter > 14) {
            muerteNum++;
            muerteCounter = 0;
            if (muerteNum > 7) muerteNum = 7; // congela en el último frame
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // SELECCIÓN DE SPRITE SEGÚN FRAME ACTUAL
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Devuelve el fotograma de idle correspondiente a  idleNum
     * y a la dirección actual del jugador.
     *
     * @return sprite idle actual, o  null si  idleNum está fuera de rango
     */
    public Image dibujarQuieto() {
        BufferedImage image = null;
        switch (direction) {
            case "right":
                if (idleNum == 1) image = quieto1JugaRight;
                if (idleNum == 2) image = quieto2JugaRight;
                if (idleNum == 3) image = quieto3JugaRight;
                if (idleNum == 4) image = quieto4JugaRight;
                if (idleNum == 5) image = quieto5JugaRight;
                break;
            case "left":
                if (idleNum == 1) image = quieto1JugaLeft;
                if (idleNum == 2) image = quieto2JugaLeft;
                if (idleNum == 3) image = quieto3JugaLeft;
                if (idleNum == 4) image = quieto4JugaLeft;
                if (idleNum == 5) image = quieto5JugaLeft;
                break;
        }
        return image;
    }

    /**
     * Devuelve el fotograma de movimiento correspondiente a {@code moveNum}
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
     * Devuelve el fotograma de ataque correspondiente a {@code atacarNum}.
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
     * Devuelve el fotograma de muerte correspondiente a muerteNum.
     * El frame 7 queda congelado hasta que la escena cambie.
     *
     * @return sprite de muerte actual, o  null si  muerteNum está fuera de rango
     */
    public Image dibujarMuerte() {
        BufferedImage image = null;
        switch (muerteNum) {
            case 1: image = muerto_1; break;
            case 2: image = muerto_2; break;
            case 3: image = muerto_3; break;
            case 4: image = muerto_4; break;
            case 5: image = muerto_5; break;
            case 6: image = muerto_6; break;
            case 7: image = muerto_7; break;
        }
        return image;
    }
    // ════════════════════════════════════════════════════════════════════════
    // COMBATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Inicia la secuencia de ataque del jugador.
     *
     * Solo tiene efecto si no hay ya un ataque en curso ( estoyAtacando == false).
     * Guarda el tipo de ataque elegido en  #ataqueElegido sin ejecutarlo todavía,
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
     * Delega el ataque débil (seguro) en la implementación de  Entidad,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueSeguro() {
        super.ataqueSeguro(gp.enemigoActual);
    }

    /**
     * Delega el ataque equilibrado en la implementación de  Entidad,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueEquilibrado() {
        super.ataqueEquilibrado(gp.enemigoActual);
    }

    /**
     * Delega el ataque fuerte (arriesgado) en la implementación de Entidad,
     * pasándole el enemigo activo del panel de juego como objetivo.
     */
    public void ataqueArriesgado() {
        super.ataqueArriesgado(gp.enemigoActual);
    }

    // ════════════════════════════════════════════════════════════════════════
    // COMPROBACIONES DE FIN DE ANIMACIÓN
    // ════════════════════════════════════════════════════════════════════════

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

    // ════════════════════════════════════════════════════════════════════════
    // ÁREAS DE INTERACCIÓN (hitboxes de proximidad)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Devuelve el hitbox frontal del jugador en la escena 1.
     *
     * Es una línea horizontal de 75 px de ancho y 1 px de alto,
     * usada para detectar proximidad a puertas u objetos en el eje X.
     *
     *
     * @return rectángulo de detección en la escena 1
     */
    public Rectangle getBorde1() {
        return new Rectangle(x1Jugador, y1Jugador, 75, 1);
    }

    /**
     * Devuelve el hitbox frontal del jugador en la escena 2.
     *
     * Es una línea horizontal de 64 px de ancho y 1 px de alto,
     * usada para detectar proximidad a zonas de pelea o escaleras.
     *
     *
     * @return rectángulo de detección en la escena 2
     */
    public Rectangle getBorde2() {
        return new Rectangle(x2Jugador, y2Jugador, 64, 1);
    }



    public void moverPelea(){
        gp.gameState = gp.statePelea;
        moveNum = 1;
        direction = "right";
        cercaPuerta = false;
        x2Jugador = 130;
    }
}