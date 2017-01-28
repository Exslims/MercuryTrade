package com.mercury.platform.server.bus.event;

import java.net.InetSocketAddress;

/**
 * Created by Frost on 28.01.2017.
 */
public class ClientUnregisteredEvent {
    private String ipAddress;

    public ClientUnregisteredEvent(InetSocketAddress address) {
        this.ipAddress = address.getHostName();
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
