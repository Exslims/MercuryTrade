package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 10.12.2016.
 */
public class Message {
    private String sourceString;
    private Date messageDate;
    private String whisperNickname;
    private String offer;
    private Double curCount;
    private String currency;

    public Message() {
    }

    public Message(String sourceString, String whisperNickname, String offer, Date msgDate, Double curCount, String currency) {
        this.sourceString = sourceString;
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

    public Double getCurCount() {
        return curCount;
    }

    public void setCurCount(Double curCount) {
        this.curCount = curCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSourceString() {
        return sourceString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }
}
