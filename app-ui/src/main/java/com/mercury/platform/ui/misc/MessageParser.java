package com.mercury.platform.ui.misc;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Константин on 23.12.2016.
 */
public class MessageParser {
    public static Map<String,String> parse(String message){
        Map<String,String> messageModel = new HashMap<>();

        Date msgDate = new Date(StringUtils.substring(message, 0, 20));
        messageModel.put("messageDate", new SimpleDateFormat().format(msgDate));
        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        if(itemName == null){
            itemName = StringUtils.substringBetween(message, "to buy your ", " for my");
        }
        messageModel.put("itemName",itemName);
        String price = StringUtils.substringBetween(message, "listed for ", " in ");
        if(price == null){
            price = StringUtils.substringBetween(message, "for my ", " in ");
        }
        if(price != null) {
            String[] split = price.split(" ");
            messageModel.put("curCount", split[0]);
            messageModel.put("currency",split[1]);
        }

        String offer = StringUtils.substringAfterLast(message, "in Breach."); //todo
        String tabName = StringUtils.substringBetween(message, "(stash tab ", "; position:");
        if(tabName !=null ){
            offer = StringUtils.substringAfter(message, ")");
            messageModel.put("tabName",tabName);
        }
        if(!Objects.equals(offer, "")) {
            messageModel.put("offer", offer);
        }
        return messageModel;
    }
}
