package com.mercury.platform.ui.components.panel.chat;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.ScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Константин on 27.01.2017.
 */
public class ChatFilterPanel extends JPanel {
    private ComponentsFactory componentsFactory;
    private JScrollBar vBar;

    private JPanel container;
    private JFrame owner;
    public ChatFilterPanel(JFrame owner) {
        this.owner = owner;
        componentsFactory = new ComponentsFactory();
        container = new ScrollContainer();
        container.setBackground(AppThemeColor.SLIDE_BG);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createLineBorder(AppThemeColor.HEADER)
        ));

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.SLIDE_BG);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                owner.repaint();
            }
        });
        scrollPane.setPreferredSize(new Dimension(100,50));

        container.getParent().setBackground(AppThemeColor.SLIDE_BG);
        vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        vBar.addAdjustmentListener(e -> owner.repaint());

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(scrollPane);
    }
    public void addMessage(String stubMessage){
        String nickname = StringUtils.substringBetween(stubMessage, "] $", ":");
        if(nickname == null){
            nickname = StringUtils.substringBetween(stubMessage, "] #", ":");
        }
        String message = StringUtils.substringAfter(stubMessage, nickname + ":");
        nickname = StringUtils.deleteWhitespace(nickname);
        if(nickname.contains(">")){
            nickname = StringUtils.substringAfterLast(nickname, ">");
        }

        JPanel messagePanel = componentsFactory.getTransparentPanel(new BorderLayout());
        JButton invite = componentsFactory.getIconButton("app/invite.png", 12, AppThemeColor.SLIDE_BG, TooltipConstants.INVITE);
        String nicknameF = nickname;
        invite.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new ChatCommandEvent("/invite " + nicknameF));
            }
        });
        JButton openChat = componentsFactory.getIconButton("app/openChat.png", 12, AppThemeColor.SLIDE_BG, TooltipConstants.OPEN_CHAT);
        openChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new OpenChatEvent(nicknameF));
            }
        });
        invite.setBorder(null);
        openChat.setBorder(null);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        miscPanel.add(invite);
        miscPanel.add(openChat);
        miscPanel.add(componentsFactory.getTextLabel(nickname + ":"));

        messagePanel.add(miscPanel,BorderLayout.LINE_START);
        messagePanel.add(componentsFactory.getSimpleTextAre(message), BorderLayout.CENTER);
        container.add(messagePanel);
        trimContainer();
    }
    private void trimContainer(){
        if(container.getComponentCount() > 20){
            for (int i = 0; i < 5; i++) {
                container.remove(0);
            }
            owner.pack();
        }
    }
    public void clear(){
        container.removeAll();
    }
}
