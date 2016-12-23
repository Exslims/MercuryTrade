package com.home.clicker.ui.misc;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Константин on 23.12.2016.
 */
public class MessageParser {
    public static Map<String,String> parse(String message){
        Map<String,String> parts = new HashMap<>();

        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        if(itemName == null){
            itemName = StringUtils.substringBetween(message, "to buy your ", " for my");
        }
        parts.put("itemName",itemName);
        String price = StringUtils.substringBetween(message, "listed for ", " in ");
        if(price == null){
            price = StringUtils.substringBetween(message, "for my ", " in ");
        }
        if(price != null) {
            String[] split = price.split(" ");
            parts.put("curCount", split[0]);
            parts.put("currency",split[1]);
        }

        String offer = StringUtils.substringAfterLast(message, "in Breach."); //todo
        String tabName = StringUtils.substringBetween(message, "(stash tab ", "; position:");
        if(tabName !=null ){
            offer = StringUtils.substringAfter(message, ")");
            parts.put("tabName",tabName);
        }
        if(!Objects.equals(offer, "")) {
            parts.put("offer", offer);
        }
        return parts;
    }
}
