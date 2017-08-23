package com.mercury.platform.ui.frame.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SetUpCommander<T> {
    protected Map<Class, T> frames;
    protected List<T> activeFrames;

    public SetUpCommander() {
        this.frames = new HashMap<>();
        this.activeFrames = new ArrayList<>();
    }

    public void addFrame(T frame) {
        frames.put(frame.getClass(), frame);
    }

    public abstract void setUpAll();

    public abstract void setUpAllExclude(Class... framesClasses);

    public abstract void setOrEndUp(Class frameClass, boolean showingSetUpFrame);

    public abstract void endUpAll();

    public abstract void endUp(Class frameClass);
}
