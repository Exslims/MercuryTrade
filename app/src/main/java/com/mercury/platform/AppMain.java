package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.path.GamePathSearcher;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.GamePathChooser;
import com.mercury.platform.ui.manager.FramesManager;

import javax.swing.*;


public class AppMain {
    public static void main(String[] args) {
        new AppStarter().startApplication();
        String gamePath = ConfigManager.INSTANCE.getGamePath();

        if(gamePath.equals("") || !ConfigManager.INSTANCE.isValidGamePath(gamePath)){
            String path = new GamePathSearcher().getGamePath();
            if(path == null){
                SwingUtilities.invokeLater(() -> {
                    GamePathChooser gamePathChooser = new GamePathChooser();
                    gamePathChooser.init();
                });
            }else {
                ConfigManager.INSTANCE.setGamePath(path);
                new FileMonitor().start();
                FramesManager.INSTANCE.start();
            }
        }else{
            new FileMonitor().start();
            FramesManager.INSTANCE.start();
        }
    }
}