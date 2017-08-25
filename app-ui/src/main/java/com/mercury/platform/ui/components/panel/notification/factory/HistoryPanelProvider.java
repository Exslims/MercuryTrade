package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.HistoryNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.HistoryController;
import com.mercury.platform.ui.components.panel.notification.controller.HistoryPanelController;


public class HistoryPanelProvider extends NotificationPanelProvider<NotificationDescriptor, HistoryController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.HISTORY);
    }

    @Override
    protected NotificationPanel<NotificationDescriptor, HistoryController> getPanel() {
        HistoryNotificationPanel panel = new HistoryNotificationPanel();
        panel.setController(new HistoryPanelController(this.data));
        return panel;
    }
}
