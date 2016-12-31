package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.impl.GamePathChooser;
import com.mercury.platform.ui.frame.impl.TaskBarFrame;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppMain {
    public static void main(String[] args) {
        String gamePath = ConfigManager.INSTANCE.getGamePath();
        if(gamePath == null || !ConfigManager.INSTANCE.isValidPath(gamePath)) {
            GamePathChooser gamePathChooser = new GamePathChooser();
            gamePathChooser.setVisible(true);
        }else {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(() -> SwingUtilities.invokeLater(TaskBarFrame::new));
            executor.execute(() -> new AppStarter().startApplication());
        }
    }
}
