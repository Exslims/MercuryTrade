package com.home.clicker;

import com.home.clicker.core.PrivateMessageManager;
import com.home.clicker.core.misc.PatchNotifier;
import com.home.clicker.ui.FileChooser;
import com.home.clicker.ui.WindowFrame;
import com.home.clicker.shared.CachedFilesUtils;

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
