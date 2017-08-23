package com.mercury.platform.ui.adr.components.panel.page;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrCapturePagePanel extends AdrPagePanel<AdrCaptureDescriptor> {
    @Override
    protected void init() {
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JLabel titleLabel = this.componentsFactory.getTextLabel("Title:");
        JLabel opacityLabel = this.componentsFactory.getTextLabel("Opacity:");
        JLabel sizeLabel = this.componentsFactory.getTextLabel("Size:");
        JLabel locationLabel = this.componentsFactory.getTextLabel("Location:");
        JLabel sectorSizeLabel = this.componentsFactory.getTextLabel("Sector size:");
        JLabel sectorLocationLabel = this.componentsFactory.getTextLabel("Sector location:");
        JLabel fpsLabel = this.componentsFactory.getTextLabel("Fps: high value can cause a drop fps");

        JTextField titleField = this.adrComponentsFactory.getTitleField(this.payload);
        JSlider opacitySlider = this.adrComponentsFactory.getOpacitySlider(this.payload);
        JPanel componentSizePanel = this.adrComponentsFactory.getComponentSizePanel(this.payload, false);
        JPanel locationPanel = this.adrComponentsFactory.getLocationPanel(this.payload, false);
        JPanel sectorSizePanel = this.adrComponentsFactory.getCaptureSizePanel(this.payload);
        JPanel sectorLocationPanel = this.adrComponentsFactory.getCaptureLocationPanel(this.payload);
        JPanel fpsPanel = this.adrComponentsFactory.getFpsSliderPanel(this.payload);

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
        generalPanel.add(sizeLabel);
        generalPanel.add(componentSizePanel);
        generalPanel.add(sectorSizeLabel);
        generalPanel.add(sectorSizePanel);
        generalPanel.add(locationLabel);
        generalPanel.add(locationPanel);
        generalPanel.add(sectorLocationLabel);
        generalPanel.add(sectorLocationPanel);
        generalPanel.add(fpsLabel);
        generalPanel.add(fpsPanel);

        generalPanel.add(opacityLabel);
        generalPanel.add(opacitySlider);

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
//        container.add(this.componentsFactory.wrapToSlide(advancedPanel));
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });
        this.add(verticalContainer, BorderLayout.CENTER);
    }
}
