package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;

/**
 * Created by Константин on 28.12.2016.
 */
public class PlayerLeftEvent implements SCEvent{
    private String nickName;

    public PlayerLeftEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
