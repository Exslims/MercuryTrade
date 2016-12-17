package com.home.clicker.shared.events.custom;

import com.home.clicker.shared.events.SCEvent;

/**
 * Created by Константин on 17.12.2016.
 */
public class CopyToClipboardEvent implements SCEvent {
    private String content;

    public CopyToClipboardEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
