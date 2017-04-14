package com.mercury.platform.shared;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.ResponseButton;
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

    private List<ResponseButton> cachedButtonsConfig;
    private Map<String, FrameSettings> cachedFramesSettings;
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
    private String flowDirection = "DOWNWARDS";
    @Getter
    private String tradeMode = "DEFAULT";
    @Getter
    private int limitMsgCount = 3;
    @Getter
    private int expandedMsgCount = 2;
    @Getter
    private boolean showPatchNotes = false;
    @Getter
    private boolean showOnStartUp = true;
    @Getter
    private boolean itemsGridEnable = true;
    @Getter
    private boolean checkUpdateOnStartUp = true;
    @Getter
    private boolean dismissAfterKick = false;
    @Getter
    private boolean showLeague = false;
    @Getter
    private boolean inGameDnd = false;
    @Getter
    private String dndResponseText = "Response text";
    @Getter
    private String defaultWords = "!wtb, uber, boss, perandus";

    public ConfigManager() {
        minimumFrameSize = new HashMap<>();
        minimumFrameSize.put("TaskBarFrame",new Dimension(109,20));
        minimumFrameSize.put("IncMessageFrame",new Dimension(360,10));
        minimumFrameSize.put("OutMessageFrame",new Dimension(280,115));
        minimumFrameSize.put("TestCasesFrame",new Dimension(400,100));
        minimumFrameSize.put("SettingsFrame",new Dimension(540,400));
        minimumFrameSize.put("HistoryFrame",new Dimension(280,400));
        minimumFrameSize.put("TimerFrame",new Dimension(240,102));
        minimumFrameSize.put("ChatFilterFrame",new Dimension(400,200));
        minimumFrameSize.put("ItemsGridFrame",new Dimension(150,150));
        minimumFrameSize.put("NotesFrame",new Dimension(540,100));
        minimumFrameSize.put("ChatFilterSettingsFrame",new Dimension(300,200));
        minimumFrameSize.put("GamePathChooser",new Dimension(600,30));
        minimumFrameSize.put("CurrencySearchFrame",new Dimension(400,300));

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

                saveButtonsConfig(getDefaultButtons());
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
            JSONArray buttons = (JSONArray) root.get("buttons");
            cachedButtonsConfig = new ArrayList<>();
            try {
                for (JSONObject next : (Iterable<JSONObject>) buttons) {
                    Object object = next.get("isKick");
                    boolean isKick = false;
                    Object object1 = next.get("isClose");
                    boolean isClose = false;
                    if(object != null){
                        isKick = Boolean.valueOf((String)object);
                        isClose = Boolean.valueOf((String)object1);
                    }
                    cachedButtonsConfig.add(new ResponseButton((long) next.get("id"),isKick,isClose,(String) next.get("title"), (String) next.get("value")));
                }
            }catch (Exception e){
                saveButtonsConfig(getDefaultButtons());
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
            whisperNotifier = WhisperNotifierStatus.valueOf(loadProperty("whisperNotifier"));
            fadeTime = Long.valueOf(loadProperty("fadeTime")).intValue();
            minOpacity = Long.valueOf(loadProperty("minOpacity")).intValue();
            maxOpacity = Long.valueOf(loadProperty("maxOpacity")).intValue();
            showOnStartUp = Boolean.valueOf(loadProperty("showOnStartUp"));
            showPatchNotes = Boolean.valueOf(loadProperty("showPatchNotes"));
            gamePath = loadProperty("gamePath");
            flowDirection = loadProperty("flowDirection");
            tradeMode = loadProperty("tradeMode");
            limitMsgCount = Long.valueOf(loadProperty("limitMsgCount")).intValue();
            expandedMsgCount = Long.valueOf(loadProperty("expandedMsgCount")).intValue();
            itemsGridEnable = Boolean.valueOf(loadProperty("itemsGridEnable"));
            checkUpdateOnStartUp = Boolean.valueOf(loadProperty("checkUpdateOnStartUp"));
            dismissAfterKick = Boolean.valueOf(loadProperty("dismissAfterKick"));
            inGameDnd = Boolean.valueOf(loadProperty("inGameDnd"));
            showLeague = Boolean.valueOf(loadProperty("showLeague"));
            dndResponseText = loadProperty("dndResponseText");
            defaultWords = loadProperty("defaultWords");
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
    public List<ResponseButton> getButtonsConfig(){
        return cachedButtonsConfig;
    }

    public FrameSettings getFrameSettings(String frameClass){
        FrameSettings settings = cachedFramesSettings.get(frameClass);
        if(settings == null) {
            FrameSettings defaultSettings = getDefaultFramesSettings().get(frameClass);
            if(defaultSettings != null) {
                cachedFramesSettings.put(frameClass, defaultSettings);
                saveFrameSettings();
            }
        }
        return cachedFramesSettings.get(frameClass);
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
    public void saveButtonsConfig(List<ResponseButton> buttons){
        cachedButtonsConfig = buttons;
        JSONArray list = new JSONArray();
        buttons.forEach((button)->{
            JSONObject buttonConfig = new JSONObject();
            buttonConfig.put("id",button.getId());
            buttonConfig.put("title",button.getTitle());
            buttonConfig.put("value",button.getResponseText());
            buttonConfig.put("isKick",String.valueOf(button.isKick()));
            buttonConfig.put("isClose",String.valueOf(button.isClose()));
            list.add(buttonConfig);
        });
        saveProperty("buttons", list);
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
    public void setDismissAfterKick(boolean dismissAfterKick) {
        this.dismissAfterKick = dismissAfterKick;
        saveProperty("dismissAfterKick", String.valueOf(this.dismissAfterKick));
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
    public void setShowPatchNotes(boolean showPatchNotes) {
        this.showPatchNotes = showPatchNotes;
        saveProperty("showPatchNotes", String.valueOf(this.showPatchNotes));
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
    public void setFlowDirection(String flowDirection) {
        this.flowDirection = flowDirection;
        saveProperty("flowDirection",flowDirection);
    }
    public void setLimitMsgCount(int limitMsgCount) {
        this.limitMsgCount = limitMsgCount;
        saveProperty("limitMsgCount",String.valueOf(this.limitMsgCount));
    }
    public void setExpandedMsgCount(int expandedMsgCount) {
        this.expandedMsgCount = expandedMsgCount;
        saveProperty("expandedMsgCount",String.valueOf(this.expandedMsgCount));
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

    public void setShowLeague(boolean showLeague) {
        this.showLeague = showLeague;
        saveProperty("showLeague",String.valueOf(this.showLeague));
    }

    public void setDefaultWords(String defaultWords) {
        this.defaultWords = defaultWords;
        saveProperty("defaultWords",defaultWords);
    }

    private List<ResponseButton> getDefaultButtons(){
        List<ResponseButton> defaultButtons = new ArrayList<>();
        defaultButtons.add(new ResponseButton(0,false,false,"1m","one minute"));
        defaultButtons.add(new ResponseButton(1,true,false,"thx","thanks"));
        defaultButtons.add(new ResponseButton(2,false,false,"no thx", "no thanks"));
        defaultButtons.add(new ResponseButton(3,false,false,"sold", "sold"));
        return defaultButtons;
    }
    public Map<String,FrameSettings> getDefaultFramesSettings(){
        Map<String, FrameSettings> defaultFramesSettings = new HashMap<>();
        defaultFramesSettings.put("TaskBarFrame",new FrameSettings(new Point(400, 500),new Dimension(109,20)));
        defaultFramesSettings.put("IncMessageFrame",new FrameSettings(new Point(700, 600),new Dimension(315,0)));
        defaultFramesSettings.put("OutMessageFrame",new FrameSettings(new Point(200, 500),new Dimension(280,115)));
        defaultFramesSettings.put("TestCasesFrame",new FrameSettings(new Point(1400, 500),new Dimension(400,100)));
        defaultFramesSettings.put("SettingsFrame",new FrameSettings(new Point(600, 600),new Dimension(540,500)));
        defaultFramesSettings.put("HistoryFrame",new FrameSettings(new Point(600, 500),new Dimension(280,400)));
        defaultFramesSettings.put("TimerFrame",new FrameSettings(new Point(400, 600),new Dimension(240,102)));
        defaultFramesSettings.put("ChatFilterFrame",new FrameSettings(new Point(400, 600),new Dimension(500,300)));
        defaultFramesSettings.put("ItemsGridFrame",new FrameSettings(new Point(12, 79),new Dimension(641,718)));
        defaultFramesSettings.put("NotesFrame",new FrameSettings(new Point(400, 600),new Dimension(540,100)));
        defaultFramesSettings.put("ChatFilterSettingsFrame",new FrameSettings(new Point(400, 600),new Dimension(320,200)));
        defaultFramesSettings.put("GamePathChooser",new FrameSettings(new Point(400, 600),new Dimension(520,30)));
        defaultFramesSettings.put("CurrencySearchFrame",new FrameSettings(new Point(400, 600),new Dimension(400,300)));
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
