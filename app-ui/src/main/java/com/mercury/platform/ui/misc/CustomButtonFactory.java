package com.mercury.platform.ui.misc;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.ui.components.ComponentsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CustomButtonFactory {
    private static Map<String,String> cachedConfigs;
    private static ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    public static JPanel getButtonsPanel(String whisper){
        Map<String, String> buttonsConfig = (Map<String, String>) ConfigManager.INSTANCE.getProperty("buttons");
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(AppThemeColor.TRANSPARENT);

        buttonsConfig.forEach((title,value)->{
            JButton button = componentsFactory.getBorderedButton(title);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(button.isEnabled()) {
                        EventRouter.fireEvent(new ChatCommandEvent("@" + whisper + " " + value));
                    }
                }

            });
            panel.add(button,0);
        });
        return panel;
    }
//    public static void saveNewButtonsConfig(Map<String,String> buttonsConfig){
//        cachedConfigs = null;
//        File customButtonConfig = ConfigManager.getCustomButtonConfig();
//        JSONObject root = new JSONObject();
//        JSONArray list = new JSONArray();
//        buttonsConfig.forEach((title,value)->{
//            JSONObject buttonConfig = new JSONObject();
//            buttonConfig.put("title",title);
//            buttonConfig.put("value", value);
//            list.add(buttonConfig);
//        });
//
//        root.put("buttons",list);
//        try {
//            FileWriter fileWriter = new FileWriter(customButtonConfig);
//            fileWriter.write(root.toJSONString());
//            fileWriter.flush();
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static Map<String,String> getButtonsConfig(){
//        Map<String, String> buttonsConfig = new HashMap<>();
//        if(cachedConfigs == null) {
//            JSONParser parser = new JSONParser();
//            try {
//                Object obj = parser.parse(new FileReader(ConfigManager.getCustomButtonConfig()));
//                JSONObject jsonObject = (JSONObject) obj;
//                JSONArray buttons = (JSONArray) jsonObject.get("buttons");
//                Iterator<JSONObject> iterator = buttons.iterator();
//                while (iterator.hasNext()) {
//                    JSONObject next = iterator.next();
//                    buttonsConfig.put((String) next.get("title"), (String) next.get("value"));
//                }
//                cachedConfigs = buttonsConfig;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                saveNewButtonsConfig(getDefaultButtons());
//                cachedConfigs = getDefaultButtons();
//                return getDefaultButtons();
//            }
//        }else {
//            return cachedConfigs;
//        }
//        return buttonsConfig;
//    }
//    private static Map<String, String > getDefaultButtons(){
//        Map<String,String> defaultButtons = new HashMap<>();
//        defaultButtons.put("1m","one minute");
//        defaultButtons.put("thx","thanks");
//        defaultButtons.put("no thx", "no thanks");
//        return defaultButtons;
//    }
}
