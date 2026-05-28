package Main;

import Entidad.Enemigo;
import Entidad.Gigante;
import Entidad.samuraiErrante;
import Objetos.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase responsable de renderizar toda la interfaz de usuario (HUD, menús, etc.).
 *
 * Se encarga de dibujar en pantalla los diferentes estados del juego:
 * título, pausa, combate, guías de interacción y pantallas de fin de partida.
 *
 */
public class UI {

    /** Referencia al panel principal del juego para acceder al estado global. */
    GamePanel gp;

    /** Manejador de teclado para leer la entrada del jugador. */
    KeyHandler keyH;

    /** Contexto gráfico 2D usado en el frame actual. */
    Graphics2D g2;

    /** Fuente estándar de 20 pt en negrita. */
    Font arial_40;

    /** Fuente grande de 80 pt en negrita (para títulos). */
    Font arial_80B;

    /** Imagen del corazón que aparece junto a la barra de vida. */
    BufferedImage corazon;

    /** Imagen del cofre que aparece al morir el jugador. */
    BufferedImage cofre;

    /** Imagen de la  vida pocion que aparece en drops o en inventario */
    BufferedImage vidaPoc;

    /** Imagen de la fuerz pocion que aparece en drops o en inventario. */
    BufferedImage fuerzaPoc;

    /**
     * Índice de la opción seleccionada en los menús de título,
     * muerte y enhorabuena.
     */
    public int comandoNum = 0;

    /**
     * Estado de la pantalla de título:
     *
     *   0 – Menú principal
     *   1 – Historia / introducción
     *   2 – Pantalla de muerte
     */
    public int titleScreenState = 0;

    /**
     * Índice de la opción seleccionada dentro del menú de combate.
     * Se usa en el menú principal para moverse (ATACAR / INVENTARIO) y en el submemu para moverse (SEGURO / EQUILIBRADO / ARRIESGADO)
     */
    public int comandoNum1 = 0;

    /**
     * Sub-estado del menú de combate:
     *
     *   0 – Te muestra por pantalla esta selección principal (ATACAR / INVENTARIO)
     *   1 – Te muestra por pantalla esta selección de tipo de ataque (SEGURO / EQUILIBRADO / ARRIESGADO)
     */
    public int subState = 0;

    /**
     * Índice del cursor en el panel de información de batalla.
     * Comienza en 1 para apuntar a "Continuar" por defecto.
     * Solo tiene uno , lo usamos para remarcar la opcion disponible que tenemos
     */
    public int comandoNum2 = 1;


    public int opcionesComando = 0; //  0=Pantalla Completa, 1=Salir

    public boolean dibujadoOpciones=false;

    /**
     * Construye la UI, inicializa las fuentes y carga el icono de corazón.
     *
     * @param gp   panel principal del juego
     * @param keyH manejador de teclado
     */
    public UI(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        arial_40 = new Font("Arial", Font.BOLD, 20);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // Obtenemos la imagen del corazón a partir del objeto de vida
        SuperObject vida = new Obj_Vida();
        corazon = vida.getImagen();

        // Obtenemos la imagen del cofre del tesoro
        SuperObject cofreTesoro = new Obj_CofreTesoro();
        cofre = cofreTesoro.getImagen();

        // Obtenemos la imagen de la pocion de fuerza
        SuperObject pocionFuerza = new Obj_PocionFuerza(gp);
        fuerzaPoc = pocionFuerza.getImagen();

        // Obtenemos la imagen de la pocion de vida
        SuperObject pocionVida = new Obj_PocionVida(gp);
        vidaPoc = pocionVida.getImagen();
    }

    // --------- Zona de dibujo --------

