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
import com.mercury.platform.shared.config.MercuryConfigurationSource;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.store.MercuryStore;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppStarter {
    private static final Logger logger = LogManager.getLogger(AppStarter.class.getSimpleName());
    public static FrameVisibleState APP_STATUS = FrameVisibleState.HIDE;
    public static WinDef.HWND poeWindow;
    private User32 user32 = User32.INSTANCE;
    private boolean shutdown = false;
    private volatile int delay = 100;
    private boolean updating = false;

    public void startApplication(){
        BaseConfigManager configuration = new BaseConfigManager(new MercuryConfigurationSource());
        configuration.load();
        Configuration.set(configuration);

        ConfigManager.INSTANCE.load();
        new SoundNotifier();
        new ChatHelper();
        new ErrorHandler();

        Executor executor = Executors.newSingleThreadExecutor();
        UpdateClientStarter updateClientStarter = new UpdateClientStarter();
        executor.execute(updateClientStarter);
        HistoryManager.INSTANCE.load();
        UpdateManager updateManager = new UpdateManager();

        MercuryStore.INSTANCE.uiLoadedSubject.subscribe(state -> {
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
                    byte[] className = new byte[512];
                    WinDef.HWND hwnd = user32.GetForegroundWindow();
                    User32.INSTANCE.GetClassNameA(hwnd, className, 512);
                    if(!Native.toString(className).equals("POEWindowClass")){
                        if(APP_STATUS == FrameVisibleState.SHOW) {
                            APP_STATUS = FrameVisibleState.HIDE;
                            MercuryStore.INSTANCE.frameVisibleSubject.onNext(FrameVisibleState.HIDE);
                        }
                    }else{
                        poeWindow = hwnd;
                        if(APP_STATUS == FrameVisibleState.HIDE) {
                            try {
                                Thread.sleep(delay);
                                delay = 100;
                            } catch (InterruptedException e) {
                            }
                            APP_STATUS = FrameVisibleState.SHOW;
                            MercuryStore.INSTANCE.frameVisibleSubject.onNext(FrameVisibleState.SHOW);
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
