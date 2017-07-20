package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrIconPagePanel extends AdrPagePanel<AdrIconDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        String opacityLabelText = "Opacity:";
        if(this.fromGroup){
            opacityLabelText = "Opacity(from group):";
        }
        String sizeText = "Icon size:";
        if(this.fromGroup){
            sizeText = "Icon size(from group):";
        }
        String locationText = "Location:";
        if(this.fromGroup){
            locationText = "Location(from group):";
        }
        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel(opacityLabelText);
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel locationLabel = this.componentsFactory.getTextLabel(locationText);

        JLabel hotKeyLabel = this.componentsFactory.getTextLabel("HotKey:");
        JLabel iconLabel = this.componentsFactory.getTextLabel("Icon:");
        JLabel iconTypeLabel = this.componentsFactory.getTextLabel("Icon type:");
        JLabel textEnableLabel = this.componentsFactory.getTextLabel("Text enable:");
        JLabel fontSizeLabel = this.componentsFactory.getTextLabel("Font size:");
        JLabel durationLabel = this.componentsFactory.getTextLabel("Duration:");
        JLabel colorLabel = this.componentsFactory.getTextLabel("Text color:");
        JLabel invertLabel = this.componentsFactory.getTextLabel("Invert tracker:");
        JLabel animationMaskLabel = this.componentsFactory.getTextLabel("Animation mask:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.REGULAR,18);
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) this.payload.getOpacity() * 100);
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);
        if(this.fromGroup){
            opacitySlider.setEnabled(false);
        }
        JPanel iconSizePanel = this.adrComponentsFactory.getIconSizePanel(this.payload,this.fromGroup);
        JButton hotKeyButton = this.adrComponentsFactory.getHotKeyButton(this.payload.getHotKeyDescriptor());

        JPanel iconSelectPanel = this.adrComponentsFactory.getIconSelectPanel(this.payload);
        JComboBox iconTypeBox = this.componentsFactory.getComboBox(new String[]{"Square", "Ellipse"});
        iconTypeBox.setSelectedIndex(this.payload.getIconType().ordinal());
        JCheckBox textEnableBox = this.componentsFactory.getCheckBox(this.payload.isTextEnable());
        JFormattedTextField fontSizeField = this.componentsFactory.getIntegerTextField(10, 200, (int) this.payload.getFontSize());
        JFormattedTextField durationField = this.componentsFactory.getIntegerTextField(1, 200, (int) this.payload.getDuration());
        JCheckBox invertBox = this.componentsFactory.getCheckBox(this.payload.isInvert());
        JCheckBox animationBox = this.componentsFactory.getCheckBox(this.payload.isAnimationEnable());
        JPanel textColorPanel = this.adrComponentsFactory.getTextColorPanel(this.payload);

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(3, 2,0,6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(10, 2,0,6));
        generalPanel.setBackground(AppThemeColor.SLIDE_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4,2,4,2)));

        specPanel.setBackground(AppThemeColor.SLIDE_BG);
        specPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4,2,4,2)));

        generalPanel.add(titleLabel);
        generalPanel.add(titleField);
        generalPanel.add(opacityLabel);
        generalPanel.add(opacitySlider);
        generalPanel.add(locationLabel);
        generalPanel.add(this.adrComponentsFactory.getLocationPanel(this.payload,this.fromGroup));

        specPanel.add(hotKeyLabel);
        specPanel.add(hotKeyButton);
        specPanel.add(sizeLabel);
        specPanel.add(iconSizePanel);
        specPanel.add(iconLabel);
        specPanel.add(iconSelectPanel);
        specPanel.add(iconTypeLabel);
        specPanel.add(iconTypeBox);
        specPanel.add(fontSizeLabel);
        specPanel.add(fontSizeField);
        specPanel.add(durationLabel);
        specPanel.add(durationField);
        specPanel.add(colorLabel);
        specPanel.add(textColorPanel);
        specPanel.add(textEnableLabel);
        specPanel.add(textEnableBox);
        specPanel.add(invertLabel);
        specPanel.add(invertBox);
        specPanel.add(animationMaskLabel);
        specPanel.add(animationBox);

        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.add(this.componentsFactory.wrapToSlide(specPanel));

        this.add(verticalContainer,BorderLayout.CENTER);
    }
}
