package com.mercury.platform.shared;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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
    private Logger logger = Logger.getLogger(ConfigManager.class);

    private static class ConfigManagerHolder {
        static final ConfigManager HOLDER_INSTANCE = new ConfigManager();
    }

    public static ConfigManager INSTANCE = ConfigManagerHolder.HOLDER_INSTANCE;

    private final String CONFIG_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrader";
    private final String CONFIG_FILE = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrader\\app-config.json";
    private Map<String, Object> properties = new HashMap<>();
    private Map<String, Timer> componentsTimers = new HashMap<>();
    private Map<String, PointListener> componentsPointListeners = new HashMap<>();

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
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                saveComponentLocation("TaskBarFrame", new Point(500, 500));
                saveComponentLocation("MessageFrame", new Point(700, 500));
                saveComponentLocation("GamePathChooser", new Point(600, 500));
                saveComponentLocation("TestCasesFrame", new Point(900, 500));
                saveComponentLocation("SettingsFrame", new Point(600, 600));
                saveComponentLocation("HistoryFrame", new Point(600, 600));
                saveComponentLocation("NotificationFrame", new Point((int) width / 2, (int) height / 2));
            } catch (IOException e) {
                logger.error(e);
            }
        } else {
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(CONFIG_FILE));
                JSONObject jsonObject = (JSONObject) obj;
                String gamePath = (String) jsonObject.get("gamePath");

                JSONArray buttons = (JSONArray) jsonObject.get("buttons");
                Map<String, String> buttonsConfig = new HashMap<>();
                if (buttons != null) {
                    for (JSONObject next : (Iterable<JSONObject>) buttons) {
                        buttonsConfig.put((String) next.get("title"), (String) next.get("value"));
                    }
                } else {
                    buttonsConfig = getDefaultButtons();
                }
                JSONObject taskBarLocation = (JSONObject) jsonObject.get("TaskBarFrame");
                Point taskBarPoint = new Point(
                        Integer.valueOf(String.valueOf(taskBarLocation.get("x"))),
                        Integer.valueOf(String.valueOf(taskBarLocation.get("y"))));

                JSONObject messageLocation = (JSONObject) jsonObject.get("MessageFrame");
                Point messagePoint = new Point(
                        Integer.valueOf(String.valueOf(messageLocation.get("x"))),
                        Integer.valueOf(String.valueOf(messageLocation.get("y"))));
                JSONObject gamePathLocation = (JSONObject) jsonObject.get("GamePathChooser");
                Point gamePathPoint = new Point(
                        Integer.valueOf(String.valueOf(gamePathLocation.get("x"))),
                        Integer.valueOf(String.valueOf(gamePathLocation.get("y"))));
                JSONObject settingsLocation = (JSONObject) jsonObject.get("SettingsFrame");
                Point settingsPoint = new Point(
                        Integer.valueOf(String.valueOf(settingsLocation.get("x"))),
                        Integer.valueOf(String.valueOf(settingsLocation.get("y"))));
                JSONObject historyLocation = (JSONObject) jsonObject.get("HistoryFrame");
                Point historyPoint = new Point(
                        Integer.valueOf(String.valueOf(historyLocation.get("x"))),
                        Integer.valueOf(String.valueOf(historyLocation.get("y"))));
                JSONObject notificationLocation = (JSONObject) jsonObject.get("NotificationFrame");
                Point notificationPoint = new Point(
                        Integer.valueOf(String.valueOf(notificationLocation.get("x"))),
                        Integer.valueOf(String.valueOf(notificationLocation.get("y"))));

                //removing
                JSONObject testLocation = (JSONObject) jsonObject.get("TestCasesFrame");
                Point testPoint = new Point(
                        Integer.valueOf(String.valueOf(testLocation.get("x"))),
                        Integer.valueOf(String.valueOf(testLocation.get("y"))));

                properties.put("gamePath", gamePath);
                properties.put("buttons", buttonsConfig);
                properties.put("TaskBarFrame", taskBarPoint);
                properties.put("MessageFrame", messagePoint);
                properties.put("GamePathChooser", gamePathPoint);
                properties.put("SettingsFrame", settingsPoint);
                properties.put("HistoryFrame", historyPoint);
                properties.put("NotificationFrame", notificationPoint);
                //removing
                properties.put("TestCasesFrame", testPoint);
            } catch (Exception e) {
                logger.error("Error in ConfigManager.load", e);
            }
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

    /**
     * Get property from loaded data.
     *
     * @param token property name
     * @return property value
     */
    public Object getProperty(String token) {
        return properties.get(token);
    }

    /**
     * Save frame location to app-config after 4 sec when frame was moved.
     *
     * @param componentName frame name, always insert frame.getClass().getSimpleName()
     *                      and don't forget to add in load() method default value.
     * @param point         frame.getLocation().
     */
    public void saveComponentLocation(String componentName, Point point) {
        properties.put(componentName, point);
        //each frame has its timer
        Timer timer = componentsTimers.get(componentName);
        if (timer == null) {
            timer = new Timer(4000, null);
            Timer finalTimer = timer;
            PointListener pointListener = new PointListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JSONObject object = new JSONObject();
                    //x and y from PointListener
                    object.put("x", x);
                    object.put("y", y);
                    saveProperty(componentName, object);
                    finalTimer.stop();
                }
            };
            componentsPointListeners.put(componentName, pointListener);
            componentsTimers.put(componentName, timer);
            timer.addActionListener(pointListener);
        } else {
            PointListener pointListener = componentsPointListeners.get(componentName);
            pointListener.x = point.x;
            pointListener.y = point.y;
        }
        timer.stop();
        timer.start();
    }

    /**
     * Save game path with checking validity.
     * @param gamePath the path to save
     */
    public void saveGamePath(String gamePath){
        if(isValidPath(gamePath)) {
            properties.put("gamePath", gamePath);
            saveProperty("gamePath", gamePath);
        }
    }

    /**
     * Save custom buttons config.
     * @param buttons map of buttons data.(title,text to send)
     */
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


    private abstract class PointListener implements ActionListener{
        public int x;
        public int y;
    }

}
