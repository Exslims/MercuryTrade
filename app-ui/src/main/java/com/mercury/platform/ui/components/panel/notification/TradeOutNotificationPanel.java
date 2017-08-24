package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyPair;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.OutgoingPanelController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import org.apache.commons.lang3.StringUtils;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;


public abstract class TradeOutNotificationPanel<T extends TradeNotificationDescriptor> extends TradeNotificationPanel<T, OutgoingPanelController> {
    private JLabel nicknameLabel;
    private Subscription playerJoinSubscription;
    private Subscription playerLeaveSubscription;

    @Override
    protected JPanel getHeader() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);

        JPanel nickNamePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        this.nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 15f, this.data.getWhisperNickname());
        nickNamePanel.add(this.getExpandButton(), BorderLayout.LINE_START);
        nickNamePanel.add(this.nicknameLabel, BorderLayout.CENTER);
        nickNamePanel.add(this.getForPanel("app/outgoing_arrow.png"), BorderLayout.LINE_END);
        root.add(nickNamePanel, BorderLayout.CENTER);

        JPanel opPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        JPanel interactionPanel = new JPanel(new GridLayout(1, 0, 3, 0));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton visiteHideout = componentsFactory.getIconButton("app/visiteHideout.png", 16, AppThemeColor.MSG_HEADER, TooltipConstants.VISIT_HO);
        visiteHideout.addActionListener(e -> this.controller.visitHideout());
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> this.controller.performOfferTrade());
        JButton leaveButton = componentsFactory.getIconButton("app/leave.png", 16, AppThemeColor.MSG_HEADER, TooltipConstants.LEAVE);
        leaveButton.addActionListener(e -> {
            this.controller.performLeave(this.notificationConfig.get().getPlayerNickname());
            if (this.notificationConfig.get().isDismissAfterLeave()) {
                this.controller.performHide();
            }
        });
        JLabel historyLabel = this.getHistoryButton();
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(visiteHideout);
        interactionPanel.add(tradeButton);
        interactionPanel.add(leaveButton);
        interactionPanel.add(historyLabel);
        interactionPanel.add(hideButton);
        this.interactButtonMap.clear();
        this.interactButtonMap.put(HotKeyType.N_VISITE_HIDEOUT, visiteHideout);
        this.interactButtonMap.put(HotKeyType.N_TRADE_PLAYER, tradeButton);
        this.interactButtonMap.put(HotKeyType.N_LEAVE, leaveButton);
        this.interactButtonMap.put(HotKeyType.N_CLOSE_NOTIFICATION, hideButton);

        JPanel timePanel = this.getTimePanel();
        opPanel.add(timePanel, BorderLayout.CENTER);
        opPanel.add(interactionPanel, BorderLayout.LINE_END);

        root.add(opPanel, BorderLayout.LINE_END);
        return root;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.playerJoinSubscription = MercuryStoreCore.playerJoinSubject.subscribe(nickname -> {
            if (this.data.getWhisperNickname().equals(nickname)) {
                this.nicknameLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
            }
        });
        this.playerLeaveSubscription = MercuryStoreCore.playerLeftSubject.subscribe(nickname -> {
            if (this.data.getWhisperNickname().equals(nickname)) {
                this.nicknameLabel.setForeground(AppThemeColor.TEXT_DISABLE);
            }
        });
    }

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
        this.playerLeaveSubscription.unsubscribe();
        this.playerJoinSubscription.unsubscribe();
    }

    protected JButton getRepeatButton() {
        JButton repeatButton = componentsFactory.getIconButton("app/reload-history.png", 15, AppThemeColor.FRAME, TooltipConstants.STILL_INTERESTED);
        repeatButton.addActionListener(action -> {
            this.controller.performResponse(StringUtils.substringAfter(this.data.getSourceString(), ":"));
        });
        return repeatButton;
    }

    @Override
    protected void updateHotKeyPool() {
        this.hotKeysPool.clear();
        this.interactButtonMap.forEach((type, button) -> {
            HotKeyPair hotKeyPair = this.hotKeysConfig.get()
                    .getOutNHotKeysList()
                    .stream()
                    .filter(it -> it.getType().equals(type))
                    .findAny().orElse(null);
            if (hotKeyPair != null && !hotKeyPair.getDescriptor().getTitle().equals("...")) {
                this.hotKeysPool.put(hotKeyPair.getDescriptor(), button);
            }
        });
        this.initResponseButtonsPanel(this.notificationConfig.get().getOutButtons());
        Window windowAncestor = SwingUtilities.getWindowAncestor(TradeOutNotificationPanel.this);
        if (windowAncestor != null) {
            windowAncestor.pack();
        }
    }
}
