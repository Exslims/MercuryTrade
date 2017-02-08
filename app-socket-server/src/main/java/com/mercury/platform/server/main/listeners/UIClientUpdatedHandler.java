package com.mercury.platform.server.main.listeners;

import com.mercury.platform.server.bus.event.ClientUpdatedEvent;
import com.mercury.platform.server.bus.handlers.ClientUpdatedEventHandler;

import javax.swing.*;

/**
 * Created by Frost on 08.02.2017.
 */
public class UIClientUpdatedHandler implements ClientUpdatedEventHandler {

    private JLabel label;
    private int updatedUsersCount;

    public UIClientUpdatedHandler(JLabel label) {
        this.label = label;
        this.updatedUsersCount = 0;
    }

    @Override
    public synchronized void onClientUpdated(ClientUpdatedEvent event) {
        this.updatedUsersCount = Integer.valueOf(label.getText());
        this.updatedUsersCount++;
        this.label.setText(String.valueOf(updatedUsersCount));
    }
}
