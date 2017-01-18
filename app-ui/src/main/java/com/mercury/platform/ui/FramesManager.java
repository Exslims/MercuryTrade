package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.mercury.platform.ui.frame.impl.test.TestCasesFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.frame.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Константин on 18.01.2017.
 */
public class FramesManager {
    private static class FramesManagerHolder {
        static final FramesManager HOLDER_INSTANCE = new FramesManager();
    }
    public static FramesManager INSTANCE = FramesManagerHolder.HOLDER_INSTANCE;

    private Map<Class,OverlaidFrame> framesMap;

    public FramesManager() {
        framesMap = new HashMap<>();
    }
    public void start(){
//        framesMap.put(ChatFilterFrame.class,new ChatFilterFrame());
        framesMap.put(GamePathChooser.class,new GamePathChooser());
        framesMap.put(HistoryFrame.class,new HistoryFrame());
        framesMap.put(IncMessageFrame.class,new IncMessageFrame());
        framesMap.put(NotificationFrame.class,new NotificationFrame());
        framesMap.put(OutMessageFrame.class,new OutMessageFrame());
        framesMap.put(SettingsFrame.class,new SettingsFrame());
        framesMap.put(TaskBarFrame.class,new TaskBarFrame());
        framesMap.put(TimerFrame.class,new TimerFrame());
        framesMap.put(TestCasesFrame.class,new TestCasesFrame());
        framesMap.put(TooltipFrame.class,new TooltipFrame());

        framesMap.forEach((k,v)->{
            v.init();
        });
        EventRouter.INSTANCE.fireEvent(new UILoadedEvent());
    }
    public void showFrame(Class frameClass){
        framesMap.get(frameClass).showComponent();
    }
    public void hideFrame(Class frameClass){
        framesMap.get(frameClass).hideComponent();
    }
    public void hideOrShowFrame(Class frameClass){
        OverlaidFrame frame = framesMap.get(frameClass);
        if(frame != null && frame.isVisible()){
            hideFrame(frameClass);
        }else {
            showFrame(frameClass);
        }
    }

}