    /**
     * Método principal de dibujo llamado cada frame desde GamePanel paintComponent.
     *
     * Delega en los métodos auxiliares según el estado actual del juego.
     *
     * @param g2 contexto gráfico del frame
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // ------ Pantalla de título (solo si el jugador no está en animación de muerte) -----
        if (gp.gameState == gp.titleState && !gp.jugador.isAnimacionMuerteTerminada) {
            drawTitleScreen();
            // Si el cursor apunta a "CRÉDITOS", mostrar la ventana emergente
            if (gp.mostrarHallofFame) {
                gp.pantallaHallOfFame.draw(g2);
            }
        }

        // ----- HUD durante el juego normal -------

        //En todos estos casos dibujamos la vida
        if (gp.gameState == gp.escenaState1 || gp.gameState == gp.escenaState2 || gp.gameState == gp.escenaState3) {
            drawJugadorVida();
            drawCronometro();

            //solo cuando estemos en la tercera y hemos terminado con gigante
            if(gp.gameState == gp.escenaState3  && gp.ispeleaFinalizada){
                drawCofre();
            }
        }

        // -------- HUD durante la pausa ------
        //Lo mismo que lo anterior pero en pause
        if (gp.gameState == gp.pauseState1 || gp.gameState == gp.pauseState2 || gp.gameState == gp.pauseState3) {
            drawJugadorVida();
            drawCronometro();
            drawPauseScreen();

            if(gp.gameState == gp.pauseState3  && gp.ispeleaFinalizada){
                drawCofre();
            }
        }

        // ------- Texto de guía de interacción (puertas, peleas, piso 3) -------
        if (gp.jugador.cercaPuerta || gp.jugador.cercaPelea || gp.jugador.cercaIrPiso3 || gp.jugador.cercaPeleaFinal || gp.jugador.cercaCongratulations && !keyH.ePressed) {
            drawTextoGuia();
        }

        // ------- Estado de combate ------------
        if (gp.gameState == gp.statePelea || gp.gameState==gp.statePelea2) {
            drawJugadorVida();
            drawCronometro();
            drawEnemigoVida();

            //Si estamos en pelea y nuestro turno entonces dibujamos
            if (gp.isSituacionPelea && gp.jugadorTurno) {
                // Es el turno del jugador y no hay resultado pendiente: mostrar menú
                drawCombatMenu();
                //si es false entonces dibujamos la informacuion
            } else if (!gp.isSituacionPelea) {
                // Hay un resultado de ataque que informar al jugador
                drawInformacionBatalla();
            }
        }

        // ---- Pantalla de opciones , para cualquier momento pero peleas o pauses -----
        if(keyH.oPressed && (gp.gameState==gp.escenaState1 || gp.gameState==gp.escenaState2 || gp.gameState==gp.escenaState3)){
            drawOpciones();
            dibujadoOpciones=true;
        }


        // ---- Pantalla de muerte (se activa cuando la animación de muerte termina) -----
        if (gp.jugador.isAnimacionMuerteTerminada && gp.gameState == gp.titleState) {
            gp.stopMusic();
            titleScreenState = 2;
            drawTitleScreen();
        }

        // -------- Pantalla de enhorabuena al completar el juego -------
        if (gp.gameState == gp.congratulationsState) {
            drawCongratulationsPantalla();
        }
        // -------- Pantalla de hallofame al completar el juego -------
        if (gp.gameState == gp.hallOfFameState) {
            gp.pantallaHallOfFame.draw(g2);
        }

        //Si el inventario se vuelve abierto , entonces mostramos contenido
        if (gp.inventarioAbierto) {
            abrirInventario();
        }

        //si objeto dropeado no es null , en escenas que no son combates
        if (gp.objetoDropeado != null && (gp.gameState == gp.escenaState2)) {
            //dibujamos la imagen en el suelo
            g2.drawImage(gp.objetoDropeado.getImagen(), gp.dropX, gp.dropY, gp.tamañoMosaico, gp.tamañoMosaico, null);
        }


    }

    // ------- HUD de vida ------------

    /**
     * Dibuja la barra de vida del jugador en la esquina inferior izquierda.
     *
     * La barra se escala con baseUnit píxeles por punto de vida,
     * de modo que el tamaño refleja tanto la vida actual como la máxima.
     */
    public void drawJugadorVida() {
        int baseUnit = 2; // px por punto de vida

        int barraX = 60;   // posición X de la barra
        int barraY = 525;  // posición Y de la barra
        int corazonX = 28; // posición X del icono de corazón
        int corazonY = 518; // posición Y del icono de corazón

        double hpBarValue    = gp.jugador.getLife() * baseUnit;
        double maxHpBarValue = gp.jugador.getBarraVida() * baseUnit;

        // Fondo oscuro (vida perdida)
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barraX, barraY, (int) maxHpBarValue, 15);

