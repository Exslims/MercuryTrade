package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;

import java.awt.*;

/**
 * Created by Константин on 24.12.2016.
 */
public class CloseMessagePanelEvent implements SCEvent {
    private Component component;

    public CloseMessagePanelEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
