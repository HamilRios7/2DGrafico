package Main;

import Entidad.*;
import Fondo.TileManager;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Panel principal del juego. Actúa como núcleo del motor:
 * contiene el game loop, coordina todos los subsistemas
 * (renderizado, física, audio, UI) y almacena el estado global de la partida.
 *
 * Extiende  JPanel para integrarse en la ventana Swing e implementa
 * Runnable para ejecutarse en su propio hilo de juego.
 *
 */
public class GamePanel extends JPanel implements Runnable {

    // ════════════════════════════════════════════════════════════════════════
    // CONFIGURACIÓN DE PANTALLA
    // ════════════════════════════════════════════════════════════════════════

    /** Tamaño base de cada mosaico (tile) antes de escalar, en píxeles. */
    int originalTamañoMosaico = 16;

    /** Factor de escala aplicado a los mosaicos para adaptarlos a la resolución. */
    int escala = 3;

    /** Tamaño final de cada mosaico en pantalla ( originalTamañoMosaico × escala). */
    public int tamañoMosaico = originalTamañoMosaico * escala; // 48 px

    /** Número de columnas de mosaicos visibles en pantalla. */
    int maxPantallaCol = 23;

    /** Número de filas de mosaicos visibles en pantalla. */
    int maxPantallaRow = 13;

    /** Anchura total de la ventana de juego en píxeles. */
    public int pantallaAnchura = tamañoMosaico * maxPantallaCol; // 1104 px

    /** Altura total de la ventana de juego en píxeles. */
    public int pantallaAltura = tamañoMosaico * maxPantallaRow;  // 624 px

    // ════════════════════════════════════════════════════════════════════════
    // RENDIMIENTO
    // ════════════════════════════════════════════════════════════════════════

    /** Fotogramas por segundo objetivo del game loop. */
    int fps = 60;

    // ════════════════════════════════════════════════════════════════════════
    // SUBSISTEMAS
    // ════════════════════════════════════════════════════════════════════════

    /** Gestor de tiles que dibuja el fondo/mapa de cada escena. */
    TileManager fondoM = new TileManager(this);

    /** Manejador de teclado que traduce las pulsaciones en flags de entrada. */
    public KeyHandler keyH = new KeyHandler(this);

    /**
     * Hilo del game loop. Mientras exista, el juego sigue corriendo.
     * Se inicia en  #startGame().
     */
    Thread gameThread;

    /** Detector de colisiones entre entidades y tiles del mapa. */
    public ColisionChecker cChecker = new ColisionChecker(this);

    /** Sistema de audio para música y efectos de sonido. */
    Sonido sound = new Sonido();

    /** Interfaz de usuario: HUD, menús y textos en pantalla. */
    public UI ui = new UI(this, keyH);

    /** Gestor de transiciones de escena y lógica de combate. */
    Actualizacion at = new Actualizacion(this);

    // ════════════════════════════════════════════════════════════════════════
    // ENTIDADES
    // ════════════════════════════════════════════════════════════════════════

    /** Entidad del jugador: movimiento, animación, combate y vida. */
    public Jugador jugador = new Jugador(this, keyH);

    /** Enemigo principal del juego: el samurái errante. */
    public samuraiErrante samuraiErrante = new samuraiErrante(this);

    // ════════════════════════════════════════════════════════════════════════
    // ESTADOS DEL JUEGO
    // ════════════════════════════════════════════════════════════════════════

    /** Estado actual del juego. Se compara con las constantes de estado definidas abajo. */
    public int gameState;

    /** Estado de la pantalla de título / menú principal. */
    public int titleState = 0;

    /** Estado de la primera escena (exterior del castillo). */
    public int escenaState1 = 1;

    /** Estado de la segunda escena (interior / sala del samurái). */
    public int escenaState2 = 2;

    /** Estado de la tercera escena (piso superior). */
    public int escenaState3 = 3;

    /** Estado de pausa de la escena 1. */
    public int pauseState1 = 4;

    /** Estado de pausa de la escena 2. */
    public int pauseState2 = 5;

    /** Estado de pausa de la escena 3. */
    public int pauseState3 = 6;

    /** Estado de combate por turnos contra el samurái. */
    public int statePelea = 10;

    /** Estado de pantalla de victoria al completar el juego. */
    public int congratulationsState = 11;

    // ════════════════════════════════════════════════════════════════════════
    // FLAGS DE COMBATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     *  true cuando la pelea ha terminado (el enemigo ha sido derrotado).
     * Desbloquea el acceso al siguiente piso.
     */
    public boolean ispeleaFinalizada = false;

    /**
     *  true mientras se está resolviendo un turno de combate
     * (animaciones en curso).  false cuando hay un resultado
     * pendiente de confirmar en la UI.
     */
    public boolean isSituacionPelea = true;

    /**
     *  true cuando es el turno del jugador para elegir acción.
     *  false cuando el enemigo está actuando.
     */
    public boolean jugadorTurno = true;

