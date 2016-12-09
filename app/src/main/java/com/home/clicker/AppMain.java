package com.home.clicker;

import com.home.clicker.javafx.*;
import com.home.clicker.misc.PatchNotifier;
import com.home.clicker.utils.CachedFilesUtils;
import javafx.application.Application;

import javax.swing.*;


public class AppMain {
    public static void main(String[] args) {
//        PatchNotifier patchNotifier = new PatchNotifier();
        if(CachedFilesUtils.getGamePath().equals("")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setVisible(true);
        }else {
            SwingUtilities.invokeLater(() -> new WindowFrame());
            new PrivateMessageManager();
            new PatchNotifier();
        }

//        new PrivateMessageManager();
//        Application.launch(com.home.clicker.javafx.WindowFrame.class,args);
    }
}
