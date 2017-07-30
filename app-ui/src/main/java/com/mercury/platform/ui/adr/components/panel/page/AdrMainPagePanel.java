package com.mercury.platform.ui.adr.components.panel.page;


import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.dialog.AdrImportDialog;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrMainPagePanel extends AdrPagePanel<AdrComponentDescriptor> {
    private AdrConfigurationService config;
    public AdrMainPagePanel(AdrConfigurationService config) {
        super();
        this.config = config;
    }

    @Override
    protected void init() {
        if(this.fromGroup){
            JLabel header = this.componentsFactory.getTextLabel(this.payload.getTitle() + " >", FontStyle.REGULAR, 20);
            header.setBorder(BorderFactory.createEmptyBorder(4,6,0,4));
            this.add(header, BorderLayout.PAGE_START);
        }
        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.FRAME_RGB);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        JPanel createIconsGroup = this.getButton(
                "app/adr/create_icon_group.png",
                TooltipConstants.ADR_CREATE_ICON_GROUP);
        createIconsGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(config.getDefaultIconGroup(), AdrComponentOperations.NEW_COMPONENT,payload)
                );
            }
        });
        JPanel createPbGroup = this.getButton(
                "app/adr/create_pb_group.png",
                TooltipConstants.ADR_CREATE_PB_GROUP);
        createPbGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(config.getDefaultPBGroup(), AdrComponentOperations.NEW_COMPONENT,payload)
                );
            }
        });
        JPanel createIcon = this.getButton(
                "app/adr/create_icon.png",
                TooltipConstants.ADR_CREATE_ICON);
        createIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrComponentDefinition definition = new AdrComponentDefinition();
                AdrIconDescriptor defaultIcon = config.getDefaultIcon();
                definition.setDescriptor(defaultIcon);
                definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
                definition.setParent(payload);
                MercuryStoreUI.adrComponentStateSubject.onNext(definition);
            }
        });
        JPanel createPb = this.getButton(
                "app/adr/create_pb.png",
                TooltipConstants.ADR_CREATE_PROGRESS_BAR);
        createPb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrComponentDefinition definition = new AdrComponentDefinition();
                AdrProgressBarDescriptor iconDescriptor = config.getDefaultProgressBar();
                definition.setDescriptor(iconDescriptor);
                definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
                definition.setParent(payload);
                MercuryStoreUI.adrComponentStateSubject.onNext(definition);
            }
        });
        JPanel importButton = this.getButton(
                "app/adr/import_icon.png",
                TooltipConstants.ADR_IMPORT_COMPONENT);
        importButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdrImportDialog(importButton).setVisible(true);
            }
        });
        if(this.fromGroup) {
            switch (((AdrTrackerGroupDescriptor) this.payload).getContentType()){
                case ICONS:{
                    container.add(this.componentsFactory.wrapToSlide(createIcon));
                    break;
                }
                case PROGRESS_BARS:{
                    container.add(this.componentsFactory.wrapToSlide(createPb));
                    break;
                }
            }
        }else {
            container.add(this.componentsFactory.wrapToSlide(createIcon));
            container.add(this.componentsFactory.wrapToSlide(createPb));
        }
        if(!this.fromGroup) {
            container.add(this.componentsFactory.wrapToSlide(createIconsGroup));
            container.add(this.componentsFactory.wrapToSlide(createPbGroup));
        }
        container.add(this.componentsFactory.wrapToSlide(importButton));
        this.add(verticalContainer,BorderLayout.CENTER);
    }

    private JPanel getButton(String iconPath, String title) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel iconLabel = this.componentsFactory.getIconLabel(iconPath, 72);
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK)
        ));
        iconLabel.setBackground(AppThemeColor.FRAME_RGB);
        root.add(iconLabel,BorderLayout.LINE_START);
        root.add(this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,20,title),BorderLayout.CENTER);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        root.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
                root.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
                root.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return root;
    }
}
