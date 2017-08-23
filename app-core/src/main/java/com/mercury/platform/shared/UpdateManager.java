package com.mercury.platform.shared;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class UpdateManager {
    private final static Logger logger = LogManager.getLogger(UpdateManager.class.getSimpleName());
    private final static String LOCAL_UPDATER_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\local-updater.jar";
    private static final String JAR_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\temp\\MercuryTrade.jar";

    public void doUpdate() {
        try {
            String path = StringUtils.substringAfter(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "/");
            logger.debug("Execute local updater, source path: {}", path);
            if (new File(JAR_FILE_PATH).exists()) {
                Runtime.getRuntime().exec("java -jar " + LOCAL_UPDATER_PATH + " " + "\"" + path + "\"");
                System.exit(0);
            }
        } catch (Exception e1) {
            logger.error("Error while execute local-updater: ", e1);
        }
    }
}
