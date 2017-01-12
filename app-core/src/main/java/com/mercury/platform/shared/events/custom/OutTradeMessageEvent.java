package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;
import com.mercury.platform.shared.pojo.Message;

/**
 * Created by Константин on 12.01.2017.
 */
public class OutTradeMessageEvent implements SCEvent {
    private Message message;

    public OutTradeMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
