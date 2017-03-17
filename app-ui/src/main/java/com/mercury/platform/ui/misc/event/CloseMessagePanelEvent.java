package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.Message;

import java.awt.*;

public class CloseMessagePanelEvent implements MercuryEvent {
    private Component component;
    private Message message;

    public CloseMessagePanelEvent(Component component, Message message) {
        this.component = component;
        this.message = message;
    }

    public Component getComponent() {
        return component;
    }

    public Message getMessage() {
        return message;
    }
}
