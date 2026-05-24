package Main;

/**
 * CRONÓMETRO DE PARTIDA
 * ─────────────────────────────────────────────────────────────────
 * Mide el tiempo que el jugador tarda en completar el juego.
 *
 * Funciona igual que un cronómetro de móvil:
 *   - arrancar()  → empieza a contar desde cero
 *   - pausar()    → congela el conteo (ej: menú de pausa)
 *   - reanudar()  → sigue contando desde donde pausó
 *   - detener()   → para y guarda el resultado final
 *
 * Usa System.nanoTime() igual que el game loop de GamePanel.run(),
 * que es el reloj más preciso disponible en Java.
 *
 * NIVEL DAM: esta clase aplica encapsulación (private/public),
 * tipos primitivos (long, boolean) y conversión de unidades.
 * ─────────────────────────────────────────────────────────────────
 */
public class CronometroPartida {

    // ── Campos privados ───────────────────────────────────────────────────
    // Son private porque solo esta clase debe modificarlos directamente.
    // El resto del proyecto los consulta a través de los métodos públicos.

    /**
     * Instante exacto (en nanosegundos) en que arrancó o se reanudó
     * el cronómetro. Se usa para calcular cuánto ha pasado desde entonces.
     *
     * Usamos long porque los nanosegundos son números muy grandes
     * (un segundo = 1.000.000.000 nanosegundos) y no caben en un int.
     */
    private long instanteInicioNanos;

    /**
     * Milisegundos acumulados de sesiones anteriores.
     * Ejemplo: si pausas a los 10 segundos, aquí se guardan 10.000 ms.
     * Cuando reanudes, se suman los nuevos milisegundos a este valor.
     */
    private long milisegundosAcumulados;

    /**
     * Indica si el cronómetro está contando en este momento.
     * true  → contando
     * false → parado o pausado
     *
     * Mismo tipo que los flags de tu proyecto (jugadorTurno, ispeleaFinalizada...)
     */
    private boolean contando;

    /**
     * Resultado final en milisegundos, fijado al llamar a detener().
     * Vale -1 mientras la partida no ha terminado todavía.
     */
    private long resultadoFinalMs = -1;


    // ── Métodos públicos ──────────────────────────────────────────────────

    /**
     * Arranca el cronómetro desde cero.
     * Llamar una sola vez al entrar a escenaState1.
     *
     * Resetea todos los valores para que una nueva partida
     * no arrastre datos de la anterior.
     */
    public void arrancar() {
        milisegundosAcumulados = 0;        // reset del acumulado
        resultadoFinalMs       = -1;       // -1 = partida en curso
        instanteInicioNanos    = System.nanoTime(); // marca el inicio
        contando               = true;
    }

    /**
     * Pausa el cronómetro.
     * Llamar al entrar a pauseState1, pauseState2 o pauseState3.
     *
     * Guarda los milisegundos transcurridos hasta ahora en
     * milisegundosAcumulados para no perderlos al reanudar.
     *
     * Si ya estaba parado no hace nada (evita sumar tiempo dos veces).
     */
    public void pausar() {
        // Protección: si ya está parado, no hacemos nada
        if (!contando) return;

        // Guardamos el tiempo transcurrido antes de parar
        milisegundosAcumulados += getMsSinceStart();
        contando = false;
    }

    /**
     * Reanuda el cronómetro tras una pausa.
     * Llamar al salir de cualquier estado de pausa.
     *
     * Resetea instanteInicioNanos al momento actual para que
     * los segundos de pausa no se cuenten.
     *
     * Si ya estaba contando no hace nada.
     */
    public void reanudar() {
        // Protección: si ya está contando, no hacemos nada
        if (contando) return;

        // Nuevo punto de partida desde ahora
        instanteInicioNanos = System.nanoTime();
        contando = true;
    }

    /**
     * Detiene el cronómetro y devuelve el tiempo total en milisegundos.
     * Llamar cuando el jugador llega a congratulationsState.
     *
     * El resultado queda guardado en resultadoFinalMs para poder
     * consultarlo después con getResultadoFinalMs().
     *
     * @return tiempo total de partida en milisegundos
     */
    public long detener() {
        // Si todavía estaba contando, añadimos el último tramo
        if (contando) {
            milisegundosAcumulados += getMsSinceStart();
            contando = false;
        }
        // Fijamos y devolvemos el resultado final
        resultadoFinalMs = milisegundosAcumulados;
        return resultadoFinalMs;
    }


    // ── Métodos de consulta (getters) ─────────────────────────────────────

    /**
     * Devuelve el tiempo transcurrido en milisegundos SIN detener el cronómetro.
     * Usar para mostrar el contador en el HUD durante la partida.
     *
     * Si está contando  → acumulado + tiempo desde el último arranque/reanudación
     * Si está parado    → solo el acumulado (el tiempo está congelado)
     *
     * @return milisegundos transcurridos hasta ahora
     */
    public long getTiempoTranscurridoMs() {
        if (contando) {
            return milisegundosAcumulados + getMsSinceStart();
        }
        return milisegundosAcumulados;
    }

    /**
     * Devuelve el resultado final fijado por detener().
     * Devuelve -1 si la partida todavía no ha terminado.
     *
     * @return tiempo final en milisegundos, o -1 si no ha terminado
     */
    public long getResultadoFinalMs() { return resultadoFinalMs; }

    /**
     * Indica si el cronómetro está contando en este momento.
     * Usar en GamePanel.update() para decidir si pausar o reanudar.
     *
     * @return true si está contando, false si está parado
     */
    public boolean estaContando() { return contando; }


    // ── Método privado auxiliar ───────────────────────────────────────────

    /**
     * Calcula los milisegundos transcurridos desde el último arranque
     * o reanudación del cronómetro.
     *
     * Conversión: 1 milisegundo = 1.000.000 nanosegundos
     * Por eso dividimos entre 1_000_000L (la L indica que es long,
     * necesario porque los nanosegundos son números enormes).
     *
     * Es private porque solo lo usa esta clase internamente.
     *
     * @return milisegundos desde el último arranque/reanudación
     */
    private long getMsSinceStart() {
        return (System.nanoTime() - instanteInicioNanos) / 1_000_000L;
    }
}