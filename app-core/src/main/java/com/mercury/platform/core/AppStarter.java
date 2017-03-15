package com.mercury.platform.core;

import com.mercury.platform.core.misc.SoundNotifier;
import com.mercury.platform.core.update.UpdateClientStarter;
import com.mercury.platform.core.utils.FileMonitor;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.core.utils.path.GamePathSearcher;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.UpdateManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
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
    private volatile int delay = 100;
    private boolean updating = false;

    public void startApplication(){
        ConfigManager.INSTANCE.load();
        new SoundNotifier();
        new ChatHelper();
        new ErrorHandler();

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
                        if(updating){
                            UpdateManager.INSTANCE.doUpdate();
                        }
                        System.exit(0);
                    }
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
                            try {
                                Thread.sleep(delay);
                                delay = 100;
                            } catch (InterruptedException e) {
                            }
                            APP_STATUS = FrameStates.SHOW;
                            EventRouter.INSTANCE.fireEvent(new ChangeFrameVisibleEvent(FrameStates.SHOW));
                        }
                    }
                }
            },0,50);
        });
        EventRouter.INSTANCE.registerHandler(AddShowDelayEvent.class,event -> {
            delay = 300;
        });
        EventRouter.INSTANCE.registerHandler(ShutdownApplication.class, event -> {
            shutdown = true;
        });
        EventRouter.INSTANCE.registerHandler(ShutDownForUpdateEvent.class, event -> {
            shutdown = true;
            updating = true;
        });
    }
}
