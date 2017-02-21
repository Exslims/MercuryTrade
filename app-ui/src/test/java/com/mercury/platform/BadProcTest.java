package com.mercury.platform;

import com.mercury.platform.core.*;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.User32;

/**
 * Created by Константин on 21.02.2017.
 */
public class BadProcTest {
    public static void main(String[] args) {
        int WINEVENT_OUTOFCONTEXT = 0;
        int WINEVENT_SKIPOWNPROCESS = 2;
        int EVENT_SYSTEM_MINIMIZEEND = 23;
        int EVENT_SYSTEM_MINIMIZESTART = 22;

        User32RW.WinEventProc testproc = new User32RW.WinEventProc() {
            @Override
            public void callback(WinNT.HANDLE hWinEventHook, int event, WinDef.HWND hwnd, int idObject, int idChild, int dwEventThread, int dwmsEventTime) {
                System.out.println("Callback called");
            }
        };

        WinNT.HANDLE test_hook = User32RW.INSTANCE.SetWinEventHook(
                EVENT_SYSTEM_MINIMIZESTART, EVENT_SYSTEM_MINIMIZEEND,
                Pointer.NULL, // Callback not in a dll
                testproc,
                0, 0,              // Process and thread IDs of interest (0 = all)
                WINEVENT_OUTOFCONTEXT | WINEVENT_SKIPOWNPROCESS);
        int er = Kernel32.INSTANCE.GetLastError();
        System.out.println("namechange reg error " + er);
        if(er == 0)
            User32RW.INSTANCE.UnhookWinEvent(test_hook);
    }

    interface User32RW extends User32 {
        User32RW INSTANCE = (User32RW) Native.loadLibrary("user32", User32RW.class);

        public WinNT.HANDLE SetWinEventHook(int eventMin, int eventMax, Pointer hmodWinEventProc, WinEventProc lpfnWinEventProc,
                                            int idProcess,
                                            int idThread,
                                            int dwflags);

        boolean UnhookWinEvent(WinNT.HANDLE handle);


        public static interface WinEventProc extends StdCallCallback {
            void callback(
                    WinNT.HANDLE hWinEventHook,
                    int event,
                    WinDef.HWND hwnd,
                    int idObject,
                    int idChild,
                    int dwEventThread,
                    int dwmsEventTime
            );
        }
    }
}
