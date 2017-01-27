package com.mercury.platform.server.bus.event;

/**
 * Created by Frost on 25.01.2017.
 */
public class ClientConnectedEvent {

    private String ipAddress;


    public ClientConnectedEvent(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
