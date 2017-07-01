package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.AbstractComponentFrame;

import java.util.ArrayList;
import java.util.List;

public class HideSettingsManager {
    private static class HideSettingsManagerHolder {
        static final HideSettingsManager HOLDER_INSTANCE = new HideSettingsManager();
    }
    public static HideSettingsManager INSTANCE = HideSettingsManagerHolder.HOLDER_INSTANCE;
    private List<AbstractComponentFrame> frames = new ArrayList<>();
    public void registerFrame(AbstractComponentFrame frame){
        this.frames.add(frame);
    }
    public void apply(int fadeTime, int minOpacity, int maxOpacity){
        ApplicationDescriptor config = Configuration.get().applicationConfiguration().get();
        config.setMaxOpacity(maxOpacity);
        config.setMinOpacity(minOpacity);
        config.setFadeTime(fadeTime);
        MercuryStoreCore.INSTANCE.saveConfigSubject.onNext(true);

        this.frames.forEach(frame -> {
            if(fadeTime > 0){
                frame.enableHideEffect(fadeTime,minOpacity,maxOpacity);
            }else {
                frame.disableHideEffect();
                frame.setOpacity(maxOpacity/100f);
            }
        });
    }
}
