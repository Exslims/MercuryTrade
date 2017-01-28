package com.mercury.platform.server.main.listeners.operations;

import com.mercury.platform.server.core.UpdaterServer;

/**
 * Created by Frost on 28.01.2017.
 */
public abstract class OperationThread extends Thread {

    protected UpdaterServer server;


    public OperationThread(UpdaterServer server) {
        this.server = server;
    }

    @Override
    public abstract void run();
}