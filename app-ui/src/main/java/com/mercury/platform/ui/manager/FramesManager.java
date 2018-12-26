package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.AdrManager;
import com.mercury.platform.ui.adr.AdrState;
import com.mercury.platform.ui.frame.AbstractComponentFrame;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.frame.AbstractScalableComponentFrame;
import com.mercury.platform.ui.frame.movable.AbstractMovableComponentFrame;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.frame.movable.NotificationFrame;
import com.mercury.platform.ui.frame.movable.TaskBarFrame;
import com.mercury.platform.ui.frame.other.*;
import com.mercury.platform.ui.frame.setup.location.SetUpLocationCommander;
import com.mercury.platform.ui.frame.setup.scale.SetUpScaleCommander;
import com.mercury.platform.ui.frame.titled.*;
import com.mercury.platform.ui.manager.routing.SettingsRoutManager;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FramesManager implements AsSubscriber {
    public static FramesManager INSTANCE = FramesManagerHolder.HOLDER_INSTANCE;
    private Map<Class, AbstractOverlaidFrame> framesMap;
    private SetUpLocationCommander locationCommander;
    private SetUpScaleCommander scaleCommander;
    private AdrManager adrManager;
    private TrayIcon trayIcon;
    private FramesManager() {
        this.framesMap = new HashMap<>();
        this.locationCommander = new SetUpLocationCommander();
        this.scaleCommander = new SetUpScaleCommander();
        this.adrManager = new AdrManager();
    }

    public void start() {
        this.createTrayIcon();

        AbstractOverlaidFrame incMessageFrame = new NotificationFrame();
        this.framesMap.put(NotificationFrame.class, incMessageFrame);
        AbstractOverlaidFrame taskBarFrame = new TaskBarFrame();
        AbstractOverlaidFrame itemsMeshFrame = new ItemsGridFrame();
        this.framesMap.put(ItemsGridFrame.class, itemsMeshFrame);
        this.locationCommander.addFrame((AbstractMovableComponentFrame) incMessageFrame);
        this.locationCommander.addFrame((AbstractMovableComponentFrame) taskBarFrame);
        this.locationCommander.addFrame((AbstractMovableComponentFrame) itemsMeshFrame);

        this.scaleCommander.addFrame((AbstractScalableComponentFrame) incMessageFrame);
        this.scaleCommander.addFrame((AbstractScalableComponentFrame) taskBarFrame);
        this.scaleCommander.addFrame((AbstractScalableComponentFrame) itemsMeshFrame);

        NotesLoader notesLoader = new NotesLoader();

        List<Note> notesOnFirstStart = notesLoader.getNotesOnFirstStart();
        this.framesMap.put(NotesFrame.class, new NotesFrame(notesOnFirstStart, NotesFrame.NotesType.INFO));

        this.framesMap.put(HistoryFrame.class, new HistoryFrame());
        SettingsFrame settingsFrame = new SettingsFrame();
        this.framesMap.put(SettingsFrame.class, settingsFrame);
        this.framesMap.put(TestCasesFrame.class, new TestCasesFrame());
        this.framesMap.put(TooltipFrame.class, new TooltipFrame());
        this.framesMap.put(ChatHistoryFrame.class, new ChatHistoryFrame());
        this.framesMap.put(NotificationAlertFrame.class, new NotificationAlertFrame());
        this.framesMap.put(MercuryLoadingFrame.class, new MercuryLoadingFrame());
        this.framesMap.put(ChatScannerFrame.class, new ChatScannerFrame());
        this.framesMap.put(UpdateReadyFrame.class, new UpdateReadyFrame());
        this.framesMap.put(TaskBarFrame.class, taskBarFrame);
        this.framesMap.put(SetUpLocationFrame.class, new SetUpLocationFrame());
        this.framesMap.put(SetUpScaleFrame.class, new SetUpScaleFrame());
        this.framesMap.put(AlertFrame.class, new AlertFrame());
        this.framesMap.put(HelpIGFrame.class, new HelpIGFrame());

        this.framesMap.forEach((k, v) -> v.init());

        ApplicationDescriptor config = Configuration.get().applicationConfiguration().get();
        this.framesMap.forEach((k, frame) -> {
            if (frame instanceof AbstractComponentFrame) {
                if (config.getFadeTime() > 0) {
                    ((AbstractComponentFrame) frame).enableHideEffect(config.getFadeTime(), config.getMinOpacity(), config.getMaxOpacity());
                } else {
                    ((AbstractComponentFrame) frame).disableHideEffect();
                    frame.setOpacity(config.getMaxOpacity() / 100f);
                }
            }
        });
        new SettingsRoutManager(settingsFrame);
        this.subscribe();
        this.adrManager.load();
        MercuryStoreCore.uiLoadedSubject.onNext(true);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.showPatchNotesSubject.subscribe(json -> {
            NotesLoader notesLoader = new NotesLoader();
            List<Note> notes = notesLoader.getPatchNotesFromString(json);
            NotesFrame patchNotesFrame = new NotesFrame(notes, NotesFrame.NotesType.PATCH);
            patchNotesFrame.init();
            patchNotesFrame.setFrameTitle("MercuryTrade v" + notesLoader.getVersionFrom(json));
            patchNotesFrame.showComponent();
        });
        MercuryStoreUI.packSubject.subscribe(className -> this.framesMap.get(className).pack());
        MercuryStoreUI.repaintSubject.subscribe(className -> this.framesMap.get(className).repaint());
    }

    public void exit() {
        this.framesMap.forEach((k, v) -> v.setVisible(false));
        MercuryStoreCore.shutdownAppSubject.onNext(true);
    }

    public void exitForUpdate() {
        this.framesMap.forEach((k, v) -> v.setVisible(false));
        MercuryStoreCore.shutdownForUpdateSubject.onNext(true);
    }

    public void showFrame(Class frameClass) {
        this.framesMap.get(frameClass).showComponent();
    }

    public void preShowFrame(Class frameClass) {
        this.framesMap.get(frameClass).setPrevState(FrameVisibleState.SHOW);
    }

    public void hideFrame(Class frameClass) {
        this.framesMap.get(frameClass).hideComponent();
    }

    public void hideOrShowFrame(Class frameClass) {
        AbstractOverlaidFrame frame = this.framesMap.get(frameClass);
        if (frame != null && frame.isVisible()) {
            hideFrame(frameClass);
        } else {
            showFrame(frameClass);
        }
    }

    public void enableMovementExclude(Class... frames) {
        this.locationCommander.setUpAllExclude(frames);
    }

    public void enableOrDisableMovementDirect(Class frameClass) {
        this.locationCommander.setOrEndUp(frameClass, false);
    }

    public void disableMovement() {
        this.locationCommander.endUpAll();
    }

    public void disableMovement(Class frameClass) {
        this.locationCommander.endUp(frameClass);
    }

    public void enableScale() {
        this.showFrame(SetUpScaleFrame.class);
        this.scaleCommander.setUpAll();
    }

    public void disableScale() {
        this.hideFrame(SetUpScaleFrame.class);
        this.scaleCommander.endUpAll();
    }

    public void performAdr() {
        if (this.adrManager.getState().equals(AdrState.DEFAULT)) {
            this.adrManager.enableSettings();
        } else {
            this.adrManager.disableSettings();
        }
    }

    public void restoreDefaultLocation() {
        this.framesMap.forEach((k, v) -> {
            FrameDescriptor settings = Configuration.get().framesConfiguration().get(k.getSimpleName());
            if (!v.getClass().equals(ItemsGridFrame.class) && settings != null) {
                v.setLocation(settings.getFrameLocation());
                if (v instanceof AbstractMovableComponentFrame) {
                    ((AbstractMovableComponentFrame) v).onLocationChange(settings.getFrameLocation());
                }
            }
        });
    }

    private void createTrayIcon() {
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
        this.trayIcon = new TrayIcon(icon, "MercuryTrade", trayMenu);
        this.trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(this.trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static class FramesManagerHolder {
        static final FramesManager HOLDER_INSTANCE = new FramesManager();
    }
}
