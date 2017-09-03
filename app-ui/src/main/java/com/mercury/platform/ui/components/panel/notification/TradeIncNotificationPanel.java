package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyPair;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public abstract class TradeIncNotificationPanel<T extends TradeNotificationDescriptor> extends TradeNotificationPanel<T, IncomingPanelController> {
    protected JPanel getHeader() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        root.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JPanel nickNamePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        this.nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 15f, this.getNicknameText());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 5));
        nickNamePanel.add(this.getExpandButton(), BorderLayout.LINE_START);
        JPanel headerPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 3, 0), AppThemeColor.MSG_HEADER);
        headerPanel.add(this.nicknameLabel);
        headerPanel.add(this.getForPanel("app/incoming_arrow.png"));
        nickNamePanel.add(headerPanel, BorderLayout.CENTER);
        root.add(nickNamePanel, BorderLayout.CENTER);

        JPanel opPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        JPanel interactionPanel = new JPanel(new GridLayout(1, 0, 4, 0));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteButton.addActionListener(e -> {
            this.controller.performInvite();
            root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER_SELECTED_BORDER));
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        kickButton.addActionListener(e -> {
            this.controller.performKick();
            if (this.notificationConfig.get().isDismissAfterKick()) {
                this.controller.performHide();
            }
        });
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> {
            this.controller.performOfferTrade();
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(inviteButton);
        interactionPanel.add(tradeButton);
        interactionPanel.add(kickButton);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        this.interactButtonMap.clear();
        this.interactButtonMap.put(HotKeyType.N_INVITE_PLAYER, inviteButton);
        this.interactButtonMap.put(HotKeyType.N_TRADE_PLAYER, tradeButton);
        this.interactButtonMap.put(HotKeyType.N_KICK_PLAYER, kickButton);
        this.interactButtonMap.put(HotKeyType.N_OPEN_CHAT, openChatButton);
        this.interactButtonMap.put(HotKeyType.N_CLOSE_NOTIFICATION, hideButton);

        JPanel timePanel = this.getTimePanel();
        opPanel.add(timePanel, BorderLayout.CENTER);
        opPanel.add(interactionPanel, BorderLayout.LINE_END);

        root.add(opPanel, BorderLayout.LINE_END);
        return root;
    }


    protected abstract JButton getStillInterestedButton();

    @Override
    protected void updateHotKeyPool() {
        this.hotKeysPool.clear();
        this.interactButtonMap.forEach((type, button) -> {
            HotKeyPair hotKeyPair = this.hotKeysConfig.get()
                    .getIncNHotKeysList()
                    .stream()
                    .filter(it -> it.getType().equals(type))
                    .findAny().orElse(null);
            if (!hotKeyPair.getDescriptor().getTitle().equals("...")) {
                this.hotKeysPool.put(hotKeyPair.getDescriptor(), button);
            }
        });
        this.initResponseButtonsPanel(this.notificationConfig.get().getButtons());
        Window windowAncestor = SwingUtilities.getWindowAncestor(TradeIncNotificationPanel.this);
        if (windowAncestor != null) {
            windowAncestor.pack();
        }
    }
}
