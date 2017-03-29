package com.mercury.platform.ui.components.panel.chat;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.List;

public class ChatFilterPanel extends JPanel {
    private ComponentsFactory componentsFactory;
    private HtmlMessageBuilder messageBuilder;

    private JScrollBar vBar;
    private JPanel container;
    private JFrame owner;
    public ChatFilterPanel(JFrame owner) {
        this.owner = owner;
        this.componentsFactory = new ComponentsFactory();
        this.messageBuilder = new HtmlMessageBuilder();

        container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.TRANSPARENT);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,4,4,4),
                BorderFactory.createLineBorder(AppThemeColor.HEADER)
        ));

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                owner.repaint();
            }
        });
        scrollPane.setPreferredSize(new Dimension(100,50));

        container.getParent().setBackground(AppThemeColor.TRANSPARENT);
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

        ChatMessagePanel chatMessagePanel = new ChatMessagePanel(
                this.componentsFactory,
                nickname,
                messageBuilder.build(message));
        container.add(chatMessagePanel);
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
    public void setNewChunks(List<String> chunks){
        this.messageBuilder.setChunkStrings(chunks);
    }
}
