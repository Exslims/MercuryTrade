package com.home.clicker.utils;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.FileChangeEvent;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * Exslims
 * 08.12.2016
 */
public class FileMonitor {
    private static final long pollingInterval = 100;
    public FileMonitor() {
        File folder = new File(CachedFilesUtils.getGamePath() + File.separator + "logs");

        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationMonitor monitor = new FileAlterationMonitor(pollingInterval);
        FileAlterationListener listener = new FileAlterationListenerAdaptor(){
            @Override
            public void onFileChange(File file) {
                if (file.getAbsolutePath().contains("Client.txt")) {
                    EventRouter.fireEvent(new FileChangeEvent());
                }
            }
        };

        observer.addListener(listener);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
