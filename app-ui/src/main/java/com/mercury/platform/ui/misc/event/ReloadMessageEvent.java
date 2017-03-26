package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.ui.components.panel.message.MessagePanel;

/**
 * Created by Константин on 26.03.2017.
 */
public class ReloadMessageEvent implements MercuryEvent {
    private MessagePanel panel;

    public ReloadMessageEvent(MessagePanel panel) {
        this.panel = panel;
    }

    public MessagePanel getPanel() {
        return panel;
    }
}
