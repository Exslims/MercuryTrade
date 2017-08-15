package com.mercury.platform.ui.components.panel.notification;

import lombok.NonNull;

public interface MessagePanelController {
    void performInvite();
    void performKick();
    void performOfferTrade();
    void performOpenChat();
    void performResponse(@NonNull String responseText);
    void performHide();
    void showITH();
    void reloadMessage(@NonNull InMessagePanel panel);
}
