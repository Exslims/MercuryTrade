package com.mercury.platform.ui.components.panel.adr.ui;


import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class AdrListEntryCellRenderer extends JLabel implements ListCellRenderer<AdrListEntry>{
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    @Override
    public Component getListCellRendererComponent(JList<? extends AdrListEntry> list, AdrListEntry value, int index, boolean isSelected, boolean cellHasFocus) {
        this.setIcon(componentsFactory.getIcon("app/adr/" + value.getIconPath() + ".png",48));
        this.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setFont(componentsFactory.getFont(FontStyle.REGULAR,20));
        this.setBackground(AppThemeColor.FRAME);
        if(isSelected){
            this.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }else {
            this.setBorder(BorderFactory.createLineBorder(AppThemeColor.FRAME));
        }
        return this;
    }
}
