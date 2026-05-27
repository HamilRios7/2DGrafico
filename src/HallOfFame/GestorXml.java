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
 *
 *
 *  (DOM: convierte el archivo XML rn objetos java organizados en arbol
 *   El DOM sirve para leer XML facilmente, modificar en memoria, crear nuevos nodos
 *   , eliminar elementos y navegar como un arbol)
 */
public class GestorXml {

    private static final String RUTA_ARCHIVO = "hall_of_fame.xml";


    // Guardar un nuevo registro

    /**
     * Guarda el registro en el XML.
     * Llamar desde KeyHandler cuando el jugador llega a congratulationsState.
     *
     * @param registro el RegistroJugador con nombre , tiempo final y fecha /hora
     */
    public static void guardar(RegistroJugador registro) {
        try {
            //Este crea la fabrica para construir XML,y configura como se leera, es decir , prepara el entorno
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();

            //Creamos uno usando la fabrica, este es el que realmente contruye el arbol y lee xml
            DocumentBuilder constructor    = fabrica.newDocumentBuilder();  // aquí puede fallar lanzando ParserConfigurationException

            //Declaramos una variable que podrá guardar un XML cargado en memoria
            Document documento;
            //Declaramos una variable que podrá guardar un elemento del XML, en este caso sera la raiz del XML, es decir , el nodo jugadores
            Element raiz;

            File archivo = new File(RUTA_ARCHIVO); // aqui puede fallar java.io.IOException
            if (archivo.exists()) {
                // El archivo ya existe → lo cambiamos y reutilizamos

                //Se lee el XML , se contruye el arbol DOM y dpcumento ahora apunta a arbol
                documento = constructor.parse(archivo);
                //Pillamos el nodo raiz del XML (en nuestro caso <jugadores>) para luego añadirle hijos (<jugador>)
                raiz  = documento.getDocumentElement();
            } else {
                // Primera vez -> creamos el documento desde cero

                //Creamos un documento vacio, no hay XML cargado , empezamos vacios
                documento = constructor.newDocument();

                //Crea un elemento XML llamando jugadores que es el raiz, aun no esta en el documento
                raiz      = documento.createElement("jugadores");

                //Añadimos el elemento como raíz del documento.
                documento.appendChild(raiz);
            }

            //Busca de la lista jugadores , los modulos jugador
            NodeList jugadores       = documento.getElementsByTagName("jugador");
            Element  entradaExistente = null;
            long     tiempoExistente  = Long.MAX_VALUE;

            for (int i = 0; i < jugadores.getLength(); i++) {
                Element jugador = (Element) jugadores.item(i);

                //Esto obtiene lo que hay dentro del xml en el nodo jugador , si es q existe alguno
                String nombreGuardado = jugador
                        .getElementsByTagName("nombre").item(0)
                        .getTextContent().trim();

                // si el nombre se encuentra en el registro, guardamos el nombre jugador en element
                //pillamos el tiempo y le colocamos el guardado si existe
                if (nombreGuardado.equals(registro.getNombre())) {
                    entradaExistente = jugador;
                    tiempoExistente  = Long.parseLong(
                            jugador.getElementsByTagName("tiempo").item(0)
                                    .getTextContent().trim());
                    break;
                }
            }

            //Si ha pillado en entrada existene el elemento jugador
            if (entradaExistente != null) {

                //Comparamos tiempo de la ultima partida que se guardo en el registro
                // , con el que el jugador tenia ya guardado en el XML
                if (registro.getTiempoMs() <    tiempoExistente) {
                    // Tiempo nuevo es mejor → actualizar la entrada existente

                    //a partir del nodo jugador que hemos guardado en el for
                    //seleccionamos de ese nodo , el primer tiempo que aparezca en los subelementos de
                    //ese nodo
                    entradaExistente.getElementsByTagName("tiempo").item(0)
                            .setTextContent(String.valueOf(registro.getTiempoMs()));
                    entradaExistente.getElementsByTagName("tiempoFormato").item(0)
                            .setTextContent(registro.getTiempoFormateado());

                    // actualizar también la fecha al mejorar
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
                // Si es jugador nuevo , entonces nueva entrada jugador

                //Creamos el nodo jugador
                Element nodoJugador = documento.createElement("jugador");

                //Creamos el nodo nombre
                Element nodoNombre = documento.createElement("nombre");

                //Le ponemos al nodo el nombre que tenemos guardado del registro
                nodoNombre.setTextContent(registro.getNombre());

                //Hacemos que el nodo nombre se anide en nodo jugador
                nodoJugador.appendChild(nodoNombre);

                Element nodoTiempo = documento.createElement("tiempo");

                //Metemos el tiempo sin formatear , pero primero lo pasamos a String
                nodoTiempo.setTextContent(String.valueOf(registro.getTiempoMs()));


                nodoJugador.appendChild(nodoTiempo);

                Element nodoFormato = documento.createElement("tiempoFormato");

                //metemos el tiempoFormateado del metodo registro que ya lo da en string
                nodoFormato.setTextContent(registro.getTiempoFormateado());
                nodoJugador.appendChild(nodoFormato);

                //fecha y hora de la partida se actualiza con un formato String
                Element nodoFecha = documento.createElement("fecha");
                nodoFecha.setTextContent(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                );

                nodoJugador.appendChild(nodoFecha);

                //metemos el nodo del jugador en la raiz
                raiz.appendChild(nodoJugador);
                System.out.println("[GestorXml] Nuevo jugador guardado: "
                        + registro.getNombre() + " — " + registro.getTiempoFormateado());
            }

            // Escribir el documento o lo que tenemos en memoria actualizado en disco
            Transformer transformador = TransformerFactory.newInstance().newTransformer();

            //Esto nos permite la indentacion ,es decir, organizar el XML para que no salga en una sola linea
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");

            //Esto nos permite crear dos espacios para separar cada nivel
            //El enlace pilla la propiedad especifica del XML Apache
            //Identificamos quien define la propiedad ,pillamos el nombre real de la propiedad y seleccionamos
            transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

           //Convierte el DOM XML en texto XML y lo escribe en el archivo.
            //Toma el XML en la memoria y lo guarda formateado en archivo, en disco.
            //Memoria significa que el programa lo guarda , no esta en ningun archivo
            transformador.transform(new DOMSource(documento), new StreamResult(archivo));  // aquí puede fallar lanzando TransformerException

        }catch (ParserConfigurationException ex) {
            //Suele ocurrir cuando la configuración del DocumentBuilderFactory es incorrecta o problemas internas del parse
        System.err.println("[GestorXml] Error al crear el parser XML: " + ex.getMessage());
         } catch (org.xml.sax.SAXException ex) {
            //Por error al leer o interpretar el XML (xml mal formados o estructura incorrecta)
        System.err.println("[GestorXml] Error al parsear el XML existente: " + ex.getMessage());
         } catch (java.io.IOException ex) {
            //el archivo no existe, no tiene permisos o ruta incorrecta. No podria llegar a ser por
            //archivo inexistente , ya que nos aseguramos de crearlo si no existe
        System.err.println("[GestorXml] Error de lectura/escritura del archivo: " + ex.getMessage());
         } catch (TransformerException ex) {
            //error al guardar  o escribir el archivo, es decir, al convertir el DOM a XML y escribirlo en disco ( transformarlo)
        System.err.println("[GestorXml] Error al escribir el XML actualizado: " + ex.getMessage());
         }
    }


    //  Leer todos los registros ordenados

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

        if (!archivo.exists()) return lista; // si no llega a existir aun, entonces simplemente le damos la lista vacía

        try {
            //Este crea la fabrica para construir XML,y configura como se leera, es decir , prepara el entorno
            DocumentBuilderFactory fabrica    = DocumentBuilderFactory.newInstance();
            //Creamos uno usando la fabrica, este es el que realmente contruye el arbol y lee xml
            DocumentBuilder        constructor = fabrica.newDocumentBuilder();

            //Lee el archivo XML, lo convierte en un arbol DOM en memoria (DOM: convierte el archivo XML rn objetos java organizados en arbol
            //El DOM sirve para leer XML facilmente, modificar en memoria, crear nuevos nodos , eliminar elementos y navegar como un arbol)
            Document   documento   = constructor.parse(archivo);


            //Pillamos todos los nodos jugador como si fuera una lista
            NodeList jugadores = documento.getElementsByTagName("jugador");
            for (int i = 0; i < jugadores.getLength(); i++) {
                //pillamos el primer elemento jugador
                Element jugador = (Element) jugadores.item(i);
                String nombre = jugador
                        .getElementsByTagName("nombre").item(0)
                        .getTextContent().trim();
                long tiempo = Long.parseLong(
                        jugador.getElementsByTagName("tiempo").item(0)
                                .getTextContent().trim());

                Node nodoFecha = jugador.getElementsByTagName("fecha").item(0);
                String fecha ;
                if (nodoFecha != null) {
                    fecha=nodoFecha.getTextContent().trim();
                } else{
                    fecha="—";
                }


                //COmo una fila de tabla de base de datos , guardamos cada parte de jugador en variables y estas
                //la añadimos en listas (Una fila lista completa=Un jugador)
                lista.add(new RegistroJugador(nombre, tiempo, fecha));

            }

            //al llamo a collections, digo que estos objetos son comparables, por lo tanto llamo a su compareTo que tiene
            Collections.sort(lista); // usa compareTo → menor tiempo primero

        } catch (ParserConfigurationException ex) {
            System.err.println("[GestorXml] Error al crear el parser XML: " + ex.getMessage());
        } catch (org.xml.sax.SAXException ex) {
            //Por error al leer o interpretar el XML (xml mal formados o estructura incorrecta)
            System.err.println("[GestorXml] Error al parsear el archivo: " + ex.getMessage());
        } catch (java.io.IOException ex) {

            //el archivo no existe, no tiene permisos o ruta incorrecta. No podria llegar a ser por
            //archivo inexistente , ya que nos aseguramos de crearlo si no existe
            System.err.println("[GestorXml] Error al leer el archivo: " + ex.getMessage());
        }

        //devolvemos esta lista completa ordenada de registros de jugadores , que se usara para mostrar en pantalla
        return lista;
    }
}