package com.mercury.platform.server.bus.event;

import java.net.InetSocketAddress;

/**
 * Created by Frost on 25.01.2017.
 */
public class ClientActiveEvent {

    private String ipAddress;

    public ClientActiveEvent(InetSocketAddress address) {
        this.ipAddress = address.getHostName();
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
