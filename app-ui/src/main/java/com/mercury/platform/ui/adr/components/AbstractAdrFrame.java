package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.DestroySubscription;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;
import rx.Subscription;

import java.awt.*;

public abstract class AbstractAdrFrame<T extends AdrComponentDescriptor> extends AbstractOverlaidFrame implements DestroySubscription {
    @Getter
    protected T descriptor;
    private int settingWl;
    private WinDef.HWND componentHwnd;

    private Subscription adrRepaintSubscription;
    private Subscription adrVisibleSubscription;

    protected AbstractAdrFrame(T descriptor) {
        super();
        this.descriptor = descriptor;
        AWTUtilities.setWindowOpaque(this, false);
    }

    private static WinDef.HWND getHWnd(Component w) {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(w));
        return hwnd;
    }

    @Override
    protected void initialize() {
        this.setLocation(descriptor.getLocation());
        this.setOpacity(descriptor.getOpacity());
        this.componentsFactory.setScale(descriptor.getScale());
    }

    @Override
    public void subscribe() {
        this.adrRepaintSubscription = MercuryStoreUI.adrRepaintSubject.subscribe(state -> {
            this.repaint();
            this.pack();
        });
        this.adrVisibleSubscription = MercuryStoreCore.adrVisibleSubject.subscribe(state -> {
            switch (state) {
                case SHOW: {
                    this.processingHideEvent = false;
                    break;
                }
                case HIDE: {
                    this.processingHideEvent = true;
                    break;
                }
            }
        });
        MercuryStoreUI.onDestroySubject.subscribe(state -> this.onDestroy());
    }

    private void setTransparent(Component w) {
        this.componentHwnd = getHWnd(w);
        this.settingWl = User32.INSTANCE.GetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE);
        int transparentWl = User32.INSTANCE.GetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE) |
                WinUser.WS_EX_LAYERED |
                WinUser.WS_EX_TRANSPARENT;
        User32.INSTANCE.SetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE, transparentWl);
    }

    @Override
    public void onDestroy() {
        this.adrRepaintSubscription.unsubscribe();
        this.adrVisibleSubscription.unsubscribe();
    }

    public abstract void setPanel(AdrComponentPanel panel);

    public void enableSettings() {
        User32.INSTANCE.SetWindowLong(componentHwnd, WinUser.GWL_EXSTYLE, settingWl);
    }

    public void disableSettings() {
        this.showComponent();
        setTransparent(this);
    }
}
