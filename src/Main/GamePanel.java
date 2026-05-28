package Main;

import Entidad.*;
import Fondo.FondosManager;
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
 * Extiende JPanel para integrarse en la ventana Swing e implementa
 * Runnable para ejecutarse en su propio hilo de juego.
 */
public class GamePanel extends JPanel implements Runnable {


    // ------ CONFIGURACIÓN DE PANTALLA BASE ------


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

    /**
     * true si el jugador a decidido poner la opcion de pantalla completa
     */
    public boolean pantallaCompleta = false;


    //  ------- RENDIMIENTO -------


    /**
     * Fotogramas por segundo objetivo del game loop, es decir, el limite que le ponemos de base.
     */
    int fps = 54;


    //----- SUBSISTEMAS ------


    /**
     * Gestor de fondos que dibuja el fondo de cada escena.
     */
    FondosManager fondoM = new FondosManager(this);

    /**
     * Manejador de teclado que traduce las pulsaciones en flags de entrada.
     */
    public KeyHandler keyH = new KeyHandler(this);

    /**
     * Hilo del game loop. Mientras exista, el juego sigue corriendo.
     * Se inicia en startGame().
     */
    Thread gameThread;

    /**
     * Detector de colisiones entre entidades y hitboxs de interaccion del mapa.
     */
    public ColisionChecker cChecker = new ColisionChecker(this);

    /**
     * Sistema de audio para música y efectos de sonido.
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

    /**
     * Gestor del inventario que es llamado por otros.
     */
    public Inventario  inventario = new Inventario(this);

    /**
     * Conometro que esta en movimiento durante las escenas y se pausa durante los estados de pausa y opciones.
     */
    public CronometroPartida cronometro = new CronometroPartida();

    /**
     * Pantalla de Hall of Fame que se muestra al finalizar el juego, con la lista de mejores tiempos.
     */
 public PantallaHallOfFame pantallaHallOfFame = new PantallaHallOfFame(this);


    //-------- ENTIDADES ---------


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


    // -------  ESTADOS DEL JUEGO ---------


    /**
     * Estado actual del juego. Se compara con las constantes de estado definidas abajo.
     */
    public  int gameState;

    /**
     * Estado de la pantalla de título / menú principal.
     */
    public final int titleState = 0;

    /**
     * Estado de la primera escena (exterior del castillo).
     */
    public final int escenaState1 = 1;

    /**
     * Estado de la segunda escena (interior / sala del samurái).
     */
    public final int escenaState2 = 2;

    /**
     * Estado de la tercera escena (sala del gigante).
     */
    public final int escenaState3 = 3;

    /**
     * Estado de pausa de la escena 1.
     */
    public final int pauseState1 = 4;

    /**
     * Estado de pausa de la escena 2.
     */
    public final int pauseState2 = 5;

    /**
     * Estado de pausa de la escena 3.
     */
    public final int pauseState3 = 6;

    /**
     * Estado de combate por turnos contra el samurái.
     */
    public final int statePelea = 10;

    /**
     * Estado de combate por turnos contra el gigante.
     */
    public final int statePelea2 = 11;

    /**
     * Estado de pantalla de victoria al completar el juego.
     */
    public final int congratulationsState = 12;

    /**
     * Estado de pantalla para mostrar el ranking al completar el juego.
     */
    public final int hallOfFameState = 13;

    /**
     * Este "estado" es el que nos permitira movernos a traves del inventario el cual puede cambiar
     */
    public int inventarioSlot = 0;


    // ----- FLAGS  Y VARIABLES COMBATE E INTERACCION --------


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

    /**
     * true cuando el enemigo  gigante ha muerto y hacer que aparezca dibujado
     * false cuando el enemigo gigante no ha muerto
     */
    public boolean cofreAparecido = false;

    /**
     * true cuando en combate seleccionamos inventario y abrimos
     * false cuando no estamos en el inventario durante el combate
     */
    public boolean inventarioAbierto=false;

    /**
     * No es null cuando el samurai dropea una pocion al morir
     */
    public SuperObject objetoDropeado = null;

