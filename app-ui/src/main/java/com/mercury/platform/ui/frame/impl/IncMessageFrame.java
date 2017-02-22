package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends MovableComponentFrame{
    private final Logger logger = LogManager.getLogger(IncMessageFrame.class.getSimpleName());
    private TradeMode tradeMode = TradeMode.DEFAULT;
    private FlowDirections flowDirections = FlowDirections.DOWNWARDS;
    private int deltaYInUpwards = 0;

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
                    int deltaY = 0;
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        int oldH = messagePanel.getPreferredSize().height;
                        if(flowDirections.equals(FlowDirections.DOWNWARDS)) {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                        }else {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                            int newH = messagePanel.getPreferredSize().height;
                            deltaY += oldH-newH;
                        }
                    }
                    if(deltaY > 0){
                        this.setLocation(this.getLocation().x, this.getLocation().y + deltaY);
                    }
                }
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    int deltaY = 0;
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        int oldH = messagePanel.getPreferredSize().height;
                        ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.SP_MODE);
                        if(flowDirections.equals(FlowDirections.UPWARDS)) {
                            int newH = messagePanel.getPreferredSize().height;
                            deltaY += newH-oldH;
                        }
                    }
                    if(deltaY > 0){
                        this.setLocation(this.getLocation().x, this.getLocation().y - deltaY);
                    }
                }
                break;
            }
        }
        this.tradeMode = mode;
        if(mainContainer.getComponentCount() > 0){
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
            MessagePanel messagePanel = null;
            switch (tradeMode) {
                case SUPER: {
                    messagePanel = new MessagePanel(message, this, MessagePanelStyle.SP_MODE);
                    break;
                }
                case DEFAULT: {
                    if(flowDirections.equals(FlowDirections.DOWNWARDS)) {
                        messagePanel = new MessagePanel(message, this, MessagePanelStyle.DOWNWARDS_SMALL);
                    }else {
                        messagePanel = new MessagePanel(message, this, MessagePanelStyle.UPWARDS_SMALL);
                    }
                }
            }
            if (!dnd && !this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
                this.setAlwaysOnTop(true);
                this.setVisible(true);
            } else {
                prevState = FrameStates.SHOW;
            }
            if(flowDirections.equals(FlowDirections.UPWARDS)){
                if(mainContainer.getComponentCount() > 0) {
                    this.setLocation(new Point(this.getLocation().x, this.getLocation().y - messagePanel.getPreferredSize().height));
                }
                mainContainer.add(messagePanel, 0);
            }else {
                mainContainer.add(messagePanel);
            }
            this.pack();
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            this.remove(panel);
            if (mainContainer.getComponentCount() > 0) {
                deltaYInUpwards += panel.getHeight();
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

        JPanel growPanel = componentsFactory.getTransparentPanel(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 0.5f;
        constraint.insets = new Insets(2,0,0,2);
        JComboBox flowDirectionPicker = componentsFactory.getComboBox(new String[]{"Upwards", "Downwards"});
        flowDirectionPicker.setSelectedIndex(FlowDirections.valueOf(flowDirections.toString()).ordinal());
        flowDirectionPicker.addActionListener(e -> {
            switch ((String)flowDirectionPicker.getSelectedItem()){
                case "Upwards":{
                    changeDirection(FlowDirections.UPWARDS);
                    break;
                }
                case "Downwards":{
                    changeDirection(FlowDirections.DOWNWARDS);
                    break;
                }
            }
        });

        growPanel.add(componentsFactory.getTextLabel("Flow direction:"),constraint);
        constraint.gridx = 1;
        growPanel.add(flowDirectionPicker,constraint);
        constraint.gridx = 0;
        constraint.gridy = 1;
        growPanel.add(componentsFactory.getTextLabel("Expand first:"),constraint);
        constraint.gridx = 1;
        JCheckBox expandFirst = new JCheckBox();
        expandFirst.setBackground(AppThemeColor.TRANSPARENT);
        growPanel.add(expandFirst,constraint);

        panel.add(labelPanel);
        panel.add(growPanel);
        return panel;
    }
    private void changeDirection(FlowDirections direction){
        if(!this.flowDirections.equals(direction) && mainContainer.getComponentCount() > 1){
            switch (direction) {
                case DOWNWARDS:{
                    Component[] components = mainContainer.getComponents();
                    for (Component component : components) {
                        if(tradeMode.equals(TradeMode.DEFAULT)) {
                            ((MessagePanel) component).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                        }else {
                            ((MessagePanel) component).setStyle(MessagePanelStyle.SP_MODE);
                        }
                        mainContainer.remove(component);
                        mainContainer.add(component, 0);
                    }
                    this.setLocation(ConfigManager.INSTANCE.getFrameSettings(this.getClass().getSimpleName()).getFrameLocation());
                    break;
                }
                case UPWARDS: {
                    int deltaY = 0;
                    Component[] components = mainContainer.getComponents();
                    for (Component component : components) {
                        if (tradeMode.equals(TradeMode.DEFAULT)) {
                            ((MessagePanel) component).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                        } else {
                            ((MessagePanel) component).setStyle(MessagePanelStyle.SP_MODE);
                        }
                        deltaY += component.getHeight();
                        mainContainer.remove(component);
                        mainContainer.add(component, 0);
                    }
                    this.setLocation(this.getLocation().x, this.getLocation().y - deltaY);
                    break;
                }
            }
        }
        this.deltaYInUpwards = 0;
        this.flowDirections = direction;
    }

    @Override
    protected Point getFrameLocation() {
        if(flowDirections.equals(FlowDirections.UPWARDS)){
            return new Point(this.getLocationOnScreen().x,this.getLocationOnScreen().y + this.getHeight());
        }
        return super.getFrameLocation();
    }
}
