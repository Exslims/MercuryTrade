package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * need refactoring this shit
 */
public class MessageParser {
    private List<String> leagues; // todo

    public MessageParser() {
        this.leagues = new ArrayList<>();
        this.leagues.add("breach");
        this.leagues.add("standard");
        this.leagues.add("hardcore");
        this.leagues.add("hardcore breach");
        this.leagues.add("legacy");
        this.leagues.add("hardcore legacy");
    }

    public Message parse(String fullMessage){
        if(fullMessage.contains("Hi, I would like")){
            if(!fullMessage.contains("listed for")){
                if(fullMessage.contains(", offer is")){
                    return parsePoeEyeNoBuyoutMessage(fullMessage);
                }
                return parsePoeTradeNoBuyoutMessage(fullMessage);
            }
            return parsePoeTradeMessage(fullMessage);
        }else if(fullMessage.contains("wtb") && fullMessage.contains("])")){
            return parsePoeAppMessage(fullMessage);
        }else if(fullMessage.contains("Hi, I'd like")){
            return parseCurrencyMessage(fullMessage);
        }
        return null;
    }


    private Message parsePoeTradeMessage(String strMessage) {
        ItemMessage message = new ItemMessage();
        String nickname = getNickname(strMessage);
        String itemName = StringUtils.substringBetween(strMessage, "to buy your ", " listed for");
        String price = StringUtils.substringBetween(strMessage, "listed for ", " in ");
        Double curCount = null;
        String currencyTitle = "";
        if(price != null) {
            curCount = Double.parseDouble(StringUtils.substringBefore(price," "));
            currencyTitle = StringUtils.substringAfter(price," ");
        }

        String offer = StringUtils.substringAfter(strMessage, " in ");
        if(offer.contains("(stash tab")){
            offer = StringUtils.substringAfter(offer,")");
        }else {
            offer = findOffer(offer.toLowerCase());
        }

        message.setWhisperNickname(nickname);
        message.setItemName(itemName);
        message.setCurrency(currencyTitle);
        message.setCurCount(curCount);
        message.setOffer(offer);
        message.setSourceString(strMessage);

        return message;
    }
    private String findOffer(String offer) {
        String validOffer = "";
        for (String league : leagues) {
            if(StringUtils.contains(offer,league)){
                validOffer = StringUtils.substringAfter(offer,league);
            }
        }
        return validOffer;
    }

    private Message parsePoeTradeNoBuyoutMessage(String strMessage) {
        ItemMessage message = new ItemMessage();
        String nickname = getNickname(strMessage);
        String itemName = StringUtils.substringBetween(strMessage, "to buy your ", " in ");

        String offer = StringUtils.substringAfter(strMessage, " in ");
        if(offer.contains("(stash tab")){
            offer = StringUtils.substringAfter(offer,")");
        }else {
            offer = findOffer(offer.toLowerCase());
        }
        message.setWhisperNickname(nickname);
        message.setItemName(itemName);
        message.setCurCount(0d);
        message.setCurrency("???");
        message.setOffer(offer);
        message.setSourceString(strMessage);

        return message;
    }

    private Message parsePoeEyeNoBuyoutMessage(String strMessage) {
        ItemMessage message = new ItemMessage();
        String nickname = getNickname(strMessage);
        String itemName = StringUtils.substringBetween(strMessage, "to buy your ", " listed in ");
        String offer = StringUtils.substringAfter(strMessage,"offer is ");

        message.setWhisperNickname(nickname);
        message.setItemName(itemName);
        message.setOffer(offer);
        message.setCurCount(0d);
        message.setCurrency("???");
        message.setSourceString(strMessage);

        return message;
    }

    private Message parsePoeAppMessage(String strMessage) {
        ItemMessage message = new ItemMessage();
        String nickname = getNickname(strMessage);
        String itemName = StringUtils.substringBetween(strMessage,"wtb "," in ");

        String price = StringUtils.substringBetween(strMessage, "listed for ", "Orb");
        Double curCount = null;
        String currencyTitle = "";
        if(price != null) {
            curCount = Double.parseDouble(StringUtils.substringBefore(price," "));
            currencyTitle = StringUtils.substringAfter(price," ").trim().toLowerCase();
        }
        String offer = StringUtils.substringAfter(strMessage," Orb");
        message.setWhisperNickname(nickname);
        message.setItemName(itemName);
        message.setCurrency(currencyTitle);
        message.setCurCount(curCount);
        message.setOffer(offer);
        message.setSourceString(strMessage);
        return message;
    }
    private Message parseCurrencyMessage(String strMessage) {
        CurrencyMessage message = new CurrencyMessage();
        String nickname = getNickname(strMessage);
        String currencyForSale = StringUtils.substringBetween(strMessage, "to buy your ", " for my");
        Double currForSaleCount = null;
        String currForSaleTitle = "";
        if(currencyForSale != null) {
            currForSaleCount = Double.parseDouble(StringUtils.substringBefore(currencyForSale," "));
            currForSaleTitle = StringUtils.substringAfter(currencyForSale," ");
        }
        String price = StringUtils.substringBetween(strMessage, "for my ", " in ");
        Double priceCount = null;
        String priceTitle = "";
        if(price != null) {
            priceCount = Double.parseDouble(StringUtils.substringBefore(price," "));
            priceTitle = StringUtils.substringAfter(price," ");
        }
        String offer = StringUtils.substringAfter(strMessage, ".");
        message.setWhisperNickname(nickname);
        message.setCurrForSaleCount(currForSaleCount);
        message.setCurrForSaleTitle(currForSaleTitle);
        message.setCurCount(priceCount);
        message.setCurrency(priceTitle);
        message.setOffer(offer);
        message.setSourceString(strMessage);
        return message;
    }

    private String getNickname(String message){
        String nickname = StringUtils.substringBetween(message, "From ", ":");
        if(nickname.contains(">")){
            nickname = StringUtils.substringAfter(nickname,"> ");
        }
        return nickname;
    }
}
