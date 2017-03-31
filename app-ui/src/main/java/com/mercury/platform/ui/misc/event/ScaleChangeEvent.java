package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScaleChangeEvent implements MercuryEvent{
    private float scale;
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
