package com.mercury.platform.core.update;


import com.mercury.platform.core.MercuryConstants;
import com.mercury.platform.core.update.core.UpdaterClient;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Frost on 14.01.2017.
 */
public class UpdateClientStarter implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(UpdateClientStarter.class.getSimpleName());
    private static final String JARS_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\temp";

    private UpdaterClient updaterClient;

    @Override
    public void run() {
        updaterClient = new UpdaterClient(MercuryConstants.SERVER_HOST, MercuryConstants.APP_VERSION, MercuryConstants.PORT);
        updaterClient.registerListener(handler -> {
            Files.write(Paths.get(JARS_FILE_PATH + "\\MercuryTrade.jar"), handler.getBytes(), StandardOpenOption.CREATE);
            setMercuryVersion(getIncrementedVersion(MercuryConstants.APP_VERSION));
            MercuryStoreCore.updateReadySubject.onNext(true);
        });
        try {
            updaterClient.start();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public synchronized void setMercuryVersion(String mercuryVersion) {
        String version = mercuryVersion.replace(".", "0");
        ApplicationHolder applicationHolder = ApplicationHolder.getInstance();
        applicationHolder.setVersion(Integer.valueOf(version));
    }

    private String getIncrementedVersion(String version) {
        String replace = version.replace(".", "0");
        Integer intVersion = Integer.valueOf(replace);
        intVersion++;
        return String.valueOf(intVersion).replace("0", ".");
    }

    public void shutdown() {
        updaterClient.shutdown();
    }
}
