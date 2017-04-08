package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.entity.SoundDescriptor;
import rx.Observable;

import java.util.Random;


public class DataTransformers {
    public static Observable.Transformer<SoundType, SoundDescriptor> transformSoundData() {
        String[] clicks = {
                "app/sounds/click1/button-pressed-10.wav",
                "app/sounds/click1/button-pressed-20.wav",
                "app/sounds/click1/button-pressed-30.wav"};
        return obs -> obs.map(soundType -> {
            SoundDescriptor descriptor = new SoundDescriptor();
            switch (soundType){
                case MESSAGE:{
                    descriptor.setWavPath("app/notification.wav");
                    descriptor.setDb(-10f);
                    break;
                }
                case CHAT_SCANNER: {

                    break;
                }
                case CLICKS: {
                    descriptor.setWavPath(clicks[new Random().nextInt(3)]);
                    descriptor.setDb(-10f);
                    break;
                }
                case UPDATE: {
                    descriptor.setWavPath("app/patch_tone.wav");
                    descriptor.setDb(-10f);
                    break;
                }
            }
            return descriptor;
        });
    }
}
