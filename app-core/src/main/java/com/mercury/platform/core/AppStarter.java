package com.mercury.platform.core;

import com.mercury.platform.core.misc.WhisperNotifier;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Константин on 31.12.2016.
 */
public class AppStarter {
    public static FrameStates APP_STATUS = FrameStates.HIDE;
    private User32 user32 = User32.INSTANCE;
    public void startApplication(){
        new WhisperNotifier();
        new PrivateMessageManager();
        new FileMonitor();

        EventRouter.registerHandler(UILoadedEvent.class, event -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    byte[] windowText = new byte[512];
                    PointerType hwnd = user32.GetForegroundWindow();
                    User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                    if(!Native.toString(windowText).equals("Path of Exile") &&
                            !Native.toString(windowText).equals("MT-Settings")){
                        if(APP_STATUS == FrameStates.SHOW) {
                            EventRouter.fireEvent(new ChangeFrameVisibleEvent(FrameStates.HIDE));
                            APP_STATUS = FrameStates.HIDE;
                        }
                    }else{
                        if(APP_STATUS == FrameStates.HIDE) {
                            EventRouter.fireEvent(new ChangeFrameVisibleEvent(FrameStates.SHOW));
                            APP_STATUS = FrameStates.SHOW;
                        }
                    }
                }
            },0,700);
        });
    }
}
