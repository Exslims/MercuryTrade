package com.mercury.platform.shared;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.shared.pojo.FrameSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Application config manager, loads config files from %userprofile%/AppData/Local/MercuryTrader.
 */
public class ConfigManager {
    private Logger logger = LogManager.getLogger(ConfigManager.class);

    private static class ConfigManagerHolder {
        static final ConfigManager HOLDER_INSTANCE = new ConfigManager();
    }

    public static ConfigManager INSTANCE = ConfigManagerHolder.HOLDER_INSTANCE;

    private final String CONFIG_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade";
    private final String CONFIG_FILE = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\app-config.json";

    private Map<String, String> cachedButtonsConfig;
    private Map<String, FrameSettings> cachedFramesSettings;

    private WhisperNotifierStatus whisperNotifier;
    private int decayTime;
    private int minOpacity;
    private int maxOpacity;

    private boolean showOnStartUp;

    /**
     * Loading application data from app-config.json file. If file does not exist, created and filled by default.
     */
    public void load() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            try {
                new File(CONFIG_FILE_PATH).mkdir();
                new File(CONFIG_FILE_PATH + "\\temp").mkdir();

                FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                fileWriter.write(new JSONObject().toJSONString());
                fileWriter.flush();
                fileWriter.close();

                saveButtonsConfig(getDefaultButtons());
                cachedFramesSettings = getDefaultFramesSettings();
                saveFrameSettings();
                setWhisperNotifier(WhisperNotifierStatus.ALWAYS);
                decayTime = 0;
                minOpacity = 100;
                maxOpacity = 100;
                showOnStartUp = true;
                saveProperty("decayTime",decayTime);
                saveProperty("minOpacity",minOpacity);
                saveProperty("maxOpacity",maxOpacity);
                saveProperty("showOnStartUp",showOnStartUp);

            } catch (IOException e) {
                logger.error(e);
            }
        } else {
            loadConfigFile();
        }
    }
    private void loadConfigFile(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));

            JSONArray buttons = (JSONArray) root.get("buttons");
            cachedButtonsConfig = new HashMap<>();
            for (JSONObject next : (Iterable<JSONObject>) buttons) {
                cachedButtonsConfig.put((String) next.get("title"), (String) next.get("value"));
            }

            JSONArray framesSetting = (JSONArray) root.get("framesSettings");
            cachedFramesSettings = new HashMap<>();
            for (JSONObject next : (Iterable<JSONObject>) framesSetting) {
                JSONObject location = (JSONObject) next.get("location");
                JSONObject size = (JSONObject) next.get("size");
                FrameSettings settings = new FrameSettings(
                        new Point(((Long)location.get("frameX")).intValue(), ((Long)location.get("frameY")).intValue()),
                        new Dimension(((Long)size.get("width")).intValue(),((Long)size.get("height")).intValue())
                );
                cachedFramesSettings.put((String) next.get("frameClassName"), settings);
            }
            whisperNotifier = WhisperNotifierStatus.valueOf((String)root.get("whisperNotifier"));
            decayTime = ((Long)root.get("decayTime")).intValue();
            minOpacity = ((Long)root.get("minOpacity")).intValue();
            maxOpacity = ((Long)root.get("maxOpacity")).intValue();
            showOnStartUp = (boolean) root.get("showOnStartUp");
        } catch (Exception e) {
            logger.error("Error in loadConfigFile: ",e);
        }
    }

    public Map<String, String> getButtonsConfig(){
        return cachedButtonsConfig;
    }

    public FrameSettings getFrameSettings(String frameClass){
        return cachedFramesSettings.get(
                frameClass) == null ?
                getDefaultFramesSettings().get(frameClass) :  cachedFramesSettings.get(frameClass);
    }
    public void saveFrameLocation(String frameClassName, Point point) {
        FrameSettings settings = cachedFramesSettings.get(frameClassName);
        settings.setFrameLocation(point);
        saveFrameSettings();
    }

    public void saveFrameSize(String frameClassName, Dimension size){
        try {
            FrameSettings settings = cachedFramesSettings.get(frameClassName);
            settings.setFrameSize(size);
        }catch (NullPointerException e){
            cachedFramesSettings.put(frameClassName,getDefaultFramesSettings().get(frameClassName));
        }
        saveFrameSettings();
    }

    /**
     * Save custom buttons config.
     * @param buttons map of buttons data.(title,text to send)
     */
    public void saveButtonsConfig(Map<String, String> buttons){
        cachedButtonsConfig = buttons;
        JSONArray list = new JSONArray();
        buttons.forEach((k,v)->{
            JSONObject buttonsConfig = new JSONObject();
            buttonsConfig.put("title",k);
            buttonsConfig.put("value",v);
            list.add(buttonsConfig);
        });
        saveProperty("buttons", list);
    }

    private void saveFrameSettings(){
        JSONArray frames = new JSONArray();
        cachedFramesSettings.forEach((frameName,frameSettings)->{
            JSONObject object = new JSONObject();
            JSONObject location = new JSONObject();
            location.put("frameX",frameSettings.getFrameLocation().x);
            location.put("frameY",frameSettings.getFrameLocation().y);

            JSONObject size = new JSONObject();
            size.put("width",frameSettings.getFrameSize().width);
            size.put("height",frameSettings.getFrameSize().height);

            object.put("location",location);
            object.put("size",size);
            object.put("frameClassName", frameName);

            frames.add(object);
        });

        saveProperty("framesSettings",frames);
    }
    public <T> void saveProperty(String token, T object){
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            Object obj = root.get(token);
            if(obj != null){
                root.replace(token,object);
            }else {
                root.put(token,object);
            }
            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
            fileWriter.write(root.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.error("Error in ConfigManager.saveProperty",e);
        }

    }

    public WhisperNotifierStatus getWhisperNotifier() {
        return whisperNotifier;
    }

    public void setWhisperNotifier(WhisperNotifierStatus whisperNotifier) {
        this.whisperNotifier = whisperNotifier;
        saveProperty("whisperNotifier", whisperNotifier.toString());
    }

    public int getDecayTime() {
        return decayTime;
    }

    public int getMinOpacity() {
        return minOpacity;
    }

    public int getMaxOpacity() {
        return maxOpacity;
    }

    public boolean isShowOnStartUp() {
        return showOnStartUp;
    }

    private Map<String, String > getDefaultButtons(){
        Map<String,String> defaultButtons = new HashMap<>();
        defaultButtons.put("1m","one minute");
        defaultButtons.put("thx","thanks");
        defaultButtons.put("no thx", "no thanks");
        defaultButtons.put("sold", "sold");
        return defaultButtons;
    }
    public Map<String,FrameSettings> getDefaultFramesSettings(){
        Map<String,FrameSettings> dFramesSettings = new HashMap<>();
        dFramesSettings.put("TaskBarFrame",new FrameSettings(new Point(400, 500),new Dimension(109,20)));
        dFramesSettings.put("IncMessageFrame",new FrameSettings(new Point(700, 600),new Dimension(280,10)));
        dFramesSettings.put("OutMessageFrame",new FrameSettings(new Point(200, 500),new Dimension(280,115)));
        dFramesSettings.put("TestCasesFrame",new FrameSettings(new Point(1400, 500),new Dimension(400,100)));
        dFramesSettings.put("SettingsFrame",new FrameSettings(new Point(600, 600),new Dimension(540,100)));
        dFramesSettings.put("HistoryFrame",new FrameSettings(new Point(600, 500),new Dimension(280,300)));
        dFramesSettings.put("TimerFrame",new FrameSettings(new Point(400, 600),new Dimension(240,102)));
        dFramesSettings.put("ChatFilterFrame",new FrameSettings(new Point(400, 600),new Dimension(240,150)));
        dFramesSettings.put("NotesFrame",new FrameSettings(new Point(400, 600),new Dimension(540,100)));
        dFramesSettings.put("SetUpLocationFrame",new FrameSettings(new Point(400, 600),new Dimension(240,30)));
        dFramesSettings.put("ChunkMessagesPicker",new FrameSettings(new Point(400, 600),new Dimension(240,30)));
        return dFramesSettings;
    }
}
