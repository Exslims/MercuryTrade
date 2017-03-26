package com.mercury.platform.ui.components.panel.message;

import lombok.NonNull;

public interface MessagePanelController {
    void performInvite();
    void performKick();
    void performOfferTrade();
    void performOpenChat();
    void performResponse(@NonNull String responseText);
    void performHide();

    /**
     * Show Item Stash Highlights
     */
    void showITH();
    void expandMessage();
    void collapseMessage();
    void reloadMessage(@NonNull MessagePanel panel);
}
