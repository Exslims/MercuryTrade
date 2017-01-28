package com.mercury.platform.server.main;

import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.handlers.ClientConnectedEventHandler;
import com.mercury.platform.server.core.UpdaterServer;
import com.mercury.platform.ui.MercuryUpdaterFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    private static final Logger LOGGER = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(() -> {
            MercuryUpdaterFrame frame = new MercuryUpdaterFrame();
            frame.setVisible(true);
        });

    }
}
