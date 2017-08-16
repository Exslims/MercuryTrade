package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ScannerDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.controller.ScannerPanelController;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class ScannerNotificationPanel extends NotificationPanel<NotificationDescriptor,ScannerPanelController> {
    private PlainConfigurationService<ScannerDescriptor> config;
    @Override
    public void onViewInit() {
        super.onViewInit();
        this.config = Configuration.get().scannerConfiguration();
        this.add(this.getHeader(),BorderLayout.PAGE_START);
        JLabel sourceLabel = this.componentsFactory.getTextLabel(this.data.getSourceString(),FontStyle.BOLD,17f);
        sourceLabel.setBackground(AppThemeColor.FRAME);
        sourceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        sourceLabel.setVerticalAlignment(SwingConstants.TOP);
        this.add(this.componentsFactory.wrapToSlide(sourceLabel,AppThemeColor.FRAME,2,2,2,2),BorderLayout.CENTER);
        this.validate();
    }
    private JPanel getHeader(){
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);

        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,this.data.getWhisperNickname());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        root.add(nicknameLabel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new GridLayout(1,0,4,0));
        interactionPanel.setPreferredSize(new Dimension(234,26));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton inviteMeButton = componentsFactory.getIconButton("app/chat_scanner_response.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        inviteMeButton.addActionListener(e -> this.controller.performResponse(this.config.get().getResponseMessage()));
        JButton visiteHideout = componentsFactory.getIconButton("app/visiteHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        visiteHideout.addActionListener(e -> this.controller.visitHideout());
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> this.controller.performTrade());
        JButton leaveButton = componentsFactory.getIconButton("app/leave.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        leaveButton.addActionListener(e -> {
            this.controller.performLeave();
        });
        JButton backToHo = componentsFactory.getIconButton("app/backToHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        backToHo.addActionListener(e -> controller.backToHideout());
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(inviteMeButton);
        interactionPanel.add(visiteHideout);
        interactionPanel.add(tradeButton);
        interactionPanel.add(leaveButton);
        interactionPanel.add(backToHo);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        root.add(interactionPanel,BorderLayout.LINE_END);
        return root;
    }
    @Override
    public void subscribe() {

    }
    @Override
    public void onViewDestroy() {

    }
}