        // Vida actual en rojo
        g2.setColor(Color.RED);
        g2.fillRect(barraX, barraY, (int) hpBarValue, 15);

        // Borde blanco
        g2.setColor(Color.WHITE);
        g2.drawRect(barraX, barraY, (int) maxHpBarValue, 15);

        // Icono de corazón
        g2.drawImage(corazon, corazonX, corazonY, 40, 25, null);



    }

    /**
     * Dibuja el tiempo en la esquina superior derecha.
     *
     */
    public void drawCronometro() {

        long ms  = gp.cronometro.getTiempoTranscurridoMs();
        long seg = (ms / 1000) % 60;
        long min = ms / 60000;
        g2.drawString(String.format("%02d:%02d", min, seg), gp.pantallaAnchura - 110, 30);
    }


    /**
     * Dibuja la barra de vida del enemigo actual encima del enemigo.
     *
     * Usa gp.enemigoActual para ser compatible con cualquier enemigo presente
     * en el combate, sin importar su tipo concreto.
     */
    public void drawEnemigoVida() {
        // Guardamos en variable local para evitar referencias nulas si cambia mid-frame

        if(gp.enemigoActual==gp.samuraiErrante) {
            int baseUnit = 2;

            int barraX = 850;
            int barraY = 255;


            double hpBarValue = gp.enemigoActual.getLife() * baseUnit;
            double maxHpBarValue = gp.enemigoActual.getBarraVida() * baseUnit;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barraX, barraY, (int) maxHpBarValue, 15);

            g2.setColor(Color.RED);
            g2.fillRect(barraX, barraY, (int) hpBarValue, 15);

            g2.setColor(Color.WHITE);
            g2.drawRect(barraX, barraY, (int) maxHpBarValue, 15);


        }
        else if (gp.enemigoActual == gp.gigante) {
            int baseUnit = 2;

            int barraX = 710;
            int barraY = 105;


            double hpBarValue = gp.enemigoActual.getLife() * baseUnit;
            double maxHpBarValue = gp.enemigoActual.getBarraVida() * baseUnit;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barraX, barraY, (int) maxHpBarValue, 15);

            g2.setColor(Color.RED);
            g2.fillRect(barraX, barraY, (int) hpBarValue, 15);

            g2.setColor(Color.WHITE);
            g2.drawRect(barraX, barraY, (int) maxHpBarValue, 15);

        }
    }
    // ------- Dibujar Cofre ---------


    /**
     * Dibuja el cofre
     *
     */
    public void drawCofre(){
        gp.cofreAparecido=true;
        int cofreX = 370; // posición X del icono de cofre
        int cofreY = 280;// posición y del icono de cofre

        g2.drawImage(cofre, cofreX, cofreY, 100, 110, null);
    }



    // --------- Pantallas de menú y estado -------

    /**
     * Dibuja el texto "PAUSADO" centrado en pantalla.
     */
    public void drawPauseScreen() {
        String text = "PAUSADO";
        int x = getXforCenteredText(text);
        int y = gp.pantallaAltura / 2;
        g2.drawString("PAUSADO", x, y);
    }

    /**
     * Dibuja la pantalla de título según el estado  #titleScreenState
     *
     *   Estado 0: menú principal con logo, animación del jugador y opciones.
     *   Estado 1: texto de historia e introducción al juego.
     *   Estado 2: pantalla de muerte con opción de salir.
     */
    public void drawTitleScreen() {

        if (titleScreenState == 0) {
            // ── Fondo oscuro ──
            g2.setColor(new Color(7, 10, 18));
            g2.fillRect(0, 0, gp.pantallaAnchura, gp.pantallaAltura);

            // ── Título del juego ──
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "El Viaje del Heroe";
            int x = getXforCenteredText(text);
            int y = gp.tamañoMosaico * 3;

            // Sombra del título
            g2.setColor(Color.black);
            g2.drawString(text, x + 5, y + 5);

            // Color principal del título
            g2.setColor(new Color(209, 0, 28));
            g2.drawString(text, x, y);

            // ── Animación idle del personaje ──
            x = (gp.pantallaAnchura / 2) - 90;
            y = y + gp.tamañoMosaico * 2;
            gp.jugador.animacionQuieto();
            BufferedImage Image = (BufferedImage) gp.jugador.dibujarQuieto();
            g2.drawImage(Image, x - 20, y - 50,
                    gp.tamañoMosaico * 5, gp.tamañoMosaico * 5, null);

            // ── Opciones del menú ──
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NUEVA PARTIDA";
            x = getXforCenteredText(text);
            y = y + gp.tamañoMosaico * 5;
            g2.drawString(text, x, y);
            if (comandoNum == 0) g2.drawString(">", x - gp.tamañoMosaico, y);

            text = "HALL OF FAME";
            x = getXforCenteredText(text);
            y = y + gp.tamañoMosaico;
            g2.drawString(text, x, y);
            if (comandoNum == 1) g2.drawString(">", x - gp.tamañoMosaico, y);

            text = "SALIR";
            x = getXforCenteredText(text);
            y = y + gp.tamañoMosaico;
            g2.drawString(text, x, y);
            if (comandoNum == 2) g2.drawString(">", x - gp.tamañoMosaico, y);

        } else if (titleScreenState == 1) {
            // ── Texto de introducción / historia ──
            g2.setColor(new Color(209, 0, 28));
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));

            String text = "El mayor enemigo de tu reino se ha abierto paso y ";
            int x = getXforCenteredText(text);
            int y = gp.tamañoMosaico * 3;
            g2.drawString(text, x, y);

            text = "ha tomado el cofre del tesoro del rey , no hay nadie ";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "que se atreva hacerle frente menos tú, un simple campesino ";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "que tomo la antigua armadura familiar y su espada que se ";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "dirige alli para recuperar lo robado y volver siendo un heroe ";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            // Botón para empezar
            text = "COMENZAR";
            x = getXforCenteredText(text); y += gp.tamañoMosaico * 2;
            g2.drawString(text, x, y);
            if (comandoNum == 0) g2.drawString(">", x - gp.tamañoMosaico, y);

        } else if (titleScreenState == 2) {
            // ── Pantalla de muerte ──
            g2.setColor(new Color(209, 0, 28));
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));

            String muerteTexto = "HAS MUERTO";
            g2.drawString(muerteTexto,
                    getXforCenteredText(muerteTexto), gp.pantallaAltura / 2);

            String reinicio = "Salir";
            g2.drawString(reinicio,
                    getXforCenteredText(reinicio), (gp.pantallaAltura / 2) + 70);
            if (comandoNum == 0) {
                g2.drawString(">",
                        getXforCenteredText(reinicio) - 30, (gp.pantallaAltura / 2) + 70);
            }
        }
    }

    //-------- Utilidad de centrado -------

    /**
     * Calcula la coordenada X necesaria para centrar un texto en la pantalla.
     *
     * @param text texto a centrar
     * @return coordenada X de inicio del texto centrado
     */
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.pantallaAnchura / 2 - length / 2;
    }

    // ------- Texto de guía contextual ---------

    /**
     * Muestra cajas de texto emergentes cuando el jugador está cerca de un
     * punto de interés (puerta, zona de pelea, acceso al siguiente piso).
     */
    public void drawTextoGuia() {
        if (gp.jugador.cercaPuerta) {
            dibujarCajaTexto(250, 150, 400, 100,
                    "Pulsa E para entrar al castillo");
        }

        if (gp.jugador.cercaPelea && !gp.ispeleaFinalizada) {
            dibujarCajaTexto(250, 150, 400, 100,
                    "Pulsa E para empezar la lucha");
        }

        if(gp.jugador.cercaPeleaFinal && !gp.ispeleaFinalizada){
            dibujarCajaTexto(250, 150, 400, 100,
                    "Pulsa E para empezar la lucha");
        }

        if (gp.jugador.cercaIrPiso3 && gp.ispeleaFinalizada) {
            dibujarCajaTexto(450, 150, 400, 100,
                    "Pulsa E para ir al siguiente piso");
        }

        if (gp.jugador.cercaCongratulations && gp.ispeleaFinalizada && gp.cofreAparecido) {
            dibujarCajaTexto(450, 150, 400, 100,
                    "Pulsa E para abrir el tesoro");
        }
    }

    /**
     * Método auxiliar privado que dibuja una caja semitransparente con texto.
     *
     * @param x      posición X de la caja
     * @param y      posición Y de la caja
     * @param ancho  anchura de la caja
     * @param alto   altura de la caja
     * @param texto  texto a mostrar dentro de la caja
     */
    private void dibujarCajaTexto(int x, int y, int ancho, int alto, String texto) {
        g2.setColor(new Color(0, 0, 0, 200)); // fondo negro semitransparente
        g2.fillRect(x, y, ancho, alto);
        g2.setColor(Color.white);
        g2.drawRect(x, y, ancho, alto);
        g2.setFont(g2.getFont().deriveFont(20F));
        g2.drawString(texto, x + 20, y + 55);
    }

    //-------- Menú de combate ------------

    /**
     * Dibuja el menú de combate en la parte inferior de la pantalla.
     *
     * En subState 0 muestra las acciones principales (ATACAR, INVENTARIO).
     * En  subState 1 muestra los tipos de ataque (DÉBIL, EQUILIBRADO, FUERTE).
     */
    public void drawCombatMenu() {
        int x = 220, y = 502, width = 800, height = 115;

        // Marco del menú
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);

        if (subState == 0) {
            // ── Menú principal: ATACAR / INVENTARIO ──
            g2.drawString("ATACAR", x + 60, y + 40);
            if (comandoNum1 == 0) g2.drawString(">", x + 30, y + 40);

            g2.drawString("INVENTARIO", x + 60, y + 90);
            if (comandoNum1 == 1) g2.drawString(">", x + 30, y + 90);

        } else if (subState == 1) {
            // ── Submenú de tipos de ataque ──
            g2.drawString("SEGURO", x + 50, y + 30);
            if (comandoNum1 == 0) g2.drawString(">", x + 30, y + 30);

            g2.drawString("EQUILIBRADO", x + 50, y + 60);
            if (comandoNum1 == 1) g2.drawString(">", x + 30, y + 60);

            g2.drawString("ARRIESGADO", x + 50, y + 90);
            if (comandoNum1 == 2) g2.drawString(">", x + 30, y + 90);

            // Pista de navegación
            g2.setFont(g2.getFont().deriveFont(10F));
            g2.drawString("Presiona ESC para volver", x + 270, y + 110);
        }
    }

    // -------- Pantalla de victoria ----------

    /**
     * Muestra la pantalla de enhorabuena al completar el juego,
     * con un mensaje central y la opción de continuar (que lleva al Hall of Fame).
     */
    public void drawCongratulationsPantalla() {
        g2.setColor(new Color(209, 0, 28));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));

        String finJuegoTexto = "ENHORABUENA , HAS TERMINADO EL JUEGO";
        g2.drawString(finJuegoTexto,
                getXforCenteredText(finJuegoTexto), gp.pantallaAltura / 2);

        String salirJuego = "Continuar";
        g2.drawString(salirJuego,
                getXforCenteredText(salirJuego), (gp.pantallaAltura / 2) + 70);
        if (comandoNum == 0) {
            g2.drawString(">",
                    getXforCenteredText(salirJuego) - 30, (gp.pantallaAltura / 2) + 70);
        }
    }



    //  -------- Inventario --------

    /**
     * Abre el inventario durante el combate, mostrando los objetos que el jugador ha recogido.
     */
    public void abrirInventario() {
        int x = 220, y = 100, width = 700, height = 400;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString("INVENTARIO", x + 20, y + 30);

        int slotSize = 64;  // Tamaño de cada casilla del inventario (ancho y alto del slot)
        int padding  = 16; // separación entre casillas
        int cols     = 6;   // Número de columnas del inventario (6 slots por fila)

        for (int i = 0; i < gp.inventario.objetos.size(); i++) {
            SuperObject obj = gp.inventario.objetos.get(i);
            // Objeto actual del inventario que se va a dibujar


            // Calcula la columna  dentro de la fila
            // Cuando llega a 6, vuelve a 0 (0-5)
            int col = i % cols;

            // Calcula la fila
            // Cada 6 objetos baja a la siguiente fila
            int row = i / cols;

            // Posición X en pantalla:
            // punto inicial + desplazamiento por columna
            int sx  = x + 20 + col * (slotSize + padding);


            // Posición Y en pantalla:
            // punto inicial + desplazamiento por fila
            // +15 extra para separación visual o espacio de texto
            int sy  = y + 50 + row * (slotSize + padding + 15);

            g2.setColor(new Color(50, 50, 50, 180));
            g2.fillRect(sx, sy, slotSize, slotSize);
            g2.setColor(Color.GRAY);
            g2.drawRect(sx, sy, slotSize, slotSize);

            //Si estamos en el slot del inventario , entonces lo resaltamos
            if (i == gp.inventarioSlot) {
                g2.setColor(new Color(200, 162, 82, 150));
                g2.fillRect(sx, sy, slotSize, slotSize);
            }

            //Lo dibujamos en cada parte si el objeto guardado no es nul cada vez moviendolo
            if (obj.getImagen() != null) {
                g2.drawImage(obj.getImagen(), sx + 8, sy + 8, 48, 48, null);
            }

            //Le dibujamos el nombre
            g2.setFont(g2.getFont().deriveFont(10F));
            g2.setColor(Color.WHITE);
            g2.drawString(obj.getName(), sx, sy + slotSize + 12);
        }

        // ------- Descripción del objeto seleccionado------

        //Si no esta vacio , entonces esccribiremos lo que hace
        if (!gp.inventario.objetos.isEmpty()) {
            SuperObject objSeleccionado = gp.inventario.objetos.get(gp.inventarioSlot);

            // Caja de descripción en la parte inferior
            int descY = y + height - 80;
            g2.setColor(new Color(30, 30, 30, 220));
            g2.fillRect(x + 10, descY, width - 20, 60);
            g2.setColor(Color.YELLOW);
            g2.drawRect(x + 10, descY, width - 20, 60);

            // Nombre del objeto
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
            g2.setColor(Color.YELLOW);
            g2.drawString(objSeleccionado.getName(), x + 20, descY + 20);

            // Descripción de lo que hace
            g2.setFont(g2.getFont().deriveFont(12F));
            g2.setColor(Color.WHITE);
            g2.drawString(objSeleccionado.getDescripcion(), x + 20, descY + 42);
        }

        g2.setFont(g2.getFont().deriveFont(12F));
        g2.setColor(Color.GRAY);
        g2.drawString("ESC para cerrar", x + 20, y + height - 88);
    }
    // ------- Información de batalla --------

    /**
     * Muestra en pantalla el resultado del último ataque (jugador o enemigo),
     * incluyendo si falló, cuánto daño hizo y cuánta vida le queda al objetivo.
     *
     * Usa gp.enemigoActual para ser compatible con cualquier enemigo presente,
     * sin necesidad de conocer su tipo concreto.
     *
     * También gestiona la pantalla de habilidad especial del enemigo.
     * El jugador debe pulsar ENTER sobre "Continuar" para avanzar al siguiente turno.
     */
    public void drawInformacionBatalla() {
        if (gp.enemigoActual == null) return;//Volvemos si no hay ningun enemigo pero esto no deberia pasar porque se asigna antes de encontrarlo y pelear

        int x = 220, y = 502, width = 800, height = 115;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);

        // Alias genérico: funciona con SamuraiErrante, Gigante, o cualquier Enemigo futuro
        Enemigo enemigo = gp.enemigoActual;

        // --- Habilidad especial del enemigo (prioridad máxima) ---
        if (enemigo.seHaMostradoPantalla) {
            if(enemigo instanceof samuraiErrante) {
                g2.drawString(
                        "El " + enemigo.getNombreEnemigo() + " ha activado su habilidad especial. Su fuerza aumenta 2 puntos",
                        x + 50, y + 40);
                g2.drawString("Continuar", x + 50, y + 90);
                if (comandoNum2 == 1) g2.drawString(">", x + 30, y + 90);
                return; // no seguir dibujando nada más
            }else if(enemigo instanceof Gigante){
                g2.drawString(
                        "El " + enemigo.getNombreEnemigo() + " ha activado su habilidad especial. Su fuerza aumenta 3 puntos",
                        x + 50, y + 40);
                g2.drawString("Continuar", x + 50, y + 90);
                if (comandoNum2 == 1) g2.drawString(">", x + 30, y + 90);
                return; // no seguir dibujando nada más
            }
        }

        if (gp.jugador.isFueAtaque()) {
            // ---- Resultado del ataque del jugador ----
            if (gp.jugador.isHeFallado() && gp.enemigoActual instanceof samuraiErrante) {
                g2.drawString(
                        "El heroe ha fallado el ataque , el enemigo ha usado contrataque",
                        x + 50, y + 40);
            } else {
                g2.drawString(
                        "El heroe ha acertado el ataque ha hecho "
                                + gp.jugador.getDañoHecho() + " puntos de daño",
                        x + 50, y + 40);
                g2.drawString(
                        "y ha dejado a "+ enemigo.getNombreEnemigo() + " con " + enemigo.getLife(),
                        x + 50, y + 60);
            }
            g2.drawString("Continuar", x + 50, y + 90);
            if (comandoNum2 == 1) g2.drawString(">", x + 30, y + 90);

        } else if (enemigo.isFueAtaque()) {
            // ----- Resultado del ataque del enemigo -----
            if (enemigo.isHeFallado()) {
                g2.drawString("El "+ enemigo.getNombreEnemigo() + " ha fallado el ataque", x + 50, y + 40);
            } else if(!enemigo.isHeFallado() && enemigo instanceof Gigante &&  gp.gigante.fueStun== true){
                g2.drawString(
                        "El "+ enemigo.getNombreEnemigo() + " ha acertado el ataque , ha hecho "
                                + enemigo.getDañoHecho() + " puntos de daño",
                        x + 50, y + 40);
                g2.drawString(
                        "y ha dejado al heroe con " + gp.jugador.getLife() +" . Ha stuneado al heroe",
                        x + 50, y + 60);
            }else{
                g2.drawString(
                        "El "+ enemigo.getNombreEnemigo() + " ha acertado el ataque , ha hecho "
                                + enemigo.getDañoHecho()  + " puntos de daño",
                        x + 50, y + 40);
                g2.drawString(
                        "y ha dejado al heroe con " + gp.jugador.getLife(),
                        x + 50, y + 60);
            }
            g2.drawString("Continuar", x + 50, y + 90);
            if (comandoNum2 == 1) g2.drawString(">", x + 30, y + 90);
        }
    }


    /**
     * Dibuja el menú de opciones del juego.
     *
     * <p>Este menú muestra diferentes configuraciones como:
     * pantalla completa y opción de salir. También dibuja un
     * indicador (>) para señalar la opción seleccionada.</p>
     */
    public void drawOpciones() {
        g2.setColor(Color.BLACK);
        g2.fillRect(380, 80, 350, 360);
        g2.setColor(Color.white);
        g2.drawRect(380, 80, 350, 360);

        g2.setFont(g2.getFont().deriveFont(34F));
        String text = "OPCIONES";
        g2.drawString(text, getXforCenteredText(text), 120);

        g2.setFont(g2.getFont().deriveFont(18F));


        if (gp.pantallaCompleta) {
            text = "Pantalla Completa: ON";
        } else {
            text = "Pantalla Completa: OFF";
        }
        g2.drawString(text, 420, 280);
        if (opcionesComando == 0) g2.drawString(">", 400, 280);

        text = "Salir";
        g2.drawString(text, 420, 340);
        if (opcionesComando == 1) g2.drawString(">", 400, 340);
    }
}