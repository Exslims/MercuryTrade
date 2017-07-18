package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class AdrGroupPagePanel extends AdrPagePanel<AdrGroupDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");

        JLabel groupTypeLabel = this.componentsFactory.getTextLabel("Group type:");
        String sizeText = "Icons size:";
        if(this.payload.getContentType().equals(AdrGroupContentType.PROGRESS_BARS)){
            sizeText = "Progress bars width:";
        }
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel groupDirectionLabel = this.componentsFactory.getTextLabel("Direction:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.BOLD,16);
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) this.payload.getOpacity() * 100);
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);

        JComboBox groupType = this.componentsFactory.getComboBox(new String[]{"Static", "Dynamic"});
        groupType.setSelectedIndex(this.payload.getGroupType().ordinal());
        JPanel iconSizePanel = this.getIconSizePanel();
        JComboBox groupDirection = this.componentsFactory.getComboBox(new String[]{"Horizontal", "Vertical"});
        groupDirection.setSelectedIndex(this.payload.getDirection().ordinal());

        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(2, 2,0,6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(3, 2,0,6));
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


        specPanel.add(groupTypeLabel);
        specPanel.add(groupType);
        specPanel.add(groupDirectionLabel);
        specPanel.add(groupDirection);
        specPanel.add(sizeLabel);
        specPanel.add(iconSizePanel);

        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.add(this.componentsFactory.wrapToSlide(specPanel));
        this.add(verticalContainer,BorderLayout.PAGE_START);
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
        return root;
    }
}
