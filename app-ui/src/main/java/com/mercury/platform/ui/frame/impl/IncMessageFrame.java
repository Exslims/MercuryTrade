package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends MovableComponentFrame{
    private TradeMode tradeMode = TradeMode.DEFAULT;

    public IncMessageFrame(){
        super("MT-IncMessagesFrame");
        setVisible(false);
        mainContainer = this.getContentPane();
    }

    private void convertFrameTo(TradeMode mode){
        switch (mode){
            case DEFAULT:{
                if(tradeMode == TradeMode.SUPER){
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel)messagePanel).setStyle(MessagePanelStyle.SMALL);
                    }
                    if(mainContainer.getComponentCount() != 0) {
                        ((MessagePanel) mainContainer.getComponent(0)).setStyle(MessagePanelStyle.BIGGEST);
                        ((MessagePanel) mainContainer.getComponent(0)).setAsTopMessage();
                    }
                }
                mainContainer.repaint();
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel)messagePanel).setStyle(MessagePanelStyle.BIGGEST);
                    }
                    if(mainContainer.getComponentCount() != 0) {
                        ((MessagePanel) mainContainer.getComponent(0)).setAsTopMessage();
                    }
                }
                break;
            }
        }
        this.tradeMode = mode;
        if(mainContainer.getComponentCount() > 0){
            packFrame();
        }
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BoxLayout(mainContainer,BoxLayout.Y_AXIS);
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
                    messagePanel = new MessagePanel(message,this, MessagePanelStyle.BIGGEST);
                    break;
                }
                case DEFAULT: {
                    messagePanel = new MessagePanel(message,this, MessagePanelStyle.BIGGEST);
                    if (mainContainer.getComponentCount() > 0) {
                        messagePanel.setStyle(MessagePanelStyle.SMALL);
                    } else {
                        messagePanel.setAsTopMessage();
                    }
                }
            }
            if (mainContainer.getComponentCount() > 0) {
                messagePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.BORDER));
            }
            mainContainer.add(messagePanel);
            packFrame();
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            this.remove(panel);
            if (mainContainer.getComponentCount() > 0) {
                MessagePanel component = (MessagePanel) mainContainer.getComponent(0);
                component.setBorder(null);
                component.setAsTopMessage();
            }
            packFrame();
            if(mainContainer.getComponentCount() == 0){
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

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Notifications"));
        panel.add(labelPanel);
        return panel;
    }

    private enum TradeMode{
        DEFAULT,SUPER
    }
}
