package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.ui.components.fields.MercuryTabbedPane;
import com.mercury.platform.ui.components.panel.settings.*;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends TitledComponentFrame {
    private List<ConfigurationPanel> innerPanels;
    public SettingsFrame(){
        super("MercuryTrade");
        setFocusable(true);
        setFocusableWindowState(true);
        setAlwaysOnTop(false);
        innerPanels = new ArrayList<>();
        processingHideEvent = false;
        processSEResize = false;
    }

    @Override
    protected void initialize() {
        super.initialize();
        initContainer();
        this.pack();
    }

    private void initContainer() {

        MercuryTabbedPane tabbedPane = new MercuryTabbedPane(this);

        ConfigurationPanel generalSettings = new GeneralSettings(this);
        innerPanels.add(generalSettings);
        ConfigurationPanel cbSettings = new CustomButtonSettings(this);
        innerPanels.add(cbSettings);

        tabbedPane.addTab("General",generalSettings);
        tabbedPane.addTab("Response buttons",cbSettings);
        tabbedPane.addTab("Help",new HelpPanel());
        tabbedPane.addTab("Support",new SupportPanel());
        tabbedPane.addTab("About",new AboutPanel());
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.PAGE_END);
    }


    private JPanel getBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.HEADER);

        JButton save = componentsFactory.getBorderedButton("Save");
        save.addActionListener(e -> {
            innerPanels.forEach(ConfigurationPanel::processAndSave);
            hideComponent();
        });
        JButton close = componentsFactory.getBorderedButton("Close");
        close.addActionListener(e -> {
            innerPanels.forEach(ConfigurationPanel::restore);
            pack();
            hideComponent();
        });
        save.setPreferredSize(new Dimension(80, 26));
        close.setPreferredSize(new Dimension(80, 26));
        panel.add(save);
        panel.add(close);
        return panel;
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            SettingsFrame.this.revalidate();
            SettingsFrame.this.repaint();
        });
    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    public void hideComponent() {
        super.hideComponent();
        EventRouter.INSTANCE.fireEvent(new AddShowDelayEvent());
    }
}
