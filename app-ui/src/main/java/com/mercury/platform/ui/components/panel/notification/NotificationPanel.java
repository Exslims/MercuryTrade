package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;
import lombok.Setter;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public abstract class NotificationPanel<T,C> extends JPanel implements AsSubscriber, ViewInit,ViewDestroy {
    @Setter
    @Getter
    protected T data;
    @Setter
    protected C controller;
    protected ComponentsFactory componentsFactory;
    protected Map<HotKeyDescriptor,JButton> hotKeysPool = new HashMap<>();
    protected Map<HotKeyType,JButton> interactButtonMap = new HashMap<>();
    @Setter
    private float paintAlphaValue = 0f;
    @Setter
    protected float paintBorderValue = 0f;
    protected boolean blurEffect;
    protected boolean blurReverse;

    private Subscription settingsPostSubscription;

    @Override
    public void onViewInit() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1,1,1,1),
                BorderFactory.createLineBorder(AppThemeColor.RESPONSE_BUTTON_BORDER, 1)));
    }
    public void setComponentsFactory(ComponentsFactory factory){
        this.componentsFactory = factory;
        this.removeAll();
        this.onViewInit();
    }

    public void onHotKeyPressed(HotKeyDescriptor descriptor){
        JButton button = this.hotKeysPool.get(descriptor);
        if(button != null){
            button.doClick();
        }
    }

    @Override
    public void subscribe() {
        this.settingsPostSubscription = MercuryStoreUI.settingsPostSubject.subscribe(state -> {
            this.updateHotKeyPool();
        });
    }
    protected abstract void updateHotKeyPool();

    @Override
    public void onViewDestroy() {
        this.settingsPostSubscription.unsubscribe();
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
    protected JPanel getTimePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppThemeColor.MSG_HEADER);
        JLabel timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 14, "0m");
        Timer timeAgo = new Timer(60000, new ActionListener() {
            private int minute = 0;
            private int hours = 0;
            private int day = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String labelText = "";
                minute++;
                if (minute > 60) {
                    hours++;
                    minute = 0;
                    if (hours > 24) {
                        day++;
                        hours = 0;
                    }
                }
                if (hours == 0 && day == 0) {
                    labelText = minute + "m";
                } else if (hours > 0) {
                    labelText = hours + "h " + minute + "m";
                } else if (day > 0) {
                    labelText = day + "d " + hours + "h " + minute + "m";
                }
                timeLabel.setText(labelText);
            }
        });
        timeAgo.start();
        root.add(timeLabel,BorderLayout.CENTER);
        return root;
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
