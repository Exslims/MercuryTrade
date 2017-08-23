package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class ItemOutNotificationPanel extends OutgoingNotificationPanel<ItemTradeNotificationDescriptor> {
    @Override
    protected JPanel getContentPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        JButton itemButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.BUTTON,
                BorderFactory.createEmptyBorder(0, 4, 0, 2),
                this.data.getItemName(), 16f);

        itemButton.setForeground(AppThemeColor.TEXT_IMPORTANT);
        itemButton.setBackground(AppThemeColor.TRANSPARENT);
        itemButton.setHorizontalAlignment(SwingConstants.LEFT);
        itemButton.setContentAreaFilled(false);
        itemButton.setRolloverEnabled(false);
        itemButton.addActionListener(action -> {
        });
        root.add(this.getFromPanel(), BorderLayout.LINE_START);
        root.add(itemButton, BorderLayout.CENTER);
        return root;
    }
}
