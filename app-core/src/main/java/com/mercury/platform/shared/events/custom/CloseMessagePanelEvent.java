package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

import java.awt.*;

/**
 * Created by Константин on 24.12.2016.
 */
public class CloseMessagePanelEvent implements MercuryEvent {
    private Component component;

    public CloseMessagePanelEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
