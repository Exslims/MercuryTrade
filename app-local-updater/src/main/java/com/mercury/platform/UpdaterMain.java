package com.mercury.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Константин on 08.01.2017.
 */
public class UpdaterMain {
    private static final String JAR_FILE_PATH = System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\versions";

    public static void main(String[] args) throws IOException {
        if(args.length == 1) {
            Files.copy(
                    Paths.get(JAR_FILE_PATH + File.separator + "MercuryTrade.jar"),
                    Paths.get(args[0]),
                    StandardCopyOption.REPLACE_EXISTING);

            new File(JAR_FILE_PATH + File.separator + "MercuryTrade.jar")
                    .delete();
        }

//        try {
//            File jar = new File(UpdaterMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
//            System.out.println(jar.getAbsolutePath());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }
}
