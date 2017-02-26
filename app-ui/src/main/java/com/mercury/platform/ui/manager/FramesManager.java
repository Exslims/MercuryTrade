package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChangedTradeModeEvent;
import com.mercury.platform.shared.events.custom.ShutdownApplication;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.impl.test.TestCasesFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.frame.impl.*;
import com.mercury.platform.ui.frame.impl.util.TradeMode;
import com.mercury.platform.ui.frame.location.SetUpLocationCommander;
import com.mercury.platform.ui.frame.location.SetUpLocationFrame;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 18.01.2017.
 */
public class FramesManager {
    private static class FramesManagerHolder {
        static final FramesManager HOLDER_INSTANCE = new FramesManager();
    }
    public static FramesManager INSTANCE = FramesManagerHolder.HOLDER_INSTANCE;

    private Map<Class,OverlaidFrame> framesMap;
    private SetUpLocationCommander locationCommander;

    private FramesManager() {
        framesMap = new HashMap<>();
        locationCommander = new SetUpLocationCommander();
    }
    public void start(){
        createTrayIcon();

        OverlaidFrame incMessageFrame = new IncMessageFrame();
        framesMap.put(IncMessageFrame.class,incMessageFrame);
        OverlaidFrame taskBarFrame = new TaskBarFrame();
        OverlaidFrame itemsMeshFrame = new ItemsGridFrame();
        framesMap.put(ItemsGridFrame.class,itemsMeshFrame);

        locationCommander.addFrame((MovableComponentFrame) incMessageFrame);
        locationCommander.addFrame((MovableComponentFrame) taskBarFrame);
        locationCommander.addFrame((MovableComponentFrame) itemsMeshFrame);
        NotesLoader notesLoader = new NotesLoader();

        List<Note> notesOnFirstStart = notesLoader.getNotesOnFirstStart();
        framesMap.put(NotesFrame.class, new NotesFrame(notesOnFirstStart, NotesFrame.NotesType.INFO));
        List<Note> patchNotes = notesLoader.getPatchNotes();
        if(ConfigManager.INSTANCE.isShowPatchNotes() && patchNotes.size() != 0){
            NotesFrame patchNotesFrame = new NotesFrame(patchNotes,NotesFrame.NotesType.PATCH);
            patchNotesFrame.init();
        }
        framesMap.put(HistoryFrame.class,new HistoryFrame());
        framesMap.put(SettingsFrame.class,new SettingsFrame());
        framesMap.put(TestCasesFrame.class,new TestCasesFrame());
        framesMap.put(TooltipFrame.class,new TooltipFrame());
        framesMap.put(NotificationFrame.class,new NotificationFrame());
        framesMap.put(DonationAlertFrame.class,new DonationAlertFrame());
        framesMap.put(ChatScannerFrame.class,new ChatScannerFrame());
        framesMap.put(UpdateReadyFrame.class,new UpdateReadyFrame());
        framesMap.put(TaskBarFrame.class,taskBarFrame);
        framesMap.put(SetUpLocationFrame.class,new SetUpLocationFrame());

        framesMap.forEach((k,v)->{
            v.init();
        });

        int decayTime = ConfigManager.INSTANCE.getDecayTime();
        int maxOpacity = ConfigManager.INSTANCE.getMaxOpacity();
        int minOpacity = ConfigManager.INSTANCE.getMinOpacity();
        framesMap.forEach((k,frame) -> {
            if(frame instanceof ComponentFrame) {
                if (decayTime > 0) {
                    ((ComponentFrame)frame).enableHideEffect(decayTime, minOpacity, maxOpacity);
                } else {
                    ((ComponentFrame)frame).disableHideEffect();
                    frame.setOpacity(maxOpacity / 100f);
                }
            }
        });
        TradeMode tradeMode = TradeMode.valueOf(ConfigManager.INSTANCE.getTradeMode());
        switch (tradeMode){
            case DEFAULT:{
                EventRouter.INSTANCE.fireEvent(new ChangedTradeModeEvent.ToDefaultTradeModeEvent());
                break;
            }
            case SUPER:{
                EventRouter.INSTANCE.fireEvent(new ChangedTradeModeEvent.ToSuperTradeModeEvent());
                break;
            }
        }
        EventRouter.INSTANCE.fireEvent(new UILoadedEvent());
    }
    public void showFrame(Class frameClass){
        framesMap.get(frameClass).showComponent();
    }
    public void preShowFrame(Class frameClass){
        framesMap.get(frameClass).setPrevState(FrameStates.SHOW);
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
    public void enableMovement(){
        locationCommander.setUpAll();
    }
    public void enableMovementExclude(String... frames){
        locationCommander.setUpAllExclude(frames);
    }
    public void enableMovement(String frameClass){
        locationCommander.setUp(frameClass);
    }
    public void disableMovement(){
        locationCommander.disableAll();
    }
    public void disableMovement(String frameClass){
        locationCommander.disable(frameClass);
    }
    public void restoreDefaultLocation(){
        framesMap.forEach((k,v) -> {
            FrameSettings settings = ConfigManager.INSTANCE.getDefaultFramesSettings().get(k.getSimpleName());
            if(v instanceof MovableComponentFrame && !v.getClass().getSimpleName().equals("ItemsGridFrame")){
                v.setLocation(settings.getFrameLocation());
                ((MovableComponentFrame) v).onLocationChange(settings.getFrameLocation());
            }
        });
    }
    private void createTrayIcon(){
        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(e -> {
            EventRouter.INSTANCE.fireEvent(new ShutdownApplication());
        });
        trayMenu.add(item);

        BufferedImage icon = null;
        try {
            icon = ImageIO.read(getClass().getClassLoader().getResource("app/app-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TrayIcon trayIcon = new TrayIcon(icon,"MercuryTrade",trayMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
