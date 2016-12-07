package com.home.clicker.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Exslims
 * 08.12.2016
 */
public class DirectoryWatchService implements Runnable {
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(CachedFilesUtils.getGamePath() + File.separator + "logs");
            dir.register(watchService,ENTRY_MODIFY);

            System.out.println("Watch Service registered for dir: " + dir.getFileName());
            while (true){
                WatchKey key;
                key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    WatchEvent<Path> wEvent = (WatchEvent<Path>)event;
                    Path fileName = wEvent.context();
                    System.out.println(kind.name() + ": " + fileName);
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            return;
        }
    }
}
