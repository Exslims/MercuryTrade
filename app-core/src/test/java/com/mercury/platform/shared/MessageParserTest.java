package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Константин on 11.01.2017.
 */
public class MessageParserTest {
    @Test
    public void parse() throws Exception {
        MessageParser parser = new MessageParser();
        Message msg1 = parser.parse("2017/01/11 11:08:01 1448535531 951 [INFO Client 6836] @From GiveMeOneSec: Hi, I would like to buy your Golem Gutter Royal Skean listed for 1 exalted in Breach (stash tab \"priced\"; position: left 4, top 7)");
        assertTrue(msg1 instanceof ItemMessage);
        Message msg2 = parser.parse("2017/01/11 13:41:25 1457739437 951 [INFO Client 6836] @From <(ROA)> Exslimsbf: Hi, I'd like to buy your 20 chaos for my 275 chrome in Breach.");
        assertTrue(msg2 instanceof CurrencyMessage);
    }

}