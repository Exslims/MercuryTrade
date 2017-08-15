package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class CurrencyIncNotificationPanel extends IncomingNotificationPanel<CurrencyTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.FRAME);
        JPanel tradePanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.FRAME);

        tradePanel.add(this.getFromPanel(),BorderLayout.CENTER);
        tradePanel.add(this.getForPanel(),BorderLayout.LINE_END);
        labelsPanel.add(tradePanel,BorderLayout.CENTER);
        JLabel offerLabel = this.getOfferLabel();
        if(offerLabel != null) {
            labelsPanel.add(offerLabel, BorderLayout.PAGE_END);
        }
        return labelsPanel;
    }
    private JPanel getFromPanel(){
        JPanel fromPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.LEFT,5,0),AppThemeColor.FRAME);
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

    @Override
    protected JButton getStillInterestedButton() {
        JButton stillIntButton = componentsFactory.getIconButton("app/still-interesting.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.STILL_INTERESTED);
        stillIntButton.addActionListener(action -> {
            String curCount = this.data.getCurCount() % 1 == 0 ?
                    String.valueOf(this.data.getCurCount().intValue()) :
                    String.valueOf(this.data.getCurCount());
            String responseText = "Hi, are you still interested in ";
            String curForSaleCount = this.data.getCurCount() % 1 == 0 ?
                    String.valueOf(this.data.getCurrForSaleCount().intValue()) :
                    String.valueOf(this.data.getCurrForSaleCount());
            responseText += curForSaleCount + " " + this.data.getCurrForSaleTitle() + " for " +
                    curCount + " " + this.data.getCurrency() + "?";
            this.controller.performResponse(responseText);
        });
        return stillIntButton;
    }
}
