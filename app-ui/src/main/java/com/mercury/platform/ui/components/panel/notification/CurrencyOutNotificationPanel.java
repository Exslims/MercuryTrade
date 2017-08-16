package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class CurrencyOutNotificationPanel extends OutgoingNotificationPanel<CurrencyTradeNotificationDescriptor> {
    @Override
    protected JPanel getContentPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        root.add(this.getFromPanel(),BorderLayout.LINE_START);
        root.add(this.getForPanel(),BorderLayout.CENTER);
        return root;
    }
    private JPanel getForPanel(){
        JPanel fromPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.LEFT,0,0),AppThemeColor.FRAME);
        fromPanel.add(this.getCurrencyPanel(this.data.getCurrForSaleCount(),this.data.getCurrForSaleTitle()));
        fromPanel.add(getCurrencyRatePanel());
        return fromPanel;
    }

    private JPanel getCurrencyRatePanel(){
        Double currForSaleCount = this.data.getCurrForSaleCount();
        Double curCount = this.data.getCurCount();
        double rate = curCount / currForSaleCount;
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        JPanel ratePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 18f, null,"("));
        JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + this.data.getCurrency() + ".png", 26);
        currencyLabel.setBorder(null);
        ratePanel.add(currencyLabel);
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 18f, null,decimalFormat.format(rate)));
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 18f, null,")"));
        ratePanel.setBorder(BorderFactory.createEmptyBorder(-5,0,-5,0));
        return ratePanel;
    }
}
