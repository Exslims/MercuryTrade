package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Константин on 11.01.2017.
 */
public class MessageParserTest {
    private MessageParser parser;
    @Before
    public void before(){
        parser = new MessageParser();
    }

    @Test
    public void testPoeTrade()  throws Exception{
        ItemMessage message1 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 1 alteration in Hardcore Breach (stash tab \"Maps\"; position: left 5, top 6)");
        assertEquals(message1.getWhisperNickname(),"Pubesmannen");
        assertEquals(message1.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message1.getCurrency(),"alteration");
        assertEquals(message1.getCurCount(),new Double(1));
        assertEquals(message1.getOffer(),"");

        ItemMessage message2 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 15 exalt in Breach (stash tab \"Maps\"; position: left 5, top 6) offer 32");
        assertEquals(message2.getWhisperNickname(),"Pubesmannen");
        assertEquals(message2.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message2.getCurrency(),"exalt");
        assertEquals(message2.getCurCount(),new Double(15));
        assertEquals(message2.getOffer()," offer 32");

        ItemMessage message3 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 15 exalt in Breach");
        assertEquals(message3.getWhisperNickname(),"Pubesmannen");
        assertEquals(message3.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message3.getCurrency(),"exalt");
        assertEquals(message3.getCurCount(),new Double(15));
        assertEquals(message3.getOffer(),"");

        ItemMessage message4 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 15 exalt in Hardcore Breach 123");
        assertEquals(message4.getWhisperNickname(),"Pubesmannen");
        assertEquals(message4.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message4.getCurrency(),"exalt");
        assertEquals(message4.getCurCount(),new Double(15));
        assertEquals(message4.getOffer()," 123");

        ItemMessage message5 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 15 exalt in Standard,123123123");
        assertEquals(message5.getWhisperNickname(),"Pubesmannen");
        assertEquals(message5.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message5.getCurrency(),"exalt");
        assertEquals(message5.getCurCount(),new Double(15));
        assertEquals(message5.getOffer(),",123123123");

        ItemMessage message6 = (ItemMessage) parser.parse("@From <TEST> Pubesmannen: Hi, I would like to buy your Bronn's Lithe Cutthroat's Garb listed for 15 exalt in hardcore,123123123");
        assertEquals(message6.getWhisperNickname(),"Pubesmannen");
        assertEquals(message6.getItemName(),"Bronn's Lithe Cutthroat's Garb");
        assertEquals(message6.getCurrency(),"exalt");
        assertEquals(message6.getCurCount(),new Double(15));
        assertEquals(message6.getOffer(),",123123123");
    }

    @Test
    public void testPoeTradeNoBuyout()  throws Exception{
        ItemMessage message1 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Hardcore Breach (stash tab \"qgems\"; position: left 12, top 4)");
        assertEquals(message1.getWhisperNickname(),"Pubesmannen");
        assertEquals(message1.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message1.getOffer(),"");

        ItemMessage message2 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Hardcore Breach");
        assertEquals(message2.getWhisperNickname(),"Pubesmannen");
        assertEquals(message2.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message2.getOffer(),"");

        ItemMessage message3 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Breach 123");
        assertEquals(message3.getWhisperNickname(),"Pubesmannen");
        assertEquals(message3.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message3.getOffer()," 123");

        ItemMessage message4 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Hardcore 123");
        assertEquals(message4.getWhisperNickname(),"Pubesmannen");
        assertEquals(message4.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message4.getOffer()," 123");

        ItemMessage message5 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Standard 123");
        assertEquals(message5.getWhisperNickname(),"Pubesmannen");
        assertEquals(message5.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message5.getOffer()," 123");

        ItemMessage message6 = (ItemMessage) parser.parse("@From Pubesmannen: Hi, I would like to buy your level 1 4% Reduced Mana Support in Standard");
        assertEquals(message6.getWhisperNickname(),"Pubesmannen");
        assertEquals(message6.getItemName(),"level 1 4% Reduced Mana Support");
        assertEquals(message6.getOffer(),"");
    }

    @Test
    public void testPoeEyeNoBuyout()  throws Exception{
        ItemMessage message1 = (ItemMessage) parser.parse("@From Gibso: Hi, I would like to buy your Herald of Ice listed in Breach, offer is ");
        assertEquals(message1.getWhisperNickname(),"Gibso");
        assertEquals(message1.getItemName(),"Herald of Ice");
        assertEquals(message1.getOffer(),"");

        ItemMessage message2 = (ItemMessage) parser.parse("@From Gibso: Hi, I would like to buy your Herald of Ice listed in Breach, offer is 123");
        assertEquals(message2.getWhisperNickname(),"Gibso");
        assertEquals(message2.getItemName(),"Herald of Ice");
        assertEquals(message2.getOffer(),"123");
    }

    @Test
    public void testPoeApp()  throws Exception{
        ItemMessage message1 = (ItemMessage) parser.parse("@From Gibso: wtb Corruption Mark Harbinger Bow in breach (Shop [left:3,top:8]) listed for 1 Chaos Orb");
        assertEquals(message1.getWhisperNickname(),"Gibso");
        assertEquals(message1.getItemName(),"Corruption Mark Harbinger Bow");
        assertEquals(message1.getCurrency(),"chaos");
        assertEquals(message1.getCurCount(),new Double(1));
        assertEquals(message1.getOffer(),"");

        ItemMessage message2 = (ItemMessage) parser.parse("@From Gibso: wtb Corruption Mark Harbinger Bow in breach (Shop [left:3,top:8]) listed for 15 Exalted Orb 123123");
        assertEquals(message2.getWhisperNickname(),"Gibso");
        assertEquals(message2.getItemName(),"Corruption Mark Harbinger Bow");
        assertEquals(message2.getCurrency(),"exalted");
        assertEquals(message2.getCurCount(),new Double(15));
        assertEquals(message2.getOffer()," 123123");
    }

    @Test
    public void testPoeCurrency()  throws Exception{
        CurrencyMessage message1 = (CurrencyMessage) parser.parse("@From tradeeer: Hi, I'd like to buy your 366 chaos for my 5 exalted in Breach.");
        assertEquals(message1.getWhisperNickname(),"tradeeer");
        assertEquals(message1.getCurrForSaleCount(),new Double(366));
        assertEquals(message1.getCurrForSaleTitle(),"chaos");
        assertEquals(message1.getCurrency(),"exalted");
        assertEquals(message1.getCurCount(),new Double(5));
        assertEquals(message1.getOffer(),"");

        CurrencyMessage message2 = (CurrencyMessage) parser.parse("@From <qwe> tradeeer: Hi, I'd like to buy your 366 chaos for my 5 exalted in Breach. 123");
        assertEquals(message2.getWhisperNickname(),"tradeeer");
        assertEquals(message2.getCurrForSaleCount(),new Double(366));
        assertEquals(message2.getCurrForSaleTitle(),"chaos");
        assertEquals(message2.getCurrency(),"exalted");
        assertEquals(message2.getCurCount(),new Double(5));
        assertEquals(message2.getOffer()," 123");
    }

}