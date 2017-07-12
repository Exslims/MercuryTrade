package com.mercury.platform.ui.adr.components.panel.ui;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdrTreeEntryCellRenderer implements TreeCellRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = new JLabel();
        if(((DefaultMutableTreeNode)value).getUserObject() instanceof String) {
            return label;
        }
        AdrComponentDescriptor descriptor = (AdrComponentDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
        switch (descriptor.getType()){
            case GROUP: {
                String iconPath = "app/adr/static_group_icon.png";
                switch (((AdrGroupDescriptor)descriptor).getGroupType()){
                    case STATIC: {
                        iconPath = "app/adr/static_group_icon.png";
                        break;
                    }
                    case DYNAMIC: {
                        iconPath = "app/adr/dynamic_group_icon.png";
                        break;
                    }
                }
                label.setIcon(componentsFactory.getImage(iconPath));
                label.setForeground(AppThemeColor.TEXT_DEFAULT);
                label.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
                label.setFont(componentsFactory.getFont(FontStyle.REGULAR,20));
                label.setBackground(AppThemeColor.FRAME);
                break;
            }
            case ICON: {
                AdrIconDescriptor iconDescriptor = (AdrIconDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
                label.setIcon(componentsFactory.getIcon("app/adr/" + iconDescriptor.getIconPath() + ".png",48));
                label.setForeground(AppThemeColor.TEXT_DEFAULT);
                label.setBorder(BorderFactory.createEmptyBorder());
                label.setBackground(AppThemeColor.FRAME);
                break;
            }
            case PROGRESS_BAR: {
                break;
            }
        }
        if(selected){
            label.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }else {
            label.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME));
        }
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("qweqwe");
            }
        });
        return label;
    }
}
