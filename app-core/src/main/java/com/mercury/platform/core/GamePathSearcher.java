package com.mercury.platform.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Константин on 24.01.2017.
 */
public class GamePathSearcher{
    private final Logger log = Logger.getLogger(GamePathSearcher.class);

    public GamePathSearcher(GameFoundCallback callback) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String line;
                    Process p = Runtime.getRuntime().exec("powershell -command Get-Process PathOfExile | Format-List path");
                    BufferedReader input = new BufferedReader
                            (new InputStreamReader(p.getInputStream(),"866"));
                    while ((line = input.readLine()) != null) {
                        if (line.contains("PathOfExile")) {
                            String path = StringUtils.substringBetween(line, "Path : ", "PathOfExile.exe");
                            callback.onFound(path);
                            timer.cancel();
                        }
                    }
                }catch (IOException e){
                    log.error(e);
                }
            }
        },0,1000);
    }
}
