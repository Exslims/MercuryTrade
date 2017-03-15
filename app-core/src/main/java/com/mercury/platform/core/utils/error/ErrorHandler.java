package com.mercury.platform.core.utils.error;

/**
 * Created by Константин on 15.03.2017.
 */
public class ErrorHandler {
    public ErrorHandler(){
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
        });
    }
}
