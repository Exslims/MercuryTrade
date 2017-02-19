package com.mercury.platform.shared.pojo;

import java.util.Date;

/**
 * Created by Константин on 11.01.2017.
 */
public class ItemMessage extends Message {
    private String itemName;
    private String tabInfo;

    public ItemMessage() {
    }

    public ItemMessage(String sourceString,String whisperNickname, Date messageDate, String itemName, Double curCount, String currency, String tabName, String offer) {
        super(sourceString,whisperNickname,offer,messageDate,curCount,currency);
        this.itemName = itemName;
        this.tabInfo = tabName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTabInfo() {
        return tabInfo;
    }

    public void setTabInfo(String tabName) {
        this.tabInfo = tabName;
    }
}
