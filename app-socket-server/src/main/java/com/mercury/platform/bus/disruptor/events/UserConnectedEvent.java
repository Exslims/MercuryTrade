package com.mercury.platform.bus.disruptor.events;

/**
 * Created by Frost on 25.01.2017.
 */
public class UserConnectedEvent {

    private String ipAddress;

    public UserConnectedEvent() {

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
