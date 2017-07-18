package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;

import javax.swing.*;
import java.awt.*;


public class AdrGroupPagePanel extends AdrPagePanel<AdrGroupDescriptor> {
    @Override
    protected void init() {
        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel groupTitleLabel = this.componentsFactory.getTextLabel("Group type:");
        String sizeText = "Icons size:";
        if(this.payload.getContentType().equals(AdrGroupContentType.PROGRESS_BARS)){
            sizeText = "Progress bars width:";
        }
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel groupDirectionLabel = this.componentsFactory.getTextLabel("Direction:");
        JLabel groupScaleLabel = this.componentsFactory.getTextLabel("Scale:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.BOLD,16);
        JComboBox groupType = this.componentsFactory.getComboBox(new String[]{"Static", "Dynamic"});
        groupType.setSelectedIndex(this.payload.getGroupType().ordinal());
        JTextField sizeField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.BOLD,16);
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) this.payload.getOpacity() * 100);
        JComboBox groupDirection = this.componentsFactory.getComboBox(new String[]{"Horizontal", "Vertical"});
        groupDirection.setSelectedIndex(this.payload.getDirection().ordinal());
        JTextField groupScaleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.BOLD,16);
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(6, 2,0,6));
        root.setBorder(BorderFactory.createEmptyBorder(6,0,0,4));
        root.add(titleLabel);
        root.add(titleField);
        root.add(groupTitleLabel);
        root.add(groupType);
        root.add(sizeLabel);
        root.add(sizeField);
        root.add(opacityLabel);
        root.add(opacitySlider);
        root.add(groupDirectionLabel);
        root.add(groupDirection);
        root.add(groupScaleLabel);
        root.add(groupScaleField);

        this.add(root,BorderLayout.PAGE_START);
    }
}
