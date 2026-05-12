package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sonido {

    Clip[] clips = new Clip[30];
    Clip currentMusic;

    public Sonido() {
        URL[] soundURL = new URL[30];

        // ── Pon aquí todas tus URLs exactamente como las tenías ──
        soundURL[0]  = getClass().getClassLoader().getResource("sonido/BlueBoyAdventure.wav");
        soundURL[1]  = getClass().getClassLoader().getResource("sonido/musicoTitulo.wav");
        soundURL[2]  = getClass().getClassLoader().getResource("sonido/enemigo1.wav");
        soundURL[3]  = getClass().getClassLoader().getResource("sonido/jefefinal2.wav");
        soundURL[4]  = getClass().getClassLoader().getResource("sonido/victor.wav");
        soundURL[5]  = getClass().getClassLoader().getResource("sonido/escena1.wav");
        soundURL[6]  = getClass().getClassLoader().getResource("sonido/efectoEspadazo.wav");
        soundURL[7]  = getClass().getClassLoader().getResource("sonido/efectoSamurai.wav");
        soundURL[8]  = getClass().getClassLoader().getResource("sonido/muerteHeroe.wav");
        soundURL[9]  = getClass().getClassLoader().getResource("sonido/samuraiMuerte.wav"); // ojo: corregido el typo
        soundURL[10] = getClass().getClassLoader().getResource("sonido/giganteMuerte_.wav");
        soundURL[11] = getClass().getClassLoader().getResource("sonido/ataqueGigante.wav");

        // Precargar todos en memoria al arrancar el juego
        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
                    clips[i] = AudioSystem.getClip();
                    clips[i].open(ais);
                } catch (Exception e) {
                    System.err.println("Error cargando sonido[" + i + "]: " + e.getMessage());
                }
            }
        }
    }

    public void playMusic(int i) {
        if (currentMusic != null && currentMusic.isOpen()) {
            currentMusic.stop();
        }
        if (clips[i] == null) return;
        currentMusic = clips[i];
        currentMusic.setFramePosition(0);
        currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopMusic() {
        if (currentMusic != null) currentMusic.stop();
    }

    public void playSE(int i) {
        if (clips[i] == null) return;
        clips[i].stop();
        clips[i].setFramePosition(0);
        clips[i].start();
    }
}