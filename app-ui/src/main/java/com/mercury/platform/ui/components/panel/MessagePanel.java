package com.mercury.platform.ui.components.panel;


import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.SCEvent;
import com.mercury.platform.shared.events.SCEventHandler;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.label.FontStyle;
import com.mercury.platform.ui.components.fields.label.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.CustomButtonFactory;
import com.mercury.platform.ui.misc.MessageParser;

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
    private List<String> supportedIcons;
    private MessagePanelStyle style;

    private int x;
    private int y;

    private String whisper;
    private JLabel whisperLabel;
    private JButton tradeButton;

    private Timer timeAgo;
    private String cachedTime = "0m ago";
    private JLabel timeLabel;
    private Color cachedWhisperColor = AppThemeColor.TEXT_NICKNAME;

    private Map<String,String> parsedMessage;

    private JPanel whisperPanel;
    private JPanel messagePanel;
    private JPanel customButtonsPanel;

    private JLabel itemLabel;
    public MessagePanel(String whisper, String message, MessagePanelStyle style) {
        super(new BorderLayout());
        supportedIcons = new ArrayList<>();
        supportedIcons.addAll(Arrays.asList("chaos","exalted","fusing","vaal"));

        this.style = style;
        this.whisper = whisper;
        this.parsedMessage = MessageParser.parse(message);

        this.removeAll();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.whisperPanel = getWhisperPanel();
        this.messagePanel = getFormattedMessagePanel();
        this.customButtonsPanel = CustomButtonFactory.getButtonsPanel(whisper);

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
                        EventRouter.fireEvent(new ChatCommandEvent("@" + whisper + " " + "Hey, are u still interested in " + parsedMessage.get("itemName") + "?"));
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

        JPanel tradePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tradePanel.setBackground(AppThemeColor.TRANSPARENT);
        tradePanel.setBorder(BorderFactory.createEmptyBorder(-11,0,-11,0));
        itemLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT, TextAlignment.CENTER,16f,parsedMessage.get("itemName"));
        tradePanel.add(itemLabel);
        tradePanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE,TextAlignment.CENTER,17f,"=>"));
        String curCount = parsedMessage.get("curCount");
        String currency = parsedMessage.get("currency");
        if(curCount != null && currency != null) {
            JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, curCount);
            JLabel currencyLabel;
            if (supportedIcons.contains(currency)){
                currencyLabel = componentsFactory.getIconLabel("currency/" + currency + ".png", 26);
            } else {
                currencyLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER, 17f, currency);
            }
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            curPanel.setBackground(AppThemeColor.TRANSPARENT);
            curPanel.add(priceLabel);
            curPanel.add(currencyLabel);
            tradePanel.add(curPanel);
        }
        labelsPanel.add(tradePanel);
        String offer = parsedMessage.get("offer");
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
        topPanel.setBackground(AppThemeColor.TRANSPARENT);

        whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,cachedWhisperColor, TextAlignment.LEFTOP,15f,whisper + ":");
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(0,5,0,5)));
        whisperLabel.setVerticalAlignment(SwingConstants.CENTER);

        topPanel.add(whisperLabel,BorderLayout.LINE_START);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 14);
        inviteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/invite " + whisper));
            }
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 14);
        kickButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/kick " + whisper));
            }
        });
        tradeButton = componentsFactory.getIconButton("app/trade.png",14);
        tradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/tradewith " + whisper));
            }
        });
        tradeButton.setEnabled(false);
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png",15);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new OpenChatEvent(whisper));
            }
        });
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 14);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new CloseMessagePanelEvent(MessagePanel.this));
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
                    EventRouter.fireEvent(new RepaintEvent.RepaintMessagePanel());
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
                EventRouter.fireEvent(new DraggedMessageFrameEvent(e.getLocationOnScreen().x -x,e.getLocationOnScreen().y - y));
            }
        });
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(PlayerJoinEvent.class, event -> {
            String nickName = ((PlayerJoinEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                cachedWhisperColor = AppThemeColor.TEXT_SUCCESS;
                tradeButton.setEnabled(true);
                EventRouter.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
        EventRouter.registerHandler(PlayerLeftEvent.class, event -> {
            String nickName = ((PlayerLeftEvent) event).getNickName();
            if(nickName.equals(whisper)){
                whisperLabel.setForeground(AppThemeColor.TEXT_DENIED);
                cachedWhisperColor = AppThemeColor.TEXT_DENIED;
                tradeButton.setEnabled(false);
                EventRouter.fireEvent(new RepaintEvent.RepaintMessagePanel());
            }
        });
    }
}
