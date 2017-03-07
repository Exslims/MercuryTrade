package com.mercury.platform.core.misc;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.MercuryEventHandler;
import com.mercury.platform.shared.events.custom.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Константин on 08.12.2016.
 */
public class SoundNotifier {
    private final Logger logger = LogManager.getLogger(SoundNotifier.class);
    private boolean dnd = false;
    public SoundNotifier() {
        EventRouter.INSTANCE.registerHandler(WhisperNotificationEvent.class, event -> {
            WhisperNotifierStatus status = ConfigManager.INSTANCE.getWhisperNotifier();
            if (status == WhisperNotifierStatus.ALWAYS ||
                    ((status == WhisperNotifierStatus.ALTAB) && (AppStarter.APP_STATUS == FrameStates.HIDE))) {
                play("app/notification.wav");
            }
        });
        EventRouter.INSTANCE.registerHandler(UpdateInfoEvent.class, event -> {
            play("app/patch_tone.wav");
        });
        EventRouter.INSTANCE.registerHandler(ChatFilterMessageEvent.class, event -> {
            play("app/chat-filter.wav");
        });
        EventRouter.INSTANCE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
        });
        EventRouter.INSTANCE.registerHandler(ButtonPressedEvent.class, event -> {
            String[] clicks = {
                    "app/sounds/click1/button-pressed-10.wav",
                    "app/sounds/click1/button-pressed-20.wav",
                    "app/sounds/click1/button-pressed-30.wav"};
            play(clicks[new Random().nextInt(3)]);
        });
    }

    private void play(String wavPath){
        if(!dnd) {
            ClassLoader classLoader = getClass().getClassLoader();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource(wavPath))) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                double gain = .5D;
                float db = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(db);
                clip.start();
            } catch (Exception e) {
                logger.error("Cannot start playing wav file: ",e);
            }
        }
    }
}
