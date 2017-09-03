package com.mercury.platform.core;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ChatHelper implements AsSubscriber {
    private Robot robot;

    public ChatHelper() {
        subscribe();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(String message) {
        this.gameToFront();
        StringSelection selection = new StringSelection(message);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        MercuryStoreCore.blockHotkeySubject.onNext(true);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);

        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        MercuryStoreCore.blockHotkeySubject.onNext(false);
    }

    private void executeTradeMessage() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        try {
            String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            if (result != null && (result.contains("listed for") || result.contains("for my"))) {
                this.gameToFront();
                MercuryStoreCore.blockHotkeySubject.onNext(true);
                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_A);

                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                Timer timer = new Timer(300, action -> {
                    StringSelection selection = new StringSelection("");
                    clipboard.setContents(selection, null);
                });
                timer.setRepeats(false);
                timer.start();

                MercuryStoreCore.blockHotkeySubject.onNext(false);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError(e));
        }
    }

    private void openChat(String whisper) {
        this.gameToFront();
        StringSelection selection = new StringSelection("@" + whisper);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        MercuryStoreCore.blockHotkeySubject.onNext(true);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);

        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        MercuryStoreCore.blockHotkeySubject.onNext(false);
    }

    private void gameToFront() {
        User32.INSTANCE.EnumWindows((hWnd, arg1) -> {
            char[] className = new char[512];
            User32.INSTANCE.GetClassName(hWnd, className, 512);
            String wText = Native.toString(className);

            if (wText.isEmpty()) {
                return true;
            }
            if (wText.equals("POEWindowClass")) {
                User32.INSTANCE.SetForegroundWindow(hWnd);
                return false;
            }
            return true;
        }, null);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.chatCommandSubject.subscribe(this::executeMessage);
        MercuryStoreCore.openChatSubject.subscribe(this::openChat);
        MercuryStoreCore.tradeWhisperSubject.subscribe(state -> this.executeTradeMessage());
        MercuryStoreCore.dndSubject.subscribe(state -> {
            TaskBarDescriptor config = Configuration.get().taskBarConfiguration().get();
            if (config.isInGameDnd()) {
                if (state) {
                    executeMessage("/dnd " + config.getDndResponseText());
                } else {
                    executeMessage("/dnd");
                }
            }
        });
    }
}
