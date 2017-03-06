package com.mercury.platform.core;

import com.mercury.platform.core.misc.SoundNotifier;
import com.mercury.platform.core.update.UpdateClientStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.path.GamePathSearcher;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.shared.events.custom.ShutdownApplication;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Константин on 31.12.2016.
 */
public class AppStarter {
    private static final Logger logger = LogManager.getLogger(AppStarter.class.getSimpleName());
    public static FrameStates APP_STATUS = FrameStates.HIDE;
    private User32 user32 = User32.INSTANCE;
    private boolean shutdown = false;

    public void startApplication(){
        ConfigManager.INSTANCE.load();
        new SoundNotifier();
        new ChatHelper();

        Executor executor = Executors.newSingleThreadExecutor();
        UpdateClientStarter updateClientStarter = new UpdateClientStarter();
        executor.execute(updateClientStarter);
        HistoryManager.INSTANCE.load();

        EventRouter.INSTANCE.registerHandler(UILoadedEvent.class, event -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(shutdown){
                        timer.cancel();
                        updateClientStarter.shutdown();
                        System.exit(0);
                    }
                    byte[] windowText = new byte[512];
                    PointerType hwnd = user32.GetForegroundWindow();
                    User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                    if(!Native.toString(windowText).equals("Path of Exile") && !Native.toString(windowText).equals("MercuryTrade")){
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
        EventRouter.INSTANCE.registerHandler(ShutdownApplication.class, event -> {
            shutdown = true;
        });
    }
}
