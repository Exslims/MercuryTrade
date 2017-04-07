package com.mercury.platform.shared.store;

import com.mercury.platform.core.misc.SoundType;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;


public class DataTransformers {
    public static Observable.Transformer<SoundType, Map<String, String>> transformSoundData() {
        return obs -> obs.map(soundType -> {
            Map<String, String> data = new HashMap<>();
            switch (soundType){
                case MESSAGE:{
                    data.put("path","app/notification.wav");
                    data.put("db","-10");
                    break;
                }
                case CHAT_SCANNER: {
                    break;
                }
                case CLICKS: {
                    break;
                }
                case UPDATE: {
                    break;
                }
            }
            return data;
        });
    }
}
