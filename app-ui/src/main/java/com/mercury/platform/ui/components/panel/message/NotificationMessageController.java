package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.misc.event.CloseMessagePanelEvent;
import com.mercury.platform.ui.misc.event.ShowItemGridEvent;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class NotificationMessageController implements MessagePanelController {
    private Message message;
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
            EventRouter.UI.fireEvent(new CloseMessagePanelEvent(message));
        });
        timer.setRepeats(false);
        timer.start();
    }
}
