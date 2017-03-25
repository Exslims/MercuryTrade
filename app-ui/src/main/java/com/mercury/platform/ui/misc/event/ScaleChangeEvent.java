package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 24.03.2017.
 */
public class ScaleChangeEvent implements MercuryEvent{
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

    public static class NotificationScaleChangeEvent extends ScaleChangeEvent {
        public NotificationScaleChangeEvent(float scale) {
            super(scale);
        }
    }
    public static class TaskBarScaleChangeEvent extends ScaleChangeEvent {
        public TaskBarScaleChangeEvent(float scale) {
            super(scale);
        }
    }
    public static class ItemPanelScaleChangeEvent extends ScaleChangeEvent {
        public ItemPanelScaleChangeEvent(float scale) {
            super(scale);
        }
    }
}
