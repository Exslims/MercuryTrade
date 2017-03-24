package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.taskbar.MercuryTaskBarController;
import com.mercury.platform.ui.components.panel.taskbar.TaskBarController;
import com.mercury.platform.ui.components.panel.taskbar.TaskBarPanel;
import com.mercury.platform.ui.frame.titled.ChatScannerFrame;
import com.mercury.platform.ui.frame.titled.HistoryFrame;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaskBarFrame extends MovableComponentFrame{
    private final Logger logger = LogManager.getLogger(TaskBarFrame.class.getSimpleName());
    private Timeline collapseAnimation;
    private static final int MAX_WIDTH = 250;
    private MouseListener collapseListener;

    public TaskBarFrame() {
        super("MercuryTrade");
        processEResize = false;
        processSEResize = false;
        prevState = FrameStates.SHOW;
    }

    @Override
    protected void initialize() {
        super.initialize();
        add(new TaskBarPanel(new MercuryTaskBarController()), BorderLayout.CENTER);
        this.pack();
        collapseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                TaskBarFrame.this.repaint();
                if (collapseAnimation != null) {
                    collapseAnimation.abort();
                }
                initCollapseAnimations("expand");
                collapseAnimation.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                TaskBarFrame.this.repaint();
                if(isVisible() && !withInPanel((JPanel)TaskBarFrame.this.getContentPane()) && !EResizeSpace) {
                    if (collapseAnimation != null) {
                        collapseAnimation.abort();
                    }
                    initCollapseAnimations("collapse");
                    collapseAnimation.play();
                }
            }
        };

        enableCollapseAnimation();
    }
    private void enableCollapseAnimation(){
        this.setWidth(configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameSize().width);
        this.addMouseListener(collapseListener);
    }
    private void disableCollapseAnimation(){
        this.setWidth(MAX_WIDTH);
        this.removeMouseListener(collapseListener);
    }
    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(RepaintEvent.RepaintTaskBar.class, event -> {
            TaskBarFrame.this.revalidate();
            TaskBarFrame.this.repaint();
        });
    }
    private void initCollapseAnimations(String state){
        collapseAnimation = new Timeline(this);
        switch (state){
            case "expand":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),MAX_WIDTH);
                break;
            }
            case "collapse":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameSize().width);
            }
        }
        collapseAnimation.setEase(new Spline(1f));
        collapseAnimation.setDuration(150);
    }
    private boolean withInPanel(JPanel panel){
        return new Rectangle(panel.getLocationOnScreen(),panel.getSize()).contains(MouseInfo.getPointerInfo().getLocation());
    }

    /**
     * For 'trident' property animations
     * @param width next width
     */
    public void setWidth(int width){
        this.setSize(new Dimension(width,this.getHeight()));
    }

    @Override
    protected void onLock() {
        super.onLock();
        enableCollapseAnimation();
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        disableCollapseAnimation();
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Task Bar"));
        panel.add(labelPanel);
        panel.setPreferredSize(this.getSize());
        panel.setBackground(AppThemeColor.FRAME);
        return panel;
    }

    @Override
    protected JPanel defaultView(ComponentsFactory factory) {
        JPanel panel = factory.getTransparentPanel(new BorderLayout());
        TaskBarController controller = new TaskBarController() {
            @Override
            public void enableDND() {}
            @Override
            public void disableDND() {}
            @Override
            public void showITH() {}
            @Override
            public void performHideout() {}
            @Override
            public void showChatFiler() {}
            @Override
            public void showHistory() {}
            @Override
            public void openPINSettings() {}
            @Override
            public void openScaleSettings() {}
            @Override
            public void showSettings() {}
            @Override
            public void exit() {}
        };
        panel.add(new TaskBarPanel(controller,factory), BorderLayout.CENTER);
        return panel;
    }
}
