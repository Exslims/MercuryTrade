package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 19.02.2017.
 */
public class ShowItemMeshEvent implements MercuryEvent{
    private String nickname;
    private String tabInfo;

    public ShowItemMeshEvent(String nickname, String tabInfo) {
        this.nickname = nickname;
        this.tabInfo = tabInfo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTabInfo() {
        return tabInfo;
    }
}
