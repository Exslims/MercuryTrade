package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class TaskBarSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<TaskBarDescriptor> taskBarService;
    private TaskBarDescriptor taskBarSnapshot;
    @Override
    public void onViewInit() {
        super.onViewInit();
        this.taskBarService = Configuration.get().taskBarConfiguration();
        this.taskBarSnapshot = CloneHelper.cloneObject(this.taskBarService.get());

        JPanel root = componentsFactory.getJPanel(new GridLayout(0, 2), AppThemeColor.ADR_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));

        JTextField responseField = componentsFactory.getTextField(this.taskBarSnapshot.getDndResponseText(), FontStyle.REGULAR, 16f);
        responseField.setEnabled(this.taskBarSnapshot.isInGameDnd());
        responseField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                taskBarSnapshot.setDndResponseText(responseField.getText());
            }
        });
        JCheckBox inGameDND = this.componentsFactory.getCheckBox(this.taskBarSnapshot.isInGameDnd());
        inGameDND.addActionListener(action -> {
            this.taskBarSnapshot.setInGameDnd(inGameDND.isSelected());
            responseField.setEnabled(inGameDND.isSelected());
        });
        root.add(componentsFactory.getTextLabel("Enable in-game dnd:", FontStyle.REGULAR));
        root.add(inGameDND);
        root.add(componentsFactory.getTextLabel("DND response:", FontStyle.REGULAR));
        root.add(this.componentsFactory.wrapToSlide(responseField, AppThemeColor.ADR_BG));

        JPanel hotKeysPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4), AppThemeColor.SETTINGS_BG);
        hotKeysPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        root.add(this.componentsFactory.getIconLabel("app/hideout.png", 24, SwingConstants.CENTER));
        HotKeyGroup hotKeyGroup = new HotKeyGroup(true);
        HotKeyPanel hotKeyHideoutPanel = new HotKeyPanel(this.taskBarSnapshot.getHideoutHotkey());
        hotKeyGroup.registerHotkey(hotKeyHideoutPanel);
        root.add(this.componentsFactory.wrapToSlide(hotKeyHideoutPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));
        this.container.add(this.componentsFactory.wrapToSlide(root));
        this.container.add(this.componentsFactory.wrapToSlide(hotKeysPanel));

        root.add(this.componentsFactory.getIconLabel("app/helpIG_icon.png", 24, SwingConstants.CENTER));
        HotKeyPanel hotKeyHelpIGPanel = new HotKeyPanel(this.taskBarSnapshot.getHelpIGHotkey());
        hotKeyGroup.registerHotkey(hotKeyHelpIGPanel);
        root.add(this.componentsFactory.wrapToSlide(hotKeyHelpIGPanel, AppThemeColor.SETTINGS_BG, 2, 4, 1, 1));

        this.container.add(this.componentsFactory.wrapToSlide(root));
        this.container.add(this.componentsFactory.wrapToSlide(hotKeysPanel));
    }

    @Override
    public void onSave() {
        this.taskBarService.set(CloneHelper.cloneObject(this.taskBarSnapshot));
    }

    @Override
    public void restore() {
        this.taskBarSnapshot = CloneHelper.cloneObject(this.taskBarService.get());
        this.removeAll();
        this.onViewInit();
    }
}
