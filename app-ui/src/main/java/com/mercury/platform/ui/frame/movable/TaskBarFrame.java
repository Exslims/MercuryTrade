package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.taskbar.MercuryTaskBarController;
import com.mercury.platform.ui.components.panel.taskbar.TaskBarController;
import com.mercury.platform.ui.components.panel.taskbar.TaskBarPanel;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class TaskBarFrame extends AbstractMovableComponentFrame {
    private Timeline collapseAnimation;
    private int MIN_WIDTH;
    private int MAX_WIDTH;
    private MouseListener collapseListener;
    private TaskBarPanel taskBarPanel;

    public TaskBarFrame() {
        super("MercuryTrade");
        componentsFactory.setScale(ConfigManager.INSTANCE.getScaleData().get("taskbar"));
        stubComponentsFactory.setScale(ConfigManager.INSTANCE.getScaleData().get("taskbar"));
        processEResize = false;
        processSEResize = false;
        prevState = FrameVisibleState.SHOW;
    }

    @Override
    protected void initialize() {
        super.initialize();
        createUI();
        this.setMaximumSize(taskBarPanel.getPreferredSize());
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
        this.setWidth(MIN_WIDTH);
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
    public void subscribe() {
    }
    private void initCollapseAnimations(String state){
        collapseAnimation = new Timeline(this);
        switch (state){
            case "expand":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),MAX_WIDTH);
                break;
            }
            case "collapse":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),MIN_WIDTH);
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
//        panel.setPreferredSize(new Dimension(taskBarPanel.getWidthOf(4),taskBarPanel.getHeight()));
//        System.out.println(panel.getPreferredSize());
        panel.setBackground(AppThemeColor.FRAME);
        return panel;
    }

    @Override
    protected void registerDirectScaleHandler() {
        MercuryStoreUI.INSTANCE.taskBarScaleSubject.subscribe(this::changeScale);
    }

    @Override
    protected void performScaling(Map<String,Float> scaleData) {
        this.componentsFactory.setScale(scaleData.get("taskbar"));
        createUI();
    }

    @Override
    public void createUI() {
        JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());
        taskBarPanel = new TaskBarPanel(new MercuryTaskBarController(),componentsFactory);
        panel.add(taskBarPanel, BorderLayout.CENTER);
        panel.setBackground(AppThemeColor.FRAME);
        mainContainer = panel;
        this.setContentPane(panel);
        this.pack();
        this.repaint();
        this.MIN_WIDTH = taskBarPanel.getWidthOf(4);
        this.MAX_WIDTH = taskBarPanel.getPreferredSize().width;
        this.setWidth(MIN_WIDTH);
    }

    @Override
    protected JPanel defaultView(ComponentsFactory factory) {
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
        JPanel panel = factory.getTransparentPanel(new BorderLayout());
        TaskBarPanel taskBarPanel = new TaskBarPanel(controller, factory);
        panel.add(new TaskBarPanel(controller,factory), BorderLayout.CENTER);
        panel.setBackground(AppThemeColor.FRAME);
        this.MIN_WIDTH = taskBarPanel.getWidthOf(4);
        this.MAX_WIDTH = taskBarPanel.getPreferredSize().width;
        this.setSize(new Dimension(MIN_WIDTH,this.getHeight()));
        this.setMinimumSize(new Dimension(MIN_WIDTH,taskBarPanel.getHeight()));
        this.setMaximumSize(new Dimension(MAX_WIDTH,taskBarPanel.getHeight()));
        return panel;
    }
}
