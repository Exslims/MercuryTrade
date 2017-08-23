package com.mercury.platform.ui.components.panel.settings;


import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class MenuListRenderer extends JButton implements ListCellRenderer<MenuEntry> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public Component getListCellRendererComponent(JList<? extends MenuEntry> list, MenuEntry value, int index, boolean isSelected, boolean cellHasFocus) {
        JButton button = this.componentsFactory.getButton(value.getText());
        button.setForeground(AppThemeColor.TEXT_DEFAULT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppThemeColor.FRAME);
        button.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 16f));
        button.setPreferredSize(new Dimension(220, 50));
        button.setIcon(value.getImageIcon());
        if (isSelected) {
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 14, 0, 0),
                    BorderFactory.createMatteBorder(0, 0, 0, 4, AppThemeColor.TEXT_DEFAULT)));
            button.setBackground(AppThemeColor.ADR_BG);
        } else {
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 1, AppThemeColor.ADR_PANEL_BORDER),
                    BorderFactory.createEmptyBorder(0, 10, 0, 3)));
        }
        button.addActionListener(action -> {
            value.getAction().onClick();
        });
        return button;
    }
}
