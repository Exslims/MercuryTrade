package com.mercury.platform.ui.components.panel.notification.factory;

import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.panel.notification.HistoryNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.HistoryController;


public class HistoryPanelProvider extends NotificationPanelProvider<NotificationDescriptor,HistoryController> {
    @Override
    public boolean isSuitable(NotificationType type) {
        return type.equals(NotificationType.HISTORY);
    }

    @Override
    protected NotificationPanel<NotificationDescriptor, HistoryController> getPanel() {
        HistoryNotificationPanel panel = new HistoryNotificationPanel();
        panel.setController(new HistoryController() {
            @Override
            public void reload() {
                MercuryStoreCore.newNotificationSubject.onNext(data);
            }

            @Override
            public void performOpenChat() {
                MercuryStoreCore.openChatSubject.onNext(data.getWhisperNickname());
            }
        });
        return panel;
    }
}
