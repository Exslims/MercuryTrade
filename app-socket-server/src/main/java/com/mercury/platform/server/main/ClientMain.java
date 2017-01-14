package com.mercury.platform.server.main;

import com.mercury.platform.server.core.UpdaterClient;

/**
 * Created by Frost on 14.01.2017.
 */
public class ClientMain {

    public static void main(String[] args) throws Exception {
        UpdaterClient updaterClient = new UpdaterClient("localhost" , 10_000);
        updaterClient.start();
    }
}
