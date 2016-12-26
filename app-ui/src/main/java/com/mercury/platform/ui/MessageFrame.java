package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CloseMessagePanelEvent;
import com.mercury.platform.shared.events.custom.DraggedMessageFrameEvent;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;

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


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MessageFrame.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
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
            this.pack();
        });
        EventRouter.registerHandler(CloseMessagePanelEvent.class, event -> {
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
