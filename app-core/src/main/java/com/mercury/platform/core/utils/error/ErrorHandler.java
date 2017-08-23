package com.mercury.platform.core.utils.error;

import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorHandler {
    private Logger logger = LogManager.getLogger(ErrorHandler.class.getSimpleName());

    public ErrorHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.error(ExceptionUtils.getStackTrace(throwable));
        });
        MercuryStoreCore.errorHandlerSubject.subscribe(error -> {
            logger.error(error.getErrorMessage() + "\n" + ExceptionUtils.getStackTrace(error.getStackTrace()));
        });
    }
}
