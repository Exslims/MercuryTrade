package com.mercury.platform.client.bus.event;

/**
 * Created by Frost on 25.01.2017.
 */
public class UpdateReceivedEvent {

    private Byte[] bytes;


    public UpdateReceivedEvent(Byte[] bytes) {
        this.bytes = bytes;
    }

    public Byte[] getBytes() {
        return bytes;
    }
}
