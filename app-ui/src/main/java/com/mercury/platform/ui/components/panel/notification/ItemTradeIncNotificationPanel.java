package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.IconConst;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;


public class ItemTradeIncNotificationPanel extends TradeIncNotificationPanel<ItemTradeNotificationDescriptor> {
    private JPanel labelsPanel;

    @Override
    protected JPanel getMessagePanel() {
        this.labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);


        JPanel itemsPanel = getItemsPanel();

        JButton stillInterestedButton = this.getStillInterestedButton();
        JLabel historyLabel = this.getHistoryButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(stillInterestedButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);

        miscPanel.add(itemsPanel);
        miscPanel.add(getOfferPanel());

        this.interactButtonMap.put(HotKeyType.N_STILL_INTERESTING, stillInterestedButton);

        this.labelsPanel.add(miscPanel, BorderLayout.CENTER);
        this.labelsPanel.add(buttons, BorderLayout.LINE_END);
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
                btn.setForeground(AppThemeColor.TEXT_IMPORTANT);
                btn.setBackground(AppThemeColor.FRAME);
                btn.setBorder(new EmptyBorder(4,4,4,4));
                btn.addActionListener(action -> {
                    this.controller.showITH();
                });
                itemsPanel.add(btn);
            });

        } else {
            itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));
            JButton btn = this.componentsFactory.getButton(this.data.getItemName());
            btn.setForeground(AppThemeColor.TEXT_IMPORTANT);
            btn.setBackground(AppThemeColor.FRAME);
            btn.setBorder(new EmptyBorder(4,4,4,4));
            btn.addActionListener(action -> {
                this.controller.showITH();
            });
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
            JLabel label = this.getOfferLabel();
            if (label != null) {
                itemsPanel.add(label);
            }
        }
        return itemsPanel;
    }

    public void setDuplicate(boolean duplicate) {
        if (duplicate) {
            JButton ignoreButton = componentsFactory.getIconButton("app/adr/visible_node_off.png", 15, AppThemeColor.FRAME, "Ignore item 1 hour");
            ignoreButton.addActionListener(e -> {
                MercuryStoreCore.expiredNotificationSubject.onNext(this.data);
                this.controller.performHide();
            });
            this.labelsPanel.add(ignoreButton, BorderLayout.LINE_START);
        }
    }

    @Override
    protected JButton getStillInterestedButton() {
        JButton stillIntButton = componentsFactory.getIconButton(IconConst.STILL_INTERESTING, 14, AppThemeColor.FRAME, TooltipConstants.STILL_INTERESTED);
        stillIntButton.addActionListener(action -> {
            String curCount = this.data.getCurCount().toString();
            String responseText = "Hi, are you still interested in ";
            if (this.data.getCurrency().equals("???")) {
                responseText += this.data.getItemName() + "?";
            } else {
                responseText += this.data.getItemName() +
                                " for " + curCount + " " + this.data.getCurrency() + "?";
            }
            this.controller.performResponse(responseText);
        });
        return stillIntButton;
    }
}
