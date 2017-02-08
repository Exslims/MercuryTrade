package com.mercury.platform.server.main.listeners;

import com.mercury.platform.server.core.UpdaterServer;
import com.mercury.platform.server.main.listeners.operations.OperationThread;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;

/**
 * Created by Frost on 28.01.2017.
 */
public class StartServerButtonListener extends MouseAdapter {

    private static class ServerStarterThread extends OperationThread {

        public ServerStarterThread(UpdaterServer server) {
            super(server);
        }

        @Override
        public void run() {
            if (server.isStarted()) {
                JOptionPane.showMessageDialog(null , "Server is already started");
            }
            else
                this.server.run();
        }
    }

    private ServerStarterThread thread;

    public StartServerButtonListener(UpdaterServer server) {
        this.thread = new ServerStarterThread(server);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Executors.newSingleThreadExecutor().submit(this.thread);
    }
}
