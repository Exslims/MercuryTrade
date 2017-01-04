package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.SetForegroundGameEvent;
import com.mercury.platform.ui.components.fields.label.FontStyle;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        disableHideEffect();
        initContainer();
        setFocusable(true);
        setFocusableWindowState(true);
        setAlwaysOnTop(false);
        processingHideEvent = false;
        this.pack();
    }

    private void initContainer() {
        JPanel centralPanel = new JPanel();
        centralPanel.setBackground(AppThemeColor.TRANSPARENT);
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));
        centralPanel.add(getGeneralSettingsPanel());
        centralPanel.add(getAboutPanel());

        this.add(centralPanel, BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.PAGE_END);
    }
    private JPanel getGeneralSettingsPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        panel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,AppThemeColor.BORDER));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(AppThemeColor.HEADER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(-4,0,-4,0));

        JPanel settingsPanel = getGeneralInnerPanel();

        JButton expandButton = componentsFactory.getIconButton("app/collapse.png", 16);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!settingsPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse.png", 16));
                    settingsPanel.setVisible(true);
                }else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand.png", 16));
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
    private JPanel getGeneralInnerPanel(){
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(AppThemeColor.TRANSPARENT);
        settingsPanel.setVisible(true);
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.BORDER));

        String[] seconds = {"0","1","2","3","4","5"};
        JComboBox secondsPicker = componentsFactory.getComboBox(seconds);

        JPanel hideSettingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hideSettingsPanel.setBackground(AppThemeColor.TRANSPARENT);
        hideSettingsPanel.add(componentsFactory.getTextLabel("Hide panels after:"));
        hideSettingsPanel.add(secondsPicker);
        hideSettingsPanel.add(componentsFactory.getTextLabel("seconds. 0 - always show"));

        JPanel minOpacitySettingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.setBackground(AppThemeColor.TRANSPARENT);
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Minimum opacity: "));

        JPanel minValuePanel = new JPanel();
        minValuePanel.setBackground(AppThemeColor.TRANSPARENT);
        JLabel minValueField = componentsFactory.getTextLabel("60%"); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,30));
        minOpacitySettingsPanel.add(minValuePanel);

        JSlider minSlider = componentsFactory.getSlider(0,100,60);
        minSlider.addChangeListener(e -> {
            minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
            SettingsFrame.this.repaint();
        });
        minOpacitySettingsPanel.add(minSlider);

        JPanel maxOpacitySettingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.setBackground(AppThemeColor.TRANSPARENT);
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Maximum opacity: "));

        JPanel maxValuePanel = new JPanel();
        maxValuePanel.setBackground(AppThemeColor.TRANSPARENT);
        JLabel maxValueField = componentsFactory.getTextLabel("60%"); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);

        JSlider maxSlider = componentsFactory.getSlider(20,100,60);
        maxSlider.addChangeListener(e -> {
            maxValueField.setText(String.valueOf(maxSlider.getValue()) + "%");
            SettingsFrame.this.setOpacity(maxSlider.getValue()/100.0f);
        });
        maxOpacitySettingsPanel.add(maxSlider);

        JPanel notifierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notifierPanel.setBackground(AppThemeColor.TRANSPARENT);
        notifierPanel.add(componentsFactory.getTextLabel("Trade messages notifier: "));
        String[] notifierStatus = {"Always", "While on al-tab","Never"};
        JComboBox notifierStatusPicker = componentsFactory.getComboBox(notifierStatus);
        notifierPanel.add(notifierStatusPicker);

        settingsPanel.add(hideSettingsPanel);
        settingsPanel.add(minOpacitySettingsPanel);
        settingsPanel.add(maxOpacitySettingsPanel);
        settingsPanel.add(notifierPanel);
        return settingsPanel;
    }

    private JPanel getAboutPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(AppThemeColor.HEADER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(-4,0,-4,0));

        JPanel aboutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aboutPanel.setBackground(AppThemeColor.TRANSPARENT);
        aboutPanel.setVisible(false);
        aboutPanel.add(componentsFactory.getTextLabel("About program and contacts here."));
        aboutPanel.add(componentsFactory.getTextField("TYPE HERE", FontStyle.BOLD,16));

        JButton expandButton = componentsFactory.getIconButton("app/expand.png", 16);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!aboutPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse.png", 16));
                    aboutPanel.setVisible(true);
                }else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand.png", 16));
                    aboutPanel.setVisible(false);
                }
                SettingsFrame.this.pack();
            }
        });
        titlePanel.add(expandButton);
        titlePanel.add(componentsFactory.getTextLabel("About"));
        panel.add(titlePanel);
        panel.add(aboutPanel);
        return panel;
    }


    private JPanel getBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.HEADER);

        JButton save = componentsFactory.getBorderedButton("Save");
        JButton close = componentsFactory.getBorderedButton("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SettingsFrame.this.setVisible(false);
                SettingsFrame.this.dispose();
            }
        });
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
