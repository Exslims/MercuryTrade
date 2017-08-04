package com.mercury.platform.shared;

import com.mercury.platform.shared.entity.message.CurrencyMessage;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MessageParser {
    private final static String poeTradeStashTabPattern = "^(.*\\s)?(.+):.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\)\\s*?(.*)$";
    private final static String poeAppPattern = "^(.*\\s)?(.+):\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*)$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(.+):.+ to buy your (\\d+(\\.\\d+)?)? (.+) for my (\\d+(\\.\\d+)?)? (.+) in (.*?)\\.\\s*(.*)$";
    private Pattern poeAppItemPattern;
    private Pattern poeTradeStashItemPattern;
    private Pattern poeTradeCurrencyPattern;

    public MessageParser() {
        poeAppItemPattern = Pattern.compile(poeAppPattern);
        poeTradeStashItemPattern = Pattern.compile(poeTradeStashTabPattern);
        poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
    }

    public Message parse(String fullMessage){
        String sourceMessage = StringUtils.substringAfter(fullMessage, "From ");
        Matcher poeAppItemMatcher = poeAppItemPattern.matcher(sourceMessage);
        if(poeAppItemMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeAppItemMatcher.group(2));
            message.setItemName(poeAppItemMatcher.group(3));
            if(poeAppItemMatcher.group(5) != null) {
                message.setCurCount(Double.parseDouble(poeAppItemMatcher.group(5)));
                message.setCurrency(poeAppItemMatcher.group(6));
            }else {
                message.setCurCount(0d);
                message.setCurrency("???");
            }
            message.setLeague(poeAppItemMatcher.group(7));
            if(poeAppItemMatcher.group(8) != null) {
                message.setTabName(poeAppItemMatcher.group(8));
                message.setLeft(Integer.parseInt(poeAppItemMatcher.group(9)));
                message.setTop(Integer.parseInt(poeAppItemMatcher.group(10)));
            }
            message.setOffer(poeAppItemMatcher.group(11));
            return message;
        }
        Matcher poeTradeStashItemMatcher = poeTradeStashItemPattern.matcher(sourceMessage);
        if(poeTradeStashItemMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeStashItemMatcher.group(2));
            message.setItemName(poeTradeStashItemMatcher.group(3));
            if(poeTradeStashItemMatcher.group(4) != null){
                message.setCurCount(Double.parseDouble(poeTradeStashItemMatcher.group(5)));
                message.setCurrency(poeTradeStashItemMatcher.group(6));
            }else {
                message.setCurCount(0d);
                message.setCurrency("???");
            }
            message.setLeague(poeTradeStashItemMatcher.group(7));
            message.setTabName(poeTradeStashItemMatcher.group(8));
            message.setLeft(Integer.parseInt(poeTradeStashItemMatcher.group(9)));
            message.setTop(Integer.parseInt(poeTradeStashItemMatcher.group(10)));
            message.setOffer(poeTradeStashItemMatcher.group(11));
            return message;
        }
        Matcher poeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(sourceMessage);
        if(poeTradeCurrencyMatcher.find()){
            CurrencyMessage message = new CurrencyMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeCurrencyMatcher.group(2));
            message.setCurrForSaleCount(Double.parseDouble(poeTradeCurrencyMatcher.group(3)));
            message.setCurrForSaleTitle(poeTradeCurrencyMatcher.group(5));
            message.setCurCount(Double.parseDouble(poeTradeCurrencyMatcher.group(6)));
            message.setCurrency(poeTradeCurrencyMatcher.group(8));
            message.setLeague(poeTradeCurrencyMatcher.group(9));
            message.setOffer(poeTradeCurrencyMatcher.group(10));
            return message;
        }
        return null;
    }
}
