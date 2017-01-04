package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends OverlaidFrame {

    public SettingsFrame(){
        super("MT-Settings");
    }

    @Override
    protected void init() {
        super.init();
        setFocusableWindowState(true);
        setFocusable(true);
        disableHideEffect();
        initContainer();
        this.pack();
    }

    private void initContainer() {
        this.add(getGeneralSettingsPanel(), BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.PAGE_END);
    }
    private JPanel getGeneralSettingsPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        panel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,AppThemeColor.BORDER));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(AppThemeColor.TRANSPARENT);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(AppThemeColor.TRANSPARENT);
        settingsPanel.add(componentsFactory.getBorderedButton("TEST"));
        settingsPanel.setVisible(true);

        JButton expandButton = componentsFactory.getIconButton("app/collapse.png", 18);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!settingsPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse.png", 18));
                    settingsPanel.setVisible(true);
                }else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand.png", 18));
                    settingsPanel.setVisible(false);
                }
                SettingsFrame.this.pack();
            }
        });
        titlePanel.add(expandButton);
        titlePanel.add(componentsFactory.getTextLabel("General"));

        panel.add(titlePanel);
        panel.add(settingsPanel);
        return panel;
    }


    private JPanel getBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.WHISPER_PANEL);

        JButton save = componentsFactory.getBorderedButton("Save");
        JButton close = componentsFactory.getBorderedButton("Close");

        panel.add(save);
        panel.add(close);
        return panel;
    }


    @Override
    public void initHandlers() {

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
