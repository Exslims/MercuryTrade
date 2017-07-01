package com.mercury.platform.ui.frame.titled;


import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.MercuryTabbedPane;
import com.mercury.platform.ui.components.panel.settings.*;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SettingsFrame extends AbstractTitledComponentFrame {
    private List<ConfigurationPanel> innerPanels;
    private boolean successfullySaved = true;
    public SettingsFrame(){
        super();
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.setAlwaysOnTop(false);
        this.innerPanels = new ArrayList<>();
        this.processingHideEvent = false;
        this.processHideEffect = false;
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.initContainer();
        this.pack();
    }

    private void initContainer() {

        MercuryTabbedPane tabbedPane = new MercuryTabbedPane(this);

        ConfigurationPanel generalSettings = new GeneralSettings();
        this.innerPanels.add(generalSettings);
        ConfigurationPanel cbSettings = new NotificationPanelSettings();
        this.innerPanels.add(cbSettings);
        ConfigurationPanel taskBarSettings = new TaskBarSettingsPanel();
        this.innerPanels.add(taskBarSettings);
        ConfigurationPanel soundSettings = new SoundSettingsPanel();
        this.innerPanels.add(soundSettings);

        tabbedPane.addTab("General",generalSettings);
        tabbedPane.addTab("Sound",soundSettings);
        tabbedPane.addTab("Notification panel",cbSettings);
        tabbedPane.addTab("Task panel",taskBarSettings);
//        tabbedPane.addTab("Help",new HelpPanel());
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
            innerPanels.forEach(settingsPanel -> {
                if(!settingsPanel.processAndSave()){
                    successfullySaved = false;
                }
            });
            if(successfullySaved) {
                MercuryStoreCore.INSTANCE.saveConfigSubject.onNext(true);
                hideComponent();
            }else {
                successfullySaved = true;
            }
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
    public void subscribe() {
    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    public void hideComponent() {
        super.hideComponent();
        MercuryStoreCore.INSTANCE.showingDelaySubject.onNext(true);
    }
}
