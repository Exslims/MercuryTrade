package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.validator.DoubleFieldValidator;
import com.mercury.platform.ui.adr.validator.DoubleFormatFieldValidator;
import com.mercury.platform.ui.adr.validator.IntegerFieldValidator;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AdrIconPagePanel extends AdrPagePanel<AdrIconDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel sizeLabel = this.componentsFactory.getTextLabel("Icon size:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");

        JLabel hotKeyLabel = this.componentsFactory.getTextLabel("HotKey:");
        JLabel iconLabel = this.componentsFactory.getTextLabel("Icon:");
        JLabel textFormatLabel = this.componentsFactory.getTextLabel("Text format:");
        JLabel fontSizeLabel = this.componentsFactory.getTextLabel("Font size:");
        JLabel durationLabel = this.componentsFactory.getTextLabel("Duration:");
        JLabel textColorLabel = this.componentsFactory.getTextLabel("Text color:");
        JLabel borderColorLabel = this.componentsFactory.getTextLabel("Border color:");
        JLabel invertLabel = this.componentsFactory.getTextLabel("Invert tracker:");
        JLabel animationMaskLabel = this.componentsFactory.getTextLabel("Animation mask:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.REGULAR,18);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                payload.setTitle(titleField.getText());
                MercuryStoreUI.adrReloadSubject.onNext(payload);
            }
        });
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) (this.payload.getOpacity() * 100));
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);
        if(this.fromGroup){
            opacitySlider.setEnabled(false);
        }
        opacitySlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                payload.setOpacity(opacitySlider.getValue() / 100f);
                MercuryStoreUI.adrReloadSubject.onNext(payload);
            }
        });
        JPanel iconSizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload,this.fromGroup);
        JButton hotKeyButton = this.adrComponentsFactory.getHotKeyButton(this.payload.getHotKeyDescriptor());
        JPanel locationPanel = this.adrComponentsFactory.getLocationPanel(this.payload, this.fromGroup);
        JPanel iconSelectPanel = this.adrComponentsFactory.getIconSelectPanel(this.payload);
        JTextField fontSizeField = this.adrComponentsFactory.getSmartField(this.payload.getFontSize(), new IntegerFieldValidator(4, 1000), value -> {
            this.payload.setFontSize(value);
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JTextField durationField = this.adrComponentsFactory.getSmartField(this.payload.getDuration(), new DoubleFieldValidator(0.1, 1000.0), value -> {
            this.payload.setDuration(value);
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JTextField textFormatField = this.adrComponentsFactory.getSmartField(this.payload.getTextFormat(), new DoubleFormatFieldValidator(), value -> {
            this.payload.setTextFormat(value);
        });
        JCheckBox invertBox = this.componentsFactory.getCheckBox(this.payload.isInvert());
        JCheckBox animationBox = this.componentsFactory.getCheckBox(this.payload.isAnimationEnable());
        animationBox.addActionListener(e ->
                this.payload.setAnimationEnable(animationBox.isSelected()));
        JPanel textColorPanel = this.adrComponentsFactory.getTextColorPanel(this.payload);
        JPanel textPanel = this.componentsFactory.getJPanel(new BorderLayout());
        textPanel.setBackground(AppThemeColor.SLIDE_BG);
        JCheckBox textEnableBox = this.componentsFactory.getCheckBox(this.payload.isTextEnable(),"Is text enable?");
        textEnableBox.addActionListener(state -> this.payload.setTextEnable(textEnableBox.isSelected()));
        textPanel.add(textEnableBox,BorderLayout.LINE_START);
        textPanel.add(textColorPanel,BorderLayout.CENTER);

        JPanel borderColorPanel = this.adrComponentsFactory.getBorderColorPanel(
                this.payload,
                color -> this.payload.setBorderColor(color));

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(this.fromGroup? 4 : 6, 2,0,6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(this.fromGroup? 6 : 7, 2,0,6));
        generalPanel.setBackground(AppThemeColor.SLIDE_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK),
                BorderFactory.createEmptyBorder(4,2,4,2)));

        specPanel.setBackground(AppThemeColor.SLIDE_BG);
        specPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));

        generalPanel.add(titleLabel);
        generalPanel.add(titleField);
        if(!this.fromGroup) {
            generalPanel.add(locationLabel);
            generalPanel.add(locationPanel);
            generalPanel.add(sizeLabel);
            generalPanel.add(iconSizePanel);
        }
        generalPanel.add(hotKeyLabel);
        generalPanel.add(hotKeyButton);
        generalPanel.add(iconLabel);
        generalPanel.add(iconSelectPanel);
        generalPanel.add(durationLabel);
        generalPanel.add(durationField);

        if(!this.fromGroup) {
            specPanel.add(opacityLabel);
            specPanel.add(opacitySlider);
        }
        specPanel.add(textColorLabel);
        specPanel.add(textPanel);
        specPanel.add(fontSizeLabel);
        specPanel.add(fontSizeField);
        specPanel.add(textFormatLabel);
        specPanel.add(textFormatField);
        specPanel.add(borderColorLabel);
        specPanel.add(borderColorPanel);
        specPanel.add(invertLabel);
        specPanel.add(invertBox);
        specPanel.add(animationMaskLabel);
        specPanel.add(animationBox);

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

        JPanel advancedPanel = this.adrComponentsFactory.getCounterPanel(specPanel, "Advanced:", AppThemeColor.ADR_BG,this.advancedExpanded);
        advancedPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));

        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.add(this.componentsFactory.wrapToSlide(advancedPanel));
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
        this.add(verticalContainer,BorderLayout.CENTER);
    }

}
