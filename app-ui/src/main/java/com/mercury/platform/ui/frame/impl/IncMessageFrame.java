package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.impl.util.GrowSettings;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends MovableComponentFrame{
    private TradeMode tradeMode = TradeMode.DEFAULT;
    private GrowSettings growSettings = GrowSettings.DOWN;

    public IncMessageFrame(){
        super("MT-IncMessagesFrame");
        setVisible(false);
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
                    }
                }
            }
            if(mainContainer.getComponentCount() > 0){
                messagePanel.setPreferredSize(new Dimension(mainContainer.getWidth(),30));
            }else {
                FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
                messagePanel.setPreferredSize(new Dimension(frameSettings.getFrameSize().width -6,messagePanel.getPreferredSize().height));
            }
            if(growSettings.equals(GrowSettings.UP)){
                if(mainContainer.getComponentCount() > 0) {
                    this.setLocation(new Point(this.getLocation().x, this.getLocation().y - messagePanel.getPreferredSize().height));
                }
                messagePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppThemeColor.BORDER));
                mainContainer.add(messagePanel,0);
                packFrame();
            }else {
                if(mainContainer.getComponentCount() > 0) {
                    messagePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.BORDER));
                }
                mainContainer.add(messagePanel);
                this.pack();
            }
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            this.remove(panel);
            if (mainContainer.getComponentCount() > 0) {
                MessagePanel component = (MessagePanel) mainContainer.getComponent(0);
                component.setBorder(null);
            }
            packFrame();
            if(mainContainer.getComponentCount() == 0){
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
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Notifications"));

        JPanel growPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        growPanel.add(componentsFactory.getTextLabel("Grow "));
        JComboBox growPicker = componentsFactory.getComboBox(new String[]{"Up", "Down"});
        growPicker.setSelectedIndex(GrowSettings.valueOf(growSettings.toString()).ordinal());
        growPicker.addActionListener(e -> {
            switch ((String)growPicker.getSelectedItem()){
                case "Up":{
                    growSettings = GrowSettings.UP;
                    break;
                }
                case "Down":{
                    growSettings = GrowSettings.DOWN;
                    break;
                }
            }
        });
        growPanel.add(growPicker);
        panel.add(labelPanel);
        panel.add(growPanel);
        return panel;
    }

    //some bullshit
    public void changeSizeOfComponent(JPanel component, int height){
        List<Component> components = Arrays.asList(mainContainer.getComponents());
        Component live = components
                .stream()
                .filter(exist -> exist.equals(component))
                .collect(Collectors.toList())
                .get(0);
        live.setPreferredSize(new Dimension(mainContainer.getWidth(),height));
        this.pack();
    }

    private enum TradeMode{
        DEFAULT,SUPER
    }
}
