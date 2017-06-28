package com.mercury.platform.ui.frame.movable.container.strategy;


import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.components.panel.message.MessagePanelStyle;
import com.mercury.platform.ui.components.panel.message.NotificationMessageController;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GrowUpStrategy extends GrowStrategy {
    private JPanel root;
    public GrowUpStrategy(List<MessagePanel> currentMessages, Container container, JFrame frame, JPanel root) {
        super(currentMessages, container,frame);
        this.root = root;
        this.container.removeAll();
        JPanel buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setBackground(AppThemeColor.BORDER);
        frame.add(buffer,0);
    }

    @Override
    public void addMessage(Message message) {
        MessagePanel messagePanel = new MessagePanel(
                message,
                MessagePanelStyle.UPWARDS_SMALL,
                new NotificationMessageController(message),
                this.componentsFactory);
        this.container.add(messagePanel);
        this.addMessage(messagePanel);
        this.root.setMaximumSize(new Dimension(Integer.MAX_VALUE,this.root.getPreferredSize().height));
        this.root.setMinimumSize(new Dimension(Integer.MAX_VALUE,this.root.getPreferredSize().height));
    }

    @Override
    public void validate() {
        this.currentMessages.forEach(panel -> this.container.add(panel,1));
        super.validate();
    }
}
