package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;


public class PlayerJoinEvent implements MercuryEvent {
    private String nickName;

    public PlayerJoinEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
