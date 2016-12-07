package com.home.clicker;

import com.home.clicker.utils.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * Exslims
 * 07.12.2016
 */
public class PrivateMessageManager {
    private GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
    private JFrame frame;
    private User32 user32 = User32.INSTANCE;

    private int count = 0;


    public PrivateMessageManager(JFrame jFrame) {
        this.frame = jFrame;
        keyboardHook.addKeyListener(getAdapter());
    }

    private void execute(GlobalKeyAdapter adapter){
        keyboardHook.removeKeyListener(adapter);

        String text = "@FokitaBF 123 + " + count;
        count++;
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        setForegroundWindow("Path of Exile");

        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        keyboardHook.addKeyListener(getAdapter());
    }

    private GlobalKeyAdapter getAdapter(){
        return new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_LMENU) {
                    frame.setVisible(true);
                }
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_LMENU) {
//                    execute(this);
                    frame.setVisible(false);
                }
            }
        };
    }

    private void setForegroundWindow(final String titleName){
        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            int count = 0;

            public boolean callback(WinDef.HWND hWnd, Pointer arg1) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (wText.isEmpty()) {
                    return true;
                }
//
//                System.out.println("Found window with text " + hWnd
//                        + ", total " + ++count + " Text: " + wText);
                if (wText.equals(titleName)) {
                    user32.SetForegroundWindow(hWnd);
                    return false;
                }
                return true;
            }
        }, null);
    }

}
