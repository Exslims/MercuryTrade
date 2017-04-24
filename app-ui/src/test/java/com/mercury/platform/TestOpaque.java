package com.mercury.platform;

import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 19.04.2017.
 */
public class TestOpaque {
    public static void main(String[] args) {
        JDialog w = new JDialog();
        w.setPreferredSize(new Dimension(100,200));
        w.setUndecorated(true);
//        w.setFocusableWindowState(false);
        w.add(new JTextField("Test"));
//        w.add(new JComponent() {
//            /**
//             * This will draw a black cross on screen.
//             */
//            protected void paintComponent(Graphics g) {
//                g.setColor(Color.BLACK);
//                g.fillRect(0, getHeight() / 2 - 10, getWidth(), 20);
//                g.fillRect(getWidth() / 2 - 10, 0, 20, getHeight());
//            }
//
//            public Dimension getPreferredSize() {
//                return new Dimension(100, 100);
//            }
//        });
        w.pack();
        w.setLocationRelativeTo(null);
        w.setVisible(true);
        w.setAlwaysOnTop(true);
        /**
         * This sets the background of the window to be transparent.
         */
        AWTUtilities.setWindowOpaque(w, false);
//        AWTUtilities.setWindowOpacity(w, 0.5f);
        setTransparent(w);
    }

    private static void setTransparent(Component w) {
        WinDef.HWND hwnd = getHWnd(w);
        int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
//        wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
        wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
    }

    /**
     * Get the window handle from the OS
     */
    private static WinDef.HWND getHWnd(Component w) {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(w));
        return hwnd;
    }
}
