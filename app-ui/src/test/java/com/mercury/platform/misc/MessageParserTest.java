package com.mercury.platform.misc;

import com.mercury.platform.shared.MessageParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Константин on 23.12.2016.
 */
public class MessageParserTest {
    @Test
    public void parse() throws Exception {
//        Map<String, String> result = MessageParser.parse("2016/12/26 05:20:19 Hi, I would like to buy your Golem Gutter Royal Skean listed for 1 exalted in Breach (stash tab \"priced\"; position: left 4, top 7)");
//        Map<String,String> expectedResult = new HashMap<>();
//        expectedResult.put("messageDate","26.12.16 5:20");
//        expectedResult.put("itemName","Golem Gutter Royal Skean");
//        expectedResult.put("curCount","1");
//        expectedResult.put("currency","exalted");
//        expectedResult.put("tabName","\"priced\"");
//        assertEquals(result,expectedResult);
//
//        result = MessageParser.parse("2016/12/26 05:20:19 Hi, I would like to buy your Golem Gutter Royal Skean listed for 1 exalted in Breach.");
//        expectedResult.remove("tabName");
//        assertEquals(result,expectedResult);
//
//        result = MessageParser.parse("2016/12/26 05:20:19 Hi, I would like to buy your Golem Gutter Royal Skean listed for 1 exalted in Breach. can u sell it cheaper pls");
//        expectedResult.put("offer"," can u sell it cheaper pls");
//        assertEquals(result,expectedResult);
//
//        result = MessageParser.parse("2016/12/26 05:20:19 Hi, I would like to buy your Golem Gutter Royal Skean listed for 1 exalted in Breach (stash tab \"priced\"; position: left 4, top 7) can u sell it cheaper pls");
//        expectedResult.put("tabName","\"priced\"");
//        assertEquals(result,expectedResult);
//
//        result = MessageParser.parse("2016/12/26 05:20:19 Hi, I'd like to buy your 209 chaos for my 3 exalted in Breach.");
//        expectedResult.clear();
//        expectedResult.put("messageDate","26.12.16 5:20");
//        expectedResult.put("itemName","209 chaos");
//        expectedResult.put("curCount","3");
//        expectedResult.put("currency","exalted");
//        assertEquals(result,expectedResult);
//
//        result = MessageParser.parse("2016/12/26 05:20:19 Hi, I'd like to buy your 209 chaos for my 3 exalted in Breach.mb 69 for 1?");
//        expectedResult.put("offer","mb 69 for 1?");
//        assertEquals(result,expectedResult);
    }

}