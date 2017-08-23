package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.manager.routing.SettingsPage;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    public MenuPanel() {
        super();
        this.setBackground(AppThemeColor.FRAME);
        this.setPreferredSize(new Dimension(220, 20));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppThemeColor.ADR_PANEL_BORDER));
        this.onViewInit();
    }

    @Override
    public void onViewInit() {
        JList<MenuEntry> list = new JList<>(getEntries());
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                list.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                list.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        list.setCellRenderer(new MenuListRenderer());
        list.setBackground(AppThemeColor.FRAME);
        list.setSelectedIndex(0);
        list.addListSelectionListener(e ->
                list.getSelectedValue().getAction().onClick());

        JLabel appIcon = this.componentsFactory.getTextLabel("MercuryTrade", FontStyle.BOLD, 22);
        appIcon.setIcon(this.componentsFactory.getIcon("app/app-icon.png", 50));
        appIcon.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        appIcon.setBackground(AppThemeColor.FRAME);
        this.add(appIcon, BorderLayout.PAGE_START);
        this.add(list, BorderLayout.CENTER);
    }

    @SuppressWarnings("all")
    private MenuEntry[] getEntries() {
        return new MenuEntry[]{
                new MenuEntry("General", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.GENERAL_SETTINGS);
                }, this.componentsFactory.getIcon("app/general_settings.png", 22)),
                new MenuEntry("Sound", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.SOUND_SETTING);
                }, this.componentsFactory.getIcon("app/sound_settings.png", 22)),
                new MenuEntry("Notification panel", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.NOTIFICATION_SETTINGS);
                }, this.componentsFactory.getIcon("app/notification_panel_settings.png", 22)),
                new MenuEntry("Task bar", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.TASK_BAR_SETTINGS);
                }, this.componentsFactory.getIcon("app/task_bar_settings.png", 22)),
                new MenuEntry("Support", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.SUPPORT);
                }, this.componentsFactory.getIcon("app/support_settings.png", 22)),
                new MenuEntry("About", () -> {
                    MercuryStoreUI.settingsStateSubject.onNext(SettingsPage.ABOUT);
                }, this.componentsFactory.getIcon("app/app-icon_sepia.png", 22)),
        };
    }
}
