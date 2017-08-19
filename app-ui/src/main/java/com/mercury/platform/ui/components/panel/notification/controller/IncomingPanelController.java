package com.mercury.platform.ui.components.panel.notification.controller;

import com.mercury.platform.ui.components.panel.notification.InMessagePanel;
import lombok.NonNull;

public interface IncomingPanelController extends NotificationController{
    void performInvite();
    void performKick();
    void showITH();
    void reloadMessage(@NonNull InMessagePanel panel);
}
