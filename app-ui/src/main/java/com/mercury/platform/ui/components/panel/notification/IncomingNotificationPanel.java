package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public abstract class IncomingNotificationPanel<T extends TradeNotificationDescriptor> extends NotificationPanel<T,MessagePanelController>{
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    @Override
    public void onViewInit() {
        this.config = Configuration.get().notificationConfiguration();
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));

        this.add(this.getHeader(),BorderLayout.PAGE_START);
        this.add(this.getMessagePanel(),BorderLayout.CENTER);
        this.add(this.getResponseButtonsPanel(),BorderLayout.PAGE_END);
    }
    private JPanel getHeader(){
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);

        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,this.getNicknameText());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        root.add(this.getExpandButton(),BorderLayout.LINE_START);
        root.add(nicknameLabel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new GridLayout(1,0));
        interactionPanel.setPreferredSize(new Dimension(220, 16));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteButton.addActionListener(e -> this.controller.performInvite());
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        kickButton.addActionListener(e -> {
            this.controller.performKick();
            if(this.config.get().isDismissAfterKick()){
                this.controller.performHide();
            }
        });
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> this.controller.performOfferTrade());
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(this.getTimePanel());
        interactionPanel.add(inviteButton);
        interactionPanel.add(kickButton);
        interactionPanel.add(tradeButton);
        interactionPanel.add(getStillInterestedButton());
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        root.add(interactionPanel,BorderLayout.LINE_END);
        return root;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void onViewDestroy() {

    }
    protected JPanel getForPanel(){
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setPreferredSize(new Dimension(110,36));
        forPanel.setBackground(AppThemeColor.FRAME);
        JLabel separator = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_MESSAGE,
                TextAlignment.CENTER,
                18f,
                "=>");
        forPanel.add(separator,BorderLayout.CENTER);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurCount(),this.data.getCurrency());
        if(currencyPanel != null) {
            forPanel.add(currencyPanel, BorderLayout.LINE_END);
        }
        return forPanel;
    }
    protected JPanel getCurrencyPanel(Double curCount, String curIconPath){
        String curCountStr = " ";
        if(curCount > 0) {
            curCountStr = curCount % 1 == 0 ?
                    String.valueOf(curCount.intValue()) :
                    String.valueOf(curCount);
        }
        if(!Objects.equals(curCountStr, "") && curIconPath != null) {
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 26);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            curPanel.setBackground(AppThemeColor.FRAME);
            curPanel.add(this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,17f,null, curCountStr));
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }
    protected JLabel getOfferLabel(){
        String offer = this.data.getOffer();
        if(offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, offer);
            offerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return offerLabel;
        }
        return null;
    }
    protected abstract JPanel getMessagePanel();
    protected abstract JButton getStillInterestedButton();
    private JPanel getResponseButtonsPanel(){
        JPanel root = new JPanel(new FlowLayout(FlowLayout.CENTER,5,2));
        root.setBackground(AppThemeColor.FRAME);
        List<ResponseButtonDescriptor> buttonsConfig = this.config.get().getButtons();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig)->{
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(),16f);
            if(buttonConfig.getTitle().length() < 10) {
                button.setPreferredSize(new Dimension(70, 26));
            }
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder( BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.BUTTON, 3)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder( BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.BUTTON, 3)
                    ));
                }
            });
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if(buttonConfig.isClose()){
                    this.controller.performHide();
                }
            });
            root.add(button);
        });
        return root;
    }
    private JPanel getTimePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        JLabel timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 14, "0m");
        Timer timeAgo = new Timer(60000, new ActionListener() {
            private int minute = 0;
            private int hours = 0;
            private int day = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String labelText = "";
                minute++;
                if (minute > 60) {
                    hours++;
                    minute = 0;
                    if (hours > 24) {
                        day++;
                        hours = 0;
                    }
                }
                if (hours == 0 && day == 0) {
                    labelText = minute + "m";
                } else if (hours > 0) {
                    labelText = hours + "h " + minute + "m";
                } else if (day > 0) {
                    labelText = day + "d " + hours + "h " + minute + "m";
                }
                timeLabel.setText(labelText);
            }
        });
        timeAgo.start();
        root.add(timeLabel,BorderLayout.CENTER);
        return root;
    }
    private String getNicknameText(){
        String whisperNickname = data.getWhisperNickname();
        String result = whisperNickname + ":";
        if(this.config.get().isShowLeague()) {
            if (data.getLeague() != null) {
                String league = data.getLeague().trim();
                if (league.length() == 0) {
                    return result;
                }
                if (league.contains("Hardcore")) {
                    if (league.equals("Hardcore")) {
                        result = "HC " + result;
                    } else {
                        result = String.valueOf(league.split(" ")[1].charAt(0)) + "HC " + result;
                    }
                } else if (league.contains("Standard")) {
                    result = "Standard " + result;
                } else {
                    result = String.valueOf(league.charAt(0)) + "SC " + result;
                }
            }
        }
        return result;
    }
    private JButton getExpandButton(){
        String iconPath = "app/default-mp.png";
//        if(expanded){
//            if(style.equals(MessagePanelStyle.IN_DOWNWARDS)){
//                iconPath = "app/expand-mp.png";
//            }else {
//                iconPath = "app/collapse-mp.png";
//            }
//        }
        JButton expandButton = componentsFactory.getIconButton(iconPath, 18f, AppThemeColor.MSG_HEADER,"");
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                if(SwingUtilities.isLeftMouseButton(e)) {
//                    if (!messagePanel.isVisible()) {
//                        expand();
//                    } else {
//                        collapse();
//                    }
//                }
            }
        });
        return expandButton;
    }
}
