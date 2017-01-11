package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;
import com.mercury.platform.shared.pojo.Message;

import java.util.List;

/**
 * Exslims
 * 08.12.2016
 */
public class NewWhispersEvent implements SCEvent {
    private Message message;

    public NewWhispersEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
