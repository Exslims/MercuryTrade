package com.mercury.platform.core.misc;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.store.DataTransformers;
import com.mercury.platform.shared.store.MercuryStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Random;

public class SoundNotifier {
    private final Logger logger = LogManager.getLogger(SoundNotifier.class);
    private boolean dnd = false;
    public SoundNotifier() {

        MercuryStore.INSTANCE.soundSubject
                .compose(DataTransformers.transformSoundData())
                .subscribe(
                        data -> play(data.get("path"), Float.valueOf(data.get("db"))));
        MercuryStore.INSTANCE.soundSettingsSubject
                .subscribe(
                    data -> play(data.get("path"), Float.valueOf(data.get("db"))));

        EventRouter.CORE.registerHandler(SoundNotificationEvent.WhisperSoundNotificationEvent.class, event -> {
            WhisperNotifierStatus status = ConfigManager.INSTANCE.getWhisperNotifier();
            if (status == WhisperNotifierStatus.ALWAYS ||
                    ((status == WhisperNotifierStatus.ALTAB) && (AppStarter.APP_STATUS == FrameVisibleState.HIDE))) {
                play("app/notification.wav",((SoundNotificationEvent.WhisperSoundNotificationEvent)event).getDb());
            }
        });
        EventRouter.CORE.registerHandler(SoundNotificationEvent.UpdateSoundNotificationEvent.class, event -> {
            play("app/patch_tone.wav",((SoundNotificationEvent.UpdateSoundNotificationEvent)event).getDb());
        });
        EventRouter.CORE.registerHandler(SoundNotificationEvent.ChatScannerSoundNotificationEvent.class, event -> {
            play("app/chat-filter.wav",((SoundNotificationEvent.ChatScannerSoundNotificationEvent)event).getDb());
        });
        EventRouter.CORE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
        });
        EventRouter.CORE.registerHandler(SoundNotificationEvent.ClicksSoundNotificationEvent.class, event -> {
            String[] clicks = {
                    "app/sounds/click1/button-pressed-10.wav",
                    "app/sounds/click1/button-pressed-20.wav",
                    "app/sounds/click1/button-pressed-30.wav"};
            play(clicks[new Random().nextInt(3)],((SoundNotificationEvent.ClicksSoundNotificationEvent)event).getDb());
        });
    }

    private void play(String wavPath, float db){
        if(!dnd) {
            ClassLoader classLoader = getClass().getClassLoader();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource(wavPath))) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                if(db != 0.0) {
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(db);
                }
                clip.start();
            } catch (Exception e) {
                logger.error("Cannot start playing wav file: ",e);
            }
        }
    }
}
