package com.mercury.platform.shared.store;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MercuryStoreCoreTest {
    @Test
    public void testSoundReducer() throws IOException {
        TestSubscriber<Map<String,String>> testSubscriber = new TestSubscriber<>();
        List<DesktopWindow> allWindows = WindowUtils.getAllWindows(false);
        allWindows.forEach(window -> {
            System.out.println(window.getFilePath());
        });
    }
}