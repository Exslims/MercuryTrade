package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 11.01.2017.
 */
public class CurrencyMessage extends Message {
    private Double currForSaleCount;
    private String currForSaleTitle;

    public CurrencyMessage() {
    }

    public CurrencyMessage(String sourceString,String whisperNickname, String offer, Date msgDate, Double currForSaleCount, String currForSaleTitle, Double priceCount, String priceTitle) {
        super(sourceString,whisperNickname, offer, msgDate,priceCount,priceTitle);
        this.currForSaleCount = currForSaleCount;
        this.currForSaleTitle = currForSaleTitle;
    }

    public Double getCurrForSaleCount() {
        return currForSaleCount;
    }

    public void setCurrForSaleCount(Double currForSaleCount) {
        this.currForSaleCount = currForSaleCount;
    }

    public String getCurrForSaleTitle() {
        return currForSaleTitle;
    }

    public void setCurrForSaleTitle(String currForSaleTitle) {
        this.currForSaleTitle = currForSaleTitle;
    }
}
