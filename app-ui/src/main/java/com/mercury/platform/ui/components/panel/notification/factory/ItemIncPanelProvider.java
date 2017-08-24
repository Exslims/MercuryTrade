package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationIncomingController;


public class ItemIncPanelProvider extends NotificationPanelProvider<ItemTradeNotificationDescriptor, IncomingPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.INC_ITEM_MESSAGE);
    }

    @Override
    protected NotificationPanel<ItemTradeNotificationDescriptor, IncomingPanelController> getPanel() {
        ItemTradeIncNotificationPanel panel = new ItemTradeIncNotificationPanel();
        panel.setController(new NotificationIncomingController(this.data));
        return panel;
    }
}
