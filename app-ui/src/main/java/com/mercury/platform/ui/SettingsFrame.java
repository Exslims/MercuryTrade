package com.mercury.platform.ui;


import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CloseFrameEvent;
import com.mercury.platform.shared.events.custom.DraggedWindowEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.ui.components.panel.SettingsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends JFrame implements HasEventHandlers {
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

        initHandlers();
    }

    @Override
    public void initHandlers() {
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
        EventRouter.registerHandler(DraggedWindowEvent.class, event -> {
            int x = ((DraggedWindowEvent) event).getX();
            int y = ((DraggedWindowEvent) event).getY();
            SettingsFrame.this.setLocation(x,y);
        });
    }
}
