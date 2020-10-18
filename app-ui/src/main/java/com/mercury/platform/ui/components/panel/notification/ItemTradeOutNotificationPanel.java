package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ItemTradeOutNotificationPanel extends TradeOutNotificationPanel<ItemTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        JPanel itemsPanel = getItemsPanel();

        JLabel historyLabel = this.getHistoryButton();
        JButton repeatButton = this.getRepeatButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(repeatButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);
        miscPanel.add(itemsPanel);

        JPanel itemsOfferedPanel = getOfferPanel();
        miscPanel.add(itemsOfferedPanel);

        this.interactButtonMap.put(HotKeyType.N_REPEAT_MESSAGE, repeatButton);

        labelsPanel.add(miscPanel, BorderLayout.CENTER);
        labelsPanel.add(buttons, BorderLayout.LINE_END);
        return labelsPanel;
    }

    public JPanel getItemsPanel() {
        JPanel itemsPanel;
        if (this.data.getItemsWanted() != null && !this.data.getItemsWanted().isEmpty()) {
            List<String> itemsWanted = this.data.getItemsWanted();

            itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));

            itemsWanted.forEach((item) -> {
                JButton btn = this.componentsFactory.getButton(item);
                btn.setForeground(AppThemeColor.INC_PANEL_ARROW);
                btn.setBackground(AppThemeColor.FRAME);
                btn.setBorder(new EmptyBorder(4,4,4,4));
                itemsPanel.add(btn);
            });

        } else {
            itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));
            JButton btn = this.componentsFactory.getButton(this.data.getItemName());
            btn.setForeground(AppThemeColor.INC_PANEL_ARROW);
            btn.setBackground(AppThemeColor.FRAME);
            btn.setBorder(new EmptyBorder(4,4,4,4));
            itemsPanel.add(btn);
        }
        return itemsPanel;
    }

    public JPanel getOfferPanel() {
        JPanel itemsPanel;
        if (this.data.getItemsOffered() != null && !this.data.getItemsOffered().isEmpty()) {
            List<String> itemsWanted = this.data.getItemsOffered();

            itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));

            itemsWanted.forEach((item) -> {
                JLabel label = this.componentsFactory.getTextLabel(item);
                label.setBackground(AppThemeColor.FRAME);
                label.setBorder(new EmptyBorder(0,0,0,0));
                itemsPanel.add(label);
            });

        } else {
            itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));
            JLabel offerLabel = this.getOfferLabel();
            if (offerLabel != null) {
                itemsPanel.add(this.getOfferLabel());
            }
        }
        return itemsPanel;
    }
}
