package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;

public class NotificationTypeRenderer implements MCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JComponent getComponent(String data) {
        NotificationType notificationType = NotificationType.valueOf(data);
        JLabel iconLabel = componentsFactory.getIconLabel("app/outgoing_arrow.png", 17);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (notificationType.equals(NotificationType.INC_CURRENCY_MESSAGE) ||
                notificationType.equals(NotificationType.INC_ITEM_MESSAGE)) {
            iconLabel.setIcon(this.componentsFactory.getIcon("app/incoming_arrow.png", 17));
        }
        return iconLabel;
    }
}
