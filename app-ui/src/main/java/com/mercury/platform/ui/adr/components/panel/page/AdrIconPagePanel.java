package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.HotKeyManager;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrIconPagePanel extends AdrPagePanel<AdrIconDescriptor> {
    @Override
    protected void init() {
        HotKeyManager hotKeyManager = new HotKeyManager();
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
            sizeText = "Icon size(from group)";
        }
        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel(opacityLabelText);
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel hotKeyLabel = this.componentsFactory.getTextLabel("HotKey:");

        JLabel iconLabel = this.componentsFactory.getTextLabel("Icon:");
        JLabel iconTypeLabel = this.componentsFactory.getTextLabel("Icon type:");
        JLabel textEnableLabel = this.componentsFactory.getTextLabel("Text enable:");
        JLabel fontSizeLabel = this.componentsFactory.getTextLabel("Font size:");
        JLabel durationLabel = this.componentsFactory.getTextLabel("Duration:");
        JLabel invertLabel = this.componentsFactory.getTextLabel("Invert tracker:");
        JLabel animationMaskLabel = this.componentsFactory.getTextLabel("Animation mask:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.REGULAR,18);
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) this.payload.getOpacity() * 100);
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);
        if(this.fromGroup){
            opacitySlider.setEnabled(false);
        }
        JPanel iconSizePanel = this.getIconSizePanel();
        JButton hotKeyButton = hotKeyManager.getHotKeyButton(this.payload.getHotKeyDescriptor());

        JPanel iconSelectPanel = this.getIconSelectPanel();
        JComboBox iconTypeBox = this.componentsFactory.getComboBox(new String[]{"Square", "Ellipse"});
        iconTypeBox.setSelectedIndex(this.payload.getIconType().ordinal());
        JCheckBox textEnableBox = this.componentsFactory.getCheckBox(this.payload.isTextEnable());
        JFormattedTextField fontSizeField = this.componentsFactory.getIntegerTextField(10, 200, (int) this.payload.getFontSize());
        JFormattedTextField durationField = this.componentsFactory.getIntegerTextField(1, 200, (int) this.payload.getDuration());
        JCheckBox invertBox = this.componentsFactory.getCheckBox(this.payload.isInvert());
        JCheckBox animationBox = this.componentsFactory.getCheckBox(this.payload.isAnimationEnable());

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(3, 2,0,6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(8, 2,0,6));
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
        generalPanel.add(hotKeyLabel);
        generalPanel.add(hotKeyButton);

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
        specPanel.add(textEnableLabel);
        specPanel.add(textEnableBox);
        specPanel.add(invertLabel);
        specPanel.add(invertBox);
        specPanel.add(animationMaskLabel);
        specPanel.add(animationBox);

        container.add(wrapToSlide(generalPanel));
        container.add(wrapToSlide(specPanel));

        this.add(verticalContainer,BorderLayout.CENTER);
    }
    private JPanel getIconSizePanel(){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(1, 4,4,0));
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel widthLabel = this.componentsFactory.getTextLabel("Width:");
        JLabel heightLabel = this.componentsFactory.getTextLabel("Height:");
        JTextField widthField = this.componentsFactory.getIntegerTextField(10,1000,this.payload.getSize().width);
        JTextField heightField = this.componentsFactory.getIntegerTextField(10,1000,this.payload.getSize().height);
        root.add(widthLabel);
        root.add(widthField);
        root.add(heightLabel);
        root.add(heightField);

        if(this.fromGroup){
            widthField.setEnabled(false);
            heightField.setEnabled(false);
        }
        return root;
    }
    private JPanel getIconSelectPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(this.componentsFactory.getIconLabel("app/adr/"+this.payload.getIconPath()+".png",24),BorderLayout.LINE_START);
        root.add(this.componentsFactory.getTextLabel(this.payload.getIconPath()),BorderLayout.CENTER);
        JButton selectIcon = this.componentsFactory.getBorderedButton("Select");
        root.add(selectIcon,BorderLayout.LINE_END);
        return root;
    }
    private JPanel wrapToSlide(JPanel panel){
        JPanel wrapper = this.componentsFactory.getJPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        wrapper.add(panel,BorderLayout.CENTER);
        return wrapper;
    }
}
