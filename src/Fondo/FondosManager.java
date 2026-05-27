package Fondo;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Gestor de fondos del juego.
 *
 * Se encarga de cargar todas las imágenes de fondo desde los recursos
 * del proyecto y de dibujarlas en pantalla por capas para cada escena del juego.
 *
 *
 * El fondo está compuesto por varias capas PNG superpuestas en orden
 */
public class FondosManager {

    /** Referencia al panel principal para acceder a las dimensiones de pantalla. */
    GamePanel gp;

    /**
     * Array de capas de fondo para la escena 1 (exterior del castillo).
     */
    Fondo[] fondos1;

    /**
     * Array de capas de fondo para la escena 2 (interior / sala del samurái).
     */
    Fondo[] fondos2;

    /**
     * Array de capas de fondo para la escena 2 (interior / sala del samurái).
     */
    Fondo[] fondos3;



    // ---- CONSTRUCTOR ------


    /**
     * Inicializa el gestor de fondos, reserva los arrays de capas y
     * carga todas las imágenes de fondo desde los recursos.
     *
     * @param gp panel principal del juego
     */
    public FondosManager(GamePanel gp) {
        this.gp = gp;

        // Reservamos 15 slots para las capas de la escena 1
        fondos1 = new Fondo[15];
        for (int i = 0; i < fondos1.length; i++) {
            //Debemos iniciar cada posicion , las inicializamos como Fondo
            fondos1[i] = new Fondo();
        }

        // Reservamos 5 slots para las capas de la escena 2
        fondos2 = new Fondo[5];
        for (int i = 0; i < fondos2.length; i++) {
            //Debemos iniciar cada posicion , las inicializamos como Fondo
            fondos2[i] = new Fondo();
        }

        fondos3 = new Fondo[5];
        for (int i = 0; i < fondos3.length; i++) {
            //Debemos iniciar cada posicion , las inicializamos como Fondo
            fondos3[i] = new Fondo();
        }

        // Cargamos todas las imágenes al iniciar (solo una vez en memoria)
        getTileImage1();
    }

    // ════════════════════════════════════════════════════════════════════════
    // CARGA DE RECURSOS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Carga desde el classpath todas las imágenes PNG de fondo
     * para todas escenas del juego.
     *
     * Las imágenes de la escena 1 ( fondo/f1/) van de capa_0 a capa_11,
     * más el elemento decorativo del castillete y la franja de suelo/escritura.
     *
     * Las imágenes de la escena 2 ( fondo/f2/) son cuatro capas
     * (plan_0, plan_2, plan_3 y fondoEscritura).
     *
     . Las imágenes de la escena 3 ( fondo/f3/) son cuatro capas
     * (plan_0,plan_1, plan_2, plan_3 y fondoEscritura).
     */
    public void getTileImage1() {
        try {
            // ------ Capas de la escena 1  --------
            fondos1[0].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_0.png"));
            fondos1[1].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_1.png"));
            fondos1[2].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_2.png"));
            fondos1[3].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_3.png"));
            fondos1[4].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_4.png"));
            fondos1[5].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_5.png"));
            fondos1[6].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_6.png"));
            fondos1[7].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_7.png"));
            fondos1[8].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_8.png"));
            fondos1[9].imagen  = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_9.png"));
            fondos1[10].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_10.png"));
            fondos1[11].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/capa_11.png"));
            fondos1[12].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/castillete.png"));  // Castillo
            fondos1[13].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/fondoEscritura.png"));  //Franja de escritura juego

            // ------ Capas de la escena 2  --------
            fondos2[0].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_0.png"));
            fondos2[1].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_2.png"));
            fondos2[2].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_3.png"));
            fondos2[3].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/fondoEscritura.png"));// Franja  de escritura juego de la escena 2 (mismo recurso que escena 1)


            // ------ Capas de la escena 3  --------
            fondos3[3].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_0.png"));
            fondos3[2].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_1.png"));
            fondos3[1].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_2.png"));
            fondos3[0].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_3.png"));
            fondos3[4].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/fondoEscritura.png")); // Franja  de escritura juego de la escena 3 (mismo recurso que escena 1 y 2)


        } catch (IOException e) {
            // Si falta algún recurso, el juego no puede continuar correctamente
            System.err.println("[TileManager] Error cargando imágenes de fondo:");
            e.printStackTrace();
        }
    }


    // RENDERIZADO

    /**
     * Dibuja el fondo de la escena 1 (exterior del castillo) por capas.
     *
     * El orden de dibujado va de la capa más lejana (índice 0)
     * a la más cercana . Todas las capas principales
     * se estiran a lo ancho de la pantalla y se desplazan -210 px en Y
     * para encuadrar correctamente el arte dentro de la ventana de juego.
     *
     *
     * Excepciones de posición y tamaño:
     *
     *    fondos1[7] solo 70 px de alto (franja puntual, posiblemente niebla).
     *   fondos1[12] (castillete): posicionado en la esquina derecha con tamaño fijo.
     *   fondos1[13] (escritura): se extiende más allá de los bordes para centrar la franja al gusto.
     *
     *
     * @param g contexto gráfico 2D , lo recibe de PaintComponent
     */
    public void draw1(Graphics2D g) {
        // ------ Capas de paisaje  ------
        g.drawImage(fondos1[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[3].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[4].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[5].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[6].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[7].imagen,    0, -210, gp.pantallaAnchura,  70, null);
        g.drawImage(fondos1[8].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[9].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[10].imagen,   0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[11].imagen,   0, -210, gp.pantallaAnchura, 700, null);

        // Silueta del castillo: esquina derecha, tamaño y posición fijos
        g.drawImage(fondos1[12].imagen, 780, 180, 360, 275, null);

        //Franja de escritura juego: se extiende más allá del borde izquierdo
        // para que el recorte quede centrado visualmente
        g.drawImage(fondos1[13].imagen, -350, 490, 1800, 140, null);
    }

    /**
     * Dibuja el fondo de la escena 2 (interior del castillo) por capas.
     *
     * Usa el mismo desplazamiento vertical (-210 px) y anchura de pantalla
     * que draw1(Graphics2D), pero con solo cuatro capas al ser
     * un escenario más sencillo.
     *
     *
     * @param g contexto gráfico 2D de PaintComponent
     */
    public void draw2(Graphics2D g) {
        // ------- Capas de interior -------
        g.drawImage(fondos2[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);

        //Franja de escritura juego
        g.drawImage(fondos2[3].imagen, -350, 490, 1800, 140, null);
    }


    public void draw3(Graphics2D g) {
        // ── Capas de interior (fondo → primer plano) ─────────────────────────
        g.drawImage(fondos3[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[3].imagen,    0, -210, gp.pantallaAnchura, 700, null);

        //Franja de escritura juego
        g.drawImage(fondos3[4].imagen, -350, 490, 1800, 140, null);
    }
}