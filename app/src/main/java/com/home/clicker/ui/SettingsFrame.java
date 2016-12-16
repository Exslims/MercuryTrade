package com.home.clicker.ui;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.SCEvent;
import com.home.clicker.events.SCEventHandler;
import com.home.clicker.events.custom.CloseFrameEvent;
import com.home.clicker.events.custom.RepaintEvent;
import com.home.clicker.ui.components.SettingsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends JFrame {
    public SettingsFrame(){
        super("Settings");
        setLayout(new FlowLayout());
        getRootPane().setOpaque(false);
        setUndecorated(true);
        setSize(800, 500);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(new Color(0, 0, 0, 0));
        setOpacity(0.9f);
        setAlwaysOnTop(true);
        setFocusableWindowState(true);
        setFocusable(true);

        SettingsPanel settingsPanel = new SettingsPanel();
        settingsPanel.setLocation(500,500);
        this.add(settingsPanel);


        EventRouter.registerHandler(CloseFrameEvent.class, event -> {
            EventRouter.clear(CloseFrameEvent.class);
            SettingsFrame.this.setVisible(false);
            SettingsFrame.this.dispose();
        });
        EventRouter.registerHandler(RepaintEvent.class, event -> {
            EventRouter.clear(RepaintEvent.class);
            SettingsFrame.this.revalidate();
            SettingsFrame.this.repaint();
        });
    }
}
