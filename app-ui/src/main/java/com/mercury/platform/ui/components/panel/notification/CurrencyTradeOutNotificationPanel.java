package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;


public class CurrencyTradeOutNotificationPanel extends TradeOutNotificationPanel<CurrencyTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        JLabel historyLabel = this.getHistoryButton();
        JButton repeatButton = this.getRepeatButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(repeatButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);
        miscPanel.add(this.getFromPanel(), BorderLayout.CENTER);
        JLabel offerLabel = this.getOfferLabel();
        if (offerLabel != null) {
            miscPanel.add(offerLabel);
        }
        this.interactButtonMap.put(HotKeyType.N_REPEAT_MESSAGE, repeatButton);

        labelsPanel.add(miscPanel, BorderLayout.CENTER);
        labelsPanel.add(buttons, BorderLayout.LINE_END);
        return labelsPanel;
    }

    private JPanel getFromPanel() {
        JPanel fromPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 0), AppThemeColor.FRAME);
        JLabel currencyLabel = this.componentsFactory.getIconLabel("currency/" + this.data.getCurrForSaleTitle() + ".png", 26);

        if (this.data.getItems().size() > 0) {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));

            this.data.getItems().forEach((item) -> {
                itemsPanel.add(this.componentsFactory.getTextLabel(item));
            });
            fromPanel.add(itemsPanel, BorderLayout.LINE_START);

            if (this.data.getItems().size() > 1) {
                this.setAdditionalHeightDelta((this.data.getItems().size() - 1) * 15);
            }
        } else if (currencyLabel.getIcon() == null) {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.add(this.componentsFactory.getTextLabel(this.data.getCurrForSaleCount().intValue() + " " + this.data.getCurrForSaleTitle()));
            fromPanel.add(itemsPanel, BorderLayout.LINE_START);
        } else {
            JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurrForSaleCount(), this.data.getCurrForSaleTitle());
            currencyPanel.setBackground(AppThemeColor.FRAME);
            fromPanel.add(currencyPanel, BorderLayout.LINE_START);
            fromPanel.add(getCurrencyRatePanel(), BorderLayout.CENTER);
        }
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
