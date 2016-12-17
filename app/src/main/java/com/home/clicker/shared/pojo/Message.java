package com.home.clicker.shared.pojo;

/**
 * Created by Константин on 10.12.2016.
 */
public class Message {
    private String whisperNickname;
    private String message;

    public Message(String whisperNickname, String message) {
        this.whisperNickname = whisperNickname;
        this.message = message;
    }

    public String getWhisperNickname() {
        return whisperNickname;
    }

    public void setWhisperNickname(String whisperNickname) {
        this.whisperNickname = whisperNickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
