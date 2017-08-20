package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class ItemIncNotificationPanel extends IncomingNotificationPanel<ItemTradeNotificationDescriptor> {
    private JPanel labelsPanel;
    @Override
    protected JPanel getMessagePanel() {
        this.labelsPanel = new JPanel();
        this.labelsPanel.setLayout(new BorderLayout());
        this.labelsPanel.setBackground(AppThemeColor.FRAME);

        JPanel tradePanel = new JPanel(new BorderLayout());
        tradePanel.setBackground(AppThemeColor.FRAME);
        JButton itemButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.BUTTON,
                BorderFactory.createEmptyBorder(0,4,0,2),
                this.data.getItemName(), 16f);

        itemButton.setForeground(AppThemeColor.TEXT_IMPORTANT);
        itemButton.setBackground(AppThemeColor.TRANSPARENT);
        itemButton.setHorizontalAlignment(SwingConstants.LEFT);
        itemButton.setContentAreaFilled(false);
        itemButton.setRolloverEnabled(false);
        itemButton.addActionListener(action -> {
            this.controller.showITH();
        });
        tradePanel.add(itemButton,BorderLayout.CENTER);
        tradePanel.add(this.getForPanel(),BorderLayout.LINE_END);

        this.labelsPanel.add(tradePanel,BorderLayout.CENTER);
        JLabel offerLabel = this.getOfferLabel();
        if(offerLabel != null) {
            this.labelsPanel.add(offerLabel, BorderLayout.PAGE_END);
        }
        return labelsPanel;
    }

    @Override
    public void setDuplicate(boolean duplicate) {
        if(duplicate){
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
        JButton stillIntButton = componentsFactory.getIconButton("app/still-interesting.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.STILL_INTERESTED);
        stillIntButton.addActionListener(action-> {
            String curCount = this.data.getCurCount().toString();
            String responseText = "Hi, are you still interested in ";
            if(this.data.getCurrency().equals("???")){
                responseText += this.data.getItemName() + "?";
            }else {
                responseText += this.data.getItemName() +
                        " for " + curCount + " " + this.data.getCurrency() + "?";
            }
            this.controller.performResponse(responseText);
        });
        return stillIntButton;
    }
}
