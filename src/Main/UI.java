package Main;

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
    BufferedImage cofre;
    BufferedImage vidaPoc;
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
     * Se usa en el submenú principal (ATACAR / INVENTARIO).
     */
    public int comandoNum1 = 0;

    /**
     * Sub-estado del menú de combate:
     *
     *   0 – Selección principal (ATACAR / INVENTARIO)
     *   1 – Selección de tipo de ataque (DÉBIL / EQUILIBRADO / FUERTE)
     *
     */
    public int subState = 0;

    /**
     * Índice del cursor en el panel de información de batalla.
     * Comienza en 1 para apuntar a "Continuar" por defecto.
     */
    public int comandoNum2 = 1;


    public int opcionesComando = 0; // 0=Sonido, 1=Pantalla Completa, 2=Salir

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
        SuperObject vida = new Obj_Vida(gp);
        corazon = vida.getImagen();

        SuperObject cofreTesoro = new Obj_CofreTesoro(gp);
        cofre = cofreTesoro.getImagen();

        SuperObject pocionFuerza = new Obj_PocionFuerza(gp);
        fuerzaPoc = pocionFuerza.getImagen();

        SuperObject pocionVida = new Obj_PocionVida(gp);
        vidaPoc = pocionVida.getImagen();
    }

    // ── Punto de entrada del renderizado ────────────────────────────────────

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

        // ── Pantalla de título (solo si el jugador no está en animación de muerte) ──
        if (gp.gameState == gp.titleState && !gp.jugador.isAnimacionMuerteTerminada) {
            drawTitleScreen();
            // Si el cursor apunta a "CRÉDITOS", mostrar la ventana emergente
            if (comandoNum == 1) {
                drawCreditos();
            }
        }

        // ** HUD durante el juego normal *****************
        if (gp.gameState == gp.escenaState1 || gp.gameState == gp.escenaState2 || gp.gameState == gp.escenaState3) {
            drawJugadorVida();

            if(gp.gameState == gp.escenaState3  && gp.ispeleaFinalizada){
                drawCofre();
            }
        }

        // ── HUD durante la pausa ─────────────────────────────────────────────
        if (gp.gameState == gp.pauseState1 || gp.gameState == gp.pauseState2 || gp.gameState == gp.pauseState3) {
            drawJugadorVida();
            drawPauseScreen();

            if(gp.gameState == gp.pauseState3  && gp.ispeleaFinalizada){
                drawCofre();
            }
        }

        // ── Texto de guía de interacción (puertas, peleas, piso 3) ──────────
        if (gp.jugador.cercaPuerta || gp.jugador.cercaPelea || gp.jugador.cercaIrPiso3 || gp.jugador.cercaPeleaFinal || gp.jugador.cercaCongratulations && !keyH.ePressed) {
            drawTextoGuia();
        }

        // ── Estado de combate ────────────────────────────────────────────────
        if (gp.gameState == gp.statePelea || gp.gameState==gp.statePelea2) {
            drawJugadorVida();
            drawEnemigoVida();

            if (gp.isSituacionPelea && gp.jugadorTurno) {
                // Es el turno del jugador y no hay resultado pendiente: mostrar menú
                drawCombatMenu();
            } else if (!gp.isSituacionPelea) {
                // Hay un resultado de ataque que informar al jugador
                drawInformacionBatalla();
            }
        }


        if(keyH.oPressed && (gp.gameState==gp.escenaState1 || gp.gameState==gp.escenaState2 || gp.gameState==gp.escenaState3)){
            drawOpciones();
            dibujadoOpciones=true;
        }


        // ── Pantalla de muerte (se activa cuando la animación de muerte termina) ──
        if (gp.jugador.isAnimacionMuerteTerminada && gp.gameState == gp.titleState) {
            gp.stopMusic();
            titleScreenState = 2;
            drawTitleScreen();
        }

        // ── Pantalla de enhorabuena al completar el juego ────────────────────
        if (gp.gameState == gp.congratulationsState) {
            drawCongratulationsPantalla();
        }
        if (gp.gameState == gp.hallOfFameState) {
            gp.pantallaHallOfFame.draw(g2);
        }


        if (gp.inventarioAbierto) {
            abrirInventario();
        }

        if (gp.objetoDropeado != null && (gp.gameState == gp.escenaState2 || gp.gameState == gp.escenaState3)) {
            g2.drawImage(gp.objetoDropeado.getImagen(), gp.dropX, gp.dropY,
                    gp.tamañoMosaico, gp.tamañoMosaico, null);
        }


    }

    // ── HUD de vida ──────────────────────────────────────────────────────────

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
        int corazonY = 518;

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


        long ms  = gp.cronometro.getTiempoTranscurridoMs();
        long seg = (ms / 1000) % 60;
        long min = ms / 60000;
        g2.drawString(String.format("%02d:%02d", min, seg), gp.pantallaAnchura - 110, 30);
    }

    /**
     * Dibuja la barra de vida del enemigo actual en la parte superior derecha.
     *
     * Usa gp.enemigoActual para ser compatible con cualquier enemigo presente
     * en el combate, sin importar su tipo concreto.
     */
    public void drawEnemigoVida() {
        // Guardamos en variable local para evitar referencias nulas si cambia mid-frame
        if (gp.enemigoActual == null) return;
        else if(gp.enemigoActual==gp.samuraiErrante) {
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
    // ── Dibujar Cofre ───────────────────────────────────────────

    public void drawCofre(){
        gp.cofreAparecido=true;
        int cofreX = 370; // posición X del icono de corazón
        int cofreY = 280;

        g2.drawImage(cofre, cofreX, cofreY, 100, 110, null);
    }



    // ── Pantallas de menú y estado ───────────────────────────────────────────

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
     * Dibuja la pantalla de título según el estado {@link #titleScreenState}
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
            String text = "Stairgrave";
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

            text = "CREDITOS";
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

            String text = "Nadie sabe de dónde salió el Castillo.";
            int x = getXforCenteredText(text);
            int y = gp.tamañoMosaico * 3;
            g2.drawString(text, x, y);

            text = "Nadie llega a la cima.";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "No porque no puedan, sino porque el Castillo no lo permite.";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "Y aun así, tú sigues subiendo.";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            text = "Desgarra el camino. O conviértete en él.";
            x = getXforCenteredText(text); y += gp.tamañoMosaico;
            g2.drawString(text, x, y);

            // Botón para empezar
            text = "DESGARRAR";
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

    // ── Utilidad de centrado ─────────────────────────────────────────────────

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

    // ── Texto de guía contextual ─────────────────────────────────────────────

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

    // ── Menú de combate ──────────────────────────────────────────────────────

    /**
     * Dibuja el menú de combate en la parte inferior de la pantalla.
     *
     * En {@link #subState} 0 muestra las acciones principales (ATACAR, INVENTARIO).
     * En {@link #subState} 1 muestra los tipos de ataque (DÉBIL, EQUILIBRADO, FUERTE).
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
            g2.drawString("DEBIL", x + 50, y + 30);
            if (comandoNum1 == 0) g2.drawString(">", x + 30, y + 30);

            g2.drawString("EQUILIBRADO", x + 50, y + 60);
            if (comandoNum1 == 1) g2.drawString(">", x + 30, y + 60);

            g2.drawString("FUERTE", x + 50, y + 90);
            if (comandoNum1 == 2) g2.drawString(">", x + 30, y + 90);

            // Pista de navegación
            g2.setFont(g2.getFont().deriveFont(10F));
            g2.drawString("Presiona ESC para volver", x + 270, y + 110);
        }
    }

    // ── Pantalla de victoria ─────────────────────────────────────────────────

    /**
     * Muestra la pantalla de enhorabuena al completar el juego,
     * con opción de salir.
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

    /**
     * Dibuja la ventana emergente de créditos cuando el cursor apunta a
     * "CRÉDITOS" en el menú principal.
     */
    public void drawCreditos() {
        int x = 330, y = 180, ancho = 450, alto = 160;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, ancho, alto);
        g2.setColor(Color.white);
        g2.drawRect(x, y, ancho, alto);

        g2.setFont(g2.getFont().deriveFont(20F));
        g2.drawString("CREADO POR HAMIL Y JOSUE", x + 80, y + 55);
    }

    // ── Inventario ───────────────────────────────────────────────────────────

    /**
     * Abre el inventario durante el combate pasando el turno al enemigo
     * temporalmente mientras se navega por él.
     */
    public void abrirInventario() {
        int x = 220, y = 100, width = 700, height = 400;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString("INVENTARIO", x + 20, y + 30);

        int slotSize = 64;
        int padding  = 16;
        int cols     = 6;

        for (int i = 0; i < gp.inventario.objetos.size(); i++) {
            SuperObject obj = gp.inventario.objetos.get(i);

            int col = i % cols;
            int row = i / cols;
            int sx  = x + 20 + col * (slotSize + padding);
            int sy  = y + 50 + row * (slotSize + padding + 15);

            g2.setColor(new Color(50, 50, 50, 180));
            g2.fillRect(sx, sy, slotSize, slotSize);
            g2.setColor(Color.GRAY);
            g2.drawRect(sx, sy, slotSize, slotSize);

            if (i == gp.inventarioSlot) {
                g2.setColor(new Color(200, 162, 82, 150));
                g2.fillRect(sx, sy, slotSize, slotSize);
            }

            if (obj.getImagen() != null) {
                g2.drawImage(obj.getImagen(), sx + 8, sy + 8, 48, 48, null);
            }

            g2.setFont(g2.getFont().deriveFont(10F));
            g2.setColor(Color.WHITE);
            g2.drawString(obj.getName(), sx, sy + slotSize + 12);
        }

        // ── Descripción del objeto seleccionado ───────────────────────────────
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

            // Descripción
            g2.setFont(g2.getFont().deriveFont(12F));
            g2.setColor(Color.WHITE);
            g2.drawString(objSeleccionado.getDescripcion(), x + 20, descY + 42);
        }

        g2.setFont(g2.getFont().deriveFont(12F));
        g2.setColor(Color.GRAY);
        g2.drawString("ESC para cerrar", x + 20, y + height - 88);
    }
    // ── Información de batalla ───────────────────────────────────────────────

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
        if (gp.enemigoActual == null) return;

        int x = 220, y = 502, width = 800, height = 115;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);

        // Alias genérico: funciona con SamuraiErrante, Gigante, o cualquier Enemigo futuro
        var enemigo = gp.enemigoActual;

        // ── Habilidad especial del enemigo (prioridad máxima) ──
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
            // ── Resultado del ataque del jugador ──
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
            // ── Resultado del ataque del enemigo ──
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


    public void drawOpciones() {
        g2.setColor(Color.BLACK);
        g2.fillRect(380, 80, 350, 360);
        g2.setColor(Color.white);
        g2.drawRect(380, 80, 350, 360);

        g2.setFont(g2.getFont().deriveFont(34F));
        String text = "OPCIONES";
        g2.drawString(text, getXforCenteredText(text), 120);

        g2.setFont(g2.getFont().deriveFont(18F));

        text = "Sonido";
        g2.drawString(text, 420, 220);
        if (opcionesComando == 0) g2.drawString(">", 400, 220);

        text = gp.pantallaCompleta ? "Pantalla Completa: ON" : "Pantalla Completa: OFF";
        g2.drawString(text, 420, 280);
        if (opcionesComando == 1) g2.drawString(">", 400, 280);

        text = "Salir";
        g2.drawString(text, 420, 340);
        if (opcionesComando == 2) g2.drawString(">", 400, 340);
    }
}