    /**
     * Coordenadas en las que se dropeara las pociones
     *
     */
    public int dropX, dropY;

    /**
     * true cuando nos hemos tomado una pocion de fuerza y tenemos el efecto
     * false cuando no tenemos el efecto
     */
    public boolean fuerzaActiva = false;

    //----- -------- FLAG ACTUAR --------

    /**
     * true cuando en el menu le das a enter en Hall of Fame
     * false cuando no le has dado , o una vez mostrado le das a ESC
     */
    public boolean mostrarHallofFame =false;

    /**
     * true cuando interactuas con cofre  para llegar a congratulationsState
     * false el resto del juego antes de eso
     */
    public boolean juegoTerminado=false;

     //--------- Registro nombre sin sobreescribir-----
    /**
     * En caso de que no pongamos nombre en PantalaIntroducirNombre , se asignará automaticamente este
     */
    public String nombreJugador = "Anónimo";


    // ----- REFERENCIA AL ENEMIGO ACTIVO -----


    /**
     * Enemigo actualmente en combate.
     * Asignar antes de entrar al combate:
     * gp.enemigoActual = gp.samuraiErrante;  // combate 1
     * gp.enemigoActual = gp.gigante;          // combate boss
     */
    public Enemigo enemigoActual;


    // ------ -CONSTRUCTOR --------


    /**
     * Inicializa el panel de juego: configura dimensiones, fondo, doble buffer
     * y registra el manejador de teclado.
     */
    public GamePanel() {
        //esto define el tamaño “preferido” del panel.
        this.setPreferredSize(new Dimension(pantallaAnchura, pantallaAltura));
        //definimos el fondo del JFrame, aunque luego lo sobreescribamos con el JPanel
        this.setBackground(Color.black);

        // El doble buffer evita el parpadeo al renderizar cada frame
        //El juego dibuja primero en una “imagen oculta” (el buffer) y luego la muestra completa en pantalla, lo que da una apariencia más suave.
        this.setDoubleBuffered(true);

        //Nos añade un listener de teclado, el que vera las acciones con las teclas que asignemos
        this.addKeyListener(keyH);

        // Necesario para que el panel reciba eventos de teclado, sin esto , no detectara teclas
        this.setFocusable(true);
    }


    //  ------ INICIALIZACIÓN Y ARRANQUE ------


    /**
     * Configura el estado inicial del juego antes de arrancar el loop.
     * Actualmente establece el estado en la pantalla de título.
     */
    public void setupGame() {
        //iniciamos la cancion
        playMusic(1);
        //asignamos estado de inicio a la pantalla de titulo
        gameState = titleState;

        //añadimos a nuestro Inventario , los objetos  necesarios que son pociones de vida
        inventario.añadirObjeto(new Obj_PocionVida(this));
        inventario.añadirObjeto(new Obj_PocionVida(this));
        inventario.añadirObjeto(new Obj_PocionVida(this));
    }

    /**
     * Crea e inicia el hilo del game loop.
     * Al llamar a  Thread.start(), Java invoca automáticamente run().
     */
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    //------ GAME LOOP --------


