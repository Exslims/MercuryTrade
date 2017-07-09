package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.movable.container.MessageFrame;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

//todo proxy
public class NotificationMessageController implements MessagePanelController {
    private static final Logger log = LogManager.getLogger(NotificationMessageController.class);
    private Message message;
    public NotificationMessageController(Message message){
        this.message = message;
    }
    @Override
    public void performInvite() {
        MercuryStoreCore.INSTANCE.chatCommandSubject.onNext("/invite " + message.getWhisperNickname());
        showITH();
    }

    @Override
    public void performKick() {
        MercuryStoreCore.INSTANCE.chatCommandSubject.onNext("/kick " + message.getWhisperNickname());

    }

    @Override
    public void performOfferTrade() {
        MercuryStoreCore.INSTANCE.chatCommandSubject.onNext("/tradewith " + message.getWhisperNickname());
    }

    @Override
    public void performOpenChat() {
        MercuryStoreCore.INSTANCE.openChatSubject.onNext(message.getWhisperNickname());
    }

    @Override
    public void performResponse(@NonNull String responseText) {
        MercuryStoreCore.INSTANCE.chatCommandSubject.onNext("@" + message.getWhisperNickname() + " " + responseText);
    }

    @Override
    public void performHide() {
        closeMessagePanel();
    }

    @Override
    public void showITH() {
        if(message instanceof ItemMessage) {
            this.copyItemNameToClipboard(((ItemMessage) message).getItemName());
            if (((ItemMessage) message).getTabName() != null) {
                MercuryStoreUI.showItemGridSubject.onNext((ItemMessage) message);
            }
        }
    }

    @Override
    public void reloadMessage(MessagePanel panel) {
        MercuryStoreUI.reloadMessageSubject.onNext(panel);
    }

    private void copyItemNameToClipboard(@NonNull String itemName){
        Timer timer = new Timer(30, action -> {
            StringSelection selection = new StringSelection(itemName);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void closeMessagePanel(){
        Timer timer = new Timer(30, action -> {
            MercuryStoreUI.closeMessage.onNext(message);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
