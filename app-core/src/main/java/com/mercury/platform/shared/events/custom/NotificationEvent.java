package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;


public class NotificationEvent implements MercuryEvent {
    private String stroke;

    public NotificationEvent(String stroke) {
        this.stroke = stroke;
    }

    public String getNotification() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }
}
