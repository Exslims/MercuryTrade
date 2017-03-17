package com.mercury.platform.core.utils;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChangePOEFolderEvent;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class FileMonitor {
    private static final long POLLING_INTERVAL = 350;
    private MessageFileHandler fileHandler;
    private FileAlterationMonitor monitor;
    public FileMonitor(){
        EventRouter.CORE.registerHandler(ChangePOEFolderEvent.class, event -> {
            if(monitor != null){
                try {
                    monitor.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                start();
            }
        });
    }
    public void start(){
        String gamePath = ConfigManager.INSTANCE.getGamePath();

        File folder = new File(gamePath+"logs");
        fileHandler = new MessageFileHandler(gamePath + "logs/Client.txt");
        FileAlterationObserver observer = new FileAlterationObserver(folder);
        monitor = new FileAlterationMonitor(POLLING_INTERVAL);
        FileAlterationListener listener = new FileAlterationListenerAdaptor(){
            @Override
            public void onFileChange(File file) {
                fileHandler.parse();
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
