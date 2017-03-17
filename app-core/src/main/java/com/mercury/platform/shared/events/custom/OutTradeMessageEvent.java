package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.Message;


public class OutTradeMessageEvent implements MercuryEvent {
    private Message message;

    public OutTradeMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
