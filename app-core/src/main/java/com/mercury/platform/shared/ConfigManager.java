package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.FrameSettings;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    private Logger logger = Logger.getLogger(ConfigManager.class);

    private static class ConfigManagerHolder {
        static final ConfigManager HOLDER_INSTANCE = new ConfigManager();
    }

    public static ConfigManager INSTANCE = ConfigManagerHolder.HOLDER_INSTANCE;

    private final String CONFIG_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrader";
    private final String CONFIG_FILE = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrader\\app-config.json";
    private Map<String, Timer> componentsTimers = new HashMap<>();
    private Map<String, PointListener> componentsPointListeners = new HashMap<>();

    private Map<String, String> cachedButtonsConfig;
    private Map<String, FrameSettings> cachedFramesSettings;
    private String gamePath = null;

    private ConfigManager() {
        load();
    }

    /**
     * Loading application data from app-config.json file. If file does not exist, created and filled by default.
     */
    private void load() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            try {
                new File(CONFIG_FILE_PATH).mkdir();
                FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                fileWriter.write(new JSONObject().toJSONString());
                fileWriter.flush();
                fileWriter.close();

                saveButtonsConfig(getDefaultButtons());
                cachedFramesSettings = getDefaultFramesSettings();
                getDefaultFramesSettings().forEach(this::saveFrameSettings);
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
            gamePath = (String) root.get("gamePath");

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
                        new Point(Integer.valueOf(String.valueOf(location.get("frameX"))), Integer.valueOf(String.valueOf(location.get("frameY")))),
                        new Dimension(Integer.valueOf(String.valueOf(size.get("width"))), Integer.valueOf(String.valueOf(size.get("height"))))
                );
                cachedFramesSettings.put((String) next.get("frameClassName"), settings);
            }
        } catch (Exception e) {
            logger.error("Error in loadConfigFile: ",e);
        }
    }

    /**
     * Checking valida path of game.
     *
     * @param gamePath the path to check
     * @return true if path is valid
     */
    public boolean isValidPath(String gamePath) {
        File logsFile = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return logsFile.exists();
    }

    public Map<String, String> getButtonsConfig(){
        return cachedButtonsConfig;
    }

    public FrameSettings getFrameSettings(String frameClass){
        return cachedFramesSettings.get(
                frameClass) == null ?
                getDefaultFramesSettings().get(frameClass) :  cachedFramesSettings.get(frameClass);
    }
    /**
     * Save frame location to app-config after 4 sec when frame was moved.
     *
     * @param frameClassName frame name, always insert frame.getClass().getSimpleName()
     *                      and don't forget to add in load() method default value.
     * @param point         frame.getLocation().
     */
    public void saveFrameLocation(String frameClassName, Point point) {
        //each frame has its timer
        Timer timer = componentsTimers.get(frameClassName);
        if (timer == null) {
            timer = new Timer(4000, null);
            Timer finalTimer = timer;
            PointListener pointListener = new PointListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FrameSettings settings = cachedFramesSettings.get(frameClassName);
                    //x,y from PointListener
                    settings.setFrameLocation(new Point(x,y));
                    saveFrameSettings(frameClassName,settings);
                    finalTimer.stop();
                }
            };
            componentsPointListeners.put(frameClassName, pointListener);
            componentsTimers.put(frameClassName, timer);
            timer.addActionListener(pointListener);
        } else {
            PointListener pointListener = componentsPointListeners.get(frameClassName);
            pointListener.x = point.x;
            pointListener.y = point.y;
        }
        timer.stop();
        timer.start();
    }

    public void saveFrameSize(String frameClassName, Dimension size){

    }

    /**
     * Save game path with checking validity.
     * @param gamePath the path to save
     */
    public void saveGamePath(String gamePath){
        if(isValidPath(gamePath)) {
            saveProperty("gamePath", gamePath);
        }
    }

    public String getGamePath() {
        return gamePath;
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

    private void saveFrameSettings(String frameClassName,FrameSettings settings){
        cachedFramesSettings.put(frameClassName, settings);
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
    private void saveProperty(String token, Object jsonObject){
        JSONParser parser = new JSONParser();
        try {
            JSONObject root = (JSONObject) parser.parse(new FileReader(CONFIG_FILE));
            Object obj = root.get(token);
            if(obj != null){
                root.replace(token,jsonObject);
            }else {
                root.put(token,jsonObject);
            }
            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
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
        defaultButtons.put("sold", "sold");
        return defaultButtons;
    }
    private Map<String,FrameSettings> getDefaultFramesSettings(){
        Map<String,FrameSettings> dFramesSettings = new HashMap<>();
        dFramesSettings.put("TaskBarFrame",new FrameSettings(new Point(500, 500),new Dimension(114,50)));
        dFramesSettings.put("MessageFrame",new FrameSettings(new Point(700, 500),new Dimension(370,115)));
        dFramesSettings.put("GamePathChooser",new FrameSettings(new Point(600, 500),new Dimension(570,100)));
        dFramesSettings.put("TestCasesFrame",new FrameSettings(new Point(900, 500),new Dimension(400,100)));
        dFramesSettings.put("SettingsFrame",new FrameSettings(new Point(600, 600),new Dimension(114,50)));
        dFramesSettings.put("HistoryFrame",new FrameSettings(new Point(600, 600),new Dimension(400,100)));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        dFramesSettings.put("NotificationFrame",new FrameSettings(new Point((int) width / 2, (int) height / 2),new Dimension(114,50)));
        return dFramesSettings;
    }


    private abstract class PointListener implements ActionListener{
        public int x;
        public int y;
    }

}
