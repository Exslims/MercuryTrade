package com.mercury.platform.core.utils.path;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Константин on 24.01.2017.
 */
public class GamePathSearcher{
    private final Logger log = LogManager.getLogger(GamePathSearcher.class);

    public GamePathSearcher(GameFoundCallback callback) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String line;
                    String path;
                    Process p = Runtime.getRuntime().exec("powershell -command Get-Process | Format-List path");
                    BufferedReader input = new BufferedReader
                            (new InputStreamReader(p.getInputStream(),"866"));
                    while ((line = input.readLine()) != null) {
                        if (line.contains("PathOfExile")) {
                            String temp = StringUtils.substringAfter(line, "Path : ");
                            path = StringUtils.substringBeforeLast(temp, File.separator);
                            path += File.separator;
                            callback.onFound(path);
                            timer.cancel();
                            return;
                        }
                    }
                    timer.cancel();
                }catch (IOException e){
                    log.error(e);
                }
            }
        },0,1000);
    }
}
