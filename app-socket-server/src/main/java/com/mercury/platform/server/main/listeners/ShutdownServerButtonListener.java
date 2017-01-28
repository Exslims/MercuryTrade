package com.mercury.platform.server.main.listeners;

import com.mercury.platform.server.core.UpdaterServer;
import com.mercury.platform.server.main.listeners.operations.OperationThread;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;

/**
 * Created by Frost on 28.01.2017.
 */
public class ShutdownServerButtonListener extends MouseAdapter {

    private static class ServerShutdownThread extends OperationThread {

        public ServerShutdownThread(UpdaterServer server) {
            super(server);
        }

        @Override
        public void run() {
            this.server.shutdown();
        }
    }

    private ServerShutdownThread thread;

    public ShutdownServerButtonListener(UpdaterServer server) {
        this.thread = new ServerShutdownThread(server);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Executors.newSingleThreadExecutor().submit(this.thread);
    }
}