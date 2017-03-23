package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MessageParser {
    private final static String poeTradePattern = "^(.*\\s)?(.+):.+ to buy your (.+) listed for (\\d+) (.+) in ([\\w\\-\\s]+)\\.*\\s*(\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\))?\\s*(.*)$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(.+):.+ to buy your (\\d+) (.+) for my (\\d+) (.+) in ([\\w\\-\\s]+)\\.*\\s*(.*)$";
    private final static String poeTradeNoBuyout = "^(.*\\s)?(.+):.+ to buy your (.+) in ([\\w\\-\\s]+)\\.*\\s*(\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\))?\\s*(.*)$";

    private Pattern poeTradeItemPattern;
    private Pattern poeTradeCurrencyPattern;
    private Pattern poeTradeNoBuyoutPattern;

    public MessageParser() {
        poeTradeItemPattern = Pattern.compile(poeTradePattern);
        poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
        poeTradeNoBuyoutPattern = Pattern.compile(poeTradeNoBuyout);
    }

    public Message parse(String fullMessage){
        String sourceMessage = StringUtils.substringAfter(fullMessage, "From ");
        Matcher poeTradeItemMatcher = poeTradeItemPattern.matcher(sourceMessage);
        if(poeTradeItemMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeItemMatcher.group(2));
            message.setItemName(poeTradeItemMatcher.group(3));
            message.setCurCount(Double.parseDouble(poeTradeItemMatcher.group(4)));
            message.setCurrency(poeTradeItemMatcher.group(5));
            if(poeTradeItemMatcher.group(8) != null) {
                message.setTabName(poeTradeItemMatcher.group(8));
                message.setLeft(Integer.parseInt(poeTradeItemMatcher.group(9)));
                message.setTop(Integer.parseInt(poeTradeItemMatcher.group(10)));
            }
            message.setOffer(poeTradeItemMatcher.group(11));
            return message;
        }
        Matcher poeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(sourceMessage);
        if(poeTradeCurrencyMatcher.find()){
            CurrencyMessage message = new CurrencyMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeCurrencyMatcher.group(2));
            message.setCurrForSaleCount(Double.parseDouble(poeTradeCurrencyMatcher.group(3)));
            message.setCurrForSaleTitle(poeTradeCurrencyMatcher.group(4));
            message.setCurCount(Double.parseDouble(poeTradeCurrencyMatcher.group(5)));
            message.setCurrency(poeTradeCurrencyMatcher.group(6));
            message.setOffer(poeTradeCurrencyMatcher.group(8));
            return message;
        }
        Matcher poeTradeNoBuyoutMatcher = poeTradeNoBuyoutPattern.matcher(sourceMessage);
        if(poeTradeNoBuyoutMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeNoBuyoutMatcher.group(2));
            message.setItemName(poeTradeNoBuyoutMatcher.group(3));
            message.setCurCount(0d);
            message.setCurrency("???");
            if(poeTradeNoBuyoutMatcher.group(6) != null) {
                message.setTabName(poeTradeNoBuyoutMatcher.group(6));
                message.setLeft(Integer.parseInt(poeTradeNoBuyoutMatcher.group(7)));
                message.setTop(Integer.parseInt(poeTradeNoBuyoutMatcher.group(8)));
            }
            message.setOffer(poeTradeNoBuyoutMatcher.group(9));
            return message;
        }
        return null;
    }
}
