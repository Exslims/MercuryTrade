package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

/**
 * Exslims
 * 08.12.2016
 */
public class ChatCommandEvent implements SCEvent {
    private String message;

    public ChatCommandEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
