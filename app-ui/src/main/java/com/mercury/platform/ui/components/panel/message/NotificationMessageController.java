package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.misc.event.*;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
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
        EventRouter.CORE.fireEvent(new ChatCommandEvent("/invite " + message.getWhisperNickname()));
        showITH();
    }

    @Override
    public void performKick() {
        EventRouter.CORE.fireEvent(new ChatCommandEvent("/kick " + message.getWhisperNickname()));

    }

    @Override
    public void performOfferTrade() {
        EventRouter.CORE.fireEvent(new ChatCommandEvent("/tradewith " + message.getWhisperNickname()));
    }

    @Override
    public void performOpenChat() {
        EventRouter.CORE.fireEvent(new OpenChatEvent(message.getWhisperNickname()));
    }

    @Override
    public void performResponse(@NonNull String responseText) {

        EventRouter.CORE.fireEvent(new ChatCommandEvent("@" + message.getWhisperNickname() + " " + responseText));
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
        EventRouter.UI.fireEvent(new CollapseMessageEvent());
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
//    private void storeClipboard(){
//        try {
//            storedClipboard = (String) Toolkit.getDefaultToolkit()
//                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
//        } catch (Exception e) {
//            log.error(e);
//        }
//    }
//    private void restoreClipboard(){
//        StringSelection selection = new StringSelection(storedClipboard);
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(selection, null);
//    }
    private void closeMessagePanel(){
        Timer timer = new Timer(30, action -> {
            EventRouter.UI.fireEvent(new CloseMessagePanelEvent(message));
        });
        timer.setRepeats(false);
        timer.start();
    }
}