    // ════════════════════════════════════════════════════════════════════════
    // REFERENCIA AL ENEMIGO ACTIVO (para expansión futura con múltiples enemigos)
    // ════════════════════════════════════════════════════════════════════════

    /** Enemigo actualmente en combate (puede usarse para generalizar más adelante). */
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
        gameState = titleState;
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
     *
     * Mantiene una tasa de actualización constante a  #fps fotogramas
     * por segundo independientemente del rendimiento del hardware.
     * Cada iteración:
     *
     *   Calcula el delta acumulado desde el último frame.
     *   Cuando delta ≥ 1, llama a #update() y a  #repaint().
     *   Imprime los FPS reales por consola cada segundo (útil para depuración).
     *
     *
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / fps; // nanosegundos por frame
        double delta      = 0;
        long lastTime     = System.nanoTime();
        long timer        = 0;
        int drawCounter   = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long elapsed     = currentTime - lastTime;

            delta += elapsed / drawInterval;
            timer += elapsed;
            lastTime = currentTime;

            if (delta >= 1) {
                update();   // lógica del juego
                repaint();  // renderizado (llama a paintComponent)
                delta--;
                drawCounter++;
            }

            // Mostrar FPS reales en consola cada segundo
            if (timer >= 1_000_000_000) {
                System.out.println("FPS: " + drawCounter);
                drawCounter = 0;
                timer       = 0;
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // ACTUALIZACIÓN DE LÓGICA
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Actualiza la lógica del juego una vez por frame.
     *
     * Delega en los métodos de cada entidad y en  Actualizacion
     * según el estado activo. Los estados de pausa cortan el update
     * para "congelar" el juego.
     *
     */
    public void update() {
        if (gameState == escenaState1) {
            jugador.update1();
            // Comprueba si el jugador activa la transición a la escena 2
            at.actualizacionMoverEscena2();

        } else if (gameState == escenaState2) {
            jugador.update2();
            samuraiErrante.updateSamurai();

            // Comprueba si el jugador entra en zona de pelea
            at.actualizacionMoverEstadoPelea1();

            // Comprueba si se cumplen las condiciones para mostrar la enhorabuena
            at.actualizacionMoverEscenaCongratulatons();

        } else if (gameState == escenaState3) {
            jugador.update2();
        }

        if (gameState == statePelea) {
            // Controla animaciones de combate y lógica de turnos
            at.actualizacionCombate1();
        }

        // Los estados de pausa no actualizan nada → el juego queda congelado
        if (gameState == pauseState1 || gameState == pauseState2 || gameState == pauseState3) {
            // Intencional: sin lógica de actualización mientras está pausado
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // RENDERIZADO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Dibuja todos los elementos visuales del frame actual.
     *
     * El orden de dibujado es importante: fondo → entidades → UI (siempre encima).
     * Convierte el  Graphics de Swing a  Graphics2D para mayor control.
     *
     *
     * @param g contexto gráfico proporcionado por Swing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // ── Pantalla de título ───────────────────────────────────────────────
        if (gameState == titleState) {
            ui.draw(g2d);
            g2d.dispose();

            // ── Escena 1: exterior del castillo ─────────────────────────────────
        } else if (gameState == escenaState1) {
            fondoM.draw1(g2d);  // fondo / mapa
            jugador.draw1(g2d); // sprite del jugador
            ui.draw(g2d);       // HUD y textos
            g2d.dispose();

            // ── Escena 2 y estado de combate (comparten el mismo mapa) ──────────
        } else if (gameState == escenaState2 || gameState == statePelea) {
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            samuraiErrante.drawSamurai(g2d);
            ui.draw(g2d);
            g2d.dispose();

            // ── Pantalla de enhorabuena ──────────────────────────────────────────
        } else if (gameState == congratulationsState) {
            ui.draw(g2d);
            // No se llama a dispose() aquí; Swing lo gestiona al salir del método
        }

        // ── Pausa escena 1 ───────────────────────────────────────────────────
        if (gameState == pauseState1) {
            fondoM.draw1(g2d);
            jugador.draw1(g2d);
            ui.draw(g2d);
            g2d.dispose();

            // ── Pausa escena 2 ───────────────────────────────────────────────────
        } else if (gameState == pauseState2) {
            fondoM.draw2(g2d);
            samuraiErrante.drawSamurai(g2d);
            jugador.draw2(g2d);
            ui.draw(g2d);
            g2d.dispose();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // AUDIO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Carga y reproduce una pista de música en bucle.
     *
     * @param i índice de la pista de audio en el sistema  Sonido
     */
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    /**
     * Detiene la música que esté sonando en ese momento.
     */
    public void stopMusic() {
        sound.stop();
    }

    /**
     * Reproduce un efecto de sonido puntual (sin bucle).
     * Reservado para implementación futura.
     *
     * @param i índice del efecto de sonido
     */
    public void playSE(int i) {
        // TODO: implementar efectos de sonido
    }
}