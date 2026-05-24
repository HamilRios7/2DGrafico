package Main;

import Entidad.*;
import Fondo.TileManager;
import HallOfFame.PantallaHallOfFame;
import Objetos.Inventario;
import Objetos.Obj_PocionVida;
import Objetos.SuperObject;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel principal del juego. Actúa como núcleo del motor:
 * contiene el game loop, coordina todos los subsistemas
 * (renderizado, física, audio, UI) y almacena el estado global de la partida.
 *
 * Extiende  JPanel para integrarse en la ventana Swing e implementa
 * Runnable para ejecutarse en su propio hilo de juego.
 */
public class GamePanel extends JPanel implements Runnable {

    // ════════════════════════════════════════════════════════════════════════
    // CONFIGURACIÓN DE PANTALLA
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Tamaño base de cada mosaico (tile) antes de escalar, en píxeles.
     */
    int originalTamañoMosaico = 16;

    /**
     * Factor de escala aplicado a los mosaicos para adaptarlos a la resolución.
     */
    int escala = 3;

    /**
     * Tamaño final de cada mosaico en pantalla ( originalTamañoMosaico × escala).
     */
    public int tamañoMosaico = originalTamañoMosaico * escala; // 48 px

    /**
     * Número de columnas de mosaicos visibles en pantalla.
     */
    int maxPantallaCol = 23;

    /**
     * Número de filas de mosaicos visibles en pantalla.
     */
    int maxPantallaRow = 13;

    /**
     * Anchura total de la ventana de juego en píxeles.
     */
    public int pantallaAnchura = tamañoMosaico * maxPantallaCol; // 1104 px

    /**
     * Altura total de la ventana de juego en píxeles.
     */
    public int pantallaAltura = tamañoMosaico * maxPantallaRow;  // 624 px


    public boolean pantallaCompleta = false;

    // ════════════════════════════════════════════════════════════════════════
    // RENDIMIENTO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Fotogramas por segundo objetivo del game loop.
     */
    int fps = 54;

    // ════════════════════════════════════════════════════════════════════════
    // SUBSISTEMAS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Gestor de tiles que dibuja el fondo/mapa de cada escena.
     */
    TileManager fondoM = new TileManager(this);

    /**
     * Manejador de teclado que traduce las pulsaciones en flags de entrada.
     */
    public KeyHandler keyH = new KeyHandler(this);

    /**
     * Hilo del game loop. Mientras exista, el juego sigue corriendo.
     * Se inicia en  #startGame().
     */
    Thread gameThread;

    /**
     * Detector de colisiones entre entidades y tiles del mapa.
     */
    public ColisionChecker cChecker = new ColisionChecker(this);

    /**
     * Sistema de audio para música .
     */
    Sonido sound = new Sonido();


    /**
     * Interfaz de usuario: HUD, menús y textos en pantalla.
     */
    public UI ui = new UI(this, keyH);

    /**
     * Gestor de transiciones de escena y lógica de combate.
     */
    Actualizacion at = new Actualizacion(this);


    public Inventario  inventario = new Inventario(this);


    public CronometroPartida cronometro = new CronometroPartida();


 public PantallaHallOfFame pantallaHallOfFame = new PantallaHallOfFame(this);

    // ════════════════════════════════════════════════════════════════════════
    // ENTIDADES
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Entidad del jugador: movimiento, animación, combate y vida.
     */
    public Jugador jugador = new Jugador(this, keyH);

    /**
     * Enemigo principal del juego: el samurái errante.
     */
    public samuraiErrante samuraiErrante = new samuraiErrante(this);

    /**
     * Enemigo boss principal del juego: el Gigante
     */
    public Gigante gigante = new Gigante(this);

    // ════════════════════════════════════════════════════════════════════════
    // ESTADOS DEL JUEGO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Estado actual del juego. Se compara con las constantes de estado definidas abajo.
     */
    public int gameState;

    /**
     * Estado de la pantalla de título / menú principal.
     */
    public int titleState = 0;

    /**
     * Estado de la primera escena (exterior del castillo).
     */
    public int escenaState1 = 1;

    /**
     * Estado de la segunda escena (interior / sala del samurái).
     */
    public int escenaState2 = 2;

