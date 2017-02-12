package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.misc.MessagePanelStyle;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.impl.util.FlowDirections;
import com.mercury.platform.ui.frame.impl.util.TradeMode;
import com.mercury.platform.ui.frame.location.UndecoratedFrameState;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends MovableComponentFrame{
    private TradeMode tradeMode = TradeMode.DEFAULT;
    private FlowDirections flowDirections = FlowDirections.DOWNWARDS;
    private int deltaYInUpwards = 0; //wtf

    private boolean dnd = false;
    public IncMessageFrame(){
        super("MT-IncMessagesFrame");
        prevState = FrameStates.HIDE;
        setVisible(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
        processSEResize = false;

        this.addMouseListener(new MouseAdapter() { //todo
            @Override
            public void mouseExited(MouseEvent e) {
                if(!undecoratedFrameState.equals(UndecoratedFrameState.MOVING)
                        && flowDirections.equals(FlowDirections.UPWARDS)
                        && !isMouseWithInFrame()){
                    IncMessageFrame.this.setLocation(getLocation().x,getLocation().y+deltaYInUpwards);
                    deltaYInUpwards = 0;
                }
            }
        });
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
                        MessagePanel first = (MessagePanel) mainContainer.getComponent(0);
                        first.setStyle(MessagePanelStyle.BIGGEST);
                        first.setBorder(null);
                    }
                }
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel)messagePanel).setStyle(MessagePanelStyle.SPMODE);
                    }
                }
                break;
            }
        }
        this.tradeMode = mode;
        if(mainContainer.getComponentCount() > 1){
            this.pack();
            this.repaint();
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
        EventRouter.INSTANCE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
            if(dnd){
                this.setVisible(false);
            }else if(mainContainer.getComponentCount() > 0){
                this.setAlwaysOnTop(true);
                this.setVisible(true);
            }
        });
        EventRouter.INSTANCE.registerHandler(NewWhispersEvent.class, event -> {
            Message message = ((NewWhispersEvent) event).getMessage();
            if (!dnd && !this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
                this.setAlwaysOnTop(true);
                this.setVisible(true);
            } else {
                prevState = FrameStates.SHOW;
            }
            MessagePanel messagePanel = null;
            switch (tradeMode) {
                case SUPER: {
                    messagePanel = new MessagePanel(message,this, MessagePanelStyle.SPMODE);
                    break;
                }
                case DEFAULT: {
                    messagePanel = new MessagePanel(message,this, MessagePanelStyle.BIGGEST);
                    if (mainContainer.getComponentCount() > 0) {
                        messagePanel.setStyle(MessagePanelStyle.SMALL);
                    }
                }
            }
            if(flowDirections.equals(FlowDirections.UPWARDS)){
                if(mainContainer.getComponentCount() > 0) {
                    this.setLocation(new Point(this.getLocation().x, this.getLocation().y - messagePanel.getPreferredSize().height));
                }
                messagePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppThemeColor.BORDER));
                mainContainer.add(messagePanel, 0);
            }else {
                if(mainContainer.getComponentCount() > 0) {
                    messagePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.BORDER));
                }
                mainContainer.add(messagePanel);
            }
            this.pack();
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            deltaYInUpwards += panel.getHeight();
            this.remove(panel);
            if (mainContainer.getComponentCount() > 0) {
                MessagePanel component = (MessagePanel) mainContainer.getComponent(0);
                component.setBorder(null);
            }
            this.pack();
            if(mainContainer.getComponentCount() == 0){
                if(flowDirections.equals(FlowDirections.UPWARDS)){
                    IncMessageFrame.this.setLocation(configManager
                            .getFrameSettings(IncMessageFrame.this.getClass().getSimpleName())
                            .getFrameLocation());
                    deltaYInUpwards = 0;
                }
                this.setVisible(false);
            }
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
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,20f,"Notification panel"));

        JPanel growPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        growPanel.add(componentsFactory.getTextLabel("Flow direction"));
        JComboBox growPicker = componentsFactory.getComboBox(new String[]{"Upwards", "Downwards"});
        growPicker.setSelectedIndex(FlowDirections.valueOf(flowDirections.toString()).ordinal());
        growPicker.addActionListener(e -> {
            switch ((String)growPicker.getSelectedItem()){
                case "Upwards":{
                    flowDirections = FlowDirections.UPWARDS;
                    break;
                }
                case "Downwards":{
                    flowDirections = FlowDirections.DOWNWARDS;
                    break;
                }
            }
        });
        growPanel.add(growPicker);
        panel.add(labelPanel);
        panel.add(growPanel);
        return panel;
    }

    @Override
    protected int getMinComponentCount() {
        return 0;
    }
}
