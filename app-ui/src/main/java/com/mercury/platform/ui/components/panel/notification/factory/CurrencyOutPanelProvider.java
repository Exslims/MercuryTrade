package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.CurrencyTradeOutNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationOutgoingController;
import com.mercury.platform.ui.components.panel.notification.controller.OutgoingPanelController;


public class CurrencyOutPanelProvider extends NotificationPanelProvider<CurrencyTradeNotificationDescriptor, OutgoingPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.OUT_CURRENCY_MESSAGE);
    }

    @Override
    protected NotificationPanel<CurrencyTradeNotificationDescriptor, OutgoingPanelController> getPanel() {
        CurrencyTradeOutNotificationPanel panel = new CurrencyTradeOutNotificationPanel();
        panel.setController(new NotificationOutgoingController(this.data));
        return panel;
    }
}
