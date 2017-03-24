package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.Message;

import java.awt.*;

public class CloseMessagePanelEvent implements MercuryEvent {
    private Message message;

    public CloseMessagePanelEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
