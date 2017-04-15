package com.mercury.platform.shared.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private String sourceString;
    private Date messageDate;
    private String whisperNickname;
    private String offer;
    private Double curCount;
    private String currency;
    private String league;
}
