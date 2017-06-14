package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.misc.event.*;
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
//    private String storedClipboard;
    public NotificationMessageController(Message message){
        this.message = message;
    }
    @Override
    public void performInvite() {
        MercuryStore.INSTANCE.chatCommandSubject.onNext("/invite " + message.getWhisperNickname());
        showITH();
    }

    @Override
    public void performKick() {
        MercuryStore.INSTANCE.chatCommandSubject.onNext("/kick " + message.getWhisperNickname());

    }

    @Override
    public void performOfferTrade() {
        MercuryStore.INSTANCE.chatCommandSubject.onNext("/tradewith " + message.getWhisperNickname());
    }

    @Override
    public void performOpenChat() {
        MercuryStore.INSTANCE.openChatSubject.onNext(message.getWhisperNickname());
    }

    @Override
    public void performResponse(@NonNull String responseText) {
        MercuryStore.INSTANCE.chatCommandSubject.onNext("@" + message.getWhisperNickname() + " " + responseText);
    }

    @Override
    public void performHide() {
        closeMessagePanel();
    }

    @Override
    public void showITH() {
        if(message instanceof ItemMessage) {
            copyItemNameToClipboard(((ItemMessage) message).getItemName());
            if (((ItemMessage) message).getTabName() != null) {
                EventRouter.UI.fireEvent(new ShowItemGridEvent((ItemMessage) message));
            }
        }
    }

    @Override
    public void expandMessage() {
        EventRouter.UI.fireEvent(new ExpandMessageEvent());
    }

    @Override
    public void collapseMessage() {
        MercuryStore.INSTANCE.collapseMessagePanelSubject.onNext(true);
    }

    @Override
    public void reloadMessage(MessagePanel panel) {
        EventRouter.UI.fireEvent(new ReloadMessageEvent(panel));
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
            MercuryStore.INSTANCE.closeMessagePanelSubject.onNext(message);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
