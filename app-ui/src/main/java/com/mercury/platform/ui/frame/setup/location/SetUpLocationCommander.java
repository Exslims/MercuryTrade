package com.mercury.platform.ui.frame.setup.location;

import com.mercury.platform.ui.frame.other.SetUpLocationFrame;
import com.mercury.platform.ui.frame.movable.MovableComponentFrame;
import com.mercury.platform.ui.frame.setup.SetUpCommander;
import com.mercury.platform.ui.manager.FramesManager;

import java.util.*;

public class SetUpLocationCommander extends SetUpCommander<MovableComponentFrame>{
    public SetUpLocationCommander() {
        super();
    }
    @Override
    public void setUpAll(){
        frames.forEach((title,frame)->{
            enableMovement(frame,true);
            activeFrames.add(frame);
        });
    }

    @Override
    public void setUpAllExclude(Class[] framesClasses) {
        List<Class> framesList = Arrays.asList(framesClasses);
        frames.forEach((title, frame)->{
            if(!framesList.contains(title)) {
                enableMovement(frame,true);
                activeFrames.add(frame);
            }
        });
    }
    @Override
    public void setUp(Class frameClass, boolean showingSetUpFrame) {
        MovableComponentFrame frame = frames.get(frameClass);
        enableMovement(frame,showingSetUpFrame);
        activeFrames.add(frame);
    }

    @Override
    public void setOrEndUp(Class frameClass, boolean showingSetUpFrame) {
        MovableComponentFrame frame = frames.get(frameClass);
        if(frame.getMoveState().equals(LocationState.DEFAULT)){
            enableMovement(frame,showingSetUpFrame);
            activeFrames.add(frame);
        }else {
            disableMovement(frame);
            activeFrames.remove(frame);
        }
    }

    @Override
    public void endUpAll() {
        activeFrames.forEach(this::disableMovement);
        activeFrames.clear();
    }

    @Override
    public void endUp(Class frameClass) {
        MovableComponentFrame frame = frames.get(frameClass);
        disableMovement(frame);
        activeFrames.remove(frame);
    }
    private void enableMovement(MovableComponentFrame frame, boolean showSetUpFrame){
        frame.setState(LocationState.MOVING);
        if(showSetUpFrame){
            FramesManager.INSTANCE.showFrame(SetUpLocationFrame.class);
        }
    }
    private void disableMovement(MovableComponentFrame frame){
        frame.setState(LocationState.DEFAULT);
        FramesManager.INSTANCE.hideFrame(SetUpLocationFrame.class);
    }
}
