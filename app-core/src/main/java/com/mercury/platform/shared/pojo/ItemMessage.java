package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 11.01.2017.
 */
public class ItemMessage extends Message {
    private String itemName;
    private String tabName;

    public ItemMessage() {
    }

    public ItemMessage(String whisperNickname, Date messageDate, String itemName, int curCount, String currency, String tabName, String offer) {
        super(whisperNickname,offer,messageDate,curCount,currency);
        this.itemName = itemName;
        this.tabName = tabName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemMessage)) return false;

        ItemMessage that = (ItemMessage) o;

        if (getCurCount() != that.getCurCount()) return false;
        if (!getItemName().equals(that.getItemName())) return false;
        if (!getCurrency().equals(that.getCurrency())) return false;
        return getTabName().equals(that.getTabName());
    }

    @Override
    public int hashCode() {
        int result = getItemName().hashCode();
        result = 31 * result + getCurCount();
        result = 31 * result + getCurrency().hashCode();
        result = 31 * result + getTabName().hashCode();
        return result;
    }
}
