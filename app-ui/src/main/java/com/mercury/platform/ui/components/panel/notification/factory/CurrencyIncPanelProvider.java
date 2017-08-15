package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.CurrencyIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationIncomingController;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;

public class CurrencyIncPanelProvider extends NotificationPanelProvider<CurrencyTradeNotificationDescriptor,IncomingPanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.INC_CURRENCY_MESSAGE);
    }

    @Override
    public NotificationPanel<CurrencyTradeNotificationDescriptor, IncomingPanelController> build() {
        CurrencyIncNotificationPanel panel = new CurrencyIncNotificationPanel();
        panel.setData(this.data);
        if(this.controller != null){
            panel.setController(this.controller);
        }else {
            panel.setController(new NotificationIncomingController(this.data));
        }
        panel.setComponentsFactory(this.componentsFactory);
        panel.subscribe();
        panel.onViewInit();
        return panel;
    }
}
