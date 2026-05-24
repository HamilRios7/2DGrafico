package HallOfFame;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GESTOR XML
 * ─────────────────────────────────────────────────────────────────
 * Gestiona la lectura y escritura del archivo hall_of_fame.xml.
 *
 * Si el jugador ya tiene una entrada y su nuevo tiempo es mejor,
 * actualiza el nodo existente. Si es peor, no guarda nada.
 * Si es un jugador nuevo, añade una entrada al final.
 *
 * Estructura del XML generado:
 * <jugadores>
 *   <jugador>
 *     <nombre>Juan</nombre>
 *     <tiempo>12345</tiempo>
 *     <tiempoFormato>00:12.345</tiempoFormato>
 *   </jugador>
 * </jugadores>
 * ─────────────────────────────────────────────────────────────────
 */
public class GestorXml {

    private static final String RUTA_ARCHIVO = "hall_of_fame.xml";


    // ── Guardar un nuevo registro ─────────────────────────────────────────

    /**
     * Guarda el registro en el XML.
     * Llamar desde KeyHandler cuando el jugador llega a congratulationsState.
     *
     * @param registro el RegistroJugador con nombre y tiempo final
     */
    public static void guardar(RegistroJugador registro) {
        try {
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructor    = fabrica.newDocumentBuilder();

            Document documento;
            Element raiz;

            File archivo = new File(RUTA_ARCHIVO);
            if (archivo.exists()) {
                // El archivo ya existe → lo parseamos y reutilizamos
                documento = constructor.parse(archivo);
                raiz      = documento.getDocumentElement();
            } else {
                // Primera vez → creamos el documento desde cero
                documento = constructor.newDocument();
                raiz      = documento.createElement("jugadores");
                documento.appendChild(raiz);
            }

            // ── Buscar si ya existe una entrada con ese nombre ────────────
            NodeList jugadores       = documento.getElementsByTagName("jugador");
            Element  entradaExistente = null;
            long     tiempoExistente  = Long.MAX_VALUE;

            for (int i = 0; i < jugadores.getLength(); i++) {
                Element jugador = (Element) jugadores.item(i);
                String nombreGuardado = jugador
                        .getElementsByTagName("nombre").item(0)
                        .getTextContent().trim();

                if (nombreGuardado.equals(registro.getNombre())) {
                    entradaExistente = jugador;
                    tiempoExistente  = Long.parseLong(
                            jugador.getElementsByTagName("tiempo").item(0)
                                    .getTextContent().trim());
                    break;
                }
            }

            if (entradaExistente != null) {
                if (registro.getTiempoMs() < tiempoExistente) {
                    // Tiempo nuevo es mejor → actualizar la entrada existente
                    entradaExistente.getElementsByTagName("tiempo").item(0)
                            .setTextContent(String.valueOf(registro.getTiempoMs()));
                    entradaExistente.getElementsByTagName("tiempoFormato").item(0)
                            .setTextContent(registro.getTiempoFormateado());

                    // NUEVO: actualizar también la fecha al mejorar
                    entradaExistente.getElementsByTagName("fecha").item(0)
                            .setTextContent(
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                            );
                    System.out.println("[GestorXml] Tiempo mejorado: "
                            + registro.getNombre() + " — " + registro.getTiempoFormateado());
                } else {
                    // Tiempo nuevo es peor → no guardar nada
                    System.out.println("[GestorXml] Tiempo no mejorado, no se guarda.");
                    return;
                }
            } else {
                // Jugador nuevo → añadir nodo al final
                // Jugador nuevo → añadir nodo al final
                Element nodoJugador = documento.createElement("jugador");

                Element nodoNombre = documento.createElement("nombre");
                nodoNombre.setTextContent(registro.getNombre());
                nodoJugador.appendChild(nodoNombre);

                Element nodoTiempo = documento.createElement("tiempo");
                nodoTiempo.setTextContent(String.valueOf(registro.getTiempoMs()));
                nodoJugador.appendChild(nodoTiempo);

                Element nodoFormato = documento.createElement("tiempoFormato");
                nodoFormato.setTextContent(registro.getTiempoFormateado());
                nodoJugador.appendChild(nodoFormato);

// NUEVO: fecha y hora de la partida
                Element nodoFecha = documento.createElement("fecha");
                nodoFecha.setTextContent(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                );
                nodoJugador.appendChild(nodoFecha);

                raiz.appendChild(nodoJugador);
                System.out.println("[GestorXml] Nuevo jugador guardado: "
                        + registro.getNombre() + " — " + registro.getTiempoFormateado());
            }

            // ── Escribir el documento actualizado en disco ────────────────
            Transformer transformador = TransformerFactory.newInstance().newTransformer();
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
            transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformador.transform(new DOMSource(documento), new StreamResult(archivo));

        }catch (ParserConfigurationException ex) {
        System.err.println("[GestorXml] Error al crear el parser XML: " + ex.getMessage());
         } catch (org.xml.sax.SAXException ex) {
        System.err.println("[GestorXml] Error al parsear el XML existente: " + ex.getMessage());
         } catch (java.io.IOException ex) {
        System.err.println("[GestorXml] Error de lectura/escritura del archivo: " + ex.getMessage());
         } catch (TransformerException ex) {
        System.err.println("[GestorXml] Error al escribir el XML actualizado: " + ex.getMessage());
         }
    }


    // ── Leer todos los registros ordenados ───────────────────────────────────

    /**
     * Devuelve todos los registros del XML ordenados de menor a mayor tiempo.
     * Si el archivo no existe, devuelve una lista vacía.
     * Llamar desde PantallaHallOfFame.draw() para obtener los datos a pintar.
     *
     * @return lista de RegistroJugador ordenada (el primero es el más rápido)
     */
    public static List<RegistroJugador> cargarOrdenados() {
        List<RegistroJugador> lista = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) return lista; // sin archivo → lista vacía

        try {
            DocumentBuilderFactory fabrica    = DocumentBuilderFactory.newInstance();
            DocumentBuilder        constructor = fabrica.newDocumentBuilder();
            Document               documento   = constructor.parse(archivo);

            NodeList jugadores = documento.getElementsByTagName("jugador");
            for (int i = 0; i < jugadores.getLength(); i++) {
                Element jugador = (Element) jugadores.item(i);
                String nombre = jugador
                        .getElementsByTagName("nombre").item(0)
                        .getTextContent().trim();
                long tiempo = Long.parseLong(
                        jugador.getElementsByTagName("tiempo").item(0)
                                .getTextContent().trim());

                Node nodoFecha = jugador.getElementsByTagName("fecha").item(0);
                String fecha   = (nodoFecha != null) ? nodoFecha.getTextContent().trim() : "—";

                lista.add(new RegistroJugador(nombre, tiempo, fecha));

            }

            Collections.sort(lista); // usa compareTo → menor tiempo primero

        } catch (ParserConfigurationException ex) {
            System.err.println("[GestorXml] Error al crear el parser XML: " + ex.getMessage());
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("[GestorXml] Error al parsear el archivo: " + ex.getMessage());
        } catch (java.io.IOException ex) {
            System.err.println("[GestorXml] Error al leer el archivo: " + ex.getMessage());
        }

        return lista;
    }
}