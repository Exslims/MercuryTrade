package com.mercury.platform.server.main.listeners;

import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;
import com.mercury.platform.server.bus.handlers.ClientUnregisteredEventHandler;

import javax.swing.*;

/**
 * Created by Frost on 28.01.2017.
 */
public class UIClientUnregisteredListener implements ClientUnregisteredEventHandler {

    private JLabel label;
    private int activeUsersCount;

    public UIClientUnregisteredListener(JLabel label) {
        this.label = label;
        this.activeUsersCount = 0;
    }

    @Override
    public synchronized void onClientDisconnected(ClientUnregisteredEvent event) {
        this.activeUsersCount = Integer.valueOf(label.getText());
        this.activeUsersCount--;
        this.label.setText(String.valueOf(activeUsersCount));
    }

}