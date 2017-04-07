package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.AbstractComponentFrame;

import java.util.ArrayList;
import java.util.List;

public class HideSettingsManager {
    private static class HideSettingsManagerHolder {
        static final HideSettingsManager HOLDER_INSTANCE = new HideSettingsManager();
    }
    public static HideSettingsManager INSTANCE = HideSettingsManagerHolder.HOLDER_INSTANCE;

    private List<AbstractComponentFrame> frames;

    private HideSettingsManager() {
        frames = new ArrayList<>();
    }
    public void registerFrame(AbstractComponentFrame frame){
        frames.add(frame);
    }
    public void apply(int fadeTime, int minOpacity, int maxOpacity){
        ConfigManager.INSTANCE.setMinOpacity(minOpacity);
        ConfigManager.INSTANCE.setMaxOpacity(maxOpacity);
        ConfigManager.INSTANCE.setFadeTime(fadeTime);

        frames.forEach(frame -> {
            if(fadeTime > 0){
                frame.enableHideEffect(fadeTime,minOpacity,maxOpacity);
            }else {
                frame.disableHideEffect();
                frame.setOpacity(maxOpacity/100f);
            }
        });
    }
}
