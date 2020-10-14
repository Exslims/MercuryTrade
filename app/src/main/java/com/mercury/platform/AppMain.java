package com.mercury.platform;

import com.mercury.platform.core.DevStarter;
import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.experimental.PerformanceHelper;
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
import java.util.Arrays;

public class AppMain {
    private static final Logger logger = LogManager.getLogger(AppMain.class);
    private static boolean shouldLogPerformance = false;
    private final static String MERCURY_TRADE_FOLDER = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade";

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("-dev")) {
            shouldLogPerformance = true;
        }
        PerformanceHelper pf = new PerformanceHelper(shouldLogPerformance);
        pf.step("start");
        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("jna.nosys", "true");
        pf.step("set props");
        new ErrorHandler();
        pf.step("error handler");
        MercuryLoadingFrame mercuryLoadingFrame = new MercuryLoadingFrame();
        pf.step("new MercuryLoadingFrame()");
        mercuryLoadingFrame.init();
        pf.step("mercuryLoadingFrame.init()");
        mercuryLoadingFrame.showComponent();
        pf.step("mercuryLoadingFrame.showComponent()");

        checkCreateAppDataFolder();
        pf.step("mercuryTradeFolder creation");
        if (args.length == 0) {
            new ProdStarter().startApplication();
        } else {
            new DevStarter().startApplication();
        }
        pf.step("startApplication()");

        String configGamePath = Configuration.get().applicationConfiguration().get().getGamePath();
        pf.step("Configuration.get().applicationConfiguration().get().getGamePath()");
        if (configGamePath.equals("") || !isValidGamePath(configGamePath)) {
            pf.step("valid game path");
            String gamePath = getGamePath();
            pf.step("getGamePath");
            if (gamePath == null) {
                MercuryStoreCore.appLoadingSubject.onNext(false);
                GamePathChooser gamePathChooser = new GamePathChooser();
                gamePathChooser.init();
                pf.step("gamePathChooser.init()");
            } else {
                gamePath = gamePath + "\\";
                Configuration.get().applicationConfiguration().get().setGamePath(gamePath);
                MercuryStoreCore.saveConfigSubject.onNext(true);
                new FileMonitor().start();
                FramesManager.INSTANCE.start();
                MercuryStoreCore.appLoadingSubject.onNext(false);
                pf.step("MercuryStoreCore.appLoadingSubject.onNext");
            }
        } else {
            new FileMonitor().start();
            pf.step("new FileMonitor().start()");
            FramesManager.INSTANCE.start();
            pf.step("FramesManager.INSTANCE.start()");
            MercuryStoreCore.appLoadingSubject.onNext(false);
            pf.step("MercuryStoreCore.appLoadingSubject.onNext(false);");
        }
        pf.step("end loading");
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

    private static void checkCreateAppDataFolder() {
        File mercuryTradeFolder = new File(MERCURY_TRADE_FOLDER);
        if (!mercuryTradeFolder.exists()) {
            boolean mercuryTradeFolderCreated = mercuryTradeFolder.mkdirs();
            if (!mercuryTradeFolderCreated) {
                logger.error("Mercury trade folder in location %s couldn't be created - check permissions");
            }
        }
    }
}