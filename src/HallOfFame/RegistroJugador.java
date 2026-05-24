package HallOfFame;

/**
 * REGISTRO DE JUGADOR
 * ─────────────────────────────────────────────────────────────────
 * Representa una entrada del Hall of Fame: el nombre del jugador
 * y el tiempo que tardó en completar el juego.
 *
 * Es un modelo de datos puro — solo guarda información y ofrece
 * métodos para consultarla. No dibuja, no escribe archivos, no
 * escucha teclas. Esa separación de responsabilidades es uno de
 * los principios básicos de la Programación Orientada a Objetos.
 *
 * Implementa Comparable para que Collections.sort() pueda ordenar
 * una lista de registros de menor a mayor tiempo automáticamente.
 * ─────────────────────────────────────────────────────────────────
 */
public class RegistroJugador implements Comparable<RegistroJugador> {

    // ── Campos privados ───────────────────────────────────────────────────
    // Son final porque una entrada del Hall of Fame no debería modificarse.
    // Si un jugador mejora su tiempo, GestorXml reemplaza el nodo entero.

    /** Nombre introducido por el jugador en PantallaIntroducirNombre. */
    private final String nombre;

    /**
     * Tiempo total de partida en milisegundos.
     * Ejemplo: 125312 ms = 2 minutos, 5 segundos y 312 milisegundos.
     */
    private final long tiempoMs;


    private final String fecha;


    // ── Constructor ───────────────────────────────────────────────────────

    /**
     * @param nombre   nombre del jugador (no nulo, no vacío)
     * @param tiempoMs tiempo total de partida en milisegundos
     */
    public RegistroJugador(String nombre, long tiempoMs, String fecha) {
        this.nombre   = nombre;
        this.tiempoMs = tiempoMs;
        this.fecha    = fecha;
    }


    // ── Getters ───────────────────────────────────────────────────────────

    public String getNombre()   { return nombre; }
    public long   getTiempoMs() { return tiempoMs; }
    public String getFecha() { return fecha; }


    // ── Formato legible del tiempo ────────────────────────────────────────

    /**
     * Convierte milisegundos a formato MM:SS.mmm
     *
     * Ejemplo con tiempoMs = 125312:
     *   totalSegundos = 125312 / 1000  = 125
     *   milisegundos  = 125312 % 1000  = 312
     *   segundos      = 125    % 60    = 5
     *   minutos       = 125    / 60    = 2
     *   resultado     → "02:05.312"
     *
     * @return tiempo formateado como "MM:SS.mmm"
     */
    public String getTiempoFormateado() {
        long totalSegundos = tiempoMs / 1000;
        long milisegundos  = tiempoMs % 1000;
        long segundos      = totalSegundos % 60;
        long minutos       = totalSegundos / 60;
        return String.format("%02d:%02d.%03d", minutos, segundos, milisegundos);
    }


    // ── Ordenación ────────────────────────────────────────────────────────

    /**
     * Menor tiempo = mejor jugador = va primero en la lista.
     * Necesario para que Collections.sort() en GestorXml.cargarOrdenados()
     * ordene automáticamente sin escribir el algoritmo.
     */
    @Override
    public int compareTo(RegistroJugador otro) {
        return Long.compare(this.tiempoMs, otro.tiempoMs);
    }
}