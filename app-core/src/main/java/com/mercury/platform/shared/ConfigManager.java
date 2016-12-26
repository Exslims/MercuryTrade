package com.mercury.platform.shared;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Application config manager, loads config files from %userprofile%/AppData/Local/MercuryTrader.
 */
public class ConfigManager {
    private Logger logger = Logger.getLogger(ConfigManager.class);
    private static class ConfigManagerHolder{
        static final ConfigManager HOLDER_INSTANCE = new ConfigManager();
    }
    public static ConfigManager INSTANCE = ConfigManagerHolder.HOLDER_INSTANCE;

    private final String CONFIG_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrader\\app-config.json";
    private Map<String,Object> properties = new HashMap<>();
    private ConfigManager(){
        load();
    }
    private void load(){
        File configFile = new File(CONFIG_FILE_PATH);
        if(!configFile.exists()){
            try {
                FileWriter fileWriter = new FileWriter(CONFIG_FILE_PATH);
                fileWriter.write(new JSONObject().toJSONString());
                fileWriter.flush();
                fileWriter.close();

                saveButtonsConfig(getDefaultButtons());
            } catch (IOException e) {
                logger.error(e);
            }
        }else {
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(CONFIG_FILE_PATH));
                JSONObject jsonObject = (JSONObject) obj;
                String gamePath = (String) jsonObject.get("gamePath");

                JSONArray buttons = (JSONArray) jsonObject.get("buttons");
                Map<String, String> buttonsConfig = new HashMap<>();
                if(buttons != null){
                    for (JSONObject next : (Iterable<JSONObject>) buttons) {
                        buttonsConfig.put((String) next.get("title"), (String) next.get("value"));
                    }
                }else {
                    buttonsConfig = getDefaultButtons();
                }

                properties.put("gamePath",gamePath);
                properties.put("buttons",buttonsConfig);
            } catch (Exception e) {
                logger.error("Error in ConfigManager.load",e);
            }
        }
    }
    public boolean isValidPath(String gamePath){
        File logsFile = new File(gamePath + File.separator + "logs" + File.separator +"Client.txt");
        return logsFile.exists();
    }
    public Object getProperty(String token){
        return properties.get(token);
    }
    public void saveComponentLocation(String componentName, Point point){
        JSONObject object = new JSONObject();
        object.put("x", point.x);
        object.put("y", point.y);
        saveProperty(componentName,object);
    }
    public void saveGamePath(String gamePath){
        properties.put("gamePath", gamePath);
        saveProperty("gamePath",gamePath);
    }
    public void saveButtonsConfig(Map<String, String> buttons){
        properties.put("buttons",buttons);
        JSONArray list = new JSONArray();
        buttons.forEach((k,v)->{
            JSONObject buttonsConfig = new JSONObject();
            buttonsConfig.put("title",k);
            buttonsConfig.put("value",v);
            list.add(buttonsConfig);
        });
        saveProperty("buttons", list);
    }
    private void saveProperty(String token, Object jsonObject){
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE_PATH));
            Object obj = root.get(token);
            if(obj != null){
                root.replace(token,jsonObject);
            }else {
                root.put(token,jsonObject);
            }
            FileWriter fileWriter = new FileWriter(CONFIG_FILE_PATH);
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.error("Error in ConfigManager.saveProperty",e);
        }

    }
    private Map<String, String > getDefaultButtons(){
        Map<String,String> defaultButtons = new HashMap<>();
        defaultButtons.put("1m","one minute");
        defaultButtons.put("thx","thanks");
        defaultButtons.put("no thx", "no thanks");
        return defaultButtons;
    }

}
