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
    private Map<String, FrameDescriptor> cachedFramesSettings;
    private Map<String,Dimension> minimumFrameSize;
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

    public ConfigManager() {
        minimumFrameSize = new HashMap<>();
        minimumFrameSize.put("TaskBarFrame",new Dimension(109,20));
        minimumFrameSize.put("MessageFrame",new Dimension(360,10));
        minimumFrameSize.put("OutMessageFrame",new Dimension(280,115));
        minimumFrameSize.put("TestCasesFrame",new Dimension(400,100));
        minimumFrameSize.put("SettingsFrame",new Dimension(540,400));
        minimumFrameSize.put("HistoryFrame",new Dimension(280,400));
        minimumFrameSize.put("TimerFrame",new Dimension(240,102));
        minimumFrameSize.put("ChatFilterFrame",new Dimension(400,100));
        minimumFrameSize.put("ItemsGridFrame",new Dimension(150,150));
        minimumFrameSize.put("NotesFrame",new Dimension(540,100));
        minimumFrameSize.put("ChatFilterSettingsFrame",new Dimension(300,200));
        minimumFrameSize.put("GamePathChooser",new Dimension(600,30));
        minimumFrameSize.put("CurrencySearchFrame",new Dimension(400,300));
        minimumFrameSize.put("AdrManagerFrame",new Dimension(400,300));
        minimumFrameSize.put("AdrCellSettingsFrame",new Dimension(300,210));

        defaultAppSettings = new HashMap<>();
        defaultAppSettings.put("fadeTime",0);
        defaultAppSettings.put("minOpacity",100);
        defaultAppSettings.put("maxOpacity",100);
        defaultAppSettings.put("showOnStartUp",true);
        defaultAppSettings.put("showPatchNotes",false);
        defaultAppSettings.put("whisperNotifier",WhisperNotifierStatus.ALWAYS);
        defaultAppSettings.put("gamePath","");
        defaultAppSettings.put("flowDirection","DOWNWARDS");
        defaultAppSettings.put("tradeMode","DEFAULT");
        defaultAppSettings.put("limitMsgCount",3);
        defaultAppSettings.put("expandedMsgCount",2);
        defaultAppSettings.put("itemsGridEnable",true);
        defaultAppSettings.put("checkUpdateOnStartUp",true);
        defaultAppSettings.put("dismissAfterKick",false);
        defaultAppSettings.put("inGameDnd",false);
        defaultAppSettings.put("showLeague",false);
        defaultAppSettings.put("dndResponseText","Response text");
        defaultAppSettings.put("defaultWords","!wtb, uber, boss, perandus");
        defaultAppSettings.put("quickResponse","invite me pls");

    }

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

                cachedFramesSettings = getDefaultFramesSettings();
                saveFrameSettings();

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
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            JSONArray framesSetting = (JSONArray) root.get("framesSettings");
            cachedFramesSettings = new HashMap<>();
            for (JSONObject next : (Iterable<JSONObject>) framesSetting) {
                JSONObject location = (JSONObject) next.get("location");
                JSONObject size = (JSONObject) next.get("size");
                FrameDescriptor settings = new FrameDescriptor(
                        new Point(((Long)location.get("frameX")).intValue(), ((Long)location.get("frameY")).intValue()),
                        new Dimension(((Long)size.get("width")).intValue(),((Long)size.get("height")).intValue())
                );
                cachedFramesSettings.put((String) next.get("frameClassName"), settings);
            }
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

    public FrameDescriptor getFrameSettings(String frameClass){
        FrameDescriptor settings = cachedFramesSettings.get(frameClass);
        if(settings == null) {
            FrameDescriptor defaultSettings = getDefaultFramesSettings().get(frameClass);
            if(defaultSettings != null) {
                cachedFramesSettings.put(frameClass, defaultSettings);
                saveFrameSettings();
            }
        }
        return cachedFramesSettings.get(frameClass);
    }
    public void saveFrameLocation(String frameClassName, Point point) {
        FrameDescriptor settings = cachedFramesSettings.get(frameClassName);
        settings.setFrameLocation(point);
        saveFrameSettings();
    }

    public void saveFrameSize(String frameClassName, Dimension size){
        try {
            FrameDescriptor settings = cachedFramesSettings.get(frameClassName);
            settings.setFrameSize(size);
        }catch (NullPointerException e){
            cachedFramesSettings.put(frameClassName,getDefaultFramesSettings().get(frameClassName));
        }
        saveFrameSettings();
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

    public Map<String,FrameDescriptor> getDefaultFramesSettings(){
        Map<String, FrameDescriptor> defaultFramesSettings = new HashMap<>();
        defaultFramesSettings.put("TaskBarFrame",new FrameDescriptor(new Point(400, 500),new Dimension(109,20)));
        defaultFramesSettings.put("MessageFrame",new FrameDescriptor(new Point(700, 600),new Dimension(315,0)));
        defaultFramesSettings.put("OutMessageFrame",new FrameDescriptor(new Point(200, 500),new Dimension(280,115)));
        defaultFramesSettings.put("TestCasesFrame",new FrameDescriptor(new Point(1400, 500),new Dimension(400,100)));
        defaultFramesSettings.put("SettingsFrame",new FrameDescriptor(new Point(600, 600),new Dimension(540,500)));
        defaultFramesSettings.put("HistoryFrame",new FrameDescriptor(new Point(600, 500),new Dimension(280,400)));
        defaultFramesSettings.put("TimerFrame",new FrameDescriptor(new Point(400, 600),new Dimension(240,102)));
        defaultFramesSettings.put("ChatFilterFrame",new FrameDescriptor(new Point(400, 600),new Dimension(500,300)));
        defaultFramesSettings.put("ItemsGridFrame",new FrameDescriptor(new Point(12, 79),new Dimension(641,718)));
        defaultFramesSettings.put("NotesFrame",new FrameDescriptor(new Point(400, 600),new Dimension(540,100)));
        defaultFramesSettings.put("ChatFilterSettingsFrame",new FrameDescriptor(new Point(400, 600),new Dimension(320,200)));
        defaultFramesSettings.put("GamePathChooser",new FrameDescriptor(new Point(400, 600),new Dimension(520,30)));
        defaultFramesSettings.put("CurrencySearchFrame",new FrameDescriptor(new Point(400, 600),new Dimension(400,300)));
        defaultFramesSettings.put("AdrManagerFrame",new FrameDescriptor(new Point(400, 600),new Dimension(400,300)));
        defaultFramesSettings.put("AdrCellSettingsFrame",new FrameDescriptor(new Point(400, 600),new Dimension(300,210)));
        return defaultFramesSettings;
    }
    public Dimension getMinimumFrameSize(String frameName){
        return minimumFrameSize.get(frameName);
    }
    public boolean isValidGamePath(String gamePath){
        File file = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return file.exists();
    }
}
