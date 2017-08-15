package com.mercury.platform.ui.components.panel.notification.controller;

import com.mercury.platform.ui.components.panel.notification.InMessagePanel;
import lombok.NonNull;

public interface IncomingPanelController {
    void performInvite();
    void performKick();
    void performOfferTrade();
    void performOpenChat();
    void performResponse(@NonNull String responseText);
    void performHide();
    void showITH();
    void reloadMessage(@NonNull InMessagePanel panel);
}
