package com.home.clicker.ui;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.CloseMessagePanelEvent;
import com.home.clicker.shared.events.custom.DraggedMessageFrameEvent;
import com.home.clicker.shared.events.custom.NewWhispersEvent;
import com.home.clicker.shared.pojo.Message;
import com.home.clicker.ui.components.panel.MessagePanel;
import com.home.clicker.ui.components.panel.MessagePanelStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Константин on 24.12.2016.
 */
public class MessageFrame extends OverlaidFrame {
    public MessageFrame(){
        super("Messages");
    }

    @Override
    protected void init() {
        super.init();
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        setBackground(new Color(42, 44, 43,230));
        setSize(new Dimension(500,500));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MessageFrame.this.repaint();
            }
        });
        setVisible(false);
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewWhispersEvent.class, event -> {
            List<Message> messages = ((NewWhispersEvent) event).getMessages();
            for (Message message : messages) {
                MessagePanel messagePanel = new MessagePanel(message.getWhisperNickname(), message.getMessage(), MessagePanelStyle.BIGGEST);
                this.add(messagePanel);
            }
            this.setVisible(true);
            this.pack();
        });
        EventRouter.registerHandler(CloseMessagePanelEvent.class,event -> {
            this.remove(((CloseMessagePanelEvent) event).getComponent());
            this.pack();
        });
        EventRouter.registerHandler(DraggedMessageFrameEvent.class, event -> {
            int x = ((DraggedMessageFrameEvent) event).getX();
            int y = ((DraggedMessageFrameEvent) event).getY();
            MessageFrame.this.setLocation(x,y);
        });
    }
}
