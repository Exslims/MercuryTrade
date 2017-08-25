package com.mercury.platform.ui.components.panel.notification.controller;


import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

public class HistoryPanelController implements HistoryController {
    private NotificationDescriptor descriptor;

    public HistoryPanelController(NotificationDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void reload() {
        MercuryStoreCore.newNotificationSubject.onNext(this.descriptor);
    }

    @Override
    public void performOpenChat() {
        MercuryStoreCore.openChatSubject.onNext(this.descriptor.getWhisperNickname());
    }
}
