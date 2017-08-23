package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.OutgoingPanelController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class OutgoingNotificationPanel<T extends TradeNotificationDescriptor> extends NotificationPanel<T, OutgoingPanelController> {
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeysConfig;
    private JPanel responseButtonsPanel;
    private JPanel bottomPanel;

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.config = Configuration.get().notificationConfiguration();
        this.hotKeysConfig = Configuration.get().hotKeysConfiguration();
        this.add(this.getHeader(), BorderLayout.PAGE_START);

        this.responseButtonsPanel = new JPanel(new GridLayout(1, 0, 0, 5));
        this.responseButtonsPanel.setBackground(AppThemeColor.FRAME);
        this.bottomPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        this.bottomPanel.add(this.getContentPanel(), BorderLayout.CENTER);
        this.bottomPanel.add(this.componentsFactory.wrapToSlide(this.responseButtonsPanel, AppThemeColor.FRAME), BorderLayout.LINE_END);
        this.add(bottomPanel, BorderLayout.CENTER);
        this.updateHotKeyPool();
    }

    private JPanel getHeader() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);

        JPanel nickNamePanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 15f, this.data.getWhisperNickname());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        nickNamePanel.add(this.getExpandButton(), BorderLayout.LINE_START);
        nickNamePanel.add(nicknameLabel, BorderLayout.CENTER);
        root.add(nickNamePanel, BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new GridLayout(1, 0, 6, 0));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton visiteHideout = componentsFactory.getIconButton("app/visiteHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.VISIT_HO);
        visiteHideout.addActionListener(e -> this.controller.visitHideout());
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> this.controller.performOfferTrade());
        JButton leaveButton = componentsFactory.getIconButton("app/leave.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.LEAVE);
        leaveButton.addActionListener(e -> {
            this.controller.performLeave(this.config.get().getPlayerNickname());
            if (this.config.get().isDismissAfterLeave()) {
                this.controller.performHide();
            }
        });
//        JButton backToHo = componentsFactory.getIconButton("app/backToHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
//        backToHo.addActionListener(e -> controller.backToHideout());
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(visiteHideout);
        interactionPanel.add(tradeButton);
        interactionPanel.add(leaveButton);
//        interactionPanel.add(backToHo);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);
        this.interactButtonMap.clear();
        this.interactButtonMap.put(HotKeyType.N_VISITE_HIDEOUT, visiteHideout);
        this.interactButtonMap.put(HotKeyType.N_TRADE_PLAYER, tradeButton);
        this.interactButtonMap.put(HotKeyType.N_LEAVE, leaveButton);
//        this.interactButtonMap.put(HotKeyType.N_BACK_TO_HIDEOUT,backToHo);
        this.interactButtonMap.put(HotKeyType.N_OPEN_CHAT, openChatButton);
        this.interactButtonMap.put(HotKeyType.N_CLOSE_NOTIFICATION, hideButton);

        root.add(interactionPanel, BorderLayout.LINE_END);
        return root;
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
            if (!hotKeyPair.getDescriptor().getTitle().equals("...")) {
                this.hotKeysPool.put(hotKeyPair.getDescriptor(), button);
            }
        });
        this.initResponseButtonPanel();
        Window windowAncestor = SwingUtilities.getWindowAncestor(OutgoingNotificationPanel.this);
        if (windowAncestor != null) {
            windowAncestor.pack();
        }
    }

    private void initResponseButtonPanel() {
        this.responseButtonsPanel.removeAll();
        List<ResponseButtonDescriptor> buttonsConfig = this.config.get().getOutButtons();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig) -> {
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(), 16f, AppThemeColor.RESPONSE_BUTTON, AppThemeColor.RESPONSE_BUTTON_BORDER, AppThemeColor.RESPONSE_BUTTON);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1),
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                    ));
                }
            });
            button.addActionListener(action -> {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                        BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON, 3)
                ));
            });
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if (buttonConfig.isClose()) {
                    this.controller.performHide();
                }
            });
            this.responseButtonsPanel.add(button);
            this.hotKeysPool.put(buttonConfig.getHotKeyDescriptor(), button);
        });
    }

    protected JPanel getFromPanel() {
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setBackground(AppThemeColor.FRAME);
        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurCount(), this.data.getCurrency());
        if (currencyPanel != null) {
            currencyPanel.setPreferredSize(new Dimension(70, 36));
            forPanel.add(currencyPanel, BorderLayout.LINE_START);
        }
        JLabel separator = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.CENTER,
                18f,
                "=>");
        forPanel.add(separator, BorderLayout.CENTER);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        return forPanel;
    }

    protected JPanel getCurrencyPanel(Double curCount, String curIconPath) {
        String curCountStr = " ";
        if (curCount > 0) {
            curCountStr = curCount % 1 == 0 ?
                    String.valueOf(curCount.intValue()) :
                    String.valueOf(curCount);
        }
        if (!Objects.equals(curCountStr, "") && curIconPath != null) {
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 28);
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            curPanel.setBackground(AppThemeColor.FRAME);
            curPanel.add(this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, null, curCountStr));
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    protected abstract JPanel getContentPanel();

    @Override
    public void onViewDestroy() {
        super.onViewDestroy();
    }

    private JButton getExpandButton() {
        String iconPath = "app/expand-mp.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 18f, AppThemeColor.MSG_HEADER, "");
        expandButton.addActionListener(action -> {
            if (this.bottomPanel.isVisible()) {
                this.bottomPanel.setVisible(false);
                expandButton.setIcon(this.componentsFactory.getIcon("app/default-mp.png", 18f));
            } else {
                this.bottomPanel.setVisible(true);
                expandButton.setIcon(this.componentsFactory.getIcon("app/expand-mp.png", 18f));
            }
            SwingUtilities.getWindowAncestor(OutgoingNotificationPanel.this).pack();
        });
        return expandButton;
    }
}
