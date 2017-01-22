package com.mercury.platform.ui.frame.location;

import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Константин on 21.01.2017.
 */
public class SetUpLocationCommander {
    private UndecoratedFrameState currentState;
    private List<MovableComponentFrame> movableFrames;
    public SetUpLocationCommander() {
        movableFrames = new ArrayList<>();
        currentState = UndecoratedFrameState.DEFAULT;
    }
    public void addFrame(MovableComponentFrame movableFrame){
        movableFrames.add(movableFrame);
    }
    public void callSetupLocation(){
        movableFrames.forEach(frame->{
            switch (currentState){
                case DEFAULT:{
                    frame.setState(UndecoratedFrameState.MOVING);
                    FramesManager.INSTANCE.showFrame(SetUpLocationFrame.class);
                    break;
                }
                case MOVING:{
                    frame.setState(UndecoratedFrameState.DEFAULT);
                    FramesManager.INSTANCE.hideFrame(SetUpLocationFrame.class);
                    break;
                }
            }
        });
        if(currentState.equals(UndecoratedFrameState.DEFAULT)){
            currentState = UndecoratedFrameState.MOVING;
        }else {
            currentState = UndecoratedFrameState.DEFAULT;
        }
    }
}
