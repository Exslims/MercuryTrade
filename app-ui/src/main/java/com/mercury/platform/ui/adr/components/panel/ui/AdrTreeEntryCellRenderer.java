package com.mercury.platform.ui.adr.components.panel.ui;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
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
        label.setForeground(AppThemeColor.TEXT_DEFAULT);
        label.setBackground(AppThemeColor.FRAME);
        label.setFont(componentsFactory.getFont(FontStyle.REGULAR,16));
        label.setText(descriptor.getTitle());
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
                label.setPreferredSize(new Dimension(170,30));
                label.setIcon(componentsFactory.getIcon(iconPath,48));
                label.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
                break;
            }
            case ICON: {
                AdrIconDescriptor iconDescriptor = (AdrIconDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
                label.setPreferredSize(new Dimension(150, 48));
                label.setIcon(componentsFactory.getIcon("app/adr/" + iconDescriptor.getIconPath() + ".png",48));
                label.setBorder(BorderFactory.createEmptyBorder());
                break;
            }
            case PROGRESS_BAR: {
                AdrProgressBarDescriptor iconDescriptor = (AdrProgressBarDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
                label.setPreferredSize(new Dimension(150, 48));
                label.setIcon(componentsFactory.getIcon("app/adr/" + iconDescriptor.getIconPath() + ".png",48));
                label.setBorder(BorderFactory.createEmptyBorder());
                break;
            }
        }
        if(selected){
            label.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }else {
            label.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME));
        }
        return label;
    }
}
