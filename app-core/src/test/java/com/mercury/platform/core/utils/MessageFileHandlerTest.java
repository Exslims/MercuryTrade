package com.mercury.platform.core.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Константин on 02.01.2017.
 */
public class MessageFileHandlerTest {
    @Test
    public void buildMessages() throws Exception {
        MessageFileHandler handler = new MessageFileHandler();
        List<String> stubMessages = new ArrayList<>();
        stubMessages.add("2017/01/20 02:20:15 639290218 951 [INFO Client 6468] @From <REKTEM> ThreeBlindIce: Hi, I would like to buy your level 1 20% Raise Zombie in Breach (stash tab \"trade\"; position: left 12, top 2)");
        handler.buildMessages(stubMessages);
    }

}