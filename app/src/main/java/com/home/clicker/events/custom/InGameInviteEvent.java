package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class InGameInviteEvent implements SCEvent {
    private String command;

    public InGameInviteEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
