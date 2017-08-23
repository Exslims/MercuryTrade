package com.mercury.platform.ui.components.panel.notification.controller;

import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

//todo proxy
public class NotificationIncomingController implements IncomingPanelController {
    private static final Logger log = LogManager.getLogger(NotificationIncomingController.class);
    private NotificationDescriptor notificationDescriptor;

    public NotificationIncomingController(NotificationDescriptor notificationDescriptor) {
        this.notificationDescriptor = notificationDescriptor;
    }

    @Override
    public void performInvite() {
        MercuryStoreCore.chatCommandSubject.onNext("/invite " + notificationDescriptor.getWhisperNickname());
        showITH();
    }

    @Override
    public void performKick() {
        MercuryStoreCore.chatCommandSubject.onNext("/kick " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performOfferTrade() {
        MercuryStoreCore.chatCommandSubject.onNext("/tradewith " + notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performOpenChat() {
        MercuryStoreCore.openChatSubject.onNext(notificationDescriptor.getWhisperNickname());
    }

    @Override
    public void performResponse(@NonNull String responseText) {
        MercuryStoreCore.chatCommandSubject.onNext("@" + notificationDescriptor.getWhisperNickname() + " " + responseText);
    }

    @Override
    public void performHide() {
        this.closeMessagePanel();
    }

    @Override
    public void showITH() {
        if (notificationDescriptor instanceof ItemTradeNotificationDescriptor) {
            this.copyItemNameToClipboard(((ItemTradeNotificationDescriptor) notificationDescriptor).getItemName());
            if (((ItemTradeNotificationDescriptor) notificationDescriptor).getTabName() != null) {
                MercuryStoreUI.showItemGridSubject.onNext((ItemTradeNotificationDescriptor) notificationDescriptor);
            }
        }
    }

    private void copyItemNameToClipboard(@NonNull String itemName) {
        Timer timer = new Timer(30, action -> {
            try {
                StringSelection selection = new StringSelection(itemName);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
            } catch (IllegalStateException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError(e));
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void closeMessagePanel() {
        Timer timer = new Timer(30, action -> {
            MercuryStoreCore.removeNotificationSubject.onNext(notificationDescriptor);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
