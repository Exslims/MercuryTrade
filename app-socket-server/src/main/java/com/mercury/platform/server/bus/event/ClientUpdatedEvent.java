package com.mercury.platform.server.bus.event;

import java.net.InetSocketAddress;

/**
 * Created by Frost on 27.01.2017.
 */
public class ClientUpdatedEvent {
    private String ipAddress;

    public ClientUpdatedEvent(InetSocketAddress address) {
        this.ipAddress = address.getHostName();
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
