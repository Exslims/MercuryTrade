package com.mercury.platform.ui.frame.setup.location;

import com.mercury.platform.ui.frame.movable.AbstractMovableComponentFrame;
import com.mercury.platform.ui.frame.other.SetUpLocationFrame;
import com.mercury.platform.ui.frame.setup.SetUpCommander;
import com.mercury.platform.ui.manager.FramesManager;

import java.util.Arrays;
import java.util.List;

public class SetUpLocationCommander extends SetUpCommander<AbstractMovableComponentFrame> {
    public SetUpLocationCommander() {
        super();
    }

    @Override
    public void setUpAll() {
        this.frames.forEach((title, frame) -> {
            this.enableMovement(frame, true);
            this.activeFrames.add(frame);
        });
    }

    @Override
    public void setUpAllExclude(Class[] framesClasses) {
        List<Class> framesList = Arrays.asList(framesClasses);
        this.frames.forEach((title, frame) -> {
            if (!framesList.contains(title)) {
                this.enableMovement(frame, true);
                this.activeFrames.add(frame);
            }
        });
    }

    @Override
    public void setOrEndUp(Class frameClass, boolean showingSetUpFrame) {
        AbstractMovableComponentFrame frame = frames.get(frameClass);
        if (frame.getMoveState().equals(LocationState.DEFAULT)) {
            this.enableMovement(frame, showingSetUpFrame);
            this.activeFrames.add(frame);
        } else {
            this.disableMovement(frame);
            this.activeFrames.remove(frame);
        }
    }

    @Override
    public void endUpAll() {
        this.activeFrames.forEach(this::disableMovement);
        this.activeFrames.clear();
    }

    @Override
    public void endUp(Class frameClass) {
        AbstractMovableComponentFrame frame = frames.get(frameClass);
        this.disableMovement(frame);
        this.activeFrames.remove(frame);
    }

    private void enableMovement(AbstractMovableComponentFrame frame, boolean showSetUpFrame) {
        frame.setState(LocationState.MOVING);
        if (showSetUpFrame) {
            FramesManager.INSTANCE.showFrame(SetUpLocationFrame.class);
        }
    }

    private void disableMovement(AbstractMovableComponentFrame frame) {
        frame.setState(LocationState.DEFAULT);
        FramesManager.INSTANCE.hideFrame(SetUpLocationFrame.class);
    }
}
