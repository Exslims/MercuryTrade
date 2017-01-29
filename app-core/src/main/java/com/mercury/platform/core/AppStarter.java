package com.mercury.platform.core;

import com.mercury.platform.core.misc.SoundNotifier;
import com.mercury.platform.core.update.UpdateClientStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Константин on 31.12.2016.
 */
public class AppStarter {
    public static FrameStates APP_STATUS = FrameStates.HIDE;
    private User32 user32 = User32.INSTANCE;
    public void startApplication(){
        new SoundNotifier();
        new ChatHelper();
        new GamePathSearcher(FileMonitor::new);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new UpdateClientStarter());


        /*
        [DllImport("user32.dll&quot]
            private static extern IntPtr SetWinEventHook(uint eventMin, uint eventMax, IntPtr hmodWinEventProc, WinEventDelegate lpfnWinEventProc, uint idProcess, uint idThread, uint dwFlags);
         */
        EventRouter.INSTANCE.registerHandler(UILoadedEvent.class, event -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    byte[] windowText = new byte[512];
                    PointerType hwnd = user32.GetForegroundWindow();
                    User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                    if(!Native.toString(windowText).equals("Path of Exile")){
                        if(APP_STATUS == FrameStates.SHOW) {
                            APP_STATUS = FrameStates.HIDE;
                            EventRouter.INSTANCE.fireEvent(new ChangeFrameVisibleEvent(FrameStates.HIDE));
                        }
                    }else{
                        if(APP_STATUS == FrameStates.HIDE) {
                            APP_STATUS = FrameStates.SHOW;
                            EventRouter.INSTANCE.fireEvent(new ChangeFrameVisibleEvent(FrameStates.SHOW));
                        }
                    }
                }
            },0,500);
        });
    }
}
