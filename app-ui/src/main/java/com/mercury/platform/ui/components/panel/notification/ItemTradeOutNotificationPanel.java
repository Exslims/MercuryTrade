package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class ItemTradeOutNotificationPanel extends TradeOutNotificationPanel<ItemTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        JLabel itemLabel = componentsFactory.getTextLabel(
                this.data.getItemName(),
                FontStyle.BOLD,
                AppThemeColor.INC_PANEL_ARROW, 16f);

        JLabel historyLabel = this.getHistoryButton();
        JButton repeatButton = this.getRepeatButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(repeatButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);
        miscPanel.add(itemLabel);
        JLabel offerLabel = this.getOfferLabel();
        if (offerLabel != null) {
            miscPanel.add(offerLabel);
        }

        this.interactButtonMap.put(HotKeyType.N_REPEAT_MESSAGE, repeatButton);

        labelsPanel.add(miscPanel, BorderLayout.CENTER);
        labelsPanel.add(buttons, BorderLayout.LINE_END);
        return labelsPanel;
    }
}
