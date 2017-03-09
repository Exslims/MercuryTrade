package com.mercury.platform.ui.frame.location;

import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;

import java.util.*;

public class SetUpLocationCommander {
    private Map<String,MovableComponentFrame> movableFrames;
    private List<MovableComponentFrame> activeFrames;
    public SetUpLocationCommander() {
        movableFrames = new HashMap<>();
        activeFrames = new ArrayList<>();
    }
    public void addFrame(MovableComponentFrame movableFrame){
        movableFrames.put(movableFrame.getClass().getSimpleName(),movableFrame);
    }
    public void setUpAllExclude(String... frames){
        List<String> framesList = Arrays.asList(frames);
        movableFrames.forEach((title,frame)->{
            if(!framesList.contains(title)) {
                enableMovement(frame,true);
                activeFrames.add(frame);
            }
        });
    }
    public void setUpAll(){
        movableFrames.forEach((title,frame)->{
            enableMovement(frame,true);
            activeFrames.add(frame);
        });
    }
    public void setUp(String frameClass, boolean showingSetUpFrame){
        MovableComponentFrame frame = movableFrames.get(frameClass);
        enableMovement(frame,showingSetUpFrame);
        activeFrames.add(frame);
    }

    public void disableAll() {
        activeFrames.forEach(this::disableMovement);
        activeFrames.clear();
    }

    public void disable(String frameClass) {
        MovableComponentFrame frame = movableFrames.get(frameClass);
        disableMovement(frame);
        activeFrames.remove(frame);
    }
    private void enableMovement(MovableComponentFrame frame, boolean showSetUpFrame){
        frame.setState(UndecoratedFrameState.MOVING);
        if(showSetUpFrame){
            FramesManager.INSTANCE.showFrame(SetUpLocationFrame.class);
        }
    }
    private void disableMovement(MovableComponentFrame frame){
        frame.setState(UndecoratedFrameState.DEFAULT);
        FramesManager.INSTANCE.hideFrame(SetUpLocationFrame.class);
    }
}
