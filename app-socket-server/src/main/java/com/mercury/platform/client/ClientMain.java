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
        UpdaterClient updaterClient = new UpdaterClient("localhost" , 10_000);
        updaterClient.registerListener(handler -> LOGGER.info("update received, size = {} bytes" , handler.getBytes().length));
        updaterClient.start();
    }
}
