package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * need refactoring this shit
 */
public class MessageParser {
    public Message parse(String fullMessage){
        Message parsedMessage;
        String wNickname = StringUtils.substringBetween(fullMessage, "@From", ":");
        String message = StringUtils.substringAfter(fullMessage, wNickname + ":");
        wNickname = StringUtils.deleteWhitespace(wNickname);
        //todo regexp
        if(wNickname.contains(">")){
            wNickname = StringUtils.substringAfterLast(wNickname, ">");
        }
        Date msgDate = new Date(StringUtils.substring(fullMessage, 0, 20));
        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        if(itemName == null){
            parsedMessage = getCurrencyMessage(message);
        }else {
            parsedMessage = getItemMessage(message);
        }
        parsedMessage.setWhisperNickname(wNickname);
        parsedMessage.setMessageDate(msgDate);
        return parsedMessage;
    }
    private ItemMessage getItemMessage(String message){
        ItemMessage itemMessage = new ItemMessage();
        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        String price = StringUtils.substringBetween(message, "listed for ", " in ");
        if(price == null){
            price = StringUtils.substringBetween(message, "for my ", " in ");
        }
        int curCount = 0;
        String currencyTitle = "";
        if(price != null) {
            String[] split = price.split(" ");
            curCount = Integer.parseInt(split[0]);
            currencyTitle = split[1];
        }

        String offer = StringUtils.substringAfterLast(message, "in Breach."); //todo
        String tabName = StringUtils.substringBetween(message, "(stash tab ", "; position:");
        if(tabName !=null ){
            offer = StringUtils.substringAfter(message, ")");
        }
        itemMessage.setItemName(itemName);
        itemMessage.setCurCount(curCount);
        itemMessage.setCurrency(currencyTitle);
        itemMessage.setTabName(tabName);
        itemMessage.setOffer(offer);
        return itemMessage;
    }
    private CurrencyMessage getCurrencyMessage(String message){
        CurrencyMessage currencyMessage = new CurrencyMessage();
        String currencyForSale = StringUtils.substringBetween(message, "to buy your ", " for my");
        int currForSaleCount = 0;
        String currForSaleTitle = "";
        if(currencyForSale != null) {
            String[] split = currencyForSale.split(" ");
            currForSaleCount = Integer.parseInt(split[0]);
            currForSaleTitle = split[1];
        }
        String price = StringUtils.substringBetween(message, "for my ", " in ");
        int priceCount = 0;
        String priceTitle = "";
        if(price != null) {
            String[] split = price.split(" ");
            priceCount = Integer.parseInt(split[0]);
            priceTitle = split[1];
        }
        String offer = StringUtils.substringAfterLast(message, "in Breach."); //todo
        currencyMessage.setCurrForSaleCount(currForSaleCount);
        currencyMessage.setCurrForSaleTitle(currForSaleTitle);
        currencyMessage.setCurCount(priceCount);
        currencyMessage.setCurrency(priceTitle);
        currencyMessage.setOffer(offer);
        return currencyMessage;
    }
}
