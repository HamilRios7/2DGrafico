    package Objetos;

    import Main.GamePanel;

    import javax.imageio.ImageIO;
    import java.io.IOException;



    /**
     * Objeto tipo poción de vida.
     *
     * Al usar esta poción, el jugador recupera una cantidad de vida
     * equivalente a la mitad de su vida máxima, sin superar el límite.
     */
    public class Obj_PocionVida  extends  SuperObject{



        /** Referencia al panel del juego para acceder al jugador */
        GamePanel gp;



        /**
         * Constructor de la poción de vida.
         *
         * @param gp referencia al GamePanel
         */
        public Obj_PocionVida (GamePanel gp) {
            this.gp = gp;

            name="PocionVida";
            descripcion = "Recupera 30 puntos de vida";

            try {
                imagen= ImageIO.read(getClass().getClassLoader().getResourceAsStream("objetos/pocionvida.png"));



            }catch (IOException e){
                System.out.println("Imagen de pocion no encontrada : ");
                e.printStackTrace();
            }
        }



        /**
         * Aplica el efecto de curación al jugador.
         *
         * Recupera el 50% de la vida máxima sin superar el límite.
         */

        @Override
        public void usar() {
            // Cura siempre la mitad de la vida máxima, sin pasarse del tope
            int vida =Math.min(gp.jugador.getLife() + (gp.jugador.getBarraVida() / 2), gp.jugador.getBarraVida());
            gp.jugador.setLife(vida);
        }
    }
