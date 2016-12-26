package com.mercury.platform;

import com.mercury.platform.core.PrivateMessageManager;
import com.mercury.platform.core.misc.PatchNotifier;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.GamePathChooser;
import com.mercury.platform.ui.TaskBarFrame;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppMain {
    public static void main(String[] args) {
        String gamePath = (String) ConfigManager.INSTANCE.getProperty("gamePath");
        if(gamePath == null || !ConfigManager.INSTANCE.isValidPath(gamePath)) {
            GamePathChooser gamePathChooser = new GamePathChooser();
            gamePathChooser.setVisible(true);
        }else {
            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.execute(() -> SwingUtilities.invokeLater(TaskBarFrame::new));
            executor.execute(PrivateMessageManager::new);
            executor.execute(PatchNotifier::new);
        }
    }
}
