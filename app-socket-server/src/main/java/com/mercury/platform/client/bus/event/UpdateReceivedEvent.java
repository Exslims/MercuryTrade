package com.mercury.platform.client.bus.event;

/**
 * Created by Frost on 25.01.2017.
 */
public class UpdateReceivedEvent {

    private byte[] bytes;


    public UpdateReceivedEvent(byte [] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
