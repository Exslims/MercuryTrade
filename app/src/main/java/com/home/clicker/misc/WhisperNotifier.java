package com.home.clicker.misc;

import com.home.clicker.events.EventHandler;
import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.NewWhisperEvent;
import org.apache.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Arrays;

/**
 * Created by Константин on 08.12.2016.
 */
public class WhisperNotifier {
    private final Logger logger = Logger.getLogger(WhisperNotifier.class);
    public WhisperNotifier() {
        EventRouter.registerHandler(NewWhisperEvent.class,new EventHandler<NewWhisperEvent>(){
            public void handle(NewWhisperEvent event) {
                ClassLoader classLoader = getClass().getClassLoader();
                try(AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource("icq-message.wav"))){
                    Clip clip = AudioSystem.getClip();
                    clip.open(stream);
                    clip.start();
                }catch (Exception e){
                    logger.error("Cannot start playing music: " + e.getStackTrace());
                }
            }
        });
    }
}
