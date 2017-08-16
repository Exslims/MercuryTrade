package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public abstract class NotificationPanel<T,C> extends JPanel implements AsSubscriber, ViewInit,ViewDestroy {
    @Setter
    @Getter
    protected T data;
    @Setter
    protected C controller;
    @Setter
    protected ComponentsFactory componentsFactory;

    private float alphaValue = 0f;
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,this.alphaValue));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paint(g2);
        if (this.alphaValue < 1.0f) {
            this.alphaValue += 0.004;
            if(this.alphaValue > 1.0f){
                this.alphaValue = 1.0f;
            }
            this.repaint();
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        this.alphaValue = 0f;
        super.setVisible(aFlag);
    }
}
