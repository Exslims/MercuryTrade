package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class IconsListCellRenderer implements ListCellRenderer<String> {
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    private IconBundleConfigurationService config = Configuration.get().iconBundleConfiguration();

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel iconLabel = this.componentsFactory.getIconLabel(this.config.getIcon(value), 64);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        if (isSelected) {
            iconLabel.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        }
        return iconLabel;
    }
}
