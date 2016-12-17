package com.home.clicker.core.utils;

import com.home.clicker.shared.CachedFilesUtils;
import com.home.clicker.shared.events.SCEventHandler;
import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.NewWhispersEvent;
import com.home.clicker.shared.events.custom.FileChangeEvent;
import com.home.clicker.shared.events.custom.WhisperNotificationEvent;
import com.home.clicker.shared.pojo.Message;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
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
        EventRouter.registerHandler(FileChangeEvent.class, event -> parse());
    }
    private void parse(){
        List<String> stubMessages = new ArrayList<>();
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
                        String utf8 = new String(stroke.getBytes("ISO-8859-1"),"UTF-8");
                        stubMessages.add(utf8);
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
            if(msgDate.after(lastMessageDate) && (fullMessage.contains("Hi, I would like") || fullMessage.contains("Hi, I'd like"))){
                String wNickname = StringUtils.substringBetween(fullMessage, "@From", ":");
                String content = StringUtils.substringAfter(fullMessage, wNickname + ":");
                wNickname = StringUtils.deleteWhitespace(wNickname);
                //todo regexp
                if(wNickname.contains(">")){
                    wNickname = StringUtils.substringAfterLast(wNickname, ">");
                }
                Message message = new Message(wNickname,content);
                messages.add(message);
            }
        }
        Date date = new Date(StringUtils.substring(stubMessages.get(0), 0, 20));
        if(date.after(lastMessageDate)){
            EventRouter.fireEvent(new WhisperNotificationEvent());
            lastMessageDate = date;
        }
        if(messages.size() != 0) {
            EventRouter.fireEvent(new NewWhispersEvent(messages));
        }
    }
}
