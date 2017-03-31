package com.mercury.platform.ui.components.panel.chat;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChatFilterPanel extends JPanel {
    private Map<String,String> expiresMessages;
    private ComponentsFactory componentsFactory;
    private HtmlMessageBuilder messageBuilder;
    private JFrame owner;
    private JPanel container;
    public ChatFilterPanel(JFrame frame) {
        this.owner = frame;
        this.componentsFactory = new ComponentsFactory();
        this.messageBuilder = new HtmlMessageBuilder();
        this.expiresMessages = ExpiringMap.builder()
                .expiration(10, TimeUnit.SECONDS)
                .build();
        container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.TRANSPARENT);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2,2,2,0),
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
                EventRouter.UI.fireEvent(new RepaintEvent.RepaintChatFilter());
            }
        });

        container.getParent().setBackground(AppThemeColor.TRANSPARENT);
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        vBar.addAdjustmentListener(e -> {
            EventRouter.UI.fireEvent(new RepaintEvent.RepaintChatFilter());
        });

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(scrollPane);
    }
    public void addMessage(String stubMessage){
        String message = StringUtils.substringAfter(stubMessage,"] $");
        if(message.isEmpty()){
            message = StringUtils.substringAfter(stubMessage,"] #");
        }

        addMessageToFilter(message);
        EventRouter.UI.fireEvent(new RepaintEvent.RepaintChatFilter());
    }

    private void addMessageToFilter(String message) {
        if(!message.isEmpty()){
            String nickname = StringUtils.substringBefore(message, ":");
            String messageContent = StringUtils.substringAfter(message, nickname + ":");
            nickname = StringUtils.deleteWhitespace(nickname);
            if(nickname.contains(">")) {
                nickname = StringUtils.substringAfterLast(nickname, ">");
            }
            if(!expiresMessages.containsValue(message)){
                ChatMessagePanel chatMessagePanel = new ChatMessagePanel(
                        this.componentsFactory,
                        nickname,
                        messageBuilder.build(messageContent));
                container.add(chatMessagePanel);
                trimContainer();
                expiresMessages.put(nickname,message);
                owner.pack();
                container.scrollRectToVisible(new Rectangle(0,container.getHeight()-1,1,1));
            }
        }
    }
    private void trimContainer(){
        if(container.getComponentCount() > 20){
            container.remove(0);
        }
    }
    public void clear(){
        container.removeAll();
    }
    public void setNewChunks(List<String> chunks){
        this.messageBuilder.setChunkStrings(chunks);
    }
}
