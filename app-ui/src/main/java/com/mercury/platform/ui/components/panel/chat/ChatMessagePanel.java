package com.mercury.platform.ui.components.panel.chat;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ChatMessagePanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private String message;
    private String nickName;

    public ChatMessagePanel(
            @NonNull ComponentsFactory componentsFactory,
            @NonNull String nickName,
            @NonNull String message) {

        super(new BorderLayout());
        this.nickName = nickName;
        this.message = message;

        this.componentsFactory = componentsFactory;
        this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel operationsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));

        JButton invite = componentsFactory.getIconButton("app/invite.png", 16, AppThemeColor.SLIDE_BG, TooltipConstants.INVITE);
        invite.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.CORE.fireEvent(new ChatCommandEvent("/invite " + nickName));
                }
            }
        });
        JButton openChat = componentsFactory.getIconButton("app/openChat.png", 16, AppThemeColor.SLIDE_BG, TooltipConstants.OPEN_CHAT);
        openChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    EventRouter.CORE.fireEvent(new OpenChatEvent(nickName));
                }
            }
        });


        JLabel nicknameLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_NICKNAME,
                TextAlignment.LEFTOP,
                15f,
                nickName);
        nicknameLabel.setPreferredSize(new Dimension(75,nicknameLabel.getPreferredSize().height));
        nicknameLabel.setBorder(null);
        invite.setBorder(null);
        openChat.setBorder(null);
        operationsPanel.add(invite);
        operationsPanel.add(openChat);

        JPanel nicknamePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        nicknamePanel.add(nicknameLabel);

        miscPanel.add(operationsPanel,BorderLayout.LINE_START);
        miscPanel.add(nicknamePanel,BorderLayout.CENTER);
        nicknamePanel.setMaximumSize(new Dimension(75,30));
        JLabel messageLabel = new JLabel(message);
        messageLabel.setBackground(AppThemeColor.TRANSPARENT);
        messageLabel.setBorder(null);
        messageLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR,16f));
        messageLabel.setForeground(AppThemeColor.TEXT_DEFAULT);

        root.add(miscPanel,BorderLayout.LINE_START);
        root.add(messageLabel, BorderLayout.CENTER);
        this.add(root,BorderLayout.CENTER);
    }
}
