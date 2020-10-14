package com.mercury.platform;

import com.mercury.platform.core.DevStarter;
import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.other.MercuryLoadingFrame;
import com.mercury.platform.ui.frame.titled.GamePathChooser;
import com.mercury.platform.ui.manager.FramesManager;
import com.sun.jna.Native;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class AppMain {
    private static final Logger logger = LogManager.getLogger(AppMain.class);
    private final static String MERCURY_TRADE_FOLDER = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade";
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("jna.nosys", "true");
        new ErrorHandler();
        MercuryLoadingFrame mercuryLoadingFrame = new MercuryLoadingFrame();
        mercuryLoadingFrame.init();
        mercuryLoadingFrame.showComponent();
        File mercuryTradeFolder = new File(MERCURY_TRADE_FOLDER);
        if (!mercuryTradeFolder.exists()) {
            boolean mercuryTradeFolderCreated = mercuryTradeFolder.mkdirs();
            if (!mercuryTradeFolderCreated) {
                logger.error("Mercury trade folder in location %s couldn't be created - check permissions");
            }
        }
        if (args.length == 0) {
            new ProdStarter().startApplication();
        } else {
            new DevStarter().startApplication();
        }
        String configGamePath = Configuration.get().applicationConfiguration().get().getGamePath();
        if (configGamePath.equals("") || !isValidGamePath(configGamePath)) {
            String gamePath = getGamePath();
            if (gamePath == null) {
                MercuryStoreCore.appLoadingSubject.onNext(false);
                GamePathChooser gamePathChooser = new GamePathChooser();
                gamePathChooser.init();
            } else {
                gamePath = gamePath + "\\";
                Configuration.get().applicationConfiguration().get().setGamePath(gamePath);
                MercuryStoreCore.saveConfigSubject.onNext(true);
                new FileMonitor().start();
                FramesManager.INSTANCE.start();
                MercuryStoreCore.appLoadingSubject.onNext(false);
            }
        } else {
            new FileMonitor().start();
            FramesManager.INSTANCE.start();
            MercuryStoreCore.appLoadingSubject.onNext(false);
        }
    }

    private static boolean isValidGamePath(String gamePath) {
        File file = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return file.exists();
    }

    private static String getGamePath() {
        return WindowUtils.getAllWindows(false).stream().filter(window -> {
            char[] className = new char[512];
            User32.INSTANCE.GetClassName(window.getHWND(), className, 512);
            return Native.toString(className).equals("POEWindowClass");
        }).map(it -> {
            String filePath = it.getFilePath();
            return StringUtils.substringBeforeLast(filePath, "\\");
        }).findAny().orElse(null);
    }
}