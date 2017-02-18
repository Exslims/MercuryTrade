package com.mercury.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UpdaterMain {
    private static final Logger log = LogManager.getLogger(UpdaterMain.class.getSimpleName());
    private static final String JAR_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\temp";

    public static void main(String[] args){
        if(args.length == 1) {
            log.info("Starting update local jar, source path: {}",args[0]);
            try {
                Thread.sleep(1000);
                Files.copy(
                        Paths.get(JAR_FILE_PATH + File.separator + "MercuryTrade.jar"),
                        Paths.get(args[0]),
                        StandardCopyOption.REPLACE_EXISTING);
                log.info("Update the local file has been successfully. Execute application: " + args[0]);
                Runtime.getRuntime().exec("java -jar " + "\"" + args[0] + "\"");
                new File(JAR_FILE_PATH + File.separator + "MercuryTrade.jar")
                        .delete();
                System.exit(0);
            } catch (Exception e) {
                log.error("Error while replacing existing jar: ", e);
            }
        }
    }
}
