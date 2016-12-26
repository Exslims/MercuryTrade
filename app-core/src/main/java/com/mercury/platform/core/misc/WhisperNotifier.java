package com.mercury.platform.core.misc;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.PoeShortCastSettings;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.SCEventHandler;
import com.mercury.platform.shared.events.custom.WhisperNotificationEvent;
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
        EventRouter.registerHandler(WhisperNotificationEvent.class,new SCEventHandler<WhisperNotificationEvent>(){
            public void handle(WhisperNotificationEvent event) {
                if (PoeShortCastSettings.WHISPER_NOTIFIER_STATUS == WhisperNotifierStatus.ALWAYS ||
                        ((PoeShortCastSettings.WHISPER_NOTIFIER_STATUS == WhisperNotifierStatus.ALTAB) &&
                                (PoeShortCastSettings.APP_STATUS == FrameStates.HIDE))) {
                    ClassLoader classLoader = getClass().getClassLoader();
                    try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource("app/icq-message.wav"))) {
                        Clip clip = AudioSystem.getClip();
                        clip.open(stream);
                        clip.start();
                    } catch (Exception e) {
                        logger.debug("Cannot start playing music: " + Arrays.toString(e.getStackTrace()));
                    }
                }
            }
        });
    }
}
