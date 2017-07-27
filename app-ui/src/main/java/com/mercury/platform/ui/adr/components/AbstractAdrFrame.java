package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;

import java.awt.*;

public abstract class AbstractAdrFrame<T extends AdrComponentDescriptor> extends AbstractOverlaidFrame{
    private int settingWl;
    @Getter
    protected T descriptor;
    private WinDef.HWND componentHwnd;

    protected AbstractAdrFrame(T descriptor) {
        super();
        this.descriptor = descriptor;
        AWTUtilities.setWindowOpaque(this, false);
    }

    private void setTransparent(Component w) {
        this.componentHwnd = getHWnd(w);
        this.settingWl = User32.INSTANCE.GetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE);
        int transparentWl = User32.INSTANCE.GetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE) |
                WinUser.WS_EX_LAYERED |
                WinUser.WS_EX_TRANSPARENT;
        User32.INSTANCE.SetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE, transparentWl);
    }
    private static WinDef.HWND getHWnd(Component w) {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(w));
        return hwnd;
    }

    public void enableSettings() {
        User32.INSTANCE.SetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE, settingWl);
    }
    public void disableSettings() {
        this.showComponent();
        setTransparent(this);
    }
}
