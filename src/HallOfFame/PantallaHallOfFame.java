package HallOfFame;

import Main.GamePanel;
import java.awt.*;
import java.util.List;

/**

 * Renderiza la pantalla del Hall of Fame con Graphics2D,
 * igual que UI.java dibuja el resto de pantallas del juego.
 *
 * Llamar desde UI.draw() cuando gp.gameState == gp.hallOfFameState.

 */
public class PantallaHallOfFame {

    private final GamePanel gp;

    /** Máximo de entradas visibles en pantalla. */
    private static final int MAX_ENTRADAS = 10;

    /** Mismo rojo que el título del menú principal. */
    private static final Color ROJO_JUEGO = new Color(209, 0, 28);


    //------Constructor--------

    public PantallaHallOfFame(GamePanel gp) {
        this.gp = gp;
    }


    //Método principal de dibujado

    /**
     * Dibuja la pantalla completa del Hall of Fame.
     * Llamar desde UI.draw() en la rama hallOfFameState.
     *
     * @param g2 contexto gráfico proporcionado por paintComponent()
     */
    public void draw(Graphics2D g2) {
        //sacamos centro de pantalla
        int centroX = gp.pantallaAnchura / 2;

        //----- Fondo ------
        g2.setColor(new Color(7, 10, 18)); // mismo negro que el menú principal
        g2.fillRect(0, 0, gp.pantallaAnchura, gp.pantallaAltura);

        //----- Título -----
        g2.setFont(new Font("Arial", Font.BOLD, 64));
        g2.setColor(ROJO_JUEGO);
        String titulo = "HALL OF FAME";

        //Para calcular x, lo usamos de esta manera para calcular cuántos píxeles ocupa un texto en pantalla usando la fuente actual.
        g2.drawString(titulo, centroX - g2.getFontMetrics().stringWidth(titulo) / 2, 90);

        //  --- Línea separadora bajo el título ----
        //dibujamos una linea de 2 pixeles de grosor
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(ROJO_JUEGO);
        g2.drawLine(100, 108, gp.pantallaAnchura - 100, 108);

        //  ----- Cabecera de columnas -----
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("#",       120, 145);
        g2.drawString("NOMBRE",  200, 145);
        g2.drawString("TIEMPO",  650, 145);
        g2.drawString("FECHA",   820, 145);

         //  ----  Línea separadora bajo la cabecera -----
        g2.drawLine(100, 108, gp.pantallaAnchura - 100, 108);
        g2.drawLine(100, 155, gp.pantallaAnchura - 100, 155);

        //----- Filas de registros -----
        List<RegistroJugador> registros = GestorXml.cargarOrdenados(); //guardamos el resultado de cargarOrdenados en la lista de registros
        int mostrados = Math.min(registros.size(), MAX_ENTRADAS);// lo hacemos por si el tamaño es mayor que el limite que hemos puesto para mostrar

        for (int i = 0; i < mostrados; i++) {
            RegistroJugador r = registros.get(i);
            int y = 195 + i * 50;//para ir moviendo la fila hacia abajo

            // Resaltar la entrada del jugador que acaba de terminar
            boolean esJugadorActual ;
            if(r.getNombre().equals(gp.nombreJugador) && r.getTiempoMs() == gp.cronometro.getResultadoFinalMs()){
                esJugadorActual = true;
            } else {
                esJugadorActual = false;
            }

            if (esJugadorActual) {
                g2.setColor(new Color(209, 0, 28, 60)); // rojo semitransparente
                g2.fillRoundRect(100, y - 32, gp.pantallaAnchura - 200, 42, 8, 8);
            }

            // Color según posición (oro / plata / bronce / blanco)
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.setColor(switch (i) { // como ya nos viene ordenado , simplemente llamandolo asi lo tenemos
                case 0  -> new Color(255, 215,   0); // oro
                case 1  -> new Color(192, 192, 192); // plata
                case 2  -> new Color(205, 127,  50); // bronce
                default -> Color.WHITE;
            });

            g2.drawString((i + 1) + ".",          120, y);
            g2.drawString(r.getNombre(),           200, y);
            g2.drawString(r.getTiempoFormateado(), 650, y);
            g2.drawString(r.getFecha(),            820, y);
        }

        // --- Si todavía no hay ningún registro -------
        if (registros.isEmpty()) {
            g2.setFont(new Font("Arial", Font.ITALIC, 20));
            g2.setColor(Color.GRAY);
            String sinDatos = "— Sin registros todavía —";
            g2.drawString(sinDatos, centroX - g2.getFontMetrics().stringWidth(sinDatos) / 2, gp.pantallaAltura / 2);
        }

        //---- Instrucción de salida----
        if (gp.juegoTerminado) {
            //Si el juego termina , salimos con enter
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(Color.GRAY);
            String pista = "Pulsa ENTER para salir";
            g2.drawString(pista, centroX - g2.getFontMetrics().stringWidth(pista) / 2, gp.pantallaAltura - 30);
        }else {
            //Si es false ,es porque lo hemos llamado al comenzar el juego
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(Color.GRAY);
            String pista = "Pulsa ESC para salir";
            g2.drawString(pista, centroX - g2.getFontMetrics().stringWidth(pista) / 2, gp.pantallaAltura - 30);
        }
    }
}