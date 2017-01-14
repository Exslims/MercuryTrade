package com.mercury.platform.server.main;

import com.mercury.platform.server.core.UpdaterServer;

/**
 * Created by Frost on 02.01.2017.
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        UpdaterServer server = new UpdaterServer(10_000);
        server.run();
    }
}
