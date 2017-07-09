package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.CurrencyMessage;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.movable.container.MessageFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.HasHotkey;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class MessagePanel extends JPanel implements AsSubscriber, HasUI,HasHotkey{
    private static final Logger logger = LogManager.getLogger(MessagePanel.class.getSimpleName());

    private ComponentsFactory componentsFactory;
    private PlainConfigurationService<NotificationDescriptor> notificationService;
    private MessagePanelController controller;
    private MessagePanelStyle style;

    private String whisper;
    private JLabel whisperLabel;
    private JButton tradeButton;
    private JButton expandButton;

    @Getter
    private Message message;

    private Timer timeAgo;
    private String cachedTime = "0m";
    private JLabel timeLabel;
    private Color cachedWhisperColor = AppThemeColor.TEXT_NICKNAME;
    private JPanel whisperPanel;
    private JPanel messagePanel;
    private JPanel customButtonsPanel;

    private boolean expanded = false;

    private MessagePanel(Message message, MessagePanelStyle style) {
        super(new BorderLayout());
        this.message = message;
        this.style = style;
        this.whisper = message.getWhisperNickname();
        this.notificationService = Configuration.get().notificationConfiguration();
        if(!style.equals(MessagePanelStyle.HISTORY)) {
            this.initHotKeyListeners();
        }
    }
    public MessagePanel(Message message, MessagePanelStyle style, MessagePanelController controller, ComponentsFactory componentsFactory){
        this(message,style);
        this.componentsFactory = componentsFactory;
        this.controller = controller;
        createUI();
    }

    @Override
    public void createUI() {
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,1),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
        init();
        subscribe();
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
    }
    private void init(){
        this.removeAll();
        this.whisperPanel = getWhisperPanel();
        this.messagePanel = getFormattedMessagePanel();
        this.customButtonsPanel = getButtonsPanel();
        whisperPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(-6, 0, -6, 0)));
        if(style.equals(MessagePanelStyle.DOWNWARDS_SMALL) ||
                style.equals(MessagePanelStyle.HISTORY) || style.equals(MessagePanelStyle.SP_MODE)) {
            this.add(whisperPanel,BorderLayout.PAGE_START);
            this.add(messagePanel,BorderLayout.CENTER);
            this.add(customButtonsPanel,BorderLayout.PAGE_END);
        }else {
            this.add(customButtonsPanel,BorderLayout.PAGE_START);
            this.add(messagePanel,BorderLayout.CENTER);
            this.add(whisperPanel,BorderLayout.PAGE_END);
        }
        switch (style){
            case DOWNWARDS_SMALL:{
                messagePanel.setVisible(expanded);
                customButtonsPanel.setVisible(expanded);
                break;
            }
            case UPWARDS_SMALL:{
                messagePanel.setVisible(expanded);
                customButtonsPanel.setVisible(expanded);
                break;
            }
            case HISTORY:{
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(false);
                break;
            }
            case SP_MODE:{
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(true);
            }
        }
        this.repaint();
    }

    private JPanel getFormattedMessagePanel(){
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel,BoxLayout.Y_AXIS));
        labelsPanel.setBackground(AppThemeColor.TRANSPARENT);

        JPanel tradePanel = new JPanel(new BorderLayout());
        tradePanel.setBackground(AppThemeColor.TRANSPARENT);
        tradePanel.setBorder(BorderFactory.createEmptyBorder(-11,2,-11,0));
        if(message instanceof ItemMessage) {
            JButton itemButton = componentsFactory.getButton(
                    FontStyle.BOLD,
                    AppThemeColor.BUTTON,
                    BorderFactory.createEmptyBorder(0,4,0,2),
                    ((ItemMessage) message).getItemName(), 17f);

            itemButton.setForeground(AppThemeColor.TEXT_IMPORTANT);
            itemButton.setBackground(AppThemeColor.TRANSPARENT);
            itemButton.setHorizontalAlignment(SwingConstants.LEFT);
            itemButton.setContentAreaFilled(false);
            itemButton.setRolloverEnabled(false);
            itemButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    controller.showITH();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    itemButton.setBorder(new CompoundBorder(
                            BorderFactory.createMatteBorder(0,1,0,1,AppThemeColor.BORDER),
                            BorderFactory.createEmptyBorder(0,3,0,1)));
                    MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    itemButton.setBorder(BorderFactory.createEmptyBorder(0,4,0,2));
                    MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
                }
            });
            tradePanel.add(itemButton,BorderLayout.CENTER);
        }else if(message instanceof CurrencyMessage){
            JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fromPanel.setBackground(AppThemeColor.TRANSPARENT);
            CurrencyMessage message = (CurrencyMessage) this.message;

            String curCount = message.getCurrForSaleCount() % 1 == 0 ?
                    String.valueOf(message.getCurrForSaleCount().intValue()) :
                    String.valueOf(message.getCurrForSaleCount());
            JPanel curCountPanel = getCurrencyPanel(curCount);
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + message.getCurrForSaleTitle() + ".png", 26);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            curPanel.add(curCountPanel);
            curPanel.add(currencyLabel);
            curPanel.add(getCurrencyRatePanel());
            fromPanel.add(curPanel);
            tradePanel.add(fromPanel,BorderLayout.CENTER);
        }

        JPanel forPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        forPanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel separator = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_MESSAGE,
                TextAlignment.CENTER,
                18f,
                "=>");
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        String curCount = " ";
        if(message.getCurCount() > 0) {
            curCount = message.getCurCount() % 1 == 0 ?
                    String.valueOf(message.getCurCount().intValue()) :
                    String.valueOf(message.getCurCount());
        }
        String currency = message.getCurrency();
        if(!Objects.equals(curCount, "") && currency != null) {
            JPanel curCountPanel = getCurrencyPanel(curCount);
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + currency + ".png", 26);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            curPanel.add(separator);
            curPanel.add(curCountPanel);
            curPanel.add(currencyLabel);
            forPanel.add(curPanel);
        }
        tradePanel.add(forPanel,BorderLayout.LINE_END);
        labelsPanel.add(tradePanel);
        String offer = message.getOffer();
        if(offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, offer);
            offerLabel.setAlignmentY(Component.TOP_ALIGNMENT);
            labelsPanel.add(offerLabel);
        }
        return labelsPanel;
    }
    private JPanel getCurrencyPanel(String curCount){
        JPanel curCountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        curCountPanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,17f,null, curCount);
        curCountPanel.add(priceLabel);
        curCountPanel.setPreferredSize(new Dimension((int)(componentsFactory.getScale() * 40),curCountPanel.getPreferredSize().height));
        return curCountPanel;
    }
    private JPanel getCurrencyRatePanel(){
        CurrencyMessage message = (CurrencyMessage) this.message;
        Double currForSaleCount = message.getCurrForSaleCount();
        Double curCount = message.getCurCount();
        double rate = curCount / currForSaleCount;
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        JPanel ratePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 18f, null,"("));
        JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + message.getCurrency() + ".png", 26);
        currencyLabel.setBorder(null);
        ratePanel.add(currencyLabel);
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 18f, null,decimalFormat.format(rate)));
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 18f, null,")"));
        ratePanel.setBorder(BorderFactory.createEmptyBorder(-5,0,-5,0));
        return ratePanel;
    }
    private JPanel getWhisperPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.MSG_HEADER);

        whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,cachedWhisperColor, TextAlignment.LEFTOP,15f,getNicknameLabel());
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(0,0,0,5)));
        whisperLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel nickNamePanel = componentsFactory.getTransparentPanel(new BorderLayout());
        if(style.equals(MessagePanelStyle.HISTORY)){
            nickNamePanel.add(whisperLabel,BorderLayout.CENTER);
        }else {
            JPanel buttonWrapper = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            buttonWrapper.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
            buttonWrapper.add(getExpandButton());
            if(!style.equals(MessagePanelStyle.SP_MODE)) {
                nickNamePanel.add(buttonWrapper, BorderLayout.LINE_START);
            }
            nickNamePanel.add(whisperLabel,BorderLayout.CENTER);
        }
        topPanel.add(nickNamePanel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBorder(BorderFactory.createEmptyBorder(1,0,1,0));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        interactionPanel.add(getTimePanel());
        if(!style.equals(MessagePanelStyle.HISTORY)) {
            JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
            inviteButton.addActionListener(e -> controller.performInvite());
            JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
            kickButton.addActionListener(e -> {
                controller.performKick();
                if(this.notificationService.get().isDismissAfterKick() && !style.equals(MessagePanelStyle.SP_MODE)){
                    controller.performHide();
                }
            });
            tradeButton = componentsFactory.getIconButton("app/trade.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
            tradeButton.addActionListener(e -> controller.performOfferTrade());
            interactionPanel.add(inviteButton);
            interactionPanel.add(kickButton);
            interactionPanel.add(tradeButton);
            interactionPanel.add(getStillInterestedButton());
        }else {
            JButton reloadButton = componentsFactory.getIconButton("app/reload-history.png", 14, AppThemeColor.MSG_HEADER, "Restore");
            reloadButton.addActionListener(e -> controller.reloadMessage(this));
            interactionPanel.add(reloadButton);
        }
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addActionListener(e -> controller.performOpenChat());

        interactionPanel.add(openChatButton);
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.performHide();
                }
            }
        });
        if(!style.equals(MessagePanelStyle.HISTORY) && !style.equals(MessagePanelStyle.SP_MODE)) {
            interactionPanel.add(hideButton);
        }

        topPanel.add(interactionPanel,BorderLayout.LINE_END);
        return topPanel;
    }
    private String getNicknameLabel(){
        String whisperNickname = message.getWhisperNickname();
        String result = whisperNickname + ":";
        if(this.notificationService.get().isShowLeague()) {
            if (message.getLeague() != null) {
                String league = message.getLeague().trim();
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
    private JPanel getTimePanel(){
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.TRANSPARENT);
        timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 14, cachedTime);
        if(timeAgo == null) {
            timeAgo = new Timer(60000, new ActionListener() {
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
                    MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
                }
            });
            timeAgo.start();
        }
        panel.add(timeLabel);
        return panel;
    }
    public void disableTime(){
        if(timeAgo != null) {
            timeAgo.stop();
            timeLabel.setText("");
        }
    }
    private JButton getExpandButton(){
        String iconPath = "app/default-mp.png";
        if(expanded){
            if(style.equals(MessagePanelStyle.DOWNWARDS_SMALL)){
                iconPath = "app/expand-mp.png";
            }else {
                iconPath = "app/collapse-mp.png";
            }
        }
        expandButton = componentsFactory.getIconButton(iconPath, 18f, AppThemeColor.MSG_HEADER,"");
        expandButton.setBorder(BorderFactory.createEmptyBorder(4,4,4,0));
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (!messagePanel.isVisible()) {
                        expand();
                    } else {
                        collapse();
                    }
                }
            }
        });
        return expandButton;
    }
    public void expand(){
        expanded = true;
        if(style.equals(MessagePanelStyle.DOWNWARDS_SMALL)) {
            expandButton.setIcon(componentsFactory.getIcon("app/expand-mp.png", 18f));
            messagePanel.setVisible(true);
            customButtonsPanel.setVisible(true);
        }else {
            expandButton.setIcon(componentsFactory.getIcon("app/collapse-mp.png", 18f));
            messagePanel.setVisible(true);
            customButtonsPanel.setVisible(true);
        }
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        MercuryStoreUI.packSubject.onNext(MessageFrame.class);
    }
    public void collapse(){
        expanded = false;
        if(style.equals(MessagePanelStyle.DOWNWARDS_SMALL)) {
            expandButton.setIcon(componentsFactory.getIcon("app/default-mp.png", 18));
            messagePanel.setVisible(false);
            customButtonsPanel.setVisible(false);
        }else {
            expandButton.setIcon(componentsFactory.getIcon("app/default-mp.png", 18));
            messagePanel.setVisible(false);
            customButtonsPanel.setVisible(false);
        }
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        MercuryStoreUI.packSubject.onNext(MessageFrame.class);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setStyle(MessagePanelStyle style) {
        this.style = style;
        this.cachedTime = timeLabel.getText();
        init();
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
    }

    public MessagePanelStyle getStyle() {
        return style;
    }
    private JPanel getButtonsPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        initResponseButtons(panel);
        return panel;
    }
    @Override
    public void subscribe() {
        MercuryStoreCore.INSTANCE.playerJoinSubject.subscribe(nickname -> {
            if(nickname.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                cachedWhisperColor = AppThemeColor.TEXT_SUCCESS;
                if(!style.equals(MessagePanelStyle.HISTORY)) {
                    tradeButton.setEnabled(true);
                }
                MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
            }
        });
        MercuryStoreCore.INSTANCE.playerLeftSubject.subscribe(nickname -> {
            if (nickname.equals(whisper)) {
                whisperLabel.setForeground(AppThemeColor.TEXT_DISABLE);
                cachedWhisperColor = AppThemeColor.TEXT_DISABLE;
                if (!style.equals(MessagePanelStyle.HISTORY)) {
                    tradeButton.setEnabled(false);
                }
                MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
            }
        });
        MercuryStoreCore.INSTANCE.buttonsChangedSubject.subscribe(state -> {
            this.customButtonsPanel.removeAll();
            initResponseButtons(customButtonsPanel);
            MercuryStoreUI.repaintSubject.onNext(MessageFrame.class);
        });
    }
    private void initResponseButtons(JPanel panel){
        List<ResponseButtonDescriptor> buttonsConfig = this.notificationService.get().getButtons();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig)->{
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(),15f);
            button.addActionListener(e -> {
                controller.performResponse(buttonConfig.getResponseText());
                if(buttonConfig.isClose() && !style.equals(MessagePanelStyle.SP_MODE)){
                    controller.performHide();
                }
            });
            panel.add(button);
        });
    }
    private JButton getStillInterestedButton(){
        JButton stillIntButton = componentsFactory.getIconButton("app/still-interesting.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.STILL_INTERESTED);

        String curCount = message.getCurCount() % 1 == 0 ?
                String.valueOf(message.getCurCount().intValue()) :
                String.valueOf(message.getCurCount());
        String responseText = "Hi, are you still interested in ";
        if(message instanceof ItemMessage){
            ItemMessage message = (ItemMessage) this.message;
            if(message.getCurrency().equals("???")){
                responseText += message.getItemName() + "?";
            }else {
                responseText += message.getItemName() +
                        " for " + curCount + " " + message.getCurrency() + "?";
            }
        }else {
            CurrencyMessage message = (CurrencyMessage) this.message;
            String curForSaleCount = message.getCurCount() % 1 == 0 ?
                    String.valueOf(message.getCurrForSaleCount().intValue()) :
                    String.valueOf(message.getCurrForSaleCount());
            responseText += curForSaleCount + " " + message.getCurrForSaleTitle() + " for " +
                    curCount + " " + message.getCurrency() + "?";
        }
        String finalResponseText = responseText; // hate java
        stillIntButton.addActionListener(
                (action)->controller.performResponse(finalResponseText)
        );
        return stillIntButton;
    }

    public void setComponentsFactory(ComponentsFactory componentsFactory) {
        this.componentsFactory = componentsFactory;
    }

    @Override
    public void initHotKeyListeners() {
        KeyValueConfigurationService<String, HotKeyDescriptor> config = Configuration.get().hotKeysConfiguration();
        MercuryStoreCore.INSTANCE.hotKeySubject.subscribe(descriptor -> {
            HotKeyDescriptor hotKeyDescriptor = config.get(HotKeyType.CLOSE_NOTIFICATION.name());
            if(descriptor.equals(hotKeyDescriptor)) {
                this.controller.performHide();
            }
        });
    }
}
