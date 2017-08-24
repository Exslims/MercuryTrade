package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.CurrencyTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationIncomingController;

public class CurrencyIncPanelProvider extends NotificationPanelProvider<CurrencyTradeNotificationDescriptor, IncomingPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.INC_CURRENCY_MESSAGE);
    }

    @Override
    protected NotificationPanel<CurrencyTradeNotificationDescriptor, IncomingPanelController> getPanel() {
        CurrencyTradeIncNotificationPanel panel = new CurrencyTradeIncNotificationPanel();
        panel.setController(new NotificationIncomingController(this.data));
        return panel;
    }
}
