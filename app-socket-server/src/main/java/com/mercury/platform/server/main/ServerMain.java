package com.mercury.platform.server.main;

import com.mercury.platform.ui.MercuryUpdaterFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    private static final Logger LOGGER = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MercuryUpdaterFrame frame = new MercuryUpdaterFrame();
            frame.setVisible(true);
        });
    }
}
