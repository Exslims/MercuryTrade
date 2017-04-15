package com.mercury.platform;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Created by Константин on 15.04.2017.
 */
public class Test {
    public static void main(String[] args) {
        final User32 user32 = User32.INSTANCE;
        user32.EnumWindows(new WinUser.WNDENUMPROC() {
            int count = 0;
            @Override
            public boolean callback(WinDef.HWND hwnd, Pointer pointer) {
                byte[] windowText = new byte[512];
                byte[] className = new byte[512];
                user32.GetWindowTextA(hwnd, windowText, 512);
                user32.GetClassNameA(hwnd, className, 512);
                String title = Native.toString(windowText);
                String classN = Native.toString(className);

                // get rid of this if block if you want all windows regardless of whether
                // or not they have text
                if (title.isEmpty()) {
                    return true;
                }

                System.out.println("Title: " + title + " Class name: " + classN);
                return true;
            }
        },null);
    }
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
        boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
        int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);
        int GetClassNameA(WinDef.HWND hwnd, byte[] lpString,int nMaxCount);
    }
}
