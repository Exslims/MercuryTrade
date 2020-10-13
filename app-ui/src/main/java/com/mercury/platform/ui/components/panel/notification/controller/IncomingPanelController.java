package com.mercury.platform.ui.components.panel.notification.controller;


public interface IncomingPanelController extends NotificationController {
    void performInvite();

    void performKickLeave(String nickName);

    void showITH();
}
