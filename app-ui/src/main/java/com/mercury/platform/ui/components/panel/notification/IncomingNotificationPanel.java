package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import org.apache.commons.lang3.StringUtils;
import rx.Subscription;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public abstract class IncomingNotificationPanel<T extends TradeNotificationDescriptor> extends NotificationPanel<T,IncomingPanelController>{
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeysConfig;
    private JPanel messagePanel;
    private JPanel responseButtonsPanel;
    private JPanel chatPanel;
    private JPanel chatContainer;
    private Subscription chatSubscription;
    @Override
    public void onViewInit() {
        super.onViewInit();
        this.config = Configuration.get().notificationConfiguration();
        this.hotKeysConfig = Configuration.get().hotKeysConfiguration();
        this.add(this.getHeader(),BorderLayout.PAGE_START);
        this.messagePanel = this.getMessagePanel();
        this.responseButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,2));
        this.responseButtonsPanel.setBackground(AppThemeColor.FRAME);
        this.chatPanel = this.getChatPanel();
        this.chatPanel.setVisible(false);
        this.add(this.messagePanel,BorderLayout.CENTER);
        this.add(this.responseButtonsPanel,BorderLayout.PAGE_END);
        this.updateHotKeyPool();
    }
    private JPanel getHeader(){
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        root.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

        JPanel nickNamePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,this.getNicknameText());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0,4,0,5));
        nickNamePanel.add(this.getExpandButton(),BorderLayout.LINE_START);
        nickNamePanel.add(nicknameLabel,BorderLayout.CENTER);
        root.add(nickNamePanel,BorderLayout.CENTER);

        JPanel opPanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.MSG_HEADER);
        JPanel interactionPanel = new JPanel(new GridLayout(1,0,4,0));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteButton.addActionListener(e -> {
            this.controller.performInvite();
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER_SELECTED_BORDER));
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        kickButton.addActionListener(e -> {
            this.controller.performKick();
            if(this.config.get().isDismissAfterKick()){
                this.controller.performHide();
            }
        });
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> {
            this.controller.performOfferTrade();
        });
        JButton showChatButton = componentsFactory.getIconButton("app/chat_history.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.SWITCH_CHAT);
        showChatButton.addActionListener(e -> {
            if(this.chatPanel.isVisible()){
                this.chatPanel.setVisible(false);
                this.remove(this.chatPanel);
                this.add(this.messagePanel,BorderLayout.CENTER);
                this.add(this.responseButtonsPanel,BorderLayout.PAGE_END);
            }else {
                this.remove(this.messagePanel);
                this.remove(this.responseButtonsPanel);
                this.chatPanel.setVisible(true);
                this.add(this.chatPanel,BorderLayout.CENTER);
            }
            SwingUtilities.getWindowAncestor(IncomingNotificationPanel.this).pack();
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        JButton stillInterestedButton = getStillInterestedButton();
        interactionPanel.add(inviteButton);
        interactionPanel.add(tradeButton);
        interactionPanel.add(kickButton);
        interactionPanel.add(stillInterestedButton);
        interactionPanel.add(showChatButton);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        this.interactButtonMap.clear();
        this.interactButtonMap.put(HotKeyType.N_INVITE_PLAYER,inviteButton);
        this.interactButtonMap.put(HotKeyType.N_TRADE_PLAYER,tradeButton);
        this.interactButtonMap.put(HotKeyType.N_KICK_PLAYER,kickButton);
        this.interactButtonMap.put(HotKeyType.N_STILL_INTERESTING,stillInterestedButton);
        this.interactButtonMap.put(HotKeyType.N_SWITCH_CHAT,showChatButton);
        this.interactButtonMap.put(HotKeyType.N_OPEN_CHAT,openChatButton);
        this.interactButtonMap.put(HotKeyType.N_CLOSE_NOTIFICATION,hideButton);

        JPanel timePanel = this.getTimePanel();
        timePanel.setPreferredSize(new Dimension(50,26));
        opPanel.add(timePanel,BorderLayout.CENTER);
        opPanel.add(interactionPanel,BorderLayout.LINE_END);

        root.add(opPanel,BorderLayout.LINE_END);
        return root;
    }

    @Override
    protected void updateHotKeyPool() {
        this.hotKeysPool.clear();
        this.interactButtonMap.forEach((type, button) -> {
            HotKeyPair hotKeyPair = this.hotKeysConfig.get()
                    .getIncNHotKeysList()
                    .stream()
                    .filter(it -> it.getType().equals(type))
                    .findAny().orElse(null);
            if(!hotKeyPair.getDescriptor().getTitle().equals("...")) {
                this.hotKeysPool.put(hotKeyPair.getDescriptor(), button);
            }
        });
        this.initResponseButtonsPanel();
        Window windowAncestor = SwingUtilities.getWindowAncestor(IncomingNotificationPanel.this);
        if(windowAncestor != null) {
            windowAncestor.pack();
        }
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.chatSubscription = MercuryStoreCore.plainMessageSubject.subscribe(message -> {
           if(this.data.getWhisperNickname().equals(message.getNickName())){
               this.chatContainer.add(this.componentsFactory.getTextLabel((message.isIncoming()? "From: ":"To: ") + message.getMessage()));
               SwingUtilities.getWindowAncestor(IncomingNotificationPanel.this).pack();
           }
        });
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
        this.chatSubscription.unsubscribe();
    }
    private JPanel getChatPanel(){
        this.chatContainer = new VerticalScrollContainer();
        this.chatContainer.setLayout(new BoxLayout(this.chatContainer,BoxLayout.Y_AXIS));
        this.chatContainer.setBackground(AppThemeColor.FRAME);
        this.chatContainer.add(this.componentsFactory.getTextLabel("From:" + StringUtils.substringAfter(this.data.getSourceString(),this.data.getWhisperNickname() +":")));
        return this.componentsFactory.wrapToSlide(this.chatContainer,AppThemeColor.FRAME);
    }
    protected JPanel getForPanel(){
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setPreferredSize(new Dimension(110,36));
        forPanel.setBackground(AppThemeColor.FRAME);
        JLabel separator = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_DEFAULT,
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
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 28);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            curPanel.setBackground(AppThemeColor.FRAME);
            curPanel.add(this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER,17f,null, curCountStr));
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }
    protected JLabel getOfferLabel(){
        String offer = this.data.getOffer();
        if(offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 16f, offer);
            offerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return offerLabel;
        }
        return null;
    }
    protected abstract JPanel getMessagePanel();
    protected abstract JButton getStillInterestedButton();
    private void initResponseButtonsPanel(){
        this.responseButtonsPanel.removeAll();
        List<ResponseButtonDescriptor> buttonsConfig = this.config.get().getButtons();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig)->{
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(),16f,AppThemeColor.RESPONSE_BUTTON, AppThemeColor.RESPONSE_BUTTON_BORDER,AppThemeColor.RESPONSE_BUTTON);
            if(buttonConfig.getTitle().length() < 10) {
                button.setPreferredSize(new Dimension(60, 26));
            }
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder( BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder( BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                    ));
                }
            });
            button.addActionListener(action -> {
                button.setBorder( BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                        BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                ));
            });
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if(buttonConfig.isClose()){
                    this.controller.performHide();
                }
            });
            this.hotKeysPool.put(buttonConfig.getHotKeyDescriptor(),button);
            this.responseButtonsPanel.add(button);
        });
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
        String iconPath = "app/expand-mp.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 18f, AppThemeColor.MSG_HEADER,"");
        expandButton.addActionListener(action -> {
            if(this.messagePanel.isVisible()){
                this.messagePanel.setVisible(false);
                this.responseButtonsPanel.setVisible(false);
                expandButton.setIcon(this.componentsFactory.getIcon("app/default-mp.png",18f));
            }else {
                this.messagePanel.setVisible(true);
                this.responseButtonsPanel.setVisible(true);
                expandButton.setIcon(this.componentsFactory.getIcon("app/expand-mp.png",18f));
            }
            SwingUtilities.getWindowAncestor(IncomingNotificationPanel.this).pack();
        });
        return expandButton;
    }
}
