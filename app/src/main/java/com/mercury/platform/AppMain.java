package com.mercury.platform;

import com.mercury.platform.core.PrivateMessageManager;
import com.mercury.platform.core.misc.PatchNotifier;
import com.mercury.platform.shared.CachedFilesUtils;
import com.mercury.platform.ui.FileChooser;
import com.mercury.platform.ui.WindowFrame;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppMain {
    public static void main(String[] args) {
        if(CachedFilesUtils.getGamePath().equals("")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setVisible(true);
        }else {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.execute(() -> SwingUtilities.invokeLater(WindowFrame::new));
            executor.execute(PrivateMessageManager::new);
            executor.execute(PatchNotifier::new);
        }
    }
}
