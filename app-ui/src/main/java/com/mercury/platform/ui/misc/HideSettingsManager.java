package com.mercury.platform.ui.misc;

import com.mercury.platform.ui.frame.OverlaidFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Константин on 11.01.2017.
 */
public class HideSettingsManager {
    //todo from config file
    private int minOpacity = 100;
    private int maxOpacity = 100;
    private static class HideSettingsManagerHolder {
        static final HideSettingsManager HOLDER_INSTANCE = new HideSettingsManager();
    }
    public static HideSettingsManager INSTANCE = HideSettingsManagerHolder.HOLDER_INSTANCE;

    private List<OverlaidFrame> frames;

    private HideSettingsManager() {
        frames = new ArrayList<>();
    }
    public void registerFrame(OverlaidFrame frame){
        frames.add(frame);
    }
    public void apply(int timeToHide, int minOpacity, int maxOpacity){
        this.minOpacity = minOpacity;
        this.maxOpacity = maxOpacity;
        frames.forEach(frame -> {
            if(timeToHide > 0){
                frame.enableHideEffect(timeToHide,minOpacity,maxOpacity);
            }else {
                frame.disableHideEffect();
                frame.setOpacity(maxOpacity/100f);
            }
        });
    }

    public int getMinOpacity() {
        return minOpacity;
    }

    public int getMaxOpacity() {
        return maxOpacity;
    }
}
