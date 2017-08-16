package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
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
    @Setter
    private float paintAlphaValue = 0f;
    @Setter
    private float paintBorderValue = 0f;
    private boolean blurEffect;
    private boolean blurReverse;

    @Override
    public void onViewInit() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1)));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,this.paintAlphaValue));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paint(g2);
        if (this.paintAlphaValue < 1.0f) {
            this.paintAlphaValue += 0.004;
            if(this.paintAlphaValue > 1.0f){
                this.paintAlphaValue = 1.0f;
            }
            this.repaint();
        }
    }
    protected void onBlur(){
        this.blurEffect = true;
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1)));
        this.repaint();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(this.blurEffect) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.paintBorderValue));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            super.paintBorder(g2);
            if(this.blurReverse){
                if (this.paintBorderValue <= 1.0f) {
                    this.paintBorderValue += 0.002;
                    if (this.paintBorderValue >= 1.0f) {
                        this.blurReverse = false;
                        this.paintBorderValue = 1.0f;
                    }
                }
            }else {
                if (this.paintBorderValue >= 0f) {
                    this.paintBorderValue -= 0.002;
                    if (this.paintBorderValue <= 0f) {
                        this.blurReverse = true;
                        this.paintBorderValue = 0f;
                    }
                }
            }
            this.repaint();
        }else {
            super.paintBorder(g);
        }
    }
}
