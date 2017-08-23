package com.mercury.platform.shared;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private final static String poeTradeStashTabPattern = "^(.*\\s)?(.+):.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\)\\s*?(.*)$";
    private final static String poeTradePattern = "^(.*\\s)?(.+):.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.*?)$";
    private final static String poeAppPattern = "^(.*\\s)?(.+):\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*)$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(.+):.+ to buy your (\\d+(\\.\\d+)?)? (.+) for my (\\d+(\\.\\d+)?)? (.+) in (.*?)\\.\\s*(.*)$";
    private Pattern poeAppItemPattern;
    private Pattern poeTradeStashItemPattern;
    private Pattern poeTradeItemPattern;
    private Pattern poeTradeCurrencyPattern;

    public MessageParser() {
        this.poeAppItemPattern = Pattern.compile(poeAppPattern);
        this.poeTradeStashItemPattern = Pattern.compile(poeTradeStashTabPattern);
        this.poeTradeItemPattern = Pattern.compile(poeTradePattern);
        this.poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
    }

    public NotificationDescriptor parse(String fullMessage) {
        Matcher poeAppItemMatcher = poeAppItemPattern.matcher(fullMessage);
        if (poeAppItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setSourceString(fullMessage);
            tradeNotification.setWhisperNickname(poeAppItemMatcher.group(2));
            tradeNotification.setItemName(poeAppItemMatcher.group(3));
            if (poeAppItemMatcher.group(5) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeAppItemMatcher.group(5)));
                tradeNotification.setCurrency(poeAppItemMatcher.group(6));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeAppItemMatcher.group(7));
            if (poeAppItemMatcher.group(8) != null) {
                tradeNotification.setTabName(poeAppItemMatcher.group(8));
                tradeNotification.setLeft(Integer.parseInt(poeAppItemMatcher.group(9)));
                tradeNotification.setTop(Integer.parseInt(poeAppItemMatcher.group(10)));
            }
            tradeNotification.setOffer(poeAppItemMatcher.group(11));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeStashItemMatcher = poeTradeStashItemPattern.matcher(fullMessage);
        if (poeTradeStashItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setSourceString(fullMessage);
            tradeNotification.setWhisperNickname(poeTradeStashItemMatcher.group(2));
            tradeNotification.setItemName(poeTradeStashItemMatcher.group(3));
            if (poeTradeStashItemMatcher.group(4) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeTradeStashItemMatcher.group(5)));
                tradeNotification.setCurrency(poeTradeStashItemMatcher.group(6));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeTradeStashItemMatcher.group(7));
            tradeNotification.setTabName(poeTradeStashItemMatcher.group(8));
            tradeNotification.setLeft(Integer.parseInt(poeTradeStashItemMatcher.group(9)));
            tradeNotification.setTop(Integer.parseInt(poeTradeStashItemMatcher.group(10)));
            tradeNotification.setOffer(poeTradeStashItemMatcher.group(11));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(fullMessage);
        if (poeTradeCurrencyMatcher.find()) {
            CurrencyTradeNotificationDescriptor tradeNotification = new CurrencyTradeNotificationDescriptor();
            tradeNotification.setSourceString(fullMessage);
            tradeNotification.setWhisperNickname(poeTradeCurrencyMatcher.group(2));
            tradeNotification.setCurrForSaleCount(Double.parseDouble(poeTradeCurrencyMatcher.group(3)));
            tradeNotification.setCurrForSaleTitle(poeTradeCurrencyMatcher.group(5));
            tradeNotification.setCurCount(Double.parseDouble(poeTradeCurrencyMatcher.group(6)));
            tradeNotification.setCurrency(poeTradeCurrencyMatcher.group(8));
            tradeNotification.setLeague(poeTradeCurrencyMatcher.group(9));
            tradeNotification.setOffer(poeTradeCurrencyMatcher.group(10));
            tradeNotification.setType(NotificationType.INC_CURRENCY_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeItemMatcher = poeTradeItemPattern.matcher(fullMessage);
        if (poeTradeItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setSourceString(fullMessage);
            tradeNotification.setWhisperNickname(poeTradeItemMatcher.group(2));
            tradeNotification.setItemName(poeTradeItemMatcher.group(3));
            if (poeTradeItemMatcher.group(4) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeTradeItemMatcher.group(5)));
                tradeNotification.setCurrency(poeTradeItemMatcher.group(6));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeTradeItemMatcher.group(7));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        return null;
    }
}
