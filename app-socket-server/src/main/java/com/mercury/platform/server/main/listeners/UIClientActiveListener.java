package com.mercury.platform.server.main.listeners;

import com.mercury.platform.server.bus.event.ClientActiveEvent;
import com.mercury.platform.server.bus.handlers.ClientActiveEventHandler;

import javax.swing.JLabel;

/**
 * Created by Frost on 28.01.2017.
 */
public class UIClientActiveListener implements ClientActiveEventHandler {

    private JLabel label;
    private int activeUsersCount;

    public UIClientActiveListener(JLabel label) {
        this.label = label;
        this.activeUsersCount = 0;
    }

    @Override
    public synchronized void onClientConnected(ClientActiveEvent event) {
        this.activeUsersCount = Integer.valueOf(label.getText());
        this.activeUsersCount++;
        this.label.setText(String.valueOf(activeUsersCount));
    }
}
