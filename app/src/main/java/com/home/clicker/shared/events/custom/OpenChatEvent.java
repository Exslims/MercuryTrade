package com.home.clicker.shared.events.custom;

import com.home.clicker.shared.events.SCEvent;

/**
 * Created by Константин on 17.12.2016.
 */
public class OpenChatEvent implements SCEvent {
    private String whisper;

    public OpenChatEvent(String whisper) {
        this.whisper = whisper;
    }

    public String getWhisper() {
        return whisper;
    }
}
