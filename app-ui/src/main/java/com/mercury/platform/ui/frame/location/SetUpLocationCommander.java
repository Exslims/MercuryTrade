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
                process(frame,UndecoratedFrameState.MOVING);
                activeFrames.add(frame);
            }
        });
    }
    public void setUpAll(){
        movableFrames.forEach((title,frame)->{
            process(frame,UndecoratedFrameState.MOVING);
            activeFrames.add(frame);
        });
    }
    public void setUp(String frameClass){
        MovableComponentFrame frame = movableFrames.get(frameClass);
        process(frame,UndecoratedFrameState.MOVING);
        activeFrames.add(frame);
    }

    public void disableAll() {
        activeFrames.forEach(frame -> {
            process(frame,UndecoratedFrameState.DEFAULT);
        });
        activeFrames.clear();
    }

    public void disable(String frameClass) {
        MovableComponentFrame frame = movableFrames.get(frameClass);
        process(frame,UndecoratedFrameState.DEFAULT);
        activeFrames.remove(frame);
    }

    private void process(MovableComponentFrame frame, UndecoratedFrameState state){
        switch (state){
            case MOVING:{
                frame.setState(UndecoratedFrameState.MOVING);
                FramesManager.INSTANCE.showFrame(SetUpLocationFrame.class);
                break;
            }
            case DEFAULT:{
                frame.setState(UndecoratedFrameState.DEFAULT);
                FramesManager.INSTANCE.hideFrame(SetUpLocationFrame.class);
                break;
            }
        }
    }
}
