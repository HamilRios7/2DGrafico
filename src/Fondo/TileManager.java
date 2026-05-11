package Fondo;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Gestor de tiles y fondos del juego.
 *
 * Se encarga de cargar todas las imágenes de fondo desde los recursos
 * del proyecto y de dibujarlas en pantalla por capas (parallax)
 * para cada escena del juego.
 *
 *
 * El fondo está compuesto por varias capas PNG superpuestas en orden,
 * lo que crea un efecto de profundidad visual sin necesidad de un motor
 * de tiles tradicional
 */
public class TileManager {

    /** Referencia al panel principal para acceder a las dimensiones de pantalla. */
    GamePanel gp;

    /**
     * Array de capas de fondo para la escena 1 (exterior del castillo).
     * Cada índice corresponde a una capa PNG distinta, ordenadas de atrás hacia adelante.
     */
    Fondo1[] fondos1;

    /**
     * Array de capas de fondo para la escena 2 (interior / sala del samurái).
     * Mismo concepto que #fondos1 pero con menos capas.
     */
    Fondo2[] fondos2;

    /**
     * Array de capas de fondo para la escena 2 (interior / sala del samurái).
     * Mismo concepto que #fondos1 pero con menos capas.
     */
    Fondo3[] fondos3;


    // ════════════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Inicializa el gestor de tiles, reserva los arrays de capas y
     * carga todas las imágenes de fondo desde los recursos.
     *
     * @param gp panel principal del juego
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        // Reservamos 15 slots para las capas de la escena 1
        fondos1 = new Fondo1[15];
        for (int i = 0; i < fondos1.length; i++) {
            fondos1[i] = new Fondo1();
        }

        // Reservamos 5 slots para las capas de la escena 2
        fondos2 = new Fondo2[5];
        for (int i = 0; i < fondos2.length; i++) {
            fondos2[i] = new Fondo2();
        }

        fondos3 = new Fondo3[5];
        for (int i = 0; i < fondos3.length; i++) {
            fondos3[i] = new Fondo3();
        }

        // Cargamos todas las imágenes al iniciar (solo una vez en memoria)
        getTileImage1();
    }

    // ════════════════════════════════════════════════════════════════════════
    // CARGA DE RECURSOS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Carga desde el classpath todas las imágenes PNG de fondo
     * para ambas escenas del juego.
     *
     * Las imágenes de la escena 1 ( fondo/f1/) van de capa_0 a capa_11,
     * más el elemento decorativo del castillete y la franja de suelo/escritura.
     *
     * Las imágenes de la escena 2 ( fondo/f2/) son cuatro capas
     * (plan_0, plan_2, plan_3 y fondoEscritura).
     *
     * Se usa  getClassLoader().getResourceAsStream() para garantizar
     * compatibilidad tanto al ejecutar desde el IDE como desde un JAR empaquetado.
     */
    public void getTileImage1() {
        try {
            // ── Capas de la escena 1 (exterior / cielo / terreno) ────────────
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

            // Elemento decorativo: silueta del castillo al fondo derecho
            fondos1[12].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/castillete.png"));

            // Franja de suelo / texto decorativo en la parte inferior
            fondos1[13].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f1/fondoEscritura.png"));

            // ── Capas de la escena 2 (interior del castillo) ─────────────────
            fondos2[0].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_0.png"));
            fondos2[1].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_2.png"));
            fondos2[2].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/plan_3.png"));

            // Franja inferior de la escena 2 (mismo recurso que escena 1)
            fondos2[3].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f2/fondoEscritura.png"));


            // ── Capas de la escena 3 (interior del castillo) ─────────────────
            fondos3[3].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_0.png"));
            fondos3[2].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_1.png"));
            fondos3[1].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_2.png"));
            fondos3[0].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/plan_3.png"));
            // Franja inferior de la escena 2 (mismo recurso que escena 1)
            fondos3[4].imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream("fondo/f3/fondoEscritura.png"));


        } catch (IOException e) {
            // Si falta algún recurso, el juego no puede continuar correctamente
            System.err.println("[TileManager] Error cargando imágenes de fondo:");
            e.printStackTrace();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // RENDERIZADO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Dibuja el fondo de la escena 1 (exterior del castillo) por capas.
     *
     * El orden de dibujado va de la capa más lejana (índice 0, cielo)
     * a la más cercana (suelo, decorados). Todas las capas principales
     * se estiran a lo ancho de la pantalla y se desplazan -210 px en Y
     * para encuadrar correctamente el arte dentro de la ventana de juego.
     *
     *
     * Excepciones de posición y tamaño:
     *
     *    fondos1[7] solo 70 px de alto (franja puntual, posiblemente niebla).
     *   fondos1[12] (castillete): posicionado en la esquina derecha con tamaño fijo.
     *   fondos1[13] (escritura): se extiende más allá de los bordes para centrar la franja.
     *
     *
     * @param g contexto gráfico 2D del frame actual
     */
    public void draw1(Graphics2D g) {
        // ── Capas de paisaje (cielo → terreno) ──────────────────────────────
        g.drawImage(fondos1[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[3].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[4].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[5].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[6].imagen,    0, -210, gp.pantallaAnchura, 700, null);

        // Capa puntual de poca altura (niebla / detalle de suelo)
        g.drawImage(fondos1[7].imagen,    0, -210, gp.pantallaAnchura,  70, null);

        g.drawImage(fondos1[8].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[9].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[10].imagen,   0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos1[11].imagen,   0, -210, gp.pantallaAnchura, 700, null);

        // Silueta del castillo: esquina derecha, tamaño y posición fijos
        g.drawImage(fondos1[12].imagen, 780, 180, 360, 275, null);

        // Franja decorativa de suelo: se extiende más allá del borde izquierdo
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
     * @param g contexto gráfico 2D del frame actual
     */
    public void draw2(Graphics2D g) {
        // ── Capas de interior (fondo → primer plano) ─────────────────────────
        g.drawImage(fondos2[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos2[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);

        // Franja decorativa de suelo (mismo recurso que escena 1)
        g.drawImage(fondos2[3].imagen, -350, 490, 1800, 140, null);
    }


    public void draw3(Graphics2D g) {
        // ── Capas de interior (fondo → primer plano) ─────────────────────────
        g.drawImage(fondos3[0].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[1].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[2].imagen,    0, -210, gp.pantallaAnchura, 700, null);
        g.drawImage(fondos3[3].imagen,    0, -210, gp.pantallaAnchura, 700, null);

        // Franja decorativa de suelo (mismo recurso que escena 1)
        g.drawImage(fondos3[4].imagen, -350, 490, 1800, 140, null);
    }
}