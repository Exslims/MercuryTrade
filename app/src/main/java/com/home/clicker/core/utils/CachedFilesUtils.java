package com.home.clicker.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Exslims
 * 07.12.2016
 */
public class CachedFilesUtils {
    private static String gamePath = "";
    private static final String CONFIG_PATH = System.getenv("USERPROFILE") + File.separator +
            "AppData" + File.separator + "Local" + File.separator + "PoeShortCast";
    public static final String CONFIG_FILE_NAME = CONFIG_PATH +  File.separator + "config.txt";
    public static String getGamePath() {
        File file = new File(CONFIG_FILE_NAME);
        if (!file.exists()) {
            new File(CONFIG_PATH).mkdir();
            try {
                Files.write(Paths.get(CONFIG_FILE_NAME),("GAME_PATH = " + gamePath).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                String gamePathToken = new String(Files.readAllBytes(Paths.get(CONFIG_FILE_NAME)));
                gamePath = StringUtils.substringAfter(gamePathToken,"= ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gamePath;
    }

    public static void setGamePath(String path){
        gamePath = path;
        try {
            Files.write(Paths.get(CONFIG_FILE_NAME),("GAME_PATH = " + path).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File getCustomButtonConfig(){
        File configFile = new File(CONFIG_PATH + File.separator + "buttons.json");
        if(!configFile.exists()){
            try {
                Files.write(Paths.get(CONFIG_PATH + File.separator + "buttons.json"),"".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(CONFIG_PATH + File.separator + "buttons.json");
    }
}
