package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 11.01.2017.
 */
public class CurrencyMessage extends Message {
    private int currForSaleCount;
    private String currForSaleTitle;

    public CurrencyMessage() {
    }

    public CurrencyMessage(String whisperNickname, String offer, Date msgDate, int currForSaleCount, String currForSaleTitle, int priceCount, String priceTitle) {
        super(whisperNickname, offer, msgDate,priceCount,priceTitle);
        this.currForSaleCount = currForSaleCount;
        this.currForSaleTitle = currForSaleTitle;
    }

    public int getCurrForSaleCount() {
        return currForSaleCount;
    }

    public void setCurrForSaleCount(int currForSaleCount) {
        this.currForSaleCount = currForSaleCount;
    }

    public String getCurrForSaleTitle() {
        return currForSaleTitle;
    }

    public void setCurrForSaleTitle(String currForSaleTitle) {
        this.currForSaleTitle = currForSaleTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyMessage)) return false;

        CurrencyMessage that = (CurrencyMessage) o;

        if (getCurrForSaleCount() != that.getCurrForSaleCount()) return false;
        return getCurrForSaleTitle().equals(that.getCurrForSaleTitle());
    }

    @Override
    public int hashCode() {
        int result = getCurrForSaleCount();
        result = 31 * result + getCurrForSaleTitle().hashCode();
        return result;
    }
}
