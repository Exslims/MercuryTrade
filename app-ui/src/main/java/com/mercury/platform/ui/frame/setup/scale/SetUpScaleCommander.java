package com.mercury.platform.ui.frame.setup.scale;


import com.mercury.platform.ui.frame.AbstractScalableComponentFrame;
import com.mercury.platform.ui.frame.setup.SetUpCommander;

public class SetUpScaleCommander extends SetUpCommander<AbstractScalableComponentFrame>{
    public SetUpScaleCommander() {
        super();
    }
    @Override
    public void setUpAll() {
        frames.forEach((k,v) -> {
            v.setState(ScaleState.ENABLE);
        });
    }

    @Override
    public void setUpAllExclude(Class[] framesClasses) {

    }

    @Override
    public void setUp(Class frameClass, boolean showingSetUpFrame) {

    }

    @Override
    public void setOrEndUp(Class frameClass, boolean showingSetUpFrame) {

    }

    @Override
    public void endUpAll() {
        frames.forEach((k,v) -> {
            v.setState(ScaleState.DEFAULT);
        });
    }

    @Override
    public void endUp(Class frameClass) {

    }
}
