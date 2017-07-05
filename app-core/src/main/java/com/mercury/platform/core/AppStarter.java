package com.mercury.platform.core;

import com.mercury.platform.core.misc.SoundNotifier;
import com.mercury.platform.core.update.UpdateClientStarter;
import com.mercury.platform.core.utils.error.ErrorHandler;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.hotkey.HotKeysInterceptor;
import com.mercury.platform.shared.UpdateManager;
import com.mercury.platform.shared.config.BaseConfigManager;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.MercuryConfigurationSource;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.sun.jna.platform.win32.User32;
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
    private boolean shutdown = false;
    private volatile int delay = 100;
    private boolean updating = false;

    public void startApplication(){
        BaseConfigManager configuration = new BaseConfigManager(new MercuryConfigurationSource());
        configuration.load();
        Configuration.set(configuration);
        new SoundNotifier();
        new ChatHelper();
        new ErrorHandler();
        new HotKeysInterceptor();

        Executor executor = Executors.newSingleThreadExecutor();
        UpdateClientStarter updateClientStarter = new UpdateClientStarter();
        executor.execute(updateClientStarter);
        HistoryManager.INSTANCE.load();
        UpdateManager updateManager = new UpdateManager();

        MercuryStoreCore.INSTANCE.uiLoadedSubject.subscribe((Boolean state) -> {
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
                    char[] className = new char[512];
                    WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();

                    User32.INSTANCE.GetClassName(hwnd, className, 512);

                    APP_STATUS = FrameVisibleState.SHOW;
                    MercuryStoreCore.INSTANCE.frameVisibleSubject.onNext(FrameVisibleState.SHOW);

//                    if(!Native.toString(className).equals("POEWindowClass")){
//                        if(APP_STATUS == FrameVisibleState.SHOW) {
//                            APP_STATUS = FrameVisibleState.HIDE;
//                            MercuryStore.INSTANCE.frameVisibleSubject.onNext(FrameVisibleState.HIDE);
//                        }
//                    }else{
//                        if(APP_STATUS == FrameVisibleState.HIDE) {
//                            try {
//                                Thread.sleep(delay);
//                                delay = 100;
//                            } catch (InterruptedException e) {
//                                logger.error(e);
//                            }
//                            APP_STATUS = FrameVisibleState.SHOW;
//                            MercuryStore.INSTANCE.frameVisibleSubject.onNext(FrameVisibleState.SHOW);
//                        }
//                    }
                }
            },0,150);
        });
        MercuryStoreCore.INSTANCE.showingDelaySubject.subscribe(state -> this.delay = 300);
        MercuryStoreCore.INSTANCE.shutdownAppSubject.subscribe(state -> this.shutdown = true);
        MercuryStoreCore.INSTANCE.shutdownForUpdateSubject.subscribe(state -> {
            this.updating = true;
            this.shutdown = true;
        });
    }
}
