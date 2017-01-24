package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.ui.manager.FramesManager;


public class AppMain {
    public static void main(String[] args) {
        new AppStarter().startApplication();
        FramesManager.INSTANCE.start();
    }
}