package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.HistoryController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class HistoryNotificationPanel extends NotificationPanel<NotificationDescriptor, HistoryController> {
    @Override
    public void onViewInit() {
        super.onViewInit();
        this.add(this.getHeader(), BorderLayout.PAGE_START);
        JLabel sourceLabel = this.componentsFactory.getTextLabel(this.data.getSourceString(), FontStyle.REGULAR, 17f);
        sourceLabel.setBackground(AppThemeColor.FRAME);
        sourceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        sourceLabel.setVerticalAlignment(SwingConstants.TOP);
        this.add(sourceLabel, BorderLayout.CENTER);
    }

    private JPanel getHeader() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        root.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP, 15f, this.data.getWhisperNickname());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 5));
        root.add(nicknameLabel, BorderLayout.CENTER);

        JPanel opPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.MSG_HEADER);
        JPanel interactionPanel = new JPanel(new GridLayout(1, 0, 4, 0));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton reloadButton = componentsFactory.getIconButton("app/reload-history.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HISTORY_RELOAD);
        reloadButton.addActionListener(e -> {
            this.controller.reload();
        });
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        interactionPanel.add(reloadButton);
        interactionPanel.add(openChatButton);
        opPanel.add(interactionPanel, BorderLayout.CENTER);
        root.add(opPanel, BorderLayout.LINE_END);
        return root;
    }

    @Override
    protected void updateHotKeyPool() {
        /*NOP*/
    }
}
