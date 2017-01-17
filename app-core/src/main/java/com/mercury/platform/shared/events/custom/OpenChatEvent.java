package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 17.12.2016.
 */
public class OpenChatEvent implements MercuryEvent {
    private String whisper;

    public OpenChatEvent(String whisper) {
        this.whisper = whisper;
    }

    public String getWhisper() {
        return whisper;
    }
}
