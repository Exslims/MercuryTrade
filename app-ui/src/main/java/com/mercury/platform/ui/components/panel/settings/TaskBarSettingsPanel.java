package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class TaskBarSettingsPanel extends ConfigurationPanel{
    private PlainConfigurationService<ApplicationDescriptor> applicationConfig;
    private JTextField responseField;
    private JCheckBox enableInGameDND;

    public TaskBarSettingsPanel() {
        super();
        this.applicationConfig = Configuration.get().applicationConfiguration();
        this.createUI();
    }
    @Override
    public void createUI() {
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(1, 1));
        JPanel dndSettings = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel dndLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "DND mode");
        dndLabel.setBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                        BorderFactory.createEmptyBorder(3,5,3,5)));

        dndSettings.add(dndLabel,BorderLayout.PAGE_START);
        dndSettings.add(getDNDPanel(),BorderLayout.CENTER);
        root.add(dndSettings);
        verticalScrollContainer.add(root, BorderLayout.PAGE_START);
    }
    private JPanel getDNDPanel(){
        JPanel topPanel = componentsFactory.getTransparentPanel(new GridLayout(2,2));
        topPanel.add(componentsFactory.getTextLabel("Enable in-game dnd:", FontStyle.REGULAR), BorderLayout.LINE_START);
        enableInGameDND = new JCheckBox();
        enableInGameDND.setBackground(AppThemeColor.TRANSPARENT);
        enableInGameDND.setSelected(this.applicationConfig.get().isInGameDnd());
        responseField = componentsFactory.getTextField(this.applicationConfig.get().getDndResponseText(), FontStyle.REGULAR, 16f);
        responseField.setEnabled(this.applicationConfig.get().isInGameDnd());
        componentsFactory.setUpToggleCallbacks(enableInGameDND,
                () -> responseField.setEnabled(false),
                () -> responseField.setEnabled(true),this.applicationConfig.get().isInGameDnd());

        topPanel.add(enableInGameDND, BorderLayout.CENTER);

        topPanel.add(componentsFactory.getTextLabel("DND response:", FontStyle.REGULAR));
        topPanel.add(responseField);

        topPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3,0,3,0)));
        topPanel.setBackground(AppThemeColor.SETTINGS_BG);
        return topPanel;
    }
    @Override
    public void restore() {
        this.verticalScrollContainer.removeAll();
        this.createUI();
    }

    @Override
    public boolean processAndSave() {
        this.applicationConfig.get().setInGameDnd(enableInGameDND.isSelected());
        this.applicationConfig.get().setDndResponseText(responseField.getText());
        return true;
    }
}
