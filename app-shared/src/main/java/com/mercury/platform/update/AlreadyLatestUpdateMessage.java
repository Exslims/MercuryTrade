package com.mercury.platform.update;

/**
 * Created by Константин on 07.03.2017.
 */
public class AlreadyLatestUpdateMessage extends ServerMessage {
    public AlreadyLatestUpdateMessage() {
        super("You have latest version.");
    }
}
