package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
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
import java.util.*;
import java.util.List;

public abstract class OutgoingNotificationPanel<T extends TradeNotificationDescriptor> extends NotificationPanel<T,OutgoingPanelController> {
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    @Override
    public void onViewInit() {
        this.config = Configuration.get().notificationConfiguration();
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1)));
        this.setLayout(new BorderLayout());
        this.add(this.getHeader(),BorderLayout.PAGE_START);
        JPanel bottomPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        bottomPanel.add(this.getContentPanel(),BorderLayout.CENTER);
        bottomPanel.add(this.componentsFactory.wrapToSlide(this.getResponseButtonsPanel(),AppThemeColor.FRAME),BorderLayout.LINE_END);
        this.add(bottomPanel,BorderLayout.CENTER);
    }

    private JPanel getHeader(){
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);

        JLabel nicknameLabel = this.componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,this.data.getWhisperNickname());
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        root.add(nicknameLabel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new GridLayout(1,0,4,0));
        interactionPanel.setPreferredSize(new Dimension(200,26));
        interactionPanel.setBackground(AppThemeColor.MSG_HEADER);
        JButton visiteHideout = componentsFactory.getIconButton("app/visiteHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.INVITE);
        visiteHideout.addActionListener(e -> this.controller.visitHideout());
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.TRADE);
        tradeButton.addActionListener(e -> this.controller.performTrade());
        JButton leaveButton = componentsFactory.getIconButton("app/leave.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.KICK);
        leaveButton.addActionListener(e -> {
            this.controller.performLeave();
            if(this.config.get().isDismissAfterKick()){
                this.controller.performHide();
            }
        });
        JButton backToHo = componentsFactory.getIconButton("app/backToHideout.png", 17, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        backToHo.addActionListener(e -> controller.backToHideout());
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.OPEN_CHAT);
        openChatButton.addActionListener(e -> controller.performOpenChat());
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 15, AppThemeColor.MSG_HEADER, TooltipConstants.HIDE_PANEL);
        hideButton.addActionListener(action -> {
            this.controller.performHide();
        });
        interactionPanel.add(visiteHideout);
        interactionPanel.add(tradeButton);
        interactionPanel.add(leaveButton);
        interactionPanel.add(backToHo);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        root.add(interactionPanel,BorderLayout.LINE_END);
        return root;
    }
    private JPanel getResponseButtonsPanel(){
        JPanel root = new JPanel(new GridLayout(1,0,0,5));
        root.setBackground(AppThemeColor.FRAME);
        List<ResponseButtonDescriptor> buttonsConfig = this.config.get().getOutButtons();
        Collections.sort(buttonsConfig);
        buttonsConfig.forEach((buttonConfig)->{
            JButton button = componentsFactory.getBorderedButton(buttonConfig.getTitle(),16f,AppThemeColor.RESPONSE_BUTTON, AppThemeColor.RESPONSE_BUTTON_BORDER,AppThemeColor.RESPONSE_BUTTON);
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
            button.addActionListener(e -> {
                this.controller.performResponse(buttonConfig.getResponseText());
                if(buttonConfig.isClose()){
                    this.controller.performHide();
                }
            });
            root.add(button);
        });
        return root;
    }
    protected JPanel getFromPanel(){
        JPanel forPanel = new JPanel(new BorderLayout());
        forPanel.setBackground(AppThemeColor.FRAME);
        JPanel currencyPanel = this.getCurrencyPanel(this.data.getCurCount(),this.data.getCurrency());
        if(currencyPanel != null) {
            currencyPanel.setPreferredSize(new Dimension(70,36));
            forPanel.add(currencyPanel, BorderLayout.LINE_START);
        }
        JLabel separator = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.CENTER,
                18f,
                "=>");
        forPanel.add(separator,BorderLayout.CENTER);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
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
            JPanel curPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            curPanel.setBackground(AppThemeColor.FRAME);
            curPanel.add(this.componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,17f,null, curCountStr));
            curPanel.add(currencyLabel);
            return curPanel;
        }
        return null;
    }
    @Override
    public void subscribe() {

    }

    protected abstract JPanel getContentPanel();

    @Override
    public void onViewDestroy() {

    }
}
