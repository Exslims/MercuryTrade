package com.mercury.platform.ui.components.panel.notification.controller;


import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import javax.swing.*;

public class NotificationOutgoingController implements OutgoingPanelController{
    private NotificationDescriptor notificationDescriptor;

    public NotificationOutgoingController(NotificationDescriptor notificationDescriptor) {
        this.notificationDescriptor = notificationDescriptor;
    }

    @Override
    public void visitHideout() {

    }

    @Override
    public void backToHideout() {

    }

    @Override
    public void performTrade() {

    }

    @Override
    public void performLeave() {

    }

    @Override
    public void performHide() {
        this.closeMessagePanel();
    }

    @Override
    public void performOpenChat() {

    }

    @Override
    public void performResponse(String response) {

    }

    private void closeMessagePanel(){
        Timer timer = new Timer(30, action -> {
            MercuryStoreCore.removeNotificationSubject.onNext(notificationDescriptor);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
