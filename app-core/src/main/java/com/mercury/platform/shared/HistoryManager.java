package com.mercury.platform.shared;

import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HistoryManager {
    public static HistoryManager INSTANCE = HistoryManager.HistoryManagerHolder.HOLDER_INSTANCE;
    private final String HISTORY_FILE = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\history.json";
    private Logger logger = LogManager.getLogger(HistoryManager.class);
    private String[] messages;
    private int curIndex = 0;
    public HistoryManager() {
        messages = new String[0];
    }

    public void load() {
        File configFile = new File(HISTORY_FILE);
        if (configFile.exists()) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject root = (JSONObject) parser.parse(new FileReader(HISTORY_FILE));
                JSONArray msgsArray = (JSONArray) root.get("messages");
                messages = new String[msgsArray.size()];
                for (int i = 0; i < msgsArray.size(); i++) {
                    messages[i] = (String) msgsArray.get(i);
                }
            } catch (Exception e) {
                logger.error("Error during loading history file: ", e);
            }
        } else {
            createEmptyFile();
        }
    }

    private void createEmptyFile() {
        try {
            FileWriter fileWriter = new FileWriter(HISTORY_FILE);
            JSONObject root = new JSONObject();
            root.put("messages", new JSONArray());
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Error during creating history file: ", e);
        }
    }

    public void add(NotificationDescriptor notificationDescriptor) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(HISTORY_FILE));
            JSONArray msgsArray = (JSONArray) root.get("messages");
            msgsArray.add(0, notificationDescriptor.getSourceString());
            root.replace("messages", msgsArray);
            FileWriter fileWriter = new FileWriter(HISTORY_FILE);
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            logger.error("Error during adding message to history file: ", e);
        }
    }

    public void clear() {
        curIndex = 0;
        messages = new String[0];
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(HISTORY_FILE));
            JSONArray msgsArray = (JSONArray) root.get("messages");
            msgsArray.clear();
            root.replace("messages", msgsArray);
            FileWriter fileWriter = new FileWriter(HISTORY_FILE);
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.error("Error during creating history file: ", e);
        }
    }

    public String[] fetchNext(int messagesCount) {
        String[] chunk = new String[0];
        if ((curIndex + messagesCount) < messages.length) {
            chunk = Arrays.copyOfRange(messages, curIndex, curIndex + messagesCount);
            curIndex += messagesCount;
        } else if ((messages.length - curIndex) > 0) {
            int availableCount = messages.length - curIndex;
            chunk = Arrays.copyOfRange(messages, curIndex, curIndex + availableCount);
            curIndex += availableCount;
        }
        return chunk;
    }

    private static class HistoryManagerHolder {
        static final HistoryManager HOLDER_INSTANCE = new HistoryManager();
    }
}
