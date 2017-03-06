package com.mercury.platform.core;

import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.sun.jna.Native;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class ChatHelper implements HasEventHandlers {
    private final Logger logger = LogManager.getLogger(ChatHelper.class.getSimpleName());
    private User32 user32 = User32.INSTANCE;
    private Robot robot;

    public ChatHelper() {
        initHandlers();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(String message) {
        new Thread(() -> {
            StringSelection selection = new StringSelection(message);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);

            gameToFront();

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
        }).start();
    }
    private void openChat(String whisper){
        new Thread(() -> {
            gameToFront();

            StringSelection selection = new StringSelection("@" + whisper);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

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
        }).start();
    }

    private void gameToFront(){
        List<String> titles = new ArrayList<>();
        titles.add("Path of Exile");
        titles.add("PathOfExile");
        titles.add("PathOfExile_64");
        user32.EnumWindows((hWnd, arg1) -> {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (wText.isEmpty()) {
                    return true;
                }
                if (titles.contains(wText)) {
                    user32.SetForegroundWindow(hWnd);
                    return false;
                }
                return true;
            }, null);
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ChatCommandEvent.class, event
                -> executeMessage(((ChatCommandEvent)event).getMessage()));

        EventRouter.INSTANCE.registerHandler(OpenChatEvent.class, event -> {
            openChat(((OpenChatEvent) event).getWhisper());
        });
    }
}
