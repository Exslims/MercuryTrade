package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.ui.components.panel.CollapsiblePanel;
import com.mercury.platform.ui.components.panel.settings.AboutPanel;
import com.mercury.platform.ui.components.panel.settings.ConfigurationPanel;
import com.mercury.platform.ui.components.panel.settings.CustomButtonSettings;
import com.mercury.platform.ui.components.panel.settings.GeneralSettings;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends OverlaidFrame {
    private List<ConfigurationPanel> innerPanels = new ArrayList<>();
    public SettingsFrame(){
        super("MT-Settings");
        initContainer();
        setFocusable(true);
        setFocusableWindowState(true);
        setAlwaysOnTop(false);
        processingHideEvent = false;
        this.pack();
    }

    private void initContainer() {
        JPanel centralPanel = componentsFactory.getTransparentPanel(null);
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));

        ConfigurationPanel generalSettings = new GeneralSettings(this);
        innerPanels.add(generalSettings);
        centralPanel.add(new CollapsiblePanel<>("General", generalSettings, this, true));

        ConfigurationPanel cbSettings = new CustomButtonSettings(this);
        innerPanels.add(cbSettings);
        centralPanel.add(new CollapsiblePanel<>("Message panel", cbSettings, this, false));
        centralPanel.add(new CollapsiblePanel<>("About", new AboutPanel(), this, false));

        this.add(centralPanel, BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.PAGE_END);
    }


    private JPanel getBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.HEADER);

        JButton save = componentsFactory.getBorderedButton("Save");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                innerPanels.forEach(ConfigurationPanel::processAndSave);
                SettingsFrame.this.dispose();
            }
        });
        JButton close = componentsFactory.getBorderedButton("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SettingsFrame.this.dispose();
            }
        });
        panel.add(save);
        panel.add(close);
        return panel;
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            SettingsFrame.this.revalidate();
            SettingsFrame.this.repaint();
        });
    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
