package com.mercury.platform.core.utils.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorHandler {
    private Logger logger = LogManager.getLogger(ErrorHandler.class.getSimpleName());
    public ErrorHandler(){
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.error(throwable);
        });
    }
}
