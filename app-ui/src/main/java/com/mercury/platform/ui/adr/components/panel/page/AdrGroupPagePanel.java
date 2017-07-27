package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentOrientation;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupType;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AdrGroupPagePanel extends AdrPagePanel<AdrTrackerGroupDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");

        JLabel paddingLabel = this.componentsFactory.getTextLabel("Component's padding:");
        JLabel groupTypeLabel = this.componentsFactory.getTextLabel("Group's type:");
        String sizeText = "Icon's size:";
        if(this.payload.getContentType().equals(AdrTrackerGroupContentType.PROGRESS_BARS)){
            sizeText = "Progress bar's size:";
        }
        JLabel sizeLabel = this.componentsFactory.getTextLabel(sizeText);
        JLabel groupOrientationLabel = this.componentsFactory.getTextLabel("Group's orientation:");

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
            this.payload.setGroupType(AdrTrackerGroupType.valueOfPretty((String) groupType.getSelectedItem()));
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JPanel iconSizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload,this.fromGroup);
        JComboBox groupOrientation = this.componentsFactory.getComboBox(new String[]{"Horizontal", "Vertical"});
        groupOrientation.setSelectedIndex(this.payload.getOrientation().ordinal());
        groupOrientation.addItemListener(e -> {
            this.payload.setOrientation(AdrComponentOrientation.valueOfPretty((String) groupOrientation.getSelectedItem()));
            MercuryStoreUI.adrReloadSubject.onNext(this.payload);
        });
        JPanel generalPanel = this.componentsFactory.getJPanel(new GridLayout(3, 2,0,6));
        JPanel specPanel = this.componentsFactory.getJPanel(new GridLayout(4, 2,0,6));
        generalPanel.setBackground(AppThemeColor.ADR_BG);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER),
                BorderFactory.createEmptyBorder(4,2,4,2)));

        specPanel.setBackground(AppThemeColor.SLIDE_BG);
        specPanel.setBorder(BorderFactory.createEmptyBorder(0,0,4,2));
        generalPanel.add(titleLabel);
        generalPanel.add(titleField);
        generalPanel.add(locationLabel);
        generalPanel.add(locationPanel);
        generalPanel.add(sizeLabel);
        generalPanel.add(iconSizePanel);


        specPanel.add(opacityLabel);
        specPanel.add(opacitySlider);
        specPanel.add(groupTypeLabel);
        specPanel.add(groupType);
        specPanel.add(groupOrientationLabel);
        specPanel.add(groupOrientation);
        specPanel.add(paddingLabel);
        specPanel.add(paddingPanel);

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
        this.add(verticalContainer,BorderLayout.PAGE_START);
    }
}
