package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 07.03.2017.
 */
public class AlertEvent implements MercuryEvent {
    private String message;

    public AlertEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
