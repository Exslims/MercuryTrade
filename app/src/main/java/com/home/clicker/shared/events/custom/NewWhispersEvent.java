package com.home.clicker.shared.events.custom;

import com.home.clicker.shared.events.SCEvent;
import com.home.clicker.shared.pojo.Message;

import java.util.List;

/**
 * Exslims
 * 08.12.2016
 */
public class NewWhispersEvent implements SCEvent {
    private List<Message> messages;

    public NewWhispersEvent(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> writers) {
        this.messages = writers;
    }
}
