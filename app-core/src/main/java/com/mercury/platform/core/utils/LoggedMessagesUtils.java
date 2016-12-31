package com.mercury.platform.core.utils;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    private final String logFilePath = ConfigManager.INSTANCE.getGamePath() + File.separator + "logs" + File.separator + "Client.txt";
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
                    if(stroke.contains("@From") || stroke.contains("has joined the area.") || stroke.contains("has left the area.")) {
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
            if(msgDate.after(lastMessageDate)){
                if(fullMessage.contains("Hi, I would like") || fullMessage.contains("Hi, I'd like")){
                    String wNickname = StringUtils.substringBetween(fullMessage, "@From", ":");
                    String content = StringUtils.substringAfter(fullMessage, wNickname + ":");
                    wNickname = StringUtils.deleteWhitespace(wNickname);
                    //todo regexp
                    if(wNickname.contains(">")){
                        wNickname = StringUtils.substringAfterLast(wNickname, ">");
                    }
                    Message message = new Message(wNickname,StringUtils.substring(fullMessage, 0, 20) + " " + content);
                    messages.add(message);
                }

                if(fullMessage.contains("has joined the area.")){
                    EventRouter.fireEvent(new PlayerJoinEvent(StringUtils.substringBetween(fullMessage," : ", " has joined the area.")));
                }
                if(fullMessage.contains("has left the area.")){
                    System.out.println("left area");
                    System.out.println("<" + StringUtils.substringBetween(fullMessage," : ", " has left the area.") + ">");
                    EventRouter.fireEvent(new PlayerLeftEvent(StringUtils.substringBetween(fullMessage," : ", " has left the area.")));
                }
            }
        }
        Date date = new Date(StringUtils.substring(stubMessages.get(0), 0, 20));
        if(date.after(lastMessageDate) && messages.size() != 0){
            EventRouter.fireEvent(new WhisperNotificationEvent());
            lastMessageDate = date;
            EventRouter.fireEvent(new NewWhispersEvent(messages));
        }
    }
}
