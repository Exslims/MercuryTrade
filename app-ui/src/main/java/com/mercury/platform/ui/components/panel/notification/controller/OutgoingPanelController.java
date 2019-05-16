package com.mercury.platform.ui.components.panel.notification.controller;


public interface OutgoingPanelController extends NotificationController {
    void visitHideout();

    void performWhoIs();

    void performLeave(String nickName);
}
