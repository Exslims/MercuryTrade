package com.mercury.platform.ui.components.panel.settings.page;


import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.manager.HideSettingsManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GeneralSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<ApplicationDescriptor> applicationConfig;
    private ApplicationDescriptor applicationSnapshot;

    private JSlider minSlider;
    private JSlider maxSlider;

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.applicationConfig = Configuration.get().applicationConfiguration();
        this.applicationSnapshot = CloneHelper.cloneObject(this.applicationConfig.get());

        JPanel root = componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4));
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        root.setBackground(AppThemeColor.ADR_BG);

        JCheckBox checkEnable = this.componentsFactory.getCheckBox(this.applicationSnapshot.isCheckOutUpdate());
        checkEnable.addActionListener(action -> {
            this.applicationSnapshot.setCheckOutUpdate(checkEnable.isSelected());
        });
        JSlider fadeTimeSlider = this.componentsFactory.getSlider(0, 10, this.applicationSnapshot.getFadeTime(), AppThemeColor.SLIDE_BG);
        fadeTimeSlider.addChangeListener(e -> {
            this.applicationSnapshot.setFadeTime(fadeTimeSlider.getValue());
        });

        this.minSlider = this.componentsFactory.getSlider(40, 100, this.applicationSnapshot.getMinOpacity(), AppThemeColor.SLIDE_BG);
        this.minSlider.addChangeListener(e -> {
            if (!(this.minSlider.getValue() > this.maxSlider.getValue())) {
                this.applicationSnapshot.setMinOpacity(this.minSlider.getValue());
            } else {
                minSlider.setValue(minSlider.getValue() - 1);
            }
        });

        this.maxSlider = this.componentsFactory.getSlider(40, 100, this.applicationSnapshot.getMaxOpacity(), AppThemeColor.SLIDE_BG);
        this.maxSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (minSlider.getValue() > maxSlider.getValue()) {
                    minSlider.setValue(maxSlider.getValue());
                }
                applicationSnapshot.setMaxOpacity(maxSlider.getValue());
            }
        });

        JComboBox notifierStatusPicker = this.componentsFactory.getComboBox(new String[]{"Always play a sound", "Only when tabbed out", "Never"});
        notifierStatusPicker.setSelectedItem(this.applicationSnapshot.getNotifierStatus().asPretty());
        notifierStatusPicker.addActionListener(action -> {
            this.applicationSnapshot.setNotifierStatus(WhisperNotifierStatus.valueOfPretty((String) notifierStatusPicker.getSelectedItem()));
        });

        JTextField gamePathField = this.componentsFactory.getTextField(this.applicationSnapshot.getGamePath());
        gamePathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2)
        ));
        gamePathField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                applicationSnapshot.setGamePath(gamePathField.getText());
            }
        });

        JPanel poeFolderPanel = componentsFactory.getTransparentPanel(new BorderLayout(4, 4));
        poeFolderPanel.add(gamePathField, BorderLayout.CENTER);
        JButton changeButton = this.componentsFactory.getBorderedButton("Change");
        poeFolderPanel.add(changeButton, BorderLayout.LINE_END);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        changeButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                gamePathField.setText(fileChooser.getSelectedFile().getPath());
                applicationSnapshot.setGamePath(fileChooser.getSelectedFile().getPath());
            }
        });

        root.add(this.componentsFactory.getTextLabel("Notify me when an update is available", FontStyle.REGULAR, 16));
        root.add(checkEnable);
        root.add(this.componentsFactory.getTextLabel("Component fade out time: ", FontStyle.REGULAR, 16));
        root.add(fadeTimeSlider);
        root.add(this.componentsFactory.getTextLabel("Min opacity: ", FontStyle.REGULAR, 16));
        root.add(this.minSlider);
        root.add(this.componentsFactory.getTextLabel("Max opacity: ", FontStyle.REGULAR, 16));
        root.add(this.maxSlider);
        root.add(this.componentsFactory.getTextLabel("Notification sound alerts: ", FontStyle.REGULAR, 16));
        root.add(this.componentsFactory.wrapToSlide(notifierStatusPicker, AppThemeColor.ADR_BG, 0, 0, 0, 2));
        root.add(this.componentsFactory.getTextLabel("Path of Exile folder: ", FontStyle.REGULAR, 16));
        root.add(this.componentsFactory.wrapToSlide(poeFolderPanel, AppThemeColor.ADR_BG, 0, 0, 2, 2));

        this.container.add(this.componentsFactory.wrapToSlide(root));
    }

    @Override
    public void onSave() {
        HideSettingsManager.INSTANCE.apply(applicationSnapshot.getFadeTime(), applicationSnapshot.getMinOpacity(), applicationSnapshot.getMaxOpacity());
        this.applicationConfig.set(CloneHelper.cloneObject(this.applicationSnapshot));
    }

    @Override
    public void restore() {
        this.applicationSnapshot = CloneHelper.cloneObject(this.applicationConfig.get());
        this.removeAll();
        this.onViewInit();
    }
}
