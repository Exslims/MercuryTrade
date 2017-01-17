package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 17.12.2016.
 */
public class CopyToClipboardEvent implements MercuryEvent {
    private String content;

    public CopyToClipboardEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
