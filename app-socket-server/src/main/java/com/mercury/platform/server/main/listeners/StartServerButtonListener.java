package com.mercury.platform.server.main.listeners;

import com.mercury.platform.holder.UpdateHolder;
import com.mercury.platform.server.core.UpdaterServer;
import com.mercury.platform.server.main.listeners.operations.OperationThread;
import com.mercury.platform.ui.MercuryUpdaterFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

/**
 * Created by Frost on 28.01.2017.
 */
public class StartServerButtonListener extends MouseAdapter {
    private MercuryUpdaterFrame frame; //todo

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

    public StartServerButtonListener(MercuryUpdaterFrame frame, UpdaterServer server) { //todo
        this.frame = frame;
        this.thread = new ServerStarterThread(server);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            byte[] bytes = Files.readAllBytes(frame.getPath());
            UpdateHolder instance = UpdateHolder.getInstance();
            instance.setUpdate(bytes);
            instance.setVersion(Integer.valueOf(frame.getVersion().replace("." , "0")));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Executors.newSingleThreadExecutor().submit(this.thread);
    }
}
