package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.HotKeysSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.NotificationFrame;
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

public abstract class NotificationPanel<T, C> extends JPanel implements AsSubscriber, ViewInit, ViewDestroy {
    protected PlainConfigurationService<NotificationSettingsDescriptor> notificationConfig;
    protected PlainConfigurationService<HotKeysSettingsDescriptor> hotKeysConfig;
    @Setter
    @Getter
    protected T data;
    @Setter
    protected C controller;
    protected ComponentsFactory componentsFactory;
    protected Map<HotKeyDescriptor, JButton> hotKeysPool = new HashMap<>();
    protected Map<HotKeyType, JButton> interactButtonMap = new HashMap<>();
    @Setter
    protected float paintBorderValue = 0f;
    protected boolean blurEffect;
    protected boolean blurReverse;
    @Setter
    protected boolean duplicate;
    protected JPanel contentPanel;
    @Setter
    private float paintAlphaValue = 1f;
    private Subscription settingsPostSubscription;

    @Setter
    @Getter
    private int additionalHeightDelta = 0;

    @Override
    public void onViewInit() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1),
                BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER, 1)));
        this.notificationConfig = Configuration.get().notificationConfiguration();
        this.hotKeysConfig = Configuration.get().hotKeysConfiguration();
    }

    public void setComponentsFactory(ComponentsFactory factory) {
        this.componentsFactory = factory;
    }

    public void onHotKeyPressed(HotKeyDescriptor descriptor) {
        JButton button = this.hotKeysPool.get(descriptor);
        if (button != null) {
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
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.paintAlphaValue));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paint(g2);
        if (this.paintAlphaValue < 1.0f) {
            this.paintAlphaValue += 0.004;
            if (this.paintAlphaValue > 1.0f) {
                this.paintAlphaValue = 1.0f;
            }
            this.repaint();
        }
    }

    protected JPanel getTimePanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setPreferredSize(new Dimension((int) (38 * this.componentsFactory.getScale()), (int) (26 * this.componentsFactory.getScale())));
        root.setBackground(AppThemeColor.MSG_HEADER);
        JLabel timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 14, "0s");
        Timer timeAgo = new Timer(1000, new ActionListener() {
            private int seconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                String labelText = seconds + "s";
                if (seconds > 59) {
                    int minutes = seconds / 60;
                    if (minutes > 59) {
                        labelText = minutes / 60 + "h";
                    } else {
                        labelText = seconds / 60 + "m";
                    }
                }
                timeLabel.setText(labelText);
            }
        });
        timeAgo.start();
        root.add(timeLabel, BorderLayout.CENTER);
        return root;
    }

    protected JButton getExpandButton() {
        String iconPath = this.notificationConfig.get().getFlowDirections().equals(FlowDirections.UPWARDS) ? "app/collapse-mp.png" : "app/expand-mp.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 18f, AppThemeColor.MSG_HEADER, "");
        expandButton.addActionListener(action -> {
            NotificationFrame frame = (NotificationFrame) SwingUtilities.getWindowAncestor(NotificationPanel.this);
            if (this.contentPanel.isVisible()) {
                this.contentPanel.setVisible(false);
                expandButton.setIcon(this.componentsFactory.getIcon("app/default-mp.png", 18f));
                frame.changeBufferSize(this.contentPanel.getPreferredSize().height);
            } else {
                this.contentPanel.setVisible(true);
                expandButton.setIcon(this.componentsFactory.getIcon(iconPath, 18f));
                frame.changeBufferSize(-this.contentPanel.getPreferredSize().height);
            }
            frame.pack();
        });
        return expandButton;
    }

    protected void onBlur() {
        this.blurEffect = true;
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1),
                BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER, 1)));
        this.repaint();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (this.blurEffect) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.paintBorderValue));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            super.paintBorder(g2);
            if (this.blurReverse) {
                if (this.paintBorderValue <= 1.0f) {
                    this.paintBorderValue += 0.002;
                    if (this.paintBorderValue >= 1.0f) {
                        this.blurReverse = false;
                        this.paintBorderValue = 1.0f;
                    }
                }
            } else {
                if (this.paintBorderValue >= 0f) {
                    this.paintBorderValue -= 0.002;
                    if (this.paintBorderValue <= 0f) {
                        this.blurReverse = true;
                        this.paintBorderValue = 0f;
                    }
                }
            }
            this.repaint();
        } else {
            super.paintBorder(g);
        }
    }
}
