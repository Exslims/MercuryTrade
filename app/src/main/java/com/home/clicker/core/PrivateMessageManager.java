package com.home.clicker.core;

import com.home.clicker.shared.HasEventHandlers;
import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.ChangeFrameVisibleEvent;
import com.home.clicker.shared.events.custom.CopyToClipboardEvent;
import com.home.clicker.shared.events.custom.OpenChatEvent;
import com.home.clicker.shared.events.custom.ChatCommandEvent;
import com.home.clicker.ui.FrameStates;
import com.home.clicker.core.misc.WhisperNotifier;
import com.home.clicker.core.utils.FileMonitor;
import com.home.clicker.core.utils.LoggedMessagesUtils;
import com.home.clicker.shared.PoeShortCastSettings;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Exslims
 * 07.12.2016
 */
public class PrivateMessageManager implements HasEventHandlers {
    private GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
    private GlobalKeyAdapter adapter;
    private User32 user32 = User32.INSTANCE;
    private Robot robot;

    public PrivateMessageManager() {
        GlobalKeyAdapter adapter = getAdapter();
        this.adapter = adapter;
        initHandlers();

        keyboardHook.addKeyListener(adapter);
        new WhisperNotifier();
        new LoggedMessagesUtils();
        new FileMonitor();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        //TODO UBERI NAHUI ETO
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                byte[] windowText = new byte[512];
                PointerType hwnd = user32.GetForegroundWindow();
                User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                if(!Native.toString(windowText).equals("Path of Exile") &&
                        !Native.toString(windowText).equals("PoeShortCast")){
                    EventRouter.fireEvent(new ChangeFrameVisibleEvent(FrameStates.HIDE));
                    PoeShortCastSettings.APP_STATUS = FrameStates.HIDE;
                }else{
                    EventRouter.fireEvent(new ChangeFrameVisibleEvent(FrameStates.SHOW));
                    PoeShortCastSettings.APP_STATUS = FrameStates.SHOW;
                }
            }
        },0,1000);
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
        try {
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            System.out.println("controll pressed" + " " + data);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        robot.keyPress(KeyEvent.VK_V);
        try {
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            System.out.println("v pressed" + " " + data);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        try {
            String data = (String) clipboard.getData(DataFlavor.stringFlavor);
            System.out.println("ressed" + " " + data);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//                        useAllFlasks();
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
        EventRouter.registerHandler(ChatCommandEvent.class, event
                -> executeMessage(((ChatCommandEvent)event).getMessage()));

        EventRouter.registerHandler(OpenChatEvent.class, event -> {
            openChat(((OpenChatEvent) event).getWhisper());
        });
//        EventRouter.registerHandler(CopyToClipboardEvent.class, event -> {
//            System.out.println("in copy");
//            String content = ((CopyToClipboardEvent) event).getContent();
//            StringSelection selection = new StringSelection(content);
//            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//            clipboard.setContents(selection, null);
//        });
    }
}
