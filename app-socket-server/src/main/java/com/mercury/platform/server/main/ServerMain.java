package com.mercury.platform.server.main;

import com.mercury.platform.bus.MessageBus;
import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.core.UpdaterServer;

import java.net.InetSocketAddress;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {

//        MessageBus messageBus = MessageBus.getInstance();
//        messageBus.fireUserConnectedEvent(new InetSocketAddress("127.0.0.1" , 5021));

        MercuryServerConfig serverConfig = MercuryServerConfig.getInstance();
        UpdaterServer server = new UpdaterServer(serverConfig.getPort());
        server.run();
    }
}
