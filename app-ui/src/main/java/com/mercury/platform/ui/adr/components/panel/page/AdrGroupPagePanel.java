package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentOrientation;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupType;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AdrGroupPagePanel extends AdrPagePanel<AdrGroupDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");

        JLabel paddingLabel = this.componentsFactory.getTextLabel("Components padding:");
        JLabel groupTypeLabel = this.componentsFactory.getTextLabel("Group type:");
        String sizeText = "Icons size:";
        if(this.payload.getContentType().equals(AdrGroupContentType.PROGRESS_BARS)){
            sizeText = "Progress bars width:";
        }
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel groupDirectionLabel = this.componentsFactory.getTextLabel("Direction:");

        JTextField titleField = this.componentsFactory.getTextField(this.payload.getTitle(), FontStyle.BOLD,16);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                payload.setTitle(titleField.getText());
                MercuryStoreUI.adrReloadSubject.onNext(payload);
            }
        });
        JPanel locationPanel = this.adrComponentsFactory.getLocationPanel(this.payload, this.fromGroup);
        JPanel paddingPanel = this.adrComponentsFactory.getGapPanel(this.payload);
        JSlider opacitySlider = this.componentsFactory.getSlider(20,100, (int) (this.payload.getOpacity() * 100));
        opacitySlider.setBackground(AppThemeColor.SLIDE_BG);
        opacitySlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                payload.setOpacity(opacitySlider.getValue() / 100f);
                payload.getCells().forEach(item -> item.setOpacity(payload.getOpacity()));
                MercuryStoreUI.adrReloadSubject.onNext(payload);
            }
        });

        JComboBox groupType = this.componentsFactory.getComboBox(new String[]{"Dynamic","Static"});
        groupType.setSelectedIndex(this.payload.getGroupType().ordinal());
        groupType.addItemListener(e -> {
            this.payload.setGroupType(AdrGroupType.valueOfPretty((String) groupType.getSelectedItem()));
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JPanel iconSizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload,this.fromGroup);
        JComboBox groupOrientation = this.componentsFactory.getComboBox(new String[]{"Horizontal", "Vertical"});
        groupOrientation.setSelectedIndex(this.payload.getOrientation().ordinal());
        groupOrientation.addItemListener(e -> {
            this.payload.setOrientation(AdrComponentOrientation.valueOfPretty((String) groupOrientation.getSelectedItem()));
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(4, 2,0,6));
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
        generalPanel.add(locationLabel);
        generalPanel.add(locationPanel);
        generalPanel.add(paddingLabel);
        generalPanel.add(paddingPanel);


        specPanel.add(groupTypeLabel);
        specPanel.add(groupType);
        specPanel.add(groupDirectionLabel);
        specPanel.add(groupOrientation);
        specPanel.add(sizeLabel);
        specPanel.add(iconSizePanel);

        container.add(this.componentsFactory.wrapToSlide(generalPanel));
        container.add(this.componentsFactory.wrapToSlide(specPanel));
        this.add(verticalContainer,BorderLayout.PAGE_START);
    }
}
