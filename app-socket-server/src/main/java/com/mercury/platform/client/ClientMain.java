package com.mercury.platform.client;

import com.mercury.platform.client.core.UpdaterClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 14.01.2017.
 */
public class ClientMain {
    private static final Logger LOGGER = LogManager.getLogger(ClientMain.class);

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        String mercuryVersion = "1.0.1.3";
        int port = 10_000;
        UpdaterClient updaterClient = new UpdaterClient(host, mercuryVersion, port);
        updaterClient.registerListener(handler -> LOGGER.info("update received, size = {} bytes" , handler.getBytes().length));
        updaterClient.start();
    }
}
