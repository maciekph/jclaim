package com.itbs.util;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * This is really for GUIs, but hey - maybe someone can use it outside...
 *
 * @author Alex Rass
 * @since Jan 28, 2006
 */
public class SoundHelper {
    public final static FileFilter filter = new FileFilter() {
        public boolean accept(File f) {
            return f.isDirectory() || (f.exists() && checkSoundFile(f.getName())); //!f.isDirectory() &&
        }

        public String getDescription() {
            return "Sound Files";
        }
    };

    public static JButton getButton() {
        return new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D gr = (Graphics2D) g;
                gr.drawPolygon(new int[]{1, 5, 1}, new int[]{1, 3, 5}, 3);
            }
        };
    }

    public static boolean checkSoundFile(String s) {
        return (s.endsWith(".au") || s.endsWith(".rmf") ||
                s.endsWith(".mid") || s.endsWith(".wav") ||
                s.endsWith(".aif") || s.endsWith(".aiff"));

    }

    /**
     * Plays the sound at path.
     * No path results in system beep.
     *
     * @param path to the sound file.  null is allowed.
     * @return true if sound was played.
     */
    public static boolean playSound(String path) {
        if (path != null && !"".equals(path.trim())) {
            File location = new File(path);
            if (location.exists() && checkSoundFile(path)) {
                // play
                try {
                    AudioInputStream stream = AudioSystem.getAudioInputStream(location);
                    AudioFormat format = stream.getFormat();
                    if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
                            (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                        AudioFormat tmp = new AudioFormat(
                                AudioFormat.Encoding.PCM_SIGNED,
                                format.getSampleRate(),
                                format.getSampleSizeInBits() * 2,
                                format.getChannels(),
                                format.getFrameSize() * 2,
                                format.getFrameRate(),
                                true);
                        stream = AudioSystem.getAudioInputStream(tmp, stream);
                        format = tmp;
                    }

                    DataLine.Info info = new DataLine.Info(
                            Clip.class,
                            stream.getFormat(),
                            ((int) stream.getFrameLength() *
                                    format.getFrameSize()));

//                Line.Info linfo = new Line.Info(Clip.class);
                    Line line = AudioSystem.getLine(info);
                    Clip clip = (Clip) line;
                    clip.open(stream);
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                return true;
            }
        } // if legal attempt
        return false;
    }
}
