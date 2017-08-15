package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.CurrencyIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.MessagePanelController;
import com.mercury.platform.ui.components.panel.notification.NotificationMessageController;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;

public class CurrencyIncPanelProvider extends NotificationPanelProvider<CurrencyTradeNotificationDescriptor,MessagePanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.INC_CURRENCY_MESSAGE);
    }

    @Override
    public NotificationPanel<CurrencyTradeNotificationDescriptor, MessagePanelController> build() {
        CurrencyIncNotificationPanel panel = new CurrencyIncNotificationPanel();
        panel.setData(this.data);
        if(this.controller != null){
            panel.setController(this.controller);
        }else {
            panel.setController(new NotificationMessageController(this.data));
        }
        panel.setComponentsFactory(this.componentsFactory);
        panel.subscribe();
        panel.onViewInit();
        return panel;
    }
}
