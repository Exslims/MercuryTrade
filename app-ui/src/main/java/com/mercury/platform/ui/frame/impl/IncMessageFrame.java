package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends OverlaidFrame {
    private TradeMode tradeMode = TradeMode.DEFAULT;

    public IncMessageFrame(){
        super("Messages");
    }

    @Override
    protected void init() {
        super.init();
        setVisible(false);
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
        if(this.getContentPane().getComponentCount() > 0){
            packFrame();
        }
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
        EventRouter.INSTANCE.registerHandler(ChangedTradeModeEvent.ToSuperTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.SUPER);
        });
        EventRouter.INSTANCE.registerHandler(ChangedTradeModeEvent.ToDefaultTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.DEFAULT);
        });
        EventRouter.INSTANCE.registerHandler(NewWhispersEvent.class, event -> {
            Message message = ((NewWhispersEvent) event).getMessage();
            if (!this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
                this.setAlwaysOnTop(true);
                this.setVisible(true);
            } else {
                prevState = FrameStates.SHOW;
            }
            MessagePanel messagePanel = null;
            switch (tradeMode) {
                case SUPER: {
                    messagePanel = new MessagePanel(message.getWhisperNickname(), message, MessagePanelStyle.BIGGEST);
                    break;
                }
                case DEFAULT: {
                    messagePanel = new MessagePanel(message.getWhisperNickname(), message, MessagePanelStyle.BIGGEST);
                    if (this.getContentPane().getComponentCount() > 0) {
                        messagePanel.setStyle(MessagePanelStyle.SMALL);
                    } else {
                        messagePanel.setAsTopMessage();
                    }
                }
            }
            messagePanel.addMouseListener(new ExpandMouseListener());
            if (this.getContentPane().getComponentCount() > 0) {
                messagePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.BORDER));
            }
            this.add(messagePanel);
            if (this.getContentPane().getComponentCount() == 1) {
                this.setSize(new Dimension(this.getWidth(), 6 + messagePanel.getPreferredSize().height));
            } else {
                this.setSize(new Dimension(this.getWidth(), this.getHeight() + messagePanel.getPreferredSize().height));
            }
//            packFrame();
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
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
        EventRouter.INSTANCE.registerHandler(DraggedMessageFrameEvent.class, event -> {
            int x = ((DraggedMessageFrameEvent) event).getX();
            int y = ((DraggedMessageFrameEvent) event).getY();
            IncMessageFrame.this.setLocation(x,y);
            configManager.saveFrameLocation(this.getClass().getSimpleName(),this.getLocation());
        });
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            IncMessageFrame.this.revalidate();
            IncMessageFrame.this.repaint();
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
                    if(IncMessageFrame.this.getContentPane().getComponentCount() == 0) {
                        IncMessageFrame.this.setSize(new Dimension(IncMessageFrame.this.getWidth(), 6 + (will - was)));
                    }else {
                        IncMessageFrame.this.setSize(new Dimension(IncMessageFrame.this.getWidth(), IncMessageFrame.this.getHeight() + (will - was)));
                    }
                    break;
                }
                case BIGGEST: {
                    int was = source.getPreferredSize().height;
                    source.setStyle(MessagePanelStyle.SMALL);
                    int will = source.getPreferredSize().height;
                    if(IncMessageFrame.this.getContentPane().getComponentCount() == 0) {
                        IncMessageFrame.this.setSize(new Dimension(IncMessageFrame.this.getWidth(), 6 - (was - will)));
                    }else {
                        IncMessageFrame.this.setSize(new Dimension(IncMessageFrame.this.getWidth(), IncMessageFrame.this.getHeight() - (was - will)));
                    }
                    break;
                }
            }
        }
    }
}
