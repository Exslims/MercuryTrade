package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.ComponentFrame;

import java.util.ArrayList;
import java.util.List;

public class HideSettingsManager {
    private static class HideSettingsManagerHolder {
        static final HideSettingsManager HOLDER_INSTANCE = new HideSettingsManager();
    }
    public static HideSettingsManager INSTANCE = HideSettingsManagerHolder.HOLDER_INSTANCE;

    private List<ComponentFrame> frames;

    private HideSettingsManager() {
        frames = new ArrayList<>();
    }
    public void registerFrame(ComponentFrame frame){
        frames.add(frame);
    }
    public void apply(int decayTime, int minOpacity, int maxOpacity){
        ConfigManager.INSTANCE.setMinOpacity(minOpacity);
        ConfigManager.INSTANCE.setMaxOpacity(maxOpacity);
        ConfigManager.INSTANCE.setDecayTime(decayTime);

        frames.forEach(frame -> {
            if(decayTime > 0){
                frame.enableHideEffect(decayTime,minOpacity,maxOpacity);
            }else {
                frame.disableHideEffect();
                frame.setOpacity(maxOpacity/100f);
            }
        });
    }
}
