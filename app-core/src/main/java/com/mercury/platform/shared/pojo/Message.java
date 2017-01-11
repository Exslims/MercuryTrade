package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 10.12.2016.
 */
public class Message {
    private Date messageDate;
    private String whisperNickname;
    private String offer;
    private int curCount;
    private String currency;

    public Message() {
    }

    public Message(String whisperNickname, String offer, Date msgDate, int curCount, String currency) {
        this.offer = offer;
        this.messageDate = msgDate;
        this.whisperNickname = whisperNickname;
        this.curCount = curCount;
        this.currency = currency;
    }

    public String getWhisperNickname() {
        return whisperNickname;
    }

    public void setWhisperNickname(String whisperNickname) {
        this.whisperNickname = whisperNickname;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public int getCurCount() {
        return curCount;
    }

    public void setCurCount(int curCount) {
        this.curCount = curCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
