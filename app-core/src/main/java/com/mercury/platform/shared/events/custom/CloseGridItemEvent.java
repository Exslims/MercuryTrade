package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.ItemMessage;

public class CloseGridItemEvent implements MercuryEvent {
    private ItemMessage message;

    public CloseGridItemEvent(ItemMessage message) {
        this.message = message;
    }

    public ItemMessage getMessage() {
        return message;
    }

    public void setMessage(ItemMessage message) {
        this.message = message;
    }
}
