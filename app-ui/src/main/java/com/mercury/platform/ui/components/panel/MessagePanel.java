package com.mercury.platform.ui.components.panel;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.MessagePanelStyle;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

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
import java.util.*;

/**
 * Created by Константин on 15.12.2016.
 */
public class MessagePanel extends JPanel implements HasEventHandlers{
    private ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    private ComponentFrame owner;
    private MessagePanelStyle style;

    private String whisper;
    private JLabel whisperLabel;
    private JButton tradeButton;
    private JButton expandButton;

    private Message message;

    private Timer timeAgo;
    private String cachedTime = "0m";
    private JLabel timeLabel;
    private Color cachedWhisperColor = AppThemeColor.TEXT_NICKNAME;
    private JPanel whisperPanel;
    private JPanel messagePanel;
    private JPanel customButtonsPanel;

    public MessagePanel(Message message, ComponentFrame owner, MessagePanelStyle style) throws Exception {
        super(new BorderLayout());

        this.message = message;
        this.owner = owner;
        this.style = style;
        this.whisper = message.getWhisperNickname();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.messagePanel = getFormattedMessagePanel();
        this.customButtonsPanel = getButtonsPanel(whisper);

        init();
        initHandlers();
    }
    private void init(){
        this.removeAll();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.add(messagePanel,BorderLayout.CENTER);
        this.add(customButtonsPanel,BorderLayout.PAGE_END);
        switch (style){
            case SMALL:{
                this.whisperPanel = getWhisperPanel();
                this.add(whisperPanel,BorderLayout.PAGE_START);
                messagePanel.setVisible(false);
                customButtonsPanel.setVisible(false);
                break;
            }
            case MEDIUM:{
                break;
            }
            case BIGGEST:{
                this.whisperPanel = getWhisperPanel();
                this.add(whisperPanel,BorderLayout.PAGE_START);
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(true);
                break;
            }
            case HISTORY:{
                this.whisperPanel = getWhisperPanel();
                this.add(whisperPanel,BorderLayout.PAGE_START);
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(false);
                break;
            }
            case SPMODE:{
                this.whisperPanel = getWhisperPanel();
                this.add(whisperPanel,BorderLayout.PAGE_START);
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(true);
                break;
            }
        }
    }

