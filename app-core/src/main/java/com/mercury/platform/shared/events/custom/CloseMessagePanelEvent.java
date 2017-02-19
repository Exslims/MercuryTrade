package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.Message;

import java.awt.*;

/**
 * Created by Константин on 24.12.2016.
 */
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
