package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.ItemMessage;

public class ShowItemGridEvent implements MercuryEvent{
    private ItemMessage message;

    public ShowItemGridEvent(ItemMessage message) {
        this.message = message;
    }

    public ItemMessage getMessage() {
        return message;
    }
}
