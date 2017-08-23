package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.frame.setup.scale.ScaleState;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

//todo need generalization
public abstract class AbstractScalableComponentFrame extends AbstractComponentFrame {
    protected Container mainContainer;
    protected ComponentsFactory stubComponentsFactory;
    protected boolean inScaleSettings = false;
    private ScaleState scaleState = ScaleState.DEFAULT;
    private ScalableFrameConstraints prevConstraints;

    protected AbstractScalableComponentFrame() {
        super();
        this.mainContainer = this.getContentPane();
        this.stubComponentsFactory = new ComponentsFactory();
        MercuryStoreUI.saveScaleSubject.subscribe(this::performScaling);
        this.registerDirectScaleHandler();
    }

    protected void onScaleLock() {
        this.pack();
        this.repaint();
    }

    protected void onScaleUnlock() {
        this.repaint();
        this.pack();
    }

    protected abstract void registerDirectScaleHandler();

    protected abstract void performScaling(Map<String, Float> scaleData);

    protected void changeScale(float scale) {
        this.stubComponentsFactory.setScale(scale);
        this.initDefaultView();
    }

    public void setState(ScaleState state) {
        switch (state) {
            case DEFAULT: {
                this.scaleState = ScaleState.DEFAULT;
                this.setContentPane(mainContainer);
                this.setVisible(prevConstraints.visible);
                this.setBackground(prevConstraints.bgColor);
                this.inScaleSettings = false;
                this.onScaleLock();
                break;
            }
            case ENABLE: {
                this.inScaleSettings = true;
                this.scaleState = ScaleState.ENABLE;
                this.prevConstraints = new ScalableFrameConstraints(
                        this.isVisible(),
                        this.processEResize, this.getBackground()
                );
                initDefaultView();
                this.setVisible(true);
                this.onScaleUnlock();
                break;
            }
        }
    }

    private void initDefaultView() {
        if (scaleState.equals(ScaleState.ENABLE)) {
            JPanel panel = defaultView(stubComponentsFactory);
            this.setContentPane(panel);
            this.repaint();
            this.pack();
        }
    }

    protected abstract JPanel defaultView(ComponentsFactory factory);

    protected class ScalableFrameConstraints {
        private boolean visible;
        private boolean processEResize;
        private Color bgColor;

        ScalableFrameConstraints(
                boolean visible,
                boolean processEResize, Color bgColor) {
            this.visible = visible;
            this.processEResize = processEResize;
            this.bgColor = bgColor;
        }
    }
}
