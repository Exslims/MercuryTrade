package com.mercury.platform.ui.components.panel.adr.ui;


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

public class AdrTreeEntryCellRenderer implements TreeCellRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JPanel root = this.componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder());
        root.setBackground(AppThemeColor.FRAME);
        if(((DefaultMutableTreeNode)value).getUserObject() instanceof String) {
            return root;
        }
        AdrComponentDescriptor descriptor = (AdrComponentDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
        switch (descriptor.getType()){
            case GROUP: {
                JLabel groupLabel = new JLabel();
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
                groupLabel.setIcon(componentsFactory.getImage(iconPath));
                groupLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
                groupLabel.setBorder(BorderFactory.createEmptyBorder(0,2,0,2));
                groupLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR,20));
                groupLabel.setBackground(AppThemeColor.FRAME);
                root.add(groupLabel,BorderLayout.CENTER);
                break;
            }
            case ICON: {
                JLabel label = new JLabel();
                AdrIconDescriptor iconDescriptor = (AdrIconDescriptor) ((DefaultMutableTreeNode)value).getUserObject();
                label.setIcon(componentsFactory.getIcon("app/adr/" + iconDescriptor.getIconPath() + ".png",48));
                label.setForeground(AppThemeColor.TEXT_DEFAULT);
                label.setBorder(BorderFactory.createEmptyBorder());
                label.setBackground(AppThemeColor.FRAME);
                root.add(label,BorderLayout.CENTER);

                JPanel arrowPanel = new JPanel(new GridLayout(3, 1));
                arrowPanel.setBorder(BorderFactory.createEmptyBorder(0,4,0,4));
                arrowPanel.setBackground(AppThemeColor.FRAME);
                JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_component.png", 14, AppThemeColor.FRAME, "todo");
                removeButton.setBorder(null);
                removeButton.addActionListener(action -> {

                });
                JButton moveUp = this.componentsFactory.getIconButton("app/adr/move_up.png",14,AppThemeColor.FRAME,"todo");
                moveUp.setBorder(null);
                moveUp.addActionListener(action -> {

                });
                JButton moveDown = this.componentsFactory.getIconButton("app/adr/move_down.png",14,AppThemeColor.FRAME,"todo");
                moveDown.setBorder(null);
                moveDown.addActionListener(action -> {

                });
                arrowPanel.add(removeButton);
                arrowPanel.add(moveUp);
                arrowPanel.add(moveDown);
                root.add(arrowPanel,BorderLayout.LINE_START);
                break;
            }
            case PROGRESS_BAR: {
                break;
            }
        }
        if(selected){
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }else {
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME));
        }
        return root;
    }
}
