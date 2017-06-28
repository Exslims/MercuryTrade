package com.mercury.platform.ui.frame.movable.container.strategy;


import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.components.panel.message.MessagePanelStyle;
import com.mercury.platform.ui.components.panel.message.NotificationMessageController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GrowDownStrategy extends GrowStrategy {
    public GrowDownStrategy(List<MessagePanel> currentMessages, Container container, JFrame frame) {
        super(currentMessages, container,frame);
        this.container.removeAll();
    }
    @Override
    public void addMessage(Message message) {
        MessagePanel messagePanel = new MessagePanel(
                message,
                MessagePanelStyle.DOWNWARDS_SMALL,
                new NotificationMessageController(message),
                this.componentsFactory);
        this.container.add(messagePanel);
        this.addMessage(messagePanel);
    }

    @Override
    public void validate() {
        this.currentMessages.forEach(panel -> this.container.add(panel));
        super.validate();
    }
}
