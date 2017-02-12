package com.mercury.platform.core.update;


import com.mercury.platform.core.MercuryConstants;
import com.mercury.platform.core.update.bus.event.UpdateReceivedEvent;
import com.mercury.platform.core.update.bus.handlers.UpdateEventHandler;
import com.mercury.platform.core.update.core.UpdaterClient;
import com.mercury.platform.core.update.core.holder.VersionHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.UpdateReadyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Frost on 14.01.2017.
 */
public class UpdateClientStarter implements Runnable{
    private static final Logger LOGGER = LogManager.getLogger(UpdateClientStarter.class.getSimpleName());
    private static final String JARS_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\temp";

    @Override
    public void run() {
        UpdaterClient updaterClient = new UpdaterClient(MercuryConstants.SERVER_HOST, MercuryConstants.APP_VERSION, MercuryConstants.PORT);
        updaterClient.registerListener(handler -> {
            LOGGER.info("update received, size = {} bytes" , handler.getBytes().length);
            Files.write(Paths.get(JARS_FILE_PATH + "\\MercuryTrade.jar") , handler.getBytes() , StandardOpenOption.CREATE);
            updaterClient.shutdown();
            EventRouter.INSTANCE.fireEvent(new UpdateReadyEvent());
        });
        try {
            updaterClient.start();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
    public synchronized void setMercuryVersion(String mercuryVersion) {
        String version = mercuryVersion.replace(".", "0");
        VersionHolder versionHolder = VersionHolder.getInstance();
        versionHolder.setVersion(Integer.valueOf(version));
    }
}
