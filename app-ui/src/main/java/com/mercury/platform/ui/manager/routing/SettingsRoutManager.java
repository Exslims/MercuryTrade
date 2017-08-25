package com.mercury.platform.ui.manager.routing;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.panel.settings.page.*;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.misc.MercuryStoreUI;

public class SettingsRoutManager implements AsSubscriber {
    private SettingsPagePanel generalSettings;
    private SettingsPagePanel soundSettings;
    private SettingsPagePanel notificationSettings;
    private SettingsPagePanel taskBarSettings;
    private SettingsPagePanel supportPanel;
    private SettingsPagePanel aboutPanel;

    private SettingsFrame settingsFrame;


    public SettingsRoutManager(SettingsFrame settingsFrame) {
        this.settingsFrame = settingsFrame;

        if (Configuration.get().applicationConfiguration().get().isShowOnStartUp()) {
            Configuration.get().applicationConfiguration().get().setShowOnStartUp(false);
        }

        this.generalSettings = new GeneralSettingsPagePanel();
        this.soundSettings = new SoundSettingsPagePanel();
        this.generalSettings = new GeneralSettingsPagePanel();
        this.notificationSettings = new NotificationSettingsPagePanel();
        this.taskBarSettings = new TaskBarSettingsPagePanel();
        this.supportPanel = new SupportPagePanel();
        this.aboutPanel = new AboutPagePanel();

        this.settingsFrame.setContentPanel(this.generalSettings);
        this.subscribe();
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.settingsStateSubject.subscribe(state -> {
            switch (state) {
                case GENERAL_SETTINGS: {
                    this.settingsFrame.setContentPanel(this.generalSettings);
                    break;
                }
                case SOUND_SETTING: {
                    this.settingsFrame.setContentPanel(this.soundSettings);
                    break;
                }
                case NOTIFICATION_SETTINGS: {
                    this.settingsFrame.setContentPanel(this.notificationSettings);
                    break;
                }
                case TASK_BAR_SETTINGS: {
                    this.settingsFrame.setContentPanel(this.taskBarSettings);
                    break;
                }
                case SUPPORT: {
                    this.settingsFrame.setContentPanel(this.supportPanel);
                    break;
                }
                case ABOUT: {
                    this.settingsFrame.setContentPanel(this.aboutPanel);
                    break;
                }
            }
        });
        MercuryStoreUI.settingsRestoreSubject.subscribe(state -> {
            this.generalSettings.restore();
            this.notificationSettings.restore();
            this.soundSettings.restore();
            this.taskBarSettings.restore();
            this.settingsFrame.repaint();
            this.settingsFrame.pack();
        });
        MercuryStoreUI.settingsSaveSubject.subscribe(state -> {
            this.generalSettings.onSave();
            this.notificationSettings.onSave();
            this.soundSettings.onSave();
            this.taskBarSettings.onSave();
            new Thread(() -> MercuryStoreCore.saveConfigSubject.onNext(true)).start();
            MercuryStoreUI.settingsPostSubject.onNext(true);
        });
    }
}
