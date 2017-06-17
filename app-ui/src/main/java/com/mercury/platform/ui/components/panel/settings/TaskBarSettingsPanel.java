package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class TaskBarSettingsPanel extends ConfigurationPanel{
    private JTextField responseField;
    private JCheckBox enableInGameDND;

    public TaskBarSettingsPanel() {
        super();
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
        enableInGameDND.setSelected(ConfigManager.INSTANCE.isInGameDnd());
        responseField = componentsFactory.getTextField(ConfigManager.INSTANCE.getDndResponseText(), FontStyle.REGULAR, 16f);
        responseField.setEnabled(ConfigManager.INSTANCE.isInGameDnd());
        componentsFactory.setUpToggleCallbacks(enableInGameDND,
                () -> {
                    responseField.setEnabled(false);
                },
                () -> {
                    responseField.setEnabled(true);
                },ConfigManager.INSTANCE.isInGameDnd());

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
        ConfigManager.INSTANCE.setInGameDnd(enableInGameDND.isSelected());
        ConfigManager.INSTANCE.setDndResponseText(responseField.getText());
        return true;
    }
}
