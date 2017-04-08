package com.mercury.platform.core;

import com.mercury.platform.core.misc.SoundNotifier;
import com.mercury.platform.core.update.UpdateClientStarter;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.UpdateManager;
import com.mercury.platform.shared.config.BaseConfigManager;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.MercuryDataSource;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppStarter {
    private static final Logger logger = LogManager.getLogger(AppStarter.class.getSimpleName());
    public static FrameVisibleState APP_STATUS = FrameVisibleState.HIDE;
    private User32 user32 = User32.INSTANCE;
    private boolean shutdown = false;
    private volatile int delay = 100;
    private boolean updating = false;

    public void startApplication(){
//        BaseConfigManager configuration = new BaseConfigManager(new MercuryDataSource());
//        configuration.load();
//        Configuration.set(configuration);

        ConfigManager.INSTANCE.load();
        new SoundNotifier();
        new ChatHelper();
        new ErrorHandler();

        Executor executor = Executors.newSingleThreadExecutor();
        UpdateClientStarter updateClientStarter = new UpdateClientStarter();
        executor.execute(updateClientStarter);
        HistoryManager.INSTANCE.load();
        UpdateManager updateManager = new UpdateManager();

        EventRouter.CORE.registerHandler(UILoadedEvent.class, event -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(shutdown){
                        timer.cancel();
                        updateClientStarter.shutdown();
                        if(updating){
                            updateManager.doUpdate();
                        }
                        System.exit(0);
                    }
                    byte[] windowText = new byte[512];
                    PointerType hwnd = user32.GetForegroundWindow();
                    User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                    if(!Native.toString(windowText).equals("Path of Exile")){
                        if(APP_STATUS == FrameVisibleState.SHOW) {
                            APP_STATUS = FrameVisibleState.HIDE;
                            EventRouter.CORE.fireEvent(new ChangeFrameVisibleEvent(FrameVisibleState.HIDE));
                        }
                    }else{
                        if(APP_STATUS == FrameVisibleState.HIDE) {
                            try {
                                Thread.sleep(delay);
                                delay = 100;
                            } catch (InterruptedException e) {
                            }
                            APP_STATUS = FrameVisibleState.SHOW;
                            EventRouter.CORE.fireEvent(new ChangeFrameVisibleEvent(FrameVisibleState.SHOW));
                        }
                    }
                }
            },0,50);
        });
        EventRouter.CORE.registerHandler(AddShowDelayEvent.class, event -> {
            delay = 300;
        });
        EventRouter.CORE.registerHandler(ShutdownApplication.class, event -> {
            shutdown = true;
        });
        EventRouter.CORE.registerHandler(ShutDownForUpdateEvent.class, event -> {
            shutdown = true;
            updating = true;
        });
    }
}
