package com.mercury.platform;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.impl.GamePathChooser;
import com.mercury.platform.ui.frame.impl.SettingsFrame;
import com.mercury.platform.ui.frame.impl.TaskBarFrame;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Start application class, checking game path from config file.
 * if game path isn't set, cause Choose game path frame, else starting main frame.
 */
public class AppMain {
    public static void main(String[] args) {
        String gamePath = ConfigManager.INSTANCE.getGamePath();
        if (gamePath == null || !ConfigManager.INSTANCE.isValidPath(gamePath)) {
            SwingUtilities.invokeLater(()->{
                GamePathChooser gamePathChooser = new GamePathChooser();
                gamePathChooser.setVisible(true);
            });
        } else {
            new AppStarter().startApplication();
            SwingUtilities.invokeLater(TaskBarFrame::new);
        }
    }
}