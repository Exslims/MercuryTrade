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
    private static ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    public static JPanel getButtonsPanel(String whisper){
        Map<String, String> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();
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
}
