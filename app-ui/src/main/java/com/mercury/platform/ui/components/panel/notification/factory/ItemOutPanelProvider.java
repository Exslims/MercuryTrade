package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.ItemTradeOutNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationOutgoingController;
import com.mercury.platform.ui.components.panel.notification.controller.OutgoingPanelController;


public class ItemOutPanelProvider extends NotificationPanelProvider<ItemTradeNotificationDescriptor, OutgoingPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.OUT_ITEM_MESSAGE);
    }

    @Override
    protected NotificationPanel<ItemTradeNotificationDescriptor, OutgoingPanelController> getPanel() {
        ItemTradeOutNotificationPanel panel = new ItemTradeOutNotificationPanel();
        panel.setController(new NotificationOutgoingController(this.data));
        return panel;
    }
}
