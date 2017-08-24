package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class CurrencyTradeOutNotificationPanel extends TradeOutNotificationPanel<CurrencyTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.FRAME, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton repeatButton = this.getRepeatButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(repeatButton);
        buttons.add(openChatButton);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);
        miscPanel.add(this.getFromPanel(), BorderLayout.CENTER);
        JLabel offerLabel = this.getOfferLabel();
        if (offerLabel != null) {
            miscPanel.add(offerLabel);
        }

        this.interactButtonMap.put(HotKeyType.N_OPEN_CHAT, openChatButton);
        this.interactButtonMap.put(HotKeyType.N_REPEAT_MESSAGE, repeatButton);

        labelsPanel.add(miscPanel, BorderLayout.CENTER);
        labelsPanel.add(buttons, BorderLayout.LINE_END);
        return labelsPanel;
    }

    private JPanel getFromPanel() {
        JPanel fromPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 0), AppThemeColor.FRAME);
        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurrForSaleCount(), this.data.getCurrForSaleTitle());
        currencyPanel.setBackground(AppThemeColor.FRAME);
        fromPanel.add(currencyPanel, BorderLayout.LINE_START);
        fromPanel.add(getCurrencyRatePanel(), BorderLayout.CENTER);
        return fromPanel;
    }

    private JPanel getCurrencyRatePanel() {
        Double currForSaleCount = this.data.getCurrForSaleCount();
        Double curCount = this.data.getCurCount();
        double rate = curCount / currForSaleCount;
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        JPanel ratePanel = componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 18f, null, "("), BorderLayout.LINE_START);
        JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + this.data.getCurrency() + ".png", 26);
        currencyLabel.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 18f));
        currencyLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        currencyLabel.setText(decimalFormat.format(rate) + ")");
        currencyLabel.setBorder(null);
        ratePanel.add(currencyLabel, BorderLayout.CENTER);
        return ratePanel;
    }

}
