package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.TradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.NotificationController;
import com.mercury.platform.ui.frame.other.ChatHistoryDefinition;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import rx.Subscription;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class TradeNotificationPanel<T extends TradeNotificationDescriptor, C extends NotificationController> extends NotificationPanel<T, C> {
    protected JPanel responseButtonsPanel;
    protected JLabel nicknameLabel;

    private Subscription chatSubscription;
    private Subscription playerJoinSubscription;
    private Subscription playerLeaveSubscription;
    @Override
    public void onViewInit() {
        super.onViewInit();
        this.responseButtonsPanel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER, 5, 2), AppThemeColor.FRAME);
        this.contentPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        switch (notificationConfig.get().getFlowDirections()) {
            case DOWNWARDS: {
                this.add(this.getHeader(), BorderLayout.PAGE_START);
                this.contentPanel.add(this.responseButtonsPanel, BorderLayout.PAGE_END);
                break;
            }
            case UPWARDS: {
                this.add(this.getHeader(), BorderLayout.PAGE_END);
                this.contentPanel.add(this.responseButtonsPanel, BorderLayout.PAGE_START);
                break;
            }
        }
        this.contentPanel.add(this.getMessagePanel(), BorderLayout.CENTER);
        this.add(this.contentPanel, BorderLayout.CENTER);
        this.updateHotKeyPool();
    }

    protected abstract JPanel getHeader();

    protected abstract JPanel getMessagePanel();

    protected void initResponseButtonsPanel(List<ResponseButtonDescriptor> buttonsConfig) {
        this.responseButtonsPanel.removeAll();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig) -> {
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(), 16f, AppThemeColor.RESPONSE_BUTTON, AppThemeColor.RESPONSE_BUTTON_BORDER, AppThemeColor.RESPONSE_BUTTON);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1),
                    BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
            ));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                            BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1),
                            BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                    ));
                }
            });
            button.addActionListener(action -> {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1),
                        BorderFactory.createMatteBorder(3, 9, 3, 9, AppThemeColor.RESPONSE_BUTTON)
                ));
            });
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if (buttonConfig.isClose()) {
                    this.controller.performHide();
                }
            });
            this.hotKeysPool.put(buttonConfig.getHotKeyDescriptor(), button);
            this.responseButtonsPanel.add(button);
        });
    }

    @Override
    public void subscribe() {
        super.subscribe();
        this.chatSubscription = MercuryStoreCore.plainMessageSubject.subscribe(message -> {
            if (this.data.getWhisperNickname().equals(message.getNickName())) {
                this.data.getRelatedMessages().add(message);
            }
        });
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
        this.chatSubscription.unsubscribe();
        this.playerLeaveSubscription.unsubscribe();
        this.playerJoinSubscription.unsubscribe();
    }

    protected JLabel getHistoryButton() {
        JLabel chatHistory = componentsFactory.getIconLabel("app/chat_history.png", 15);
        chatHistory.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        chatHistory.addMouseListener(new MouseAdapter() {
            Border prevBorder;

            @Override
            public void mouseEntered(MouseEvent e) {
                prevBorder = chatHistory.getBorder();
                chatHistory.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)));
                chatHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
                MercuryStoreUI.showChatHistorySubject.onNext(new ChatHistoryDefinition(data.getRelatedMessages(), e.getLocationOnScreen()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chatHistory.setBorder(prevBorder);
                chatHistory.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                MercuryStoreUI.hideChatHistorySubject.onNext(true);
            }
        });
        return chatHistory;
    }

    protected JPanel getForPanel(String signIconPath) {
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setBackground(AppThemeColor.MSG_HEADER);
        JLabel separator = componentsFactory.getIconLabel(signIconPath, 16);
        forPanel.add(separator, BorderLayout.LINE_START);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurCount(), this.data.getCurrency());
        if (currencyPanel != null) {
            forPanel.add(currencyPanel, BorderLayout.CENTER);
        }
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
            JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 26);
            JPanel curPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.MSG_HEADER);
            curPanel.setAlignmentX(SwingConstants.LEFT);
            JLabel countLabel = this.componentsFactory.getTextLabel(curCountStr, FontStyle.BOLD, 17f);
            countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            curPanel.add(countLabel);
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }

    protected JLabel getOfferLabel() {
        String offer = this.data.getOffer();
        if (offer != null && offer.trim().length() > 0) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 16f, offer);
            offerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            return offerLabel;
        }
        return null;
    }

    protected String getNicknameText() {
        String whisperNickname = data.getWhisperNickname();
        String result = whisperNickname + ":";
        if (this.notificationConfig.get().isShowLeague()) {
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
}
