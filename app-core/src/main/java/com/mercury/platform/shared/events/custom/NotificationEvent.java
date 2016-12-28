package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;

/**
 * Created by Константин on 29.12.2016.
 */
public class NotificationEvent implements SCEvent {
    private String stroke;

    public NotificationEvent(String stroke) {
        this.stroke = stroke;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }
}
