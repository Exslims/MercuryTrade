package com.mercury.platform.ui.adr.components.panel.page;


import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrMainPagePanel extends AdrPagePanel<AdrComponentDescriptor> {
    public AdrMainPagePanel() {
        super();
    }

    @Override
    protected void init() {
//        if(this.payload instanceof AdrGroupDescriptor){
//            this.add(this.componentsFactory.getTextLabel(this.payload.getTitle() + ">"), BorderLayout.PAGE_START);
//        }

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(4, 1,6,6));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        JPanel createIconsGroup = this.getButton(
                "app/adr/create_icon_group.png",
                TooltipConstants.ADR_CREATE_ICON_GROUP,
                TooltipConstants.ADR_CREATE_ICON_GROUP_DESC);
        createIconsGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrGroupDescriptor adrGroupDescriptor = new AdrGroupDescriptor();
                adrGroupDescriptor.setContentType(AdrGroupContentType.ICONS);
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(adrGroupDescriptor, AdrComponentOperations.NEW_COMPONENT,false)
                );
            }
        });
        JPanel createPbGroup = this.getButton(
                "app/adr/create_pb_group.png",
                TooltipConstants.ADR_CREATE_PB_GROUP,
                TooltipConstants.ADR_CREATE_PB_GROUP_DESC);
        createPbGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrGroupDescriptor adrGroupDescriptor = new AdrGroupDescriptor();
                adrGroupDescriptor.setContentType(AdrGroupContentType.PROGRESS_BARS);
                MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(adrGroupDescriptor, AdrComponentOperations.NEW_COMPONENT,false)
                );
            }
        });
        JPanel createIcon = this.getButton(
                "app/adr/create_icon.png",
                TooltipConstants.ADR_CREATE_ICON,
                TooltipConstants.ADR_CREATE_ICON_DESC);
        createIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrComponentDefinition definition = new AdrComponentDefinition();
                AdrIconDescriptor iconDescriptor = new AdrIconDescriptor();
                definition.setDescriptor(iconDescriptor);
                definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
                if(payload instanceof AdrGroupDescriptor) {
                    ((AdrGroupDescriptor) payload).getCells().add(iconDescriptor);
                    definition.setFromGroup(true);
                }
                MercuryStoreUI.adrComponentStateSubject.onNext(definition);
            }
        });
        JPanel createPb = this.getButton(
                "app/adr/create_pb.png",
                TooltipConstants.ADR_CREATE_PROGRESS_BAR,
                TooltipConstants.ADR_CREATE_PROGRESS_BAR_DESC);
        createPb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdrComponentDefinition definition = new AdrComponentDefinition();
                AdrProgressBarDescriptor iconDescriptor = new AdrProgressBarDescriptor();
                definition.setDescriptor(iconDescriptor);
                definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
                if(payload instanceof AdrGroupDescriptor) {
                    ((AdrGroupDescriptor) payload).getCells().add(iconDescriptor);
                    definition.setFromGroup(true);
                }
                MercuryStoreUI.adrComponentStateSubject.onNext(definition);
            }
        });
        buttonsPanel.add(createIconsGroup);
        buttonsPanel.add(createPbGroup);
        buttonsPanel.add(createIcon);
        buttonsPanel.add(createPb);
        this.add(buttonsPanel,BorderLayout.PAGE_START);
    }

    private JPanel getButton(String iconPath, String title, String innerText) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBackground(AppThemeColor.SLIDE_BG);
        JLabel iconLabel = this.componentsFactory.getIconLabel(iconPath, 96);
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK)
        ));
        iconLabel.setBackground(AppThemeColor.SLIDE_BG);
        root.add(iconLabel,BorderLayout.LINE_START);
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout());
        labelsPanel.setBackground(AppThemeColor.SLIDE_BG);
        labelsPanel.add(this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,18,title),BorderLayout.PAGE_START);
        labelsPanel.add(this.componentsFactory.getTextLabel(innerText),BorderLayout.CENTER);
        root.add(labelsPanel,BorderLayout.CENTER);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        root.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
            }
        });
        return root;
    }
}
