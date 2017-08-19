package com.mercury.platform.ui.components.panel.notification.controller;


import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import javax.swing.*;

public class NotificationScannerController implements ScannerPanelController {
    private NotificationDescriptor notificationDescriptor;

    public NotificationScannerController(NotificationDescriptor notificationDescriptor) {
        this.notificationDescriptor = notificationDescriptor;
    }

    @Override
    public void visitHideout() {
        MercuryStoreCore.chatCommandSubject.onNext("/hideout " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performLeave(String nickName) {
        MercuryStoreCore.chatCommandSubject.onNext("/kick " + nickName);
    }

    @Override
    public void performOfferTrade() {
        MercuryStoreCore.chatCommandSubject.onNext("/tradewith " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performHide() {
        this.closeMessagePanel();
    }

    @Override
    public void performOpenChat() {
        MercuryStoreCore.openChatSubject.onNext(notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performResponse(String response) {
        MercuryStoreCore.chatCommandSubject.onNext("@" + notificationDescriptor.getWhisperNickname() + " " + response);
    }
    private void closeMessagePanel(){
        Timer timer = new Timer(30, action -> {
            MercuryStoreCore.removeNotificationSubject.onNext(notificationDescriptor);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
