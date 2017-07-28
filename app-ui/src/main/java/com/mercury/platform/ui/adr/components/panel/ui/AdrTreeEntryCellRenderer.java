package com.mercury.platform.ui.adr.components.panel.ui;


import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class AdrTreeEntryCellRenderer implements TreeCellRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        if(((DefaultMutableTreeNode)value).getUserObject() instanceof String) {
            return root;
        }
        AdrComponentDescriptor descriptor = (AdrComponentDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
        if(descriptor instanceof AdrProgressBarDescriptor){
            MercuryTracker tracker = new MercuryTracker((AdrDurationComponentDescriptor) descriptor);
            tracker.setValue((int) ((((AdrProgressBarDescriptor) descriptor).getDuration()/2) * 1000));
            tracker.setStringPainted(false);
            tracker.setPreferredSize(new Dimension(150, 26));
            root.add(tracker,BorderLayout.CENTER);
        }else {
            JLabel label = new JLabel();
            label.setForeground(AppThemeColor.TEXT_DEFAULT);
            label.setBackground(AppThemeColor.FRAME);
            label.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
            label.setText(descriptor.getTitle());
            switch (descriptor.getType()) {
                case TRACKER_GROUP: {
                    String iconPath = "app/adr/static_group_icon.png";
                    switch (((AdrTrackerGroupDescriptor) descriptor).getGroupType()) {
                        case STATIC: {
                            iconPath = "app/adr/static_group_icon.png";
                            break;
                        }
                        case DYNAMIC: {
                            iconPath = "app/adr/dynamic_group_icon.png";
                            break;
                        }
                    }
                    label.setPreferredSize(new Dimension(170, 30));
                    label.setIcon(componentsFactory.getIcon(iconPath, 48));
                    label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
                    break;
                }
                case ICON: {
                    AdrIconDescriptor iconDescriptor = (AdrIconDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
                    label.setPreferredSize(new Dimension(150, 48));
                    label.setIcon(componentsFactory.getIcon("app/adr/icons/" + iconDescriptor.getIconPath() + ".png",48));
                    label.setBorder(BorderFactory.createEmptyBorder());
                    break;
                }
            }
            root.add(label,BorderLayout.CENTER);
        }
        if(selected){
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }else {
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME));
        }
        return root;
    }
}
