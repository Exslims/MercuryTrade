package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.frame.AbstractComponentFrame;
import com.mercury.platform.ui.frame.AbstractScalableComponentFrame;
import com.mercury.platform.ui.frame.adr.AdrManagerFrame;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.frame.movable.AbstractMovableComponentFrame;
import com.mercury.platform.ui.frame.movable.container.IncMessageFrame;
import com.mercury.platform.ui.frame.other.*;
import com.mercury.platform.ui.frame.movable.TaskBarFrame;
import com.mercury.platform.ui.frame.setup.adr.AdrState;
import com.mercury.platform.ui.frame.setup.scale.SetUpScaleCommander;
import com.mercury.platform.ui.frame.titled.*;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.frame.setup.location.SetUpLocationCommander;
import com.mercury.platform.ui.frame.other.SetUpLocationFrame;
import com.mercury.platform.ui.frame.titled.chat.ChatFilterFrame;
import com.mercury.platform.ui.frame.titled.container.HistoryFrame;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FramesManager implements HasEventHandlers{
    private static class FramesManagerHolder {
        static final FramesManager HOLDER_INSTANCE = new FramesManager();
    }
    public static FramesManager INSTANCE = FramesManagerHolder.HOLDER_INSTANCE;

    private Map<Class,AbstractOverlaidFrame> framesMap;
    private SetUpLocationCommander locationCommander;
    private SetUpScaleCommander scaleCommander;
    private AdrManager adrManager;

    private FramesManager() {
        framesMap = new HashMap<>();
        locationCommander = new SetUpLocationCommander();
        scaleCommander = new SetUpScaleCommander();
        adrManager = new AdrManager();
    }
    public void start(){
        createTrayIcon();

        AbstractOverlaidFrame incMessageFrame = new IncMessageFrame();
        framesMap.put(IncMessageFrame.class,incMessageFrame);
        AbstractOverlaidFrame taskBarFrame = new TaskBarFrame();
        AbstractOverlaidFrame itemsMeshFrame = new ItemsGridFrame();
        framesMap.put(ItemsGridFrame.class,itemsMeshFrame);
        locationCommander.addFrame((AbstractMovableComponentFrame) incMessageFrame);
        locationCommander.addFrame((AbstractMovableComponentFrame) taskBarFrame);
        locationCommander.addFrame((AbstractMovableComponentFrame) itemsMeshFrame);

        scaleCommander.addFrame((AbstractScalableComponentFrame) incMessageFrame);
        scaleCommander.addFrame((AbstractScalableComponentFrame) taskBarFrame);
        scaleCommander.addFrame((AbstractScalableComponentFrame) itemsMeshFrame);

        NotesLoader notesLoader = new NotesLoader();

        List<Note> notesOnFirstStart = notesLoader.getNotesOnFirstStart();
        framesMap.put(NotesFrame.class, new NotesFrame(notesOnFirstStart, NotesFrame.NotesType.INFO));

        framesMap.put(HistoryFrame.class,new HistoryFrame());
        framesMap.put(SettingsFrame.class,new SettingsFrame());
        framesMap.put(TestCasesFrame.class,new TestCasesFrame());
        framesMap.put(TooltipFrame.class,new TooltipFrame());
        framesMap.put(NotificationFrame.class,new NotificationFrame());
        framesMap.put(MercuryLoadingFrame.class,new MercuryLoadingFrame());
        List<Note> patchNotes = notesLoader.getPatchNotes();
        if(ConfigManager.INSTANCE.isShowPatchNotes() && patchNotes.size() != 0){
            NotesFrame patchNotesFrame = new NotesFrame(patchNotes, NotesFrame.NotesType.PATCH);
            patchNotesFrame.init();
        }
        framesMap.put(ChatFilterFrame.class,new ChatFilterFrame());
        framesMap.put(UpdateReadyFrame.class,new UpdateReadyFrame());
        framesMap.put(TaskBarFrame.class,taskBarFrame);
        framesMap.put(SetUpLocationFrame.class,new SetUpLocationFrame());
        framesMap.put(SetUpScaleFrame.class,new SetUpScaleFrame());
        framesMap.put(AlertFrame.class,new AlertFrame());

        framesMap.forEach((k,v)-> v.init());

        int decayTime = ConfigManager.INSTANCE.getFadeTime();
        int maxOpacity = ConfigManager.INSTANCE.getMaxOpacity();
        int minOpacity = ConfigManager.INSTANCE.getMinOpacity();
        framesMap.forEach((k,frame) -> {
            if(frame instanceof AbstractComponentFrame) {
                if (decayTime > 0) {
                    ((AbstractComponentFrame)frame).enableHideEffect(decayTime, minOpacity, maxOpacity);
                } else {
                    ((AbstractComponentFrame)frame).disableHideEffect();
                    frame.setOpacity(maxOpacity / 100f);
                }
            }
        });
        initHandlers();
        adrManager.load();
        MercuryStore.INSTANCE.uiLoadedSubject.onNext(true);
    }
    @Override
    public void initHandlers() {
        MercuryStore.INSTANCE.showPatchNotesSubject.subscribe(json -> {
            NotesLoader notesLoader = new NotesLoader();
            List<Note> notes = notesLoader.getPatchNotesFromString(json);
            NotesFrame patchNotesFrame = new NotesFrame(notes, NotesFrame.NotesType.PATCH);
            patchNotesFrame.init();
            patchNotesFrame.setFrameTitle("MercuryTrade v" + notesLoader.getVersionFrom(json));
            patchNotesFrame.showComponent();
        });
    }
    public void exit() {
        framesMap.forEach((k,v) -> v.setVisible(false));
        MercuryStore.INSTANCE.shutdownAppSubject.onNext(true);
    }
    public void exitForUpdate() {
        framesMap.forEach((k,v) -> v.setVisible(false));
        MercuryStore.INSTANCE.shutdownForUpdateSubject.onNext(true);
    }
    public void showFrame(Class frameClass){
        framesMap.get(frameClass).showComponent();
    }
    public void preShowFrame(Class frameClass){
        framesMap.get(frameClass).setPrevState(FrameVisibleState.SHOW);
    }
    public void hideFrame(Class frameClass){
        framesMap.get(frameClass).hideComponent();
    }
    public void hideOrShowFrame(Class frameClass){
        AbstractOverlaidFrame frame = framesMap.get(frameClass);
        if(frame != null && frame.isVisible()){
            hideFrame(frameClass);
        }else {
            showFrame(frameClass);
        }
    }
    public void enableMovementExclude(Class... frames){
        locationCommander.setUpAllExclude(frames);
    }
    public void enableOrDisableMovementDirect(Class frameClass){
        locationCommander.setOrEndUp(frameClass,false);
    }
    public void disableMovement(){
        locationCommander.endUpAll();
    }
    public void disableMovement(Class frameClass){
        locationCommander.endUp(frameClass);
    }

    public void enableScale(){
        showFrame(SetUpScaleFrame.class);
        scaleCommander.setUpAll();
    }

    public void disableScale(){
        hideFrame(SetUpScaleFrame.class);
        scaleCommander.endUpAll();
    }
    public void performAdr() {
        if(this.adrManager.getState().equals(AdrState.DEFAULT)) {
            this.adrManager.enableSettings();
        }else {
            this.adrManager.disableSettings();
        }
    }
    public void restoreDefaultLocation(){
        framesMap.forEach((k,v) -> {
            FrameSettings settings = ConfigManager.INSTANCE.getDefaultFramesSettings().get(k.getSimpleName());
            if(!v.getClass().equals(ItemsGridFrame.class) && settings != null){
                v.setLocation(settings.getFrameLocation());
                if(v instanceof AbstractMovableComponentFrame){
                    ((AbstractMovableComponentFrame) v).onLocationChange(settings.getFrameLocation());
                }
            }
        });
    }
    private void createTrayIcon(){
        PopupMenu trayMenu = new PopupMenu();
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> {
            exit();
        });
        MenuItem restore = new MenuItem("Restore default location");
        restore.addActionListener(e -> {
            FramesManager.INSTANCE.restoreDefaultLocation();
        });
        trayMenu.add(restore);
        trayMenu.add(exit);

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
