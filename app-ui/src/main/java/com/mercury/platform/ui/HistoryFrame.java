package com.mercury.platform.ui;

/**
 * Created by Константин on 27.12.2016.
 */
public class HistoryFrame extends OverlaidFrame {
    public HistoryFrame(String title) {
        super("History");
    }

    @Override
    protected void init() {
        super.init();

        setVisible(false);
        disableHideEffect(); // todo
    }

    @Override
    public void initHandlers() {

    }
}
