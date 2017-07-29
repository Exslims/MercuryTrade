package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.other.MercuryLoadingFrame;
import com.mercury.platform.ui.frame.titled.GamePathChooser;
import com.mercury.platform.ui.manager.FramesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class AppMain {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d","false");
        new ErrorHandler();
        MercuryLoadingFrame mercuryLoadingFrame = new MercuryLoadingFrame();
        mercuryLoadingFrame.init();
        mercuryLoadingFrame.showComponent();
        new AppStarter().startApplication();
        String gamePath = Configuration.get().applicationConfiguration().get().getGamePath();
        if(gamePath.equals("") || !isValidGamePath(gamePath)){
            MercuryStoreCore.appLoadingSubject.onNext(false);
            GamePathChooser gamePathChooser = new GamePathChooser();
            gamePathChooser.init();
        }else {
            new FileMonitor().start();
            FramesManager.INSTANCE.start();
            MercuryStoreCore.appLoadingSubject.onNext(false);
        }
    }
    private static boolean isValidGamePath(String gamePath){
        File file = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return file.exists();
    }
}