    /**
     * Estado de la tercera escena (piso superior).
     */
    public int escenaState3 = 3;

    /**
     * Estado de pausa de la escena 1.
     */
    public int pauseState1 = 4;

    /**
     * Estado de pausa de la escena 2.
     */
    public int pauseState2 = 5;

    /**
     * Estado de pausa de la escena 3.
     */
    public int pauseState3 = 6;

    /**
     * Estado de combate por turnos contra el samurái.
     */
    public int statePelea = 10;

    /**
     * Estado de combate por turnos contra el samurái.
     */
    public int statePelea2 = 11;

    /**
     * Estado de pantalla de victoria al completar el juego.
     */
    public int congratulationsState = 12;

    public int hallOfFameState = 13;


    public int inventarioSlot = 0;

    // ════════════════════════════════════════════════════════════════════════
    // FLAGS DE COMBATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * true cuando la pelea ha terminado (el enemigo ha sido derrotado).
     * Desbloquea el acceso al siguiente piso.
     */
    public boolean ispeleaFinalizada = false;

    /**
     * true mientras se está resolviendo un turno de combate
     * (animaciones en curso). false cuando hay un resultado
     * pendiente de confirmar en la UI.
     */
    public boolean isSituacionPelea = true;

    /**
     * true cuando es el turno del jugador para elegir acción.
     * false cuando el enemigo está actuando.
     */
    public boolean jugadorTurno = true;


    public boolean cofreAparecido = false;


    public boolean inventarioAbierto=false;


    public SuperObject objetoDropeado = null;
    public int dropX, dropY;


    public boolean fuerzaActiva = false;


