package com.mercury.platform.ui.frame.titled;


import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.shared.events.custom.RequestPatchNotesEvent;
import com.mercury.platform.ui.components.fields.MercuryTabbedPane;
import com.mercury.platform.ui.components.panel.settings.*;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends TitledComponentFrame {
    private List<ConfigurationPanel> innerPanels;
    private boolean successfullySaved = true;
    public SettingsFrame(){
        super("MercuryTrade");
        setFocusable(true);
        setFocusableWindowState(true);
        setAlwaysOnTop(false);
        innerPanels = new ArrayList<>();
        processingHideEvent = false;
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
        ConfigurationPanel taskBarSettings = new TaskBarSettingsPanel();
        innerPanels.add(taskBarSettings);

        tabbedPane.addTab("General",generalSettings);
        tabbedPane.addTab("Notification panel",cbSettings);
        tabbedPane.addTab("Task panel",taskBarSettings);
//        tabbedPane.addTab("Help",new HelpPanel());
        tabbedPane.addTab("Support",new SupportPanel());
        tabbedPane.addTab("About",new AboutPanel());

        JButton openTutorial =
                componentsFactory.getIconButton("app/tutorial.png",
                        17,
                        AppThemeColor.TRANSPARENT,
                        "Open tutorial");
        openTutorial.addActionListener(action -> {
            hideComponent();
            FramesManager.INSTANCE.preShowFrame(NotesFrame.class);
        });
        JButton checkUpdates =
                componentsFactory.getIconButton("app/check-update.png",
                        15,
                        AppThemeColor.TRANSPARENT,
                        "Check for updates");
        checkUpdates.addActionListener(action -> {
            ApplicationHolder.getInstance().setManualRequest(true);
            EventRouter.CORE.fireEvent(new RequestPatchNotesEvent());
        });
        JButton openTests =
                componentsFactory.getIconButton("app/open-tests.png",
                        15,
                        AppThemeColor.TRANSPARENT,
                        "Open tests frame");
        openTests.addActionListener(action -> {
            hideComponent();
            FramesManager.INSTANCE.preShowFrame(TestCasesFrame.class);
        });
        this.miscPanel.add(openTutorial,0);
        this.miscPanel.add(checkUpdates,0);
        this.miscPanel.add(openTests,0);
        this.miscPanel.setBorder(BorderFactory.createEmptyBorder(-4,0,0,0));

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
    public void initHandlers() {
    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    public void hideComponent() {
        super.hideComponent();
        EventRouter.CORE.fireEvent(new AddShowDelayEvent());
    }
}
