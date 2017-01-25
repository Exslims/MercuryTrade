package com.mercury.platform.server.main;

import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.core.UpdaterServer;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        MercuryServerConfig serverConfig = MercuryServerConfig.getInstance();
        UpdaterServer server = new UpdaterServer(serverConfig.getPort());
        server.run();
    }
}