    public String nombreJugador = "Anónimo";
    // ════════════════════════════════════════════════════════════════════════
    // REFERENCIA AL ENEMIGO ACTIVO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Enemigo actualmente en combate.
     * Asignar antes de entrar al combate:
     * gp.enemigoActual = gp.samuraiErrante;  // combate 1
     * gp.enemigoActual = gp.gigante;          // combate boss
     */
    public Enemigo enemigoActual;

    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Inicializa el panel de juego: configura dimensiones, fondo, doble buffer
     * y registra el manejador de teclado.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(pantallaAnchura, pantallaAltura));
        this.setBackground(Color.black);

        // El doble buffer evita el parpadeo al renderizar cada frame
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);

        // Necesario para que el panel reciba eventos de teclado
        this.setFocusable(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // INICIALIZACIÓN Y ARRANQUE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Configura el estado inicial del juego antes de arrancar el loop.
     * Actualmente establece el estado en la pantalla de título.
     */
    public void setupGame() {
        playMusic(1);
        gameState = titleState;
        inventario.añadirObjeto(new Obj_PocionVida(this));
        inventario.añadirObjeto(new Obj_PocionVida(this));
        inventario.añadirObjeto(new Obj_PocionVida(this));
    }

    /**
     * Crea e inicia el hilo del game loop.
     * Al llamar a  Thread#start(), Java invoca automáticamente run().
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // ════════════════════════════════════════════════════════════════════════
    // GAME LOOP
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Núcleo del game loop basado en delta-time.
     * Mantiene una tasa de actualización constante a #fps fotogramas
     * por segundo independientemente del rendimiento del hardware.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = 0;
        int drawCounter = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;

            delta += elapsed / drawInterval;
            timer += elapsed;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCounter++;
            }

            if (timer >= 1_000_000_000) {
                System.out.println("FPS: " + drawCounter);
                drawCounter = 0;
                timer = 0;
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // ACTUALIZACIÓN DE LÓGICA
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Actualiza la lógica del juego una vez por frame.
     * Delega en los métodos de cada entidad y en Actualizacion
     * según el estado activo.
     */
    public void update() {
        if (gameState == escenaState1 && !ui.dibujadoOpciones) {
            jugador.update1();
            at.actualizacionIrEscena2();

        } else if (gameState == escenaState2 && !ui.dibujadoOpciones) {
            jugador.update2();
            enemigoActual.updateEnemigo();

            at.actualizacionEmpezarPelea1();
            at.actualizacionRecogerDrop();
            at.actualizacionIrEscena3();



        } else if (gameState == escenaState3 && !ui.dibujadoOpciones) {
            jugador.update3();
            enemigoActual.updateEnemigo();
            at.actualizacionEmpezarPeleaFinal();
            at.actualizacionRecogerDrop();
            at.actualizacionMostrarEscenaCongratulations();


        }

        if (gameState == statePelea || gameState == statePelea2) {
            at.actualizacionSistemaCombate();
        }

        // Los estados de pausa no actualizan nada → el juego queda congelado
        if (gameState == pauseState1 || gameState == pauseState2 ||
                gameState == pauseState3 || ui.dibujadoOpciones) {

            if (cronometro.estaContando()) cronometro.pausar();

        } else if (gameState == escenaState1 || gameState == escenaState2 ||
                gameState == escenaState3 || gameState == statePelea ||
                gameState == statePelea2) {

            if (!cronometro.estaContando()) cronometro.reanudar();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // RENDERIZADO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Dibuja todos los elementos visuales del frame actual.
     * Orden: fondo → entidades → UI (siempre encima).
     *
     * @param g contexto gráfico proporcionado por Swing
     */
    @Override

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoReal;
        int altoReal;

        // Crear buffer del tamaño original del juego
        BufferedImage buffer = new BufferedImage(pantallaAnchura, pantallaAltura,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();

        // ── Pantalla de título ───────────────────────────────────────────────
        if (gameState == titleState) {
            ui.draw(g2d);

            // ── Escena 1: exterior del castillo ──────────────────────────────────
        } else if (gameState == escenaState1) {
            fondoM.draw1(g2d);
            jugador.draw1(g2d);
            ui.draw(g2d);

            // ── Escena 2 y combate 1 ─────────────────────────────────────────────
        } else if (gameState == escenaState2 || gameState == statePelea) {
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);

            // ── Escena 3 y combate 2 ─────────────────────────────────────────────
        } else if (gameState == escenaState3 || gameState == statePelea2) {
            fondoM.draw3(g2d);
            jugador.draw3(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);

            // ── Enhorabuena ───────────────────────────────────────────────────────
        } else if (gameState == congratulationsState) {
            ui.draw(g2d);

            // ── Hall of Fame ──────────────────────────────────────────────────────
        } else if (gameState == hallOfFameState) {
            pantallaHallOfFame.draw(g2d);
        }

        // ── Pausa escena 1 ────────────────────────────────────────────────────
        if (gameState == pauseState1) {
            fondoM.draw1(g2d);
            jugador.draw1(g2d);
            ui.draw(g2d);

            // ── Pausa escena 2 ────────────────────────────────────────────────────
        } else if (gameState == pauseState2) {
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);

            // ── Pausa escena 3 ────────────────────────────────────────────────────
        } else if (gameState == pauseState3) {
            fondoM.draw3(g2d);
            jugador.draw3(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);
        }

        g2d.dispose();

        // ── Escalar y centrar el buffer sobre la pantalla real ────────────────
        if (pantallaCompleta) {
            anchoReal = Main.gd.getDisplayMode().getWidth();
            altoReal  = Main.gd.getDisplayMode().getHeight();
        } else {
            anchoReal = getWidth();
            altoReal  = getHeight();
        }

        double escalaX    = (double) anchoReal / pantallaAnchura;
        double escalaY    = (double) altoReal  / pantallaAltura;
        double escalaFinal = Math.min(escalaX, escalaY);

        int anchoEscalado = (int)(pantallaAnchura * escalaFinal);
        int altoEscalado  = (int)(pantallaAltura  * escalaFinal);
        int offsetX = (anchoReal  - anchoEscalado) / 2;
        int offsetY = (altoReal   - altoEscalado)  / 2;

        Graphics2D gPantalla = (Graphics2D) g;
        gPantalla.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gPantalla.drawImage(buffer, offsetX, offsetY, anchoEscalado, altoEscalado, null);
        gPantalla.dispose();
    }

    // ════════════════════════════════════════════════════════════════════════
    // AUDIO
    // ════════════════════════════════════════════════════════════════════════

    public void playMusic(int i) {
        sound.playMusic(i);
    }

    public void stopMusic() {
        sound.stopMusic();
    }

    public void playSE(int i) {
        sound.playSE(i);
    }
}