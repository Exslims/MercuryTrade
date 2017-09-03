package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrIconPagePanel extends AdrPagePanel<AdrIconDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel sizeLabel = this.componentsFactory.getTextLabel("Icon size:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");
        JLabel alwaysVisibleLabel = this.componentsFactory.getTextLabel("Always visible:");
        JLabel hotKeyLabel = this.componentsFactory.getTextLabel("HotKey:");
        JLabel iconLabel = this.componentsFactory.getTextLabel("Icon:");

        JLabel textFormatLabel = this.componentsFactory.getTextLabel("Text format:");
        JLabel textOutlineLabel = this.componentsFactory.getTextLabel("Text outline:");
        JLabel fontSizeLabel = this.componentsFactory.getTextLabel("Font size:");
        JLabel invertTimerLabel = this.componentsFactory.getTextLabel("Invert timer:");
        JLabel durationLabel = this.componentsFactory.getTextLabel("Duration:");
        JLabel soundLabel = this.componentsFactory.getTextLabel("Sound alert:");
        JLabel soundVolumeLabel = this.componentsFactory.getTextLabel("Sound volume");
        JLabel delayLabel = this.componentsFactory.getTextLabel("Delay:");
        JLabel backgroundColorLabel = this.componentsFactory.getTextLabel("Background color:");
        JLabel textColorLabel = this.componentsFactory.getTextLabel("Text color:");
        JLabel borderColorLabel = this.componentsFactory.getTextLabel("Border color:");
        JLabel animationMaskLabel = this.componentsFactory.getTextLabel("Animation mask:");
        JLabel invertMaskLabel = this.componentsFactory.getTextLabel("Invert mask:");
        JLabel maskColorLabel = this.componentsFactory.getTextLabel("Mask color:");

        JTextField titleField = this.adrComponentsFactory.getTitleField(this.payload);
        JSlider opacitySlider = this.adrComponentsFactory.getOpacitySlider(this.payload);
        JPanel iconSizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload, this.fromGroup);
        JPanel hotKeyPanel = this.adrComponentsFactory.getHotKeyPanel(this.payload);
        JPanel locationPanel = this.adrComponentsFactory.getLocationPanel(this.payload, this.fromGroup);
        JPanel iconSelectPanel = this.adrComponentsFactory.getIconPanel(this.payload);
        JTextField fontSizeField = this.adrComponentsFactory.getFontSizeField(this.payload);
        JPanel textOutlinePanel = this.adrComponentsFactory.getTextOutlinePanel(this.payload);
        JTextField durationField = this.adrComponentsFactory.getDurationField(this.payload);
        JPanel soundPanel = this.adrComponentsFactory.getSoundPanel(this.payload);
        JSlider soundVolumeSlider = this.adrComponentsFactory.getVolumeSlider(this.payload);
        JTextField delayField = this.adrComponentsFactory.getDelayField(this.payload);
        JComboBox textFormatBox = this.adrComponentsFactory.getTextFormatBox(this.payload);
        JPanel backgroundColorPanel = this.adrComponentsFactory.getBackgroundColorPanel(this.payload);
        JCheckBox alwaysVisibleBox = this.adrComponentsFactory.getAlwaysVisibleBox(this.payload);
        JCheckBox invertTimerBox = this.adrComponentsFactory.getInvertTimerBox(this.payload);
        JCheckBox invertMaskBox = this.adrComponentsFactory.getInvertMaskBox(this.payload);
        JCheckBox animationBox = this.adrComponentsFactory.getMaskEnableBox(this.payload);
        JPanel textColorPanel = this.adrComponentsFactory.getExTextColorPanel(this.payload);
        JPanel maskColorPanel = this.adrComponentsFactory.getForegroundColorPanel(this.payload);

        JPanel borderColorPanel = this.adrComponentsFactory.getBorderColorPanel(this.payload);

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 0, 6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 0, 6));
        generalPanel.setBackground(AppThemeColor.SLIDE_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4, 2, 4, 2)));

        specPanel.setBackground(AppThemeColor.SLIDE_BG);
        specPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 2));

        generalPanel.add(titleLabel);
        generalPanel.add(titleField);
        if (!this.fromGroup) {
            generalPanel.add(locationLabel);
            generalPanel.add(locationPanel);
            generalPanel.add(sizeLabel);
            generalPanel.add(iconSizePanel);
        }
        generalPanel.add(hotKeyLabel);
        generalPanel.add(hotKeyPanel);
        generalPanel.add(iconLabel);
        generalPanel.add(iconSelectPanel);
        generalPanel.add(durationLabel);
        generalPanel.add(durationField);

        if (!this.fromGroup) {
            specPanel.add(opacityLabel);
            specPanel.add(opacitySlider);
        }
        specPanel.add(alwaysVisibleLabel);
        specPanel.add(alwaysVisibleBox);
        specPanel.add(soundLabel);
        specPanel.add(soundPanel);
        specPanel.add(soundVolumeLabel);
        specPanel.add(soundVolumeSlider);
        specPanel.add(delayLabel);
        specPanel.add(delayField);
        specPanel.add(fontSizeLabel);
        specPanel.add(fontSizeField);
        specPanel.add(textFormatLabel);
        specPanel.add(textFormatBox);
        specPanel.add(textColorLabel);
        specPanel.add(textColorPanel);
        specPanel.add(textOutlineLabel);
        specPanel.add(textOutlinePanel);
        specPanel.add(borderColorLabel);
        specPanel.add(borderColorPanel);
        specPanel.add(backgroundColorLabel);
        specPanel.add(backgroundColorPanel);
        specPanel.add(maskColorLabel);
        specPanel.add(maskColorPanel);
        specPanel.add(invertTimerLabel);
        specPanel.add(invertTimerBox);
        specPanel.add(animationMaskLabel);
        specPanel.add(animationBox);
        specPanel.add(invertMaskLabel);
        specPanel.add(invertMaskBox);

        specPanel.setVisible(this.advancedExpanded);

        specPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                advancedExpanded = specPanel.isVisible();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                advancedExpanded = specPanel.isVisible();
            }
        });

        JPanel advancedPanel = this.adrComponentsFactory.getCounterPanel(specPanel, "Advanced:", AppThemeColor.ADR_BG, this.advancedExpanded);
        advancedPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));

        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.add(this.componentsFactory.wrapToSlide(advancedPanel));
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
        this.add(verticalContainer, BorderLayout.CENTER);
    }

}
