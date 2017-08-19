package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeysSettingsDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class TaskBarSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<ApplicationDescriptor> applicationConfig;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeyService;
    private HotKeysSettingsDescriptor hotKeySnapshot;
    private ApplicationDescriptor applicationSnapshot;

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.applicationConfig = Configuration.get().applicationConfiguration();
        this.hotKeyService = Configuration.get().hotKeysConfiguration();
        this.applicationSnapshot = CloneHelper.cloneObject(this.applicationConfig.get());
        this.hotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get());

        JPanel root = componentsFactory.getJPanel(new GridLayout(0,2),AppThemeColor.ADR_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));

        JTextField responseField = componentsFactory.getTextField(this.applicationConfig.get().getDndResponseText(), FontStyle.REGULAR, 16f);
        responseField.setEnabled(this.applicationConfig.get().isInGameDnd());
        JCheckBox inGameDND = this.componentsFactory.getCheckBox(this.applicationSnapshot.isInGameDnd());
        inGameDND.addActionListener(action -> {
            this.applicationSnapshot.setInGameDnd(inGameDND.isSelected());
            responseField.setEnabled(inGameDND.isSelected());
        });
        root.add(componentsFactory.getTextLabel("Enable in-game dnd:", FontStyle.REGULAR));
        root.add(inGameDND);
        root.add(componentsFactory.getTextLabel("DND response:", FontStyle.REGULAR));
        root.add(this.componentsFactory.wrapToSlide(responseField,AppThemeColor.ADR_BG));

        JPanel hotKeysPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.SETTINGS_BG);
        hotKeysPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.hotKeySnapshot.getTaskBarNHotKeysList().forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 24,SwingConstants.CENTER));
            root.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(pair.getDescriptor()),AppThemeColor.SETTINGS_BG,2,4,1,1));

        });
        this.container.add(this.componentsFactory.wrapToSlide(root));
        this.container.add(this.componentsFactory.wrapToSlide(hotKeysPanel));
    }

    @Override
    public void onSave() {
        this.applicationConfig.set(CloneHelper.cloneObject(this.applicationSnapshot));
        this.hotKeyService.set(CloneHelper.cloneObject(this.hotKeySnapshot));
    }

    @Override
    public void restore() {
        this.applicationSnapshot = CloneHelper.cloneObject(this.applicationConfig.get());
        this.hotKeySnapshot = CloneHelper.cloneObject(this.hotKeyService.get());
        this.removeAll();
        this.onViewInit();
    }
}
