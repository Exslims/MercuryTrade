package com.mercury.platform.core.misc;

import com.mercury.platform.shared.store.DataTransformers;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundNotifier {
    private final Logger logger = LogManager.getLogger(SoundNotifier.class);
    private boolean dnd = false;

    public SoundNotifier() {
        MercuryStoreCore.soundSubject
                .compose(DataTransformers.transformSoundData())
                .subscribe(data -> play(data.getWavPath(), data.getDb()));
        MercuryStoreCore.soundSettingsSubject
                .subscribe(data -> play(data.getWavPath(), data.getDb()));
        MercuryStoreCore.dndSubject
                .subscribe(value -> this.dnd = value);
        MercuryStoreCore.soundDescriptorSubject.subscribe(soundDescriptor -> {
            this.play(soundDescriptor.getWavPath(), soundDescriptor.getDb());
        });
    }

    private void play(String wavPath, float db) {
        if (!dnd && db > -40) {
            ClassLoader classLoader = getClass().getClassLoader();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource(wavPath))) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                if (db != 0.0) {
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(db);
                }
                clip.start();
            } catch (Exception e) {
                logger.error("Cannot start playing wav file: ", e);
            }
        }
    }
}
