package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.frame.setup.scale.ScaleState;
import com.mercury.platform.ui.misc.event.ScaleChangeEvent;

import javax.swing.*;
import java.awt.*;

public abstract class ScalableComponentFrame extends ComponentFrame{
    protected Container mainContainer;
    private ScaleState scaleState = ScaleState.DEFAULT;
    private ComponentsFactory stubComponentsFactory;
    private ScalableFrameConstraints prevConstraints;

    protected ScalableComponentFrame(String title) {
        super(title);
        mainContainer = this.getContentPane();
        stubComponentsFactory = new ComponentsFactory();

        EventRouter.UI.registerHandler(ScaleChangeEvent.class, event -> {
            float scale = ((ScaleChangeEvent) event).getScale();
            changeScale(scale);
        });
    }
    private void changeScale(float scale){
        stubComponentsFactory.setScale(scale);
        initDefaultView();
    }
    public void setState(ScaleState state){
        switch (state){
            case DEFAULT:{
                this.scaleState = ScaleState.DEFAULT;
                this.setContentPane(mainContainer);
                this.setVisible(prevConstraints.visible);
                this.setLocation(prevConstraints.location);
                this.setMinimumSize(prevConstraints.minSize);
                this.setMaximumSize(prevConstraints.maxSize);
                this.repaint();
                this.pack();
                break;
            }
            case ENABLE: {
                this.scaleState = ScaleState.ENABLE;
                this.prevConstraints = new ScalableFrameConstraints(
                        this.isVisible(),
                        this.getMinimumSize(),
                        this.getMaximumSize(),
                        this.getLocation()
                );
                initDefaultView();
                this.setLocation(configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameLocation());
                this.setMinimumSize(null);
                this.setVisible(true);
                this.repaint();
                this.pack();
                break;
            }
        }
    }
    private void initDefaultView(){
        if(scaleState.equals(ScaleState.ENABLE)){
            JPanel panel = defaultView(stubComponentsFactory);
            this.setContentPane(panel);
            this.repaint();
            this.pack();
        }
    }
    protected abstract JPanel defaultView(ComponentsFactory factory);

    protected class ScalableFrameConstraints {
        private boolean visible;
        private Dimension minSize;
        private Dimension maxSize;
        private Point location;

        ScalableFrameConstraints(
                        boolean visible,
                        Dimension minSize,
                        Dimension maxSize,
                        Point location) {
            this.visible = visible;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.location = location;
        }
    }
}
