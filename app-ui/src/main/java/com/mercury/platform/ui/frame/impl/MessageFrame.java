package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Константин on 24.12.2016.
 */
public class MessageFrame extends OverlaidFrame {
    private TradeMode tradeMode = TradeMode.DEFAULT;

    public MessageFrame(){
        super("Messages");
    }

    @Override
    protected void init() {
        super.init();
        setVisible(false);
        disableHideEffect();
    }

    private void convertFrameTo(TradeMode mode){
        switch (mode){
            case DEFAULT:{
                if(tradeMode == TradeMode.SUPER){
                    Component[] components = this.getContentPane().getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel)messagePanel).setStyle(MessagePanelStyle.SMALL);
                    }
                    if(this.getContentPane().getComponentCount() != 0) {
                        ((MessagePanel) this.getContentPane().getComponent(0)).setStyle(MessagePanelStyle.BIGGEST);
                        ((MessagePanel) this.getContentPane().getComponent(0)).setAsTopMessage();
                    }
                }
                this.repaint();
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    Component[] components = this.getContentPane().getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel)messagePanel).setStyle(MessagePanelStyle.BIGGEST);
                    }
                    if(this.getContentPane().getComponentCount() != 0) {
                        ((MessagePanel) this.getContentPane().getComponent(0)).setAsTopMessage();
                    }
                }
                break;
            }
        }
        this.tradeMode = mode;
        packFrame();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS);
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(ChangedTradeModeEvent.ToSuperTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.SUPER);
        });
        EventRouter.registerHandler(ChangedTradeModeEvent.ToDefaultTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.DEFAULT);
        });
        EventRouter.registerHandler(NewWhispersEvent.class, event -> {
            List<Message> messages = ((NewWhispersEvent) event).getMessages();
            for (Message message : messages) {
                if(!this.isVisible()){
                    this.setVisible(true);
                }
                MessagePanel messagePanel = null;
                switch (tradeMode){
                    case SUPER:{
                        messagePanel = new MessagePanel(message.getWhisperNickname(), message.getMessage(), MessagePanelStyle.BIGGEST);
                        break;
                    }
                    case DEFAULT:{
                        messagePanel = new MessagePanel(message.getWhisperNickname(), message.getMessage(), MessagePanelStyle.BIGGEST);
                        if(this.getContentPane().getComponentCount() > 0){
                            messagePanel.setStyle(MessagePanelStyle.SMALL);
                        }else {
                            messagePanel.setAsTopMessage();
                        }
                        messagePanel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                MessagePanel source = (MessagePanel) e.getSource();
                                switch (source.getStyle()) {
                                    case SMALL: {
                                        source.setStyle(MessagePanelStyle.BIGGEST);
                                        break;
                                    }
                                    case BIGGEST: {
                                        source.setStyle(MessagePanelStyle.SMALL);
                                        break;
                                    }
                                }
                                packFrame();
                            }
                        });
                    }
                }
                if(this.getContentPane().getComponentCount() > 0){
                    messagePanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, AppThemeColor.BORDER));
                }
                this.add(messagePanel);
            }
            packFrame();
        });
        EventRouter.registerHandler(CloseMessagePanelEvent.class, event -> {
            this.remove(((CloseMessagePanelEvent) event).getComponent());
            if (this.getContentPane().getComponentCount() > 0) {
                MessagePanel component = (MessagePanel) this.getContentPane().getComponent(0);
                component.setBorder(null);
                component.setAsTopMessage();
            }
            if(this.getContentPane().getComponentCount() == 0){
                this.setVisible(false);
            }
            packFrame();
        });
        EventRouter.registerHandler(DraggedMessageFrameEvent.class, event -> {
            int x = ((DraggedMessageFrameEvent) event).getX();
            int y = ((DraggedMessageFrameEvent) event).getY();
            MessageFrame.this.setLocation(x,y);
            configManager.saveFrameLocation(this.getClass().getSimpleName(),this.getLocation());
        });
        EventRouter.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            MessageFrame.this.revalidate();
            MessageFrame.this.repaint();
        });
    }
    private enum TradeMode{
        DEFAULT,SUPER
    }
}
