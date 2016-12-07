package com.home.clicker.utils;

import org.junit.Before;
import org.junit.Test;

/**
 * Exslims
 * 07.12.2016
 */
public class LogedMessagesUtilsTest {
    LogedMessagesUtils logedMessagesUtils;

    @Before
    public void setUp() throws Exception {
        logedMessagesUtils = new LogedMessagesUtils();
    }

    @Test
    public void testGetLoggedMessagesFile() throws Exception {
        logedMessagesUtils.execute();
    }
}