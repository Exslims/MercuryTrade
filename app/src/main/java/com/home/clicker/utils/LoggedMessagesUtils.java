package com.home.clicker.utils;

import com.home.clicker.events.EventHandler;
import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.FileChangeEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exslims
 * 07.12.2016
 */
//TODO Cleanup Client.txt
public class LoggedMessagesUtils {
    private final String logFilePath = CachedFilesUtils.getGamePath() + File.separator + "logs" + File.separator + "Client.txt";

    public LoggedMessagesUtils() {
        EventRouter.registerHandler(FileChangeEvent.class,new EventHandler<FileChangeEvent>(){
            public void handle(FileChangeEvent event) {
                parse();
            }
        });
    }
    private void parse(){
        List<String> messages = new ArrayList<String>();
        File logFile = new File(logFilePath);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile,"r");
            int lines = 0;
            StringBuilder builder = new StringBuilder();
            long length = logFile.length();
            length--;
            randomAccessFile.seek(length);
            for(long seek = length; seek >= 0; --seek){
                randomAccessFile.seek(seek);
                char c = (char)randomAccessFile.read();
                builder.append(c);
                if(c == '\n'){
                    builder = builder.reverse();
                    messages.add(builder.toString());
                    lines++;
                    if (lines == 5){
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
