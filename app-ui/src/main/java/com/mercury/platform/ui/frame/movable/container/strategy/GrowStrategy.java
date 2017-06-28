package com.mercury.platform.ui.frame.movable.container.strategy;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.frame.movable.container.MessageFrame;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GrowStrategy {
    protected int visibleCount;
    protected int unfoldCount;
    protected List<MessagePanel> currentMessages;
    protected Container container;
    protected boolean expanded;
    protected JFrame messageFrame;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    public GrowStrategy(List<MessagePanel> currentMessages, Container container, JFrame messageFrame){
        this.currentMessages = currentMessages;
        this.container = container;
        this.messageFrame = messageFrame;
        this.componentsFactory.setScale(ConfigManager.INSTANCE.getScaleData().get("notification"));
    }

    public abstract void addMessage(Message message);
    public void showNextMessage() {
        int limitMsgCount = ConfigManager.INSTANCE.getLimitMsgCount();
        if (this.currentMessages.size() == 0) {
            this.messageFrame.setVisible(false);
        } else if (this.currentMessages.size() >= limitMsgCount) {
            this.currentMessages.get(limitMsgCount - 1).setVisible(true);
            this.visibleCount++;
        }
    }
    public void validate() {
        this.performLimit(ConfigManager.INSTANCE.getLimitMsgCount());
        this.performUnfold(ConfigManager.INSTANCE.getExpandedMsgCount());
    }

    protected void addMessage(MessagePanel messagePanel){
        this.visibleCount++;
        this.currentMessages.add(messagePanel);
        if(this.unfoldCount < ConfigManager.INSTANCE.getExpandedMsgCount()){
            messagePanel.expand();
            this.unfoldCount++;
        }
        if(!expanded && this.visibleCount > ConfigManager.INSTANCE.getLimitMsgCount()){
            messagePanel.setVisible(false);
            this.visibleCount--;
        }
        MercuryStoreUI.INSTANCE.packSubject.onNext(MessageFrame.class);
    }
    public void removeMessage(Message message){
        MessagePanel messagePanel = this.currentMessages.stream()
                .filter(panel -> panel.getMessage().equals(message))
                .collect(Collectors.toList()).get(0);
        if(messagePanel.isExpanded()){
            this.unfoldCount--;
        }
        this.visibleCount--;
        this.container.remove(messagePanel);
        this.currentMessages.remove(messagePanel);
        this.showNextMessage();
        MercuryStoreUI.INSTANCE.packSubject.onNext(MessageFrame.class);
    }
    public void expandMessages(){
        this.expanded = true;
        this.currentMessages.forEach(panel -> panel.setVisible(true));
        MercuryStoreUI.INSTANCE.packSubject.onNext(MessageFrame.class);
    }
    public void collapseMessage(){
        this.expanded = false;
        this.visibleCount = ConfigManager.INSTANCE.getLimitMsgCount();
        this.currentMessages.stream().skip(ConfigManager.INSTANCE.getLimitMsgCount())
                .forEach(panel -> panel.setVisible(false));
        MercuryStoreUI.INSTANCE.packSubject.onNext(MessageFrame.class);
    }
    public void performLimit(int limit){
        this.visibleCount = 0;
        this.currentMessages.forEach(panel -> panel.setVisible(false));
        this.currentMessages.stream().limit(limit)
                .forEach(panel -> {
                    this.visibleCount++;
                    panel.setVisible(true);
                });
    }
    public void performUnfold(int unfold){
        this.unfoldCount = 0;
        this.currentMessages.forEach(MessagePanel::collapse);
        this.currentMessages.stream().limit(unfold)
                .forEach(panel -> {
                    this.unfoldCount++;
                    panel.expand();
                });
    }

}
