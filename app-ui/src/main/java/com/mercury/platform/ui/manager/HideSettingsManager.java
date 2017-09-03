package com.mercury.platform.ui.manager;

import com.mercury.platform.ui.frame.AbstractComponentFrame;

import java.util.ArrayList;
import java.util.List;

public class HideSettingsManager {
    public static HideSettingsManager INSTANCE = HideSettingsManagerHolder.HOLDER_INSTANCE;
    private List<AbstractComponentFrame> frames = new ArrayList<>();

    public void registerFrame(AbstractComponentFrame frame) {
        this.frames.add(frame);
    }

    public void apply(int fadeTime, int minOpacity, int maxOpacity) {
        this.frames.forEach(frame -> {
            if (fadeTime > 0) {
                frame.enableHideEffect(fadeTime, minOpacity, maxOpacity);
            } else {
                frame.disableHideEffect();
                frame.setOpacity(maxOpacity / 100f);
            }
        });
    }

    private static class HideSettingsManagerHolder {
        static final HideSettingsManager HOLDER_INSTANCE = new HideSettingsManager();
    }
}
