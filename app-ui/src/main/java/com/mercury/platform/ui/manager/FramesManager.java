package com.mercury.platform.ui.manager;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.UILoadedEvent;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.impl.test.TestCasesFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.frame.impl.*;
import com.mercury.platform.ui.frame.location.SetUpLocationCommander;
import com.mercury.platform.ui.frame.location.SetUpLocationFrame;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    private SetUpLocationCommander locationCommander;

    private FramesManager() {
        framesMap = new HashMap<>();
        locationCommander = new SetUpLocationCommander();
    }
    public void start(){
        createTrayIcon();
        ConfigManager.INSTANCE.load();

        OverlaidFrame chatFilter = new ChatFilterFrame();
        framesMap.put(ChatFilterFrame.class,chatFilter);
        OverlaidFrame incMessageFrame = new IncMessageFrame();
        framesMap.put(IncMessageFrame.class,incMessageFrame);
        OverlaidFrame taskBarFrame = new TaskBarFrame();
        framesMap.put(TaskBarFrame.class,taskBarFrame);

        locationCommander.addFrame((MovableComponentFrame) incMessageFrame);
        locationCommander.addFrame((MovableComponentFrame) taskBarFrame);
        locationCommander.addFrame((MovableComponentFrame) chatFilter);

        framesMap.put(HistoryFrame.class,new HistoryFrame());
//        framesMap.put(OutMessageFrame.class,new OutMessageFrame());
        framesMap.put(SettingsFrame.class,new SettingsFrame());
//        framesMap.put(TimerFrame.class,new TimerFrame());
        framesMap.put(TestCasesFrame.class,new TestCasesFrame());
        framesMap.put(NotesFrame.class,new NotesFrame());
        framesMap.put(TooltipFrame.class,new TooltipFrame());
        framesMap.put(NotificationFrame.class,new NotificationFrame());
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
                    ConfigManager.INSTANCE.saveProperty("decayTime", decayTime);
                    ((ComponentFrame)frame).enableHideEffect(decayTime, minOpacity, maxOpacity);
                } else {
                    ((ComponentFrame)frame).disableHideEffect();
                    frame.setOpacity(maxOpacity / 100f);
                }
            }
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
    public void enableMovement(){
        locationCommander.callSetupLocation();
    }

    private void createTrayIcon(){
        PopupMenu trayMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(e -> {
            System.exit(0);
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
