package com.mercury.platform.ui.misc.event;

/**
 * Created by Константин on 24.03.2017.
 */
public class ScaleChangeEvent {
    private float scale;

    public ScaleChangeEvent(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
