package com.home.clicker.core;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Exslims
 * 07.12.2016
 */
public interface User32 extends StdCallLibrary {
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
    WinDef.HWND SetFocus(WinDef.HWND hWnd);
    int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);
    int GetWindowTextA(PointerType hWnd, byte[] lpString, int nMaxCount);
    boolean SetForegroundWindow(WinDef.HWND hWnd);
    WinDef.HWND GetForegroundWindow();
}
