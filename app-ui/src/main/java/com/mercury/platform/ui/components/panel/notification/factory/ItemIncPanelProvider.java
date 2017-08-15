package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.panel.notification.ItemIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.MessagePanelController;
import com.mercury.platform.ui.components.panel.notification.NotificationMessageController;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;


public class ItemIncPanelProvider extends NotificationPanelProvider<ItemTradeNotificationDescriptor,MessagePanelController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.INC_ITEM_MESSAGE);
    }

    @Override
    public NotificationPanel<ItemTradeNotificationDescriptor, MessagePanelController> build() {
        ItemIncNotificationPanel panel = new ItemIncNotificationPanel();
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
