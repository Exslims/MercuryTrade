package com.mercury.platform.shared;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.StashTab;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Application config manager, loads config files from %userprofile%/AppData/Local/MercuryTrader.
 */
//todo generalization
@SuppressWarnings("unchecked")
@Deprecated
public class ConfigManager {
    private Logger logger = LogManager.getLogger(ConfigManager.class);

    private static class ConfigManagerHolder {
        static final ConfigManager HOLDER_INSTANCE = new ConfigManager();
    }

    public static ConfigManager INSTANCE = ConfigManagerHolder.HOLDER_INSTANCE;

    private final String CONFIG_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade";
    private final String CONFIG_FILE = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\app-config.json";
    private Map<String,Object> defaultAppSettings;

    @Getter
    private WhisperNotifierStatus whisperNotifier = WhisperNotifierStatus.ALWAYS;
    @Getter
    private int minOpacity = 100;
    @Getter
    private int maxOpacity = 100;
    @Getter
    private int fadeTime = 0;

    @Getter
    private String gamePath = "";
    @Getter
    private boolean showPatchNotes = false;
    @Getter
    private boolean showOnStartUp = true;
    @Getter
    private boolean itemsGridEnable = true;
    @Getter
    private boolean checkUpdateOnStartUp = true;
    @Getter
    private boolean inGameDnd = false;
    @Getter
    private String dndResponseText = "Response text";


