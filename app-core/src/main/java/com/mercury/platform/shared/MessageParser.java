package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private final static String poeTradePattern = "^(.*\\s)?(\\w+): Hi, I would like to buy your (.+) listed for (\\d+) (.+) in ([\\w -]+)(\\.*) (\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\))?(.*)$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(\\w+): Hi, I'd like to buy your (\\d+) (.+) for my (\\d+) (.+) in ([\\w-]+)(\\.*)(\\s*)(.*)$";

    private Pattern poeTradeItemPattern;
    private Pattern poeTradeCurrencyPattern;

    public MessageParser() {
        poeTradeItemPattern = Pattern.compile(poeTradePattern);
        poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
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
            if(poeTradeItemMatcher.group(9) != null) {
                message.setTabName(poeTradeItemMatcher.group(9));
                message.setLeft(Integer.parseInt(poeTradeItemMatcher.group(10)));
                message.setTop(Integer.parseInt(poeTradeItemMatcher.group(11)));
            }
            message.setOffer(poeTradeItemMatcher.group(12));
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
            message.setOffer(poeTradeCurrencyMatcher.group(10));
            return message;
        }
        return null;
    }
}
