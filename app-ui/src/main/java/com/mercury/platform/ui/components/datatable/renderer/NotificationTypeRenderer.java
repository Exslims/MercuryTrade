package com.mercury.platform.ui.components.datatable.renderer;

import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;

public class NotificationTypeRenderer implements MCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JComponent getComponent(String data) {
        NotificationType notificationType = NotificationType.valueOf(data);
        switch (notificationType) {
            case INC_ITEM_MESSAGE: {
                JLabel iconLabel = componentsFactory.getIconLabel("app/incoming_arrow.png", 18);
                iconLabel.setVerticalAlignment(SwingConstants.CENTER);
                iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                return iconLabel;
            }
        }
        return null;
    }
}
