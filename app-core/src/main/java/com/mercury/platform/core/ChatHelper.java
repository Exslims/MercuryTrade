package com.mercury.platform.core;

import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.shared.events.custom.CopyToClipboardEvent;
import com.mercury.platform.shared.events.custom.OpenChatEvent;
import com.mercury.platform.shared.events.custom.SetForegroundGameEvent;
import com.sun.jna.Native;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * Exslims
 * 07.12.2016
 */
public class ChatHelper implements HasEventHandlers {
    private GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
    private GlobalKeyAdapter adapter;
    private User32 user32 = User32.INSTANCE;
    private Robot robot;

    public ChatHelper() {
        GlobalKeyAdapter adapter = getAdapter();
        this.adapter = adapter;
        initHandlers();
        keyboardHook.addKeyListener(adapter);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(String message) {
        keyboardHook.removeKeyListener(adapter);
        StringSelection selection = new StringSelection(message);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);

        setForegroundWindow("Path of Exile");

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

        keyboardHook.addKeyListener(adapter);
    }
    private void openChat(String whisper){
        keyboardHook.removeKeyListener(adapter);
        setForegroundWindow("Path of Exile");

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

        keyboardHook.addKeyListener(adapter);
    }


    private GlobalKeyAdapter getAdapter(){
        return new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                switch (event.getVirtualKeyCode()){
                    case GlobalKeyEvent.VK_F2: {
//                        EventRouter.fireEvent(new StateChangeEvent(FrameStates.SHOW));
                    }
                    break;
                    case GlobalKeyEvent.VK_F3: {
//                        EventRouter.fireEvent(new StateChangeEvent(FrameStates.UNDEFINED));
                    }
                    break;
                    case GlobalKeyEvent.VK_TAB: {
//                        if(!altPressed) {
//                            nextTab();
//                        }
                    }
                    break;
                    case GlobalKeyEvent.VK_2:{
                        useAllFlasks();
                    }
                    break;
                }
            }
        };
    }

    public void setForegroundWindow(final String titleName){
            user32.EnumWindows((hWnd, arg1) -> {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (wText.isEmpty()) {
                    return true;
                }
                if (wText.equals(titleName)) {
                    user32.SetForegroundWindow(hWnd);
                    return false;
                }
                return true;
            }, null);
    }

    private void nextTab(){
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
    }
    private void useAllFlasks(){
        robot.keyPress(KeyEvent.VK_3);
        robot.keyRelease(KeyEvent.VK_3);
        robot.keyPress(KeyEvent.VK_4);
        robot.keyRelease(KeyEvent.VK_4);
        robot.keyPress(KeyEvent.VK_5);
        robot.keyRelease(KeyEvent.VK_5);
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ChatCommandEvent.class, event
                -> executeMessage(((ChatCommandEvent)event).getMessage()));

        EventRouter.INSTANCE.registerHandler(OpenChatEvent.class, event -> {
            openChat(((OpenChatEvent) event).getWhisper());
        });
        EventRouter.INSTANCE.registerHandler(CopyToClipboardEvent.class, event -> {
            String content = ((CopyToClipboardEvent) event).getContent();
            StringSelection selection = new StringSelection(content);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        });
        EventRouter.INSTANCE.registerHandler(SetForegroundGameEvent.class, event -> {
            setForegroundWindow("Path of Exile");
        });
    }
}
