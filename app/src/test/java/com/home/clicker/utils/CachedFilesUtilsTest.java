package com.home.clicker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Exslims
 * 07.12.2016
 */
public class CachedFilesUtilsTest {

    @Test
    public void testGetGamePath() throws Exception {
       assertEquals(CachedFilesUtils.getGamePath(),"");
    }
}