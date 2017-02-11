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

    public ItemMessage(String sourceString,String whisperNickname, Date messageDate, String itemName, Double curCount, String currency, String tabName, String offer) {
        super(sourceString,whisperNickname,offer,messageDate,curCount,currency);
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
}
