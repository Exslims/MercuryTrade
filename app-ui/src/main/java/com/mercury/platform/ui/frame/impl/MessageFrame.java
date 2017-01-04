package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.FrameStates;
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
    protected String getFrameTitle() {
        return null;
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
                if(!this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW){
                    this.setVisible(true);
                }else {
                    prevState = FrameStates.SHOW;
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
                    }
                }
                messagePanel.addMouseListener(new ExpandMouseListener());
                if(this.getContentPane().getComponentCount() > 0){
                    messagePanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0, AppThemeColor.BORDER));
                }
                this.add(messagePanel);
                if(this.getContentPane().getComponentCount() == 1) {
                    this.setSize(new Dimension(this.getWidth(), 6 + messagePanel.getPreferredSize().height));
                }else {
                    this.setSize(new Dimension(this.getWidth(), this.getHeight() + messagePanel.getPreferredSize().height));
                }
            }
//            packFrame();
        });
        EventRouter.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            this.remove(panel);
            if (this.getContentPane().getComponentCount() > 0) {
                MessagePanel component = (MessagePanel) this.getContentPane().getComponent(0);
                component.setBorder(null);
                component.setAsTopMessage();
            }
            this.setSize(new Dimension(this.getWidth(), this.getHeight() - panel.getPreferredSize().height));
            if(this.getContentPane().getComponentCount() == 0){
                this.setVisible(false);
            }
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
    private class ExpandMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            MessagePanel source = (MessagePanel) e.getSource();
            switch (source.getStyle()) {
                case SMALL: {
                    int was = source.getPreferredSize().height;
                    source.setStyle(MessagePanelStyle.BIGGEST);
                    int will = source.getPreferredSize().height;
                    if(MessageFrame.this.getContentPane().getComponentCount() == 0) {
                        MessageFrame.this.setSize(new Dimension(MessageFrame.this.getWidth(), 6 + (will - was)));
                    }else {
                        MessageFrame.this.setSize(new Dimension(MessageFrame.this.getWidth(), MessageFrame.this.getHeight() + (will - was)));
                    }
                    break;
                }
                case BIGGEST: {
                    int was = source.getPreferredSize().height;
                    source.setStyle(MessagePanelStyle.SMALL);
                    int will = source.getPreferredSize().height;
                    if(MessageFrame.this.getContentPane().getComponentCount() == 0) {
                        MessageFrame.this.setSize(new Dimension(MessageFrame.this.getWidth(), 6 - (was - will)));
                    }else {
                        MessageFrame.this.setSize(new Dimension(MessageFrame.this.getWidth(), MessageFrame.this.getHeight() - (was - will)));
                    }
                    break;
                }
            }
        }
    }
}
