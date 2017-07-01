package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.setup.scale.ScaleState;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
//todo need generalization
public abstract class AbstractScalableComponentFrame extends AbstractComponentFrame implements HasUI{
    protected Container mainContainer;
    private ScaleState scaleState = ScaleState.DEFAULT;
    protected ComponentsFactory stubComponentsFactory;
    private ScalableFrameConstraints prevConstraints;
    protected boolean sizeWasChanged = false;
    protected boolean inScaleSettings = false;

    protected AbstractScalableComponentFrame() {
        super();
        this.mainContainer = this.getContentPane();
        this.stubComponentsFactory = new ComponentsFactory();
        MercuryStoreUI.INSTANCE.saveScaleSubject.subscribe(this::performScaling);
        this.registerDirectScaleHandler();
    }


    protected void onScaleLock(){
        this.pack();
        this.repaint();
    }
    protected void onScaleUnlock(){
        this.repaint();
        this.pack();
    }

    protected abstract void registerDirectScaleHandler();
    protected abstract void performScaling(Map<String,Float> scaleData);

    protected void changeScale(float scale){
        this.stubComponentsFactory.setScale(scale);
        this.initDefaultView();
    }
    public void setState(ScaleState state){
        switch (state){
            case DEFAULT:{
                this.scaleState = ScaleState.DEFAULT;
                this.setContentPane(mainContainer);
                this.setVisible(prevConstraints.visible);
                this.setBackground(prevConstraints.bgColor);
                this.setLocation(prevConstraints.location);
                if(sizeWasChanged){
                    this.setPreferredSize(this.getSize());
                    this.setMinimumSize(this.getSize());
                    this.setMaximumSize(this.getSize());
                    sizeWasChanged = false;
                }else {
                    this.setMinimumSize(prevConstraints.minSize);
                    this.setMaximumSize(prevConstraints.maxSize);
                }
                this.inScaleSettings = false;
                this.onScaleLock();
                break;
            }
            case ENABLE: {
                this.inScaleSettings = true;
                this.scaleState = ScaleState.ENABLE;
                this.prevConstraints = new ScalableFrameConstraints(
                        this.isVisible(),
                        this.getMinimumSize(),
                        this.getMaximumSize(),
                        this.getLocation(),
                        this.getBackground()
                );
                initDefaultView();
                this.setLocation(this.framesConfig.get(this.getClass().getSimpleName()).getFrameLocation());
                this.setMinimumSize(null);
                this.setVisible(true);
                this.onScaleUnlock();
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
        private Color bgColor;

        ScalableFrameConstraints(
                        boolean visible,
                        Dimension minSize,
                        Dimension maxSize,
                        Point location,
                        Color bgColor) {
            this.visible = visible;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.location = location;
            this.bgColor = bgColor;
        }
    }
}
