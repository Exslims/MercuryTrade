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
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.shared.MessageParser;

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
    private MessagePanelStyle style;

    private int x;
    private int y;

    private String whisper;
    private JLabel whisperLabel;
    private JButton tradeButton;

    private Message message;

    private Timer timeAgo;
    private String cachedTime = "0m ago";
    private JLabel timeLabel;
    private Color cachedWhisperColor = AppThemeColor.TEXT_NICKNAME;
    private JPanel whisperPanel;
    private JPanel messagePanel;
    private JPanel customButtonsPanel;

    public MessagePanel(String whisper, Message message, MessagePanelStyle style) {
        super(new BorderLayout());

        this.message = message;
        this.style = style;
        this.whisper = whisper;

        this.removeAll();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.whisperPanel = getWhisperPanel();
        this.messagePanel = getFormattedMessagePanel();
        this.customButtonsPanel = getButtonsPanel(whisper);

        init();
        initHandlers();
    }
    private void init(){
        this.removeAll();
        this.setBackground(AppThemeColor.TRANSPARENT);
        switch (style){
            case SMALL:{
                this.add(whisperPanel,BorderLayout.PAGE_START);
                break;
            }
            case MEDIUM:{
                this.add(whisperPanel,BorderLayout.PAGE_START);
                this.add(messagePanel,BorderLayout.CENTER);
                break;
            }
            case BIGGEST:{
                this.add(whisperPanel,BorderLayout.PAGE_START);
                this.add(messagePanel,BorderLayout.CENTER);
                this.add(customButtonsPanel,BorderLayout.PAGE_END);
                break;
            }
            case HISTORY:{
                this.add(whisperPanel,BorderLayout.PAGE_START);
                this.add(messagePanel,BorderLayout.CENTER);
                JButton stillIntButton = componentsFactory.getBorderedButton("interested?");
                stillIntButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("@" + whisper + " " + "Hey, are u still interested in " + ((ItemMessage)message).getItemName() + "?"));
                    }
                });
                customButtonsPanel.add(stillIntButton,0);
                this.add(customButtonsPanel,BorderLayout.PAGE_END);
                break;
            }
        }
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
            curCountPanel.setPreferredSize(new Dimension(40,30));
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
            curCountPanel.setPreferredSize(new Dimension(40,30));
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
        if(offer != null && offer.length() > 2) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 16f, offer);
            offerLabel.setAlignmentY(Component.TOP_ALIGNMENT);
            labelsPanel.add(offerLabel);
        }
        return labelsPanel;
    }
    private JPanel getWhisperPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(-5,0,-5,0));
        topPanel.setBackground(AppThemeColor.HEADER);

        whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,cachedWhisperColor, TextAlignment.LEFTOP,15f,whisper + ":");
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(0,5,0,5)));
        whisperLabel.setVerticalAlignment(SwingConstants.CENTER);

        topPanel.add(whisperLabel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14, AppThemeColor.HEADER);
        inviteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/invite " + whisper));
            }
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14, AppThemeColor.HEADER);
        kickButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/kick " + whisper));
            }
        });
        tradeButton = componentsFactory.getIconButton("app/trade.png",14, AppThemeColor.HEADER);
        tradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/tradewith " + whisper));
            }
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png",15, AppThemeColor.HEADER);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new OpenChatEvent(whisper));
            }
        });
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.HEADER);
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
                        labelText = minute + "m ago";
                    } else if (hours > 0) {
                        labelText = hours + "h " + minute + "m ago";
                    } else if (day > 0) {
                        labelText = day + "d " + hours + "h " + minute + "m ago";
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

    public void setStyle(MessagePanelStyle style) {
        this.style = style;
        this.cachedTime = timeLabel.getText();
        init();
    }

    public MessagePanelStyle getStyle() {
        return style;
    }
    public void setAsTopMessage(){
        whisperLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        whisperLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new DraggedMessageFrameEvent(e.getLocationOnScreen().x -x,e.getLocationOnScreen().y - y));
            }
        });
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
                whisperLabel.setForeground(AppThemeColor.TEXT_DENIED);
                cachedWhisperColor = AppThemeColor.TEXT_DENIED;
                tradeButton.setEnabled(false);
                EventRouter.INSTANCE.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
    }
}
