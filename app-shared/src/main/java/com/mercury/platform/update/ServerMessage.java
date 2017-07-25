package com.mercury.platform.update;

import java.io.Serializable;

public class ServerMessage implements Serializable {
    private String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
