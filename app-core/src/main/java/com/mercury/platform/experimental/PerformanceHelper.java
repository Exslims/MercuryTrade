package com.mercury.platform.experimental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//some bullshit
public class PerformanceHelper {
    private static final Logger logger = LogManager.getLogger(PerformanceHelper.class);
    private long currentValue = 0;
    private long currentTime = System.currentTimeMillis();
    private long sumValue = 0;
    private boolean shouldLog;

    public PerformanceHelper() {
        shouldLog = false;
    }

    public PerformanceHelper(boolean shouldLog) {
        this.shouldLog = shouldLog;
    }

    public void step(String msg) {
        this.sumValue += System.currentTimeMillis() - currentTime;
        this.currentValue = System.currentTimeMillis() - currentTime;
        this.currentTime = System.currentTimeMillis();
        if (shouldLog) {
            logger.warn(msg + " : " + this.currentValue + " / " + sumValue);
        }
    }

    public void reset() {
        this.currentValue = 0;
        this.currentTime = System.currentTimeMillis();
    }
}
