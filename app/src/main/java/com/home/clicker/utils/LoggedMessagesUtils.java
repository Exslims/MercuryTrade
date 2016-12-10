package com.home.clicker.utils;

import com.home.clicker.events.SCEventHandler;
import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FileChangeEvent;
import com.home.clicker.events.custom.WhisperNotificationEvent;
import com.home.clicker.events.custom.NewWhispersEvent;
import com.home.clicker.pojo.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Exslims
 * 07.12.2016
 */
//TODO Cleanup Client.txt
public class LoggedMessagesUtils {
    private final Logger logger = Logger.getLogger(LoggedMessagesUtils.class);
    private final String logFilePath = CachedFilesUtils.getGamePath() + File.separator + "logs" + File.separator + "Client.txt";
    private Date lastMessageDate = new Date();

    public LoggedMessagesUtils() {
        EventRouter.registerHandler(FileChangeEvent.class,new SCEventHandler<FileChangeEvent>(){
            public void handle(FileChangeEvent event) {
                parse();
            }
        });
    }
    private void parse(){
        List<String> stubMessages = new ArrayList<String>();
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
                    String stroke = builder.toString();
                    if(stroke.contains("@From")) {
                        stubMessages.add(stroke);
                        lines++;
                    }
                    builder = new StringBuilder();
                    if (lines == 30){
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Message> messages = new ArrayList<>();
        for (String fullMessage : stubMessages) {
            Date msgDate = new Date(StringUtils.substring(fullMessage, 0, 20));
            if(msgDate.after(lastMessageDate)){
                String wNickname = StringUtils.substringBetween(fullMessage, "@From", ":");
                String content = StringUtils.substringAfter(fullMessage, wNickname + ":");
                wNickname = StringUtils.deleteWhitespace(wNickname);
                //todo regexp
                if(wNickname.contains(">")){
                    wNickname = StringUtils.substringAfterLast(wNickname, ">");
                }
                logger.info("content: " + content);
                logger.info("nickname: " + wNickname);
                Message message = new Message(wNickname,content);
                messages.add(message);
            }
        }
        Date date = new Date(StringUtils.substring(stubMessages.get(0), 0, 20));
        if(date.after(lastMessageDate)){
            EventRouter.fireEvent(new WhisperNotificationEvent());
            lastMessageDate = date;
        }
        EventRouter.fireEvent(new ActualWritersChangeEvent(messages));
    }
}