    /**
     * Loading application data from app-config.json file. If file does not exist, created and filled by default.
     */
    public void load() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            try {
                new File(CONFIG_FILE_PATH).mkdir();
                new File(CONFIG_FILE_PATH + "\\temp").mkdir();

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream resourceAsStream = classLoader.getResourceAsStream("app/local-updater.jar");
                File dest = new File(CONFIG_FILE_PATH + File.separator + "local-updater.jar");
                FileUtils.copyInputStreamToFile(resourceAsStream,dest);

                FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                fileWriter.write(new JSONObject().toJSONString());
                fileWriter.flush();
                fileWriter.close();


                saveProperty("fadeTime",String.valueOf(defaultAppSettings.get("fadeTime")));
                saveProperty("minOpacity",String.valueOf(defaultAppSettings.get("minOpacity")));
                saveProperty("maxOpacity",String.valueOf(defaultAppSettings.get("maxOpacity")));
                saveProperty("showOnStartUp",String.valueOf(defaultAppSettings.get("showOnStartUp")));
                saveProperty("showPatchNotes",String.valueOf(defaultAppSettings.get("showPatchNotes")));
                saveProperty("gamePath",gamePath);
                saveProperty("whisperNotifier", defaultAppSettings.get("whisperNotifier").toString());
                saveProperty("flowDirection", defaultAppSettings.get("flowDirection"));
                saveProperty("tradeMode", defaultAppSettings.get("tradeMode"));
                saveProperty("limitMsgCount", String.valueOf(defaultAppSettings.get("limitMsgCount")));
                saveProperty("expandedMsgCount", String.valueOf(defaultAppSettings.get("expandedMsgCount")));
                saveProperty("itemsGridEnable", String.valueOf(defaultAppSettings.get("itemsGridEnable")));
                saveProperty("checkUpdateOnStartUp", String.valueOf(defaultAppSettings.get("checkUpdateOnStartUp")));
                saveProperty("dismissAfterKick", String.valueOf(defaultAppSettings.get("dismissAfterKick")));
                saveProperty("inGameDnd", String.valueOf(defaultAppSettings.get("inGameDnd")));
                saveProperty("dndResponseText", defaultAppSettings.get("dndResponseText"));
                saveProperty("defaultWords", defaultAppSettings.get("defaultWords"));
                saveProperty("quickResponse", defaultAppSettings.get("quickResponse"));
                saveProperty("scaleData", defaultAppSettings.get("scaleData"));
                saveProperty("showLeague", String.valueOf(defaultAppSettings.get("showLeague")));

            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            loadConfigFile();
        }
    }
    private void loadConfigFile(){
        try {
            whisperNotifier = WhisperNotifierStatus.valueOf(loadProperty("whisperNotifier"));
            fadeTime = Long.valueOf(loadProperty("fadeTime")).intValue();
            minOpacity = Long.valueOf(loadProperty("minOpacity")).intValue();
            maxOpacity = Long.valueOf(loadProperty("maxOpacity")).intValue();
            showOnStartUp = Boolean.valueOf(loadProperty("showOnStartUp"));
            showPatchNotes = Boolean.valueOf(loadProperty("showPatchNotes"));
            gamePath = loadProperty("gamePath");
            itemsGridEnable = Boolean.valueOf(loadProperty("itemsGridEnable"));
            checkUpdateOnStartUp = Boolean.valueOf(loadProperty("checkUpdateOnStartUp"));
            inGameDnd = Boolean.valueOf(loadProperty("inGameDnd"));
            dndResponseText = loadProperty("dndResponseText");
        } catch (Exception e) {
            logger.error("Error in loadConfigFile: ",e);
        }
    }

    private String loadProperty(String key){
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            Object object = root.get(key);
            if(object != null){
                return (String) object;
            }else {
                return String.valueOf(defaultAppSettings.get(key));
            }
        }catch (Exception e){
            logger.error("Error while loading property: " + key,e);
            saveProperty(key,String.valueOf(defaultAppSettings.get(key)));
            return String.valueOf(defaultAppSettings.get(key));
        }
    }
    private  <T> void saveProperty(String token, T object){
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
            logger.error("Error in ConfigManager.saveProperty with \"" + token + "\" token.",e);
        }

    }

    public void saveStashTabs(List<StashTab> tabs){
        JSONArray list = new JSONArray();
        tabs.forEach((button)->{
            JSONObject stashTab = new JSONObject();
            stashTab.put("title",button.getTitle());
            stashTab.put("isQuad",String.valueOf(button.isQuad()));
            list.add(stashTab);
        });
        saveProperty("stashTabs", list);
    }
    public List<StashTab> getStashTabs(){
        List<StashTab> stashTabs = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            JSONArray tabs = (JSONArray) root.get("stashTabs");
            if (tabs == null) {
                saveStashTabs(stashTabs);
                return stashTabs;
            } else {
                for (JSONObject next : (Iterable<JSONObject>) tabs) {
                    StashTab stashTab = new StashTab((String)next.get("title"), Boolean.valueOf((String)next.get("isQuad")),false);
                    stashTabs.add(stashTab);
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
        return stashTabs;
    }
    public void saveScaleData(Map<String,Float> scaleData){
        JSONArray list = new JSONArray();
        scaleData.forEach((type,value)->{
            JSONObject scaleSetting = new JSONObject();
            scaleSetting.put("type",type);
            scaleSetting.put("value",String.valueOf(value));
            list.add(scaleSetting);
        });
        saveProperty("scaleData", list);

    }
    public Map<String, Float> getScaleData(){
        Map<String, Float> scaleData = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            JSONArray scaleDataArray = (JSONArray) root.get("scaleData");
            if (scaleDataArray == null) {
                scaleData.put("notification",1f);
                scaleData.put("taskbar",1f);
                scaleData.put("itemcell",1f);
                scaleData.put("other",1f);
                saveScaleData(scaleData);
                return scaleData;
            } else {
                for (JSONObject next : (Iterable<JSONObject>) scaleDataArray) {
                    scaleData.put((String)next.get("type"),Float.valueOf((String) next.get("value")));
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
        return scaleData;
    }

    public void setCheckUpdateOnStartUp(boolean checkUpdateOnStartUp) {
        this.checkUpdateOnStartUp = checkUpdateOnStartUp;
        saveProperty("checkUpdateOnStartUp", String.valueOf(this.checkUpdateOnStartUp));
    }

    public void setFadeTime(int fadeTime) {
        this.fadeTime = fadeTime;
        saveProperty("fadeTime", String.valueOf(this.fadeTime));
    }
    public void setMinOpacity(int minOpacity) {
        this.minOpacity = minOpacity;
        saveProperty("minOpacity", String.valueOf(this.minOpacity));
    }
    public void setMaxOpacity(int maxOpacity) {
        this.maxOpacity = maxOpacity;
        saveProperty("maxOpacity", String.valueOf(this.maxOpacity));
    }
    public void setShowOnStartUp(boolean showOnStartUp) {
        this.showOnStartUp = showOnStartUp;
        saveProperty("showOnStartUp", String.valueOf(this.showOnStartUp));
    }
    public void setWhisperNotifier(WhisperNotifierStatus whisperNotifier) {
        this.whisperNotifier = whisperNotifier;
        saveProperty("whisperNotifier", whisperNotifier.toString());
    }
    public void setGamePath(String gamePath){
        this.gamePath = gamePath;
        saveProperty("gamePath",gamePath);
    }
    public void setItemsGridEnable(boolean itemsGridEnable) {
        this.itemsGridEnable = itemsGridEnable;
        saveProperty("itemsGridEnable",String.valueOf(this.itemsGridEnable));
    }
    public void setInGameDnd(boolean inGameDnd) {
        this.inGameDnd = inGameDnd;
        saveProperty("inGameDnd",String.valueOf(this.inGameDnd));
    }
    public void setDndResponseText(String dndResponseText) {
        this.dndResponseText = dndResponseText;
        saveProperty("dndResponseText",dndResponseText);
    }
    public boolean isValidGamePath(String gamePath){
        File file = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return file.exists();
    }
}