    /**
     * Núcleo del game loop basado en delta-time.
     * Mantiene una tasa de actualización constante a #fps fotogramas
     * por segundo independientemente del rendimiento del hardware , el cual yo soy el que delimita el numero
     */
    @Override
    public void run() {
        // Intervalo de tiempo entre frames en nanosegundos
        // Ej: si fps = 60 -> 1 segundo / 60 = ~16.6 ms por frame
        double intervaloDibujo = 1000000000.0 / fps;

        // Acumulador que decide cuándo toca actualizar/dibujar
        double delta = 0;

        // Tiempo del último frame en nanosegundos
        long ultimoFrameTiempo = System.nanoTime();


        // Temporizador para contar 1 segundo y mostrar FPS
        long timer = 0;

        // Contador de frames dibujados en 1 segundo
        int drawCounter = 0;

        while (gameThread != null) {
            // Tiempo actual en nanosegundos
            long tiempoActual = System.nanoTime();

            // Tiempo transcurrido desde el último frame
            long tiempoTranscurrido = tiempoActual - ultimoFrameTiempo;


            // Convertimos el tiempo a “frames acumulados”
            // Si pasa suficiente tiempo, delta llegará a 1 o más, lo que indica que toca actualizar y dibujar.
            delta += tiempoTranscurrido / intervaloDibujo;

            // Acumulamos tiempo para medir FPS
            timer += tiempoTranscurrido;

            // Actualizamos referencia de tiempo
            ultimoFrameTiempo = tiempoActual;

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


    //------ ACTUALIZACIÓN DE LÓGICA --------


    /**
     * Actualiza la lógica del juego una vez por frame.
     * Delega en los métodos de cada entidad y en Actualizacion
     * según el estado activo.
     */
    public void update() {

        // Actualiza la lógica de la escena 1
        if (gameState == escenaState1 && !ui.dibujadoOpciones) {
            jugador.update1();
            at.actualizacionIrEscena2();

            // Actualiza la lógica de la escena 2
        } else if (gameState == escenaState2 && !ui.dibujadoOpciones) {
            jugador.update2();
            enemigoActual.updateEnemigo();

            at.actualizacionEmpezarPelea1();
            at.actualizacionRecogerDrop();
            at.actualizacionIrEscena3();


            // Actualiza la lógica de la escena 3
        } else if (gameState == escenaState3 && !ui.dibujadoOpciones) {
            jugador.update3();
            enemigoActual.updateEnemigo();
            at.actualizacionEmpezarPeleaFinal();
            at.actualizacionMostrarEscenaCongratulations();


        }

        // Delega la actualizacion de la logica en los estados de pelea  a actualizacionSistemaComabe
        if (gameState == statePelea || gameState == statePelea2) {
            at.actualizacionSistemaCombate();
        }

        // Los estados de pausa no actualizan nada, solo pausan conometro -> el juego queda congelado
        if (gameState == pauseState1 || gameState == pauseState2 ||
                gameState == pauseState3 || ui.dibujadoOpciones) {

            if (cronometro.estaContando()) cronometro.pausar();
        //Reanudamos el conometro en caso de que hayamos sido pausado
        } else if (gameState == escenaState1 || gameState == escenaState2 ||
                gameState == escenaState3 || gameState == statePelea ||
                gameState == statePelea2) {

            if (!cronometro.estaContando()) cronometro.reanudar();
        }
    }


    // ------ RENDERIZADO ------


    /**
     * Dibuja todos los elementos visuales del frame actual.
     * Orden: fondo -> entidades -> UI (siempre encima).
     *
     * @param g contexto gráfico proporcionado por Swing
     */
    @Override

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoReal;
        int altoReal;


        //Creamos una imagen en memoria donde se dibuja antes de mostrar del tamaño dicho
        BufferedImage buffer = new BufferedImage(pantallaAnchura, pantallaAltura, BufferedImage.TYPE_INT_ARGB);
       //Nos da un objeto Graphics2D para dibujar dentro de esa imagen (buffer) , es decir , en memoria.
        Graphics2D g2d = buffer.createGraphics();

        // --------  Pantalla de título ----------
        if (gameState == titleState) {
            //dibujamos ui de seleccion de menu
            ui.draw(g2d);

            // --------- Escena 1: exterior del castillo -------
        } else if (gameState == escenaState1) {
            //dibujamos primer fondo
            fondoM.draw1(g2d);
            //dibujamos jugador en sus posiciones diferentes
            jugador.draw1(g2d);

            //dibujamos ui de seleccion de menu y vida
            ui.draw(g2d);

            // ------ Escena 2 y combate 1 --------
        } else if (gameState == escenaState2 || gameState == statePelea) {
            //dibujamos segundo fondo
            fondoM.draw2(g2d);
            //dibujamos jugador en sus posiciones diferentes
            jugador.draw2(g2d);
            //dibujamos enemigo en sus posicion
            enemigoActual.drawEnemigo(g2d);

            //dibujamos ui de seleccion de menu, vida y combate
            ui.draw(g2d);

            // -------- Escena 3 y combate 2 -------
        } else if (gameState == escenaState3 || gameState == statePelea2) {
            //dibujamos tercer fondo
            fondoM.draw3(g2d);
            //dibujamos jugador en sus posiciones diferentes
            jugador.draw3(g2d);
            //dibujamos enemigo en sus posicion
            enemigoActual.drawEnemigo(g2d);
            //dibujamos ui de seleccion de menu, vida y combate
            ui.draw(g2d);

            //------- Enhorabuena --------
        } else if (gameState == congratulationsState) {
            //dibujamos ui de enhorabuena
            ui.draw(g2d);

            //  ------ Hall of Fame --------
        } else if (gameState == hallOfFameState) {
            //dibujamos hall of fame pantalla
            pantallaHallOfFame.draw(g2d);
        }

        // -------- Pausa escena 1 --------
        if (gameState == pauseState1) {
            //dejamos todo actualizado en pause

            fondoM.draw1(g2d);
            jugador.draw1(g2d);
            ui.draw(g2d);

            // --------  Pausa escena 2 -----------
        } else if (gameState == pauseState2) {
            fondoM.draw2(g2d);
            jugador.draw2(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);

            // -------  Pausa escena 3 --------
        } else if (gameState == pauseState3) {
            fondoM.draw3(g2d);
            jugador.draw3(g2d);
            enemigoActual.drawEnemigo(g2d);
            ui.draw(g2d);
        }

        g2d.dispose();

        // Si el juego está en modo pantalla completa
        if (pantallaCompleta) {

            // Obtiene el ancho real de la pantalla del monitor con gd que hemos iniciado en Main
            anchoReal = Main.gd.getDisplayMode().getWidth();
            // Obtiene el alto real de la pantalla del monitor con gd que hemos iniciado en Main
            altoReal  = Main.gd.getDisplayMode().getHeight();
        } else {
            // Si no está en pantalla completa, usa el tamaño del panel
            anchoReal = pantallaAnchura;
            altoReal  = pantallaAltura;
        }

        // Escala horizontal (cuánto se tiene que ampliar o reducir el ancho)
        double escalaX    = (double) anchoReal / pantallaAnchura;

        // Escala vertical (cuánto se tiene que ampliar o reducir el alto)
        double escalaY    = (double) altoReal  / pantallaAltura;

        // Se elige la escala más pequeña para evitar deformaciones y mantener la proporción original del juego.
        double escalaFinal = Math.min(escalaX, escalaY);

        // Ancho final después de aplicar escala
        int anchoEscalado = (int)(pantallaAnchura * escalaFinal);
        // Alto final después de aplicar escala
        int altoEscalado  = (int)(pantallaAltura  * escalaFinal);

        // Espacio sobrante horizontal (para centrar y no haya franjas separadas)
        int offsetX = (anchoReal  - anchoEscalado) / 2;

        // Espacio sobrante vertical (para centrar el dibujado y no haya franjas separadas )
        int offsetY = (altoReal   - altoEscalado)  / 2;

        //Solo dibuja en pantalla, es lo que ve el jugador  o  es la salida final
        Graphics2D gPantalla = (Graphics2D) g;

        // Mejora la calidad de escalado (evita pixelado feo al ampliar y ajustar )
        gPantalla.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Dibuja el buffer (imagen del juego) en pantalla escalada y centrada
        //ya que primero dibuja en memoria (buffer) -> pasa imagen a pantalla
        gPantalla.drawImage(buffer, offsetX, offsetY, anchoEscalado, altoEscalado, null);

        // Libera recursos gráficos (importante para evitar fugas de memoria)
        gPantalla.dispose();
    }


    // ------  AUDIO ------


    /**
     * Reproduce la música de fondo correspondiente al índice dado que le doy
     * @param i indice de la lista de reproductor de musica
     */
    public void playMusic(int i) {
        sound.playMusic(i);
    }

    /**
     * Paramos cancion que este reproduciendose
     */
    public void stopMusic() {
        sound.stopMusic();
    }

    /**
     * Reproduce la música del momento correspondiente al índice dado que le doy
     * @param i indice de la lista de reproductor de los efectos de sonido
     */
    public void playSE(int i) {
        sound.playSE(i);
    }
}