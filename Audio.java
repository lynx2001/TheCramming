import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
    private Clip clip;
    private boolean isLoop;

    public Audio(String pathName, boolean isLoop) {
        this.isLoop = isLoop;
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File(pathName);
            
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
                clip.open(audioInputStream);
            } catch (IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        clip.setFramePosition(0);
        clip.start();
        if (isLoop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        clip.stop();
    }
}