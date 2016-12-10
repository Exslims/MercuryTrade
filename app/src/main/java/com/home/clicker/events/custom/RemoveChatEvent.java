package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class RemoveChatEvent implements SCEvent {
    private String whisperNickname;

    public RemoveChatEvent(String whisperNickname) {
        this.whisperNickname = whisperNickname;
    }

    public String getWhisperNickname() {
        return whisperNickname;
    }
}