    private JPanel getFormattedMessagePanel() throws Exception{
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel,BoxLayout.Y_AXIS));
        labelsPanel.setBackground(AppThemeColor.TRANSPARENT);

        JPanel tradePanel = new JPanel(new BorderLayout());
        tradePanel.setBackground(AppThemeColor.TRANSPARENT);
        tradePanel.setBorder(BorderFactory.createEmptyBorder(-11,0,-11,0));
        if(message instanceof ItemMessage) {
            JLabel itemLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_IMPORTANT, TextAlignment.CENTER, 16f, ((ItemMessage)message).getItemName());
            tradePanel.add(itemLabel,BorderLayout.CENTER);
        }else if(message instanceof CurrencyMessage){
            CurrencyMessage message = (CurrencyMessage) this.message;
            JPanel curCountPanel = new JPanel();
            curCountPanel.setPreferredSize(new Dimension(40,34));
            curCountPanel.setBackground(AppThemeColor.TRANSPARENT);

            String curCount = message.getCurrForSaleCount() % 1 == 0 ? String.valueOf(message.getCurrForSaleCount().intValue()) : String.valueOf(message.getCurrForSaleCount());
            JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, curCount);
            curCountPanel.add(priceLabel);
            JLabel currencyLabel;
            currencyLabel = componentsFactory.getIconLabel("currency/" + message.getCurrForSaleTitle() + ".png", 26);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            curPanel.add(curCountPanel);
            curPanel.add(currencyLabel);
            curPanel.setBorder(BorderFactory.createMatteBorder(4,0,0,0,AppThemeColor.TRANSPARENT));
            tradePanel.add(curPanel,BorderLayout.CENTER);
        }

        JPanel forPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        forPanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel separator = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, "=>");
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        forPanel.add(separator);
        String curCount = message.getCurCount() % 1 == 0 ? String.valueOf(message.getCurCount().intValue()) : String.valueOf(message.getCurCount());
        String currency = message.getCurrency();
        if(!Objects.equals(curCount, "") && currency != null) {
            JPanel curCountPanel = new JPanel();
            curCountPanel.setPreferredSize(new Dimension(40,34));
            curCountPanel.setBackground(AppThemeColor.TRANSPARENT);

            JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, curCount);
            curCountPanel.add(priceLabel);
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + currency + ".png", 26);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            curPanel.add(curCountPanel);
            curPanel.add(currencyLabel);
            forPanel.add(curPanel);
        }
        tradePanel.add(forPanel,BorderLayout.LINE_END);
        labelsPanel.add(tradePanel);
        String offer = message.getOffer();
        if(offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 16f, offer);
            offerLabel.setAlignmentY(Component.TOP_ALIGNMENT);
            labelsPanel.add(offerLabel);
        }
        return labelsPanel;
    }
    private JPanel getWhisperPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(-5,0,-5,0));
        topPanel.setBackground(AppThemeColor.MSG_HEADER);


        whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,cachedWhisperColor, TextAlignment.LEFTOP,15f,whisper + ":");
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(0,0,0,5)));
        whisperLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel nickNamePanel = componentsFactory.getTransparentPanel(new BorderLayout());
        if(style.equals(MessagePanelStyle.HISTORY)){
            nickNamePanel.add(whisperLabel,BorderLayout.CENTER);
        }else {
            nickNamePanel.add(getExpandButton(),BorderLayout.LINE_START);
            nickNamePanel.add(whisperLabel,BorderLayout.CENTER);
        }
        topPanel.add(nickNamePanel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/invite " + whisper));
            }
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        kickButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/kick " + whisper));
            }
        });
        tradeButton = componentsFactory.getIconButton("app/trade.png",14, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/tradewith " + whisper));
            }
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png",15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new OpenChatEvent(whisper));
            }
        });
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.MSG_HEADER,TooltipConstants.HIDE_PANEL);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new CloseMessagePanelEvent(MessagePanel.this));
            }
        });

        interactionPanel.add(getTimePanel());
        interactionPanel.add(inviteButton);
        interactionPanel.add(kickButton);
        interactionPanel.add(tradeButton);
        interactionPanel.add(openChatButton);
        if(!style.equals(MessagePanelStyle.HISTORY)) {
            interactionPanel.add(hideButton);
        }

        topPanel.add(interactionPanel,BorderLayout.LINE_END);
        return topPanel;
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
                    EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
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
        String iconPath = (style == MessagePanelStyle.SMALL) ? "app/expand-mp.png":"app/collapse-mp.png";
        expandButton = componentsFactory.getIconButton(iconPath, 16, AppThemeColor.MSG_HEADER,TooltipConstants.EXPAND_COLLAPSE);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!messagePanel.isVisible() && !customButtonsPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse-mp.png", 16));
                    messagePanel.setVisible(true);
                    customButtonsPanel.setVisible(true);
                }else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand-mp.png", 16));
                    messagePanel.setVisible(false);
                    customButtonsPanel.setVisible(false);
                }
                owner.pack();
            }
        });
        return expandButton;
    }

    public void setStyle(MessagePanelStyle style) {
        this.style = style;
        this.cachedTime = timeLabel.getText();
        init();
    }

    public MessagePanelStyle getStyle() {
        return style;
    }
    private JPanel getButtonsPanel(String whisper){
        Map<String, String> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(AppThemeColor.TRANSPARENT);

        buttonsConfig.forEach((title,value)->{
            JButton button = componentsFactory.getBorderedButton(title);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(button.isEnabled()) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("@" + whisper + " " + value));
                    }
                }

            });
            panel.add(button,0);
        });
        return panel;
    }
    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(PlayerJoinEvent.class, event -> {
            String nickName = ((PlayerJoinEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                cachedWhisperColor = AppThemeColor.TEXT_SUCCESS;
                tradeButton.setEnabled(true);
                EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
        EventRouter.INSTANCE.registerHandler(PlayerLeftEvent.class, event -> {
            String nickName = ((PlayerLeftEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_DISABLE);
                cachedWhisperColor = AppThemeColor.TEXT_DISABLE;
                tradeButton.setEnabled(false);
                EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
    }
}
