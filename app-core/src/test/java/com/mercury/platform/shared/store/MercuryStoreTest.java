package com.mercury.platform.shared.store;

import com.mercury.platform.shared.entity.KeyData;
import com.mercury.platform.shared.entity.SoundDescriptor;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MercuryStoreTest {
    @Test
    public void testSoundReducer() throws IOException {
        TestSubscriber<Map<String,String>> testSubscriber = new TestSubscriber<>();
        List<DesktopWindow> allWindows = WindowUtils.getAllWindows(false);
        allWindows.forEach(window -> {
            System.out.println(window.getFilePath());
        });
    }
}