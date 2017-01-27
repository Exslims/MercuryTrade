package com.mercury.platform.server.main;

import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.handlers.ClientConnectedEventHandler;
import com.mercury.platform.server.core.UpdaterServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    private static final Logger LOGGER = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) throws Exception {
        MercuryServerConfig serverConfig = MercuryServerConfig.getInstance();

        UpdaterServerAsyncEventBus asyncEventBus = UpdaterServerAsyncEventBus.getInstance();

        asyncEventBus.register((ClientConnectedEventHandler) event ->
                LOGGER.info("Client connected, IP = {}" , event.getIpAddress())
        );

        UpdaterServer server = new UpdaterServer(serverConfig.getPort());
        server.run();
    }
}
