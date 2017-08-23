package com.mercury.platform.experimental;

//some bullshit
public class PerformanceHelper {
    private long currentValue = 0;
    private long currentTime = System.currentTimeMillis();

    public void step(String msg) {
        this.currentValue += System.currentTimeMillis() - currentTime;
        this.currentTime = System.currentTimeMillis();
        System.out.println(msg + " : " + this.currentValue);
    }

    public void reset() {
        this.currentValue = 0;
        this.currentTime = System.currentTimeMillis();
    }
}
