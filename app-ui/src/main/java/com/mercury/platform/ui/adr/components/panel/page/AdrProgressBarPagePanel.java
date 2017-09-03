package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrProgressBarPagePanel extends AdrPagePanel<AdrProgressBarDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");
        JLabel sizeLabel = this.componentsFactory.getTextLabel("Progress bar size:");
        JLabel pbOrientationLabel = this.componentsFactory.getTextLabel("Orientation:");
        JLabel hotKeyLabel = this.componentsFactory.getTextLabel("HotKey:");
        JLabel iconLabel = this.componentsFactory.getTextLabel("Icon:");

        JLabel insetsLabel = this.componentsFactory.getTextLabel("Insets:");
        JLabel iconAlignmentLabel = this.componentsFactory.getTextLabel("Icon alignment:");
        JLabel textFormatLabel = this.componentsFactory.getTextLabel("Text format:");
        JLabel textOutlineLabel = this.componentsFactory.getTextLabel("Text outline:");
        JLabel fontSizeLabel = this.componentsFactory.getTextLabel("Font size:");
        JLabel durationLabel = this.componentsFactory.getTextLabel("Duration:");
        JLabel soundLabel = this.componentsFactory.getTextLabel("Sound alert:");
        JLabel soundVolumeLabel = this.componentsFactory.getTextLabel("Sound volume");
        JLabel delayLabel = this.componentsFactory.getTextLabel("Delay:");
        JLabel invertTimerLabel = this.componentsFactory.getTextLabel("Invert timer:");
        JLabel textColorLabel = this.componentsFactory.getTextLabel("Text color:");
        JLabel backgroundColorLabel = this.componentsFactory.getTextLabel("Background color:");
        JLabel foregroundColorLabel = this.componentsFactory.getTextLabel("Foreground color:");
        JLabel borderColorLabel = this.componentsFactory.getTextLabel("Border color:");
        JLabel invertMaskLabel = this.componentsFactory.getTextLabel("Invert mask:");

        JTextField titleField = this.adrComponentsFactory.getTitleField(this.payload);
        JSlider opacitySlider = this.adrComponentsFactory.getOpacitySlider(this.payload);
        JPanel sizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload, this.fromGroup);
        JComboBox pbOrientation = this.adrComponentsFactory.getPbOrientationBox(this.payload);
        JPanel locationPanel = this.adrComponentsFactory.getLocationPanel(this.payload, this.fromGroup);
        JPanel hotKeyPanel = this.adrComponentsFactory.getHotKeyPanel(this.payload);
        JPanel iconPanel = this.adrComponentsFactory.getIconPanel(this.payload);
        JPanel soundPanel = this.adrComponentsFactory.getSoundPanel(this.payload);
        JSlider soundVolumeSlider = this.adrComponentsFactory.getVolumeSlider(this.payload);
        JPanel insetsPanel = this.adrComponentsFactory.getInsetsPanel(this.payload);
        JComboBox iconAlignment = this.adrComponentsFactory.getIconAlignment(this.payload);
        JTextField fontSizeField = this.adrComponentsFactory.getFontSizeField(this.payload);
        JPanel textOutlinePanel = this.adrComponentsFactory.getTextOutlinePanel(this.payload);
        JTextField durationField = this.adrComponentsFactory.getDurationField(this.payload);
        JTextField delayField = this.adrComponentsFactory.getDelayField(this.payload);
        JComboBox textFormatBox = this.adrComponentsFactory.getTextFormatBox(this.payload);
        JCheckBox invertTimerBox = this.adrComponentsFactory.getInvertTimerBox(this.payload);
        JCheckBox invertMaskBox = this.adrComponentsFactory.getInvertMaskBox(this.payload);
        JPanel backgroundColorPanel = this.adrComponentsFactory.getBackgroundColorPanel(this.payload);
        JPanel foregroundColorPanel = this.adrComponentsFactory.getForegroundColorPanel(this.payload);
        JPanel borderColorPanel = this.adrComponentsFactory.getBorderColorPanel(this.payload);
        JPanel textColorPanel = this.adrComponentsFactory.getExTextColorPanel(this.payload);

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 0, 6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 0, 6));
        generalPanel.setBackground(AppThemeColor.ADR_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4, 2, 4, 2)));

        specPanel.setBackground(AppThemeColor.ADR_BG);
        specPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 2));

        generalPanel.add(titleLabel);
        generalPanel.add(titleField);
        if (!this.fromGroup) {
            generalPanel.add(locationLabel);
            generalPanel.add(locationPanel);
            generalPanel.add(sizeLabel);
            generalPanel.add(sizePanel);
        }
        generalPanel.add(hotKeyLabel);
        generalPanel.add(hotKeyPanel);
        generalPanel.add(iconLabel);
        generalPanel.add(iconPanel);
        generalPanel.add(durationLabel);
        generalPanel.add(durationField);

        if (!this.fromGroup) {
            specPanel.add(opacityLabel);
            specPanel.add(opacitySlider);
        }
        specPanel.add(soundLabel);
        specPanel.add(soundPanel);
        specPanel.add(soundVolumeLabel);
        specPanel.add(soundVolumeSlider);
        specPanel.add(delayLabel);
        specPanel.add(delayField);
        specPanel.add(pbOrientationLabel);
        specPanel.add(pbOrientation);
        specPanel.add(iconAlignmentLabel);
        specPanel.add(iconAlignment);
        specPanel.add(backgroundColorLabel);
        specPanel.add(backgroundColorPanel);
        specPanel.add(foregroundColorLabel);
        specPanel.add(foregroundColorPanel);
        specPanel.add(textColorLabel);
        specPanel.add(textColorPanel);
        specPanel.add(textOutlineLabel);
        specPanel.add(textOutlinePanel);
        specPanel.add(insetsLabel);
        specPanel.add(insetsPanel);
        specPanel.add(fontSizeLabel);
        specPanel.add(fontSizeField);
        specPanel.add(borderColorLabel);
        specPanel.add(borderColorPanel);
        specPanel.add(textFormatLabel);
        specPanel.add(textFormatBox);
        specPanel.add(invertTimerLabel);
        specPanel.add(invertTimerBox);
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
