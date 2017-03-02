package com.mercury.platform.ui.components.panel;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.shared.pojo.ResponseButton;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.MessagePanelStyle;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.frame.impl.MessagesContainer;
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
import java.util.List;

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

    private boolean expanded = false;

    public MessagePanel(Message message, ComponentFrame owner, MessagePanelStyle style){
        super(new BorderLayout());
        this.message = message;
        this.owner = owner;
        this.style = style;
        this.whisper = message.getWhisperNickname();
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,1),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));

        this.messagePanel = getFormattedMessagePanel();
        this.customButtonsPanel = getButtonsPanel(whisper);
        init();
        initHandlers();
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
    }
    private void init(){
        this.removeAll();
        this.whisperPanel = getWhisperPanel();
        whisperPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(-7, 0, -8, 0)));
        if(style.equals(MessagePanelStyle.DOWNWARDS_SMALL) ||
                style.equals(MessagePanelStyle.HISTORY)) {
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
                messagePanel.setVisible(false);
                customButtonsPanel.setVisible(false);
                break;
            }
            case UPWARDS_SMALL:{
                messagePanel.setVisible(false);
                customButtonsPanel.setVisible(false);
                break;
            }
            case HISTORY:{
                messagePanel.setVisible(true);
                customButtonsPanel.setVisible(false);
                break;
            }
        }
        this.repaint();
        owner.pack();
    }

    private JPanel getFormattedMessagePanel(){
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
        String curCount = " ";
        if(message.getCurCount() > 0) {
            curCount = message.getCurCount() % 1 == 0 ? String.valueOf(message.getCurCount().intValue()) : String.valueOf(message.getCurCount());
        }
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
            JPanel buttonWrapper = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            buttonWrapper.add(getExpandButton());
            nickNamePanel.add(buttonWrapper,BorderLayout.LINE_START);
            nickNamePanel.add(whisperLabel,BorderLayout.CENTER);
        }
        topPanel.add(nickNamePanel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        interactionPanel.add(getTimePanel());
        if(!style.equals(MessagePanelStyle.HISTORY)) {
            JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
            inviteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/invite " + whisper));
                        if(message instanceof ItemMessage) {
                            if (((ItemMessage) message).getTabInfo() != null) {
                                EventRouter.INSTANCE.fireEvent(new ShowItemMeshEvent(message.getWhisperNickname(), ((ItemMessage) message).getTabInfo()));
                            }
                        }
                    }
                }
            });
            JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
            kickButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/kick " + whisper));
                    }
                }
            });
            tradeButton = componentsFactory.getIconButton("app/trade.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
            tradeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/tradewith " + whisper));
                    }
                }
            });
            interactionPanel.add(inviteButton);
            interactionPanel.add(kickButton);
            interactionPanel.add(tradeButton);
        }
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.INSTANCE.fireEvent(new OpenChatEvent(whisper));
                }
            }
        });

        interactionPanel.add(openChatButton);
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.INSTANCE.fireEvent(new CloseMessagePanelEvent(MessagePanel.this, message));
                }
            }
        });
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
        String iconPath = "app/default-mp.png";
        expandButton = componentsFactory.getIconButton(iconPath, 18, AppThemeColor.MSG_HEADER,"");
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
            expandButton.setIcon(componentsFactory.getIcon("app/expand-mp.png", 18));
            messagePanel.setVisible(true);
            customButtonsPanel.setVisible(true);
        }else {
            expandButton.setIcon(componentsFactory.getIcon("app/collapse-mp.png", 18));
            messagePanel.setVisible(true);
            customButtonsPanel.setVisible(true);
        }
        setMaximumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        setMinimumSize(new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        ((MessagesContainer)owner).onExpandMessage();
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
        ((MessagesContainer)owner).onCollapseMessage();
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
    private JPanel getButtonsPanel(String whisper){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(AppThemeColor.TRANSPARENT);
        initResponseButtons(panel);
        return panel;
    }
    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(PlayerJoinEvent.class, event -> {
            String nickName = ((PlayerJoinEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                cachedWhisperColor = AppThemeColor.TEXT_SUCCESS;
                if(!style.equals(MessagePanelStyle.HISTORY)) {
                    tradeButton.setEnabled(true);
                }
                EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
        EventRouter.INSTANCE.registerHandler(PlayerLeftEvent.class, event -> {
            String nickName = ((PlayerLeftEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_DISABLE);
                cachedWhisperColor = AppThemeColor.TEXT_DISABLE;
                if(!style.equals(MessagePanelStyle.HISTORY)) {
                    tradeButton.setEnabled(false);
                }
                EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
        EventRouter.INSTANCE.registerHandler(CustomButtonsChangedEvent.class, event -> {
            this.customButtonsPanel.removeAll();
            initResponseButtons(customButtonsPanel);
            owner.repaint();
        });
    }
    private void initResponseButtons(JPanel panel){
        List<ResponseButton> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig)->{
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle());
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("@" + whisper + " " + buttonConfig.getResponseText()));
                    }
                }
            });
            panel.add(button,0);
        });
    }
}
