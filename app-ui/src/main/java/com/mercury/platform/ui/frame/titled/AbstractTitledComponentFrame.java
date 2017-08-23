package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.ui.frame.AbstractComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractTitledComponentFrame extends AbstractComponentFrame {
    protected JPanel miscPanel;
    protected JButton hideButton;
    protected JPanel headerPanel;
    private JLabel frameTitleLabel;

    protected AbstractTitledComponentFrame() {
        super();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.initHeaderPanel();
    }

    private void initHeaderPanel() {
        if (layout instanceof BorderLayout) {
            this.headerPanel = new JPanel(new BorderLayout());
            this.headerPanel.setBackground(AppThemeColor.HEADER);
            this.headerPanel.setPreferredSize(new Dimension(100, 26));
            this.headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppThemeColor.MSG_HEADER_BORDER));

            JLabel appIcon = componentsFactory.getIconLabel("app/app-icon.png", 16);
            this.frameTitleLabel = componentsFactory.getTextLabel(getFrameTitle());
            this.frameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            this.frameTitleLabel.setVerticalAlignment(SwingConstants.CENTER);
            this.frameTitleLabel.addMouseListener(new DraggedFrameMouseListener());
            this.frameTitleLabel.addMouseMotionListener(new DraggedFrameMotionListener());

            appIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            this.headerPanel.add(appIcon, BorderLayout.LINE_START);
            this.headerPanel.add(this.frameTitleLabel, BorderLayout.CENTER);

            this.miscPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.HEADER);
            this.hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.HEADER, "");
            this.hideButton.addActionListener(action -> {
                this.hideComponent();
            });
            this.miscPanel.add(hideButton, BorderLayout.LINE_END);
            this.headerPanel.add(miscPanel, BorderLayout.LINE_END);
            this.add(headerPanel, BorderLayout.PAGE_START);
        }
    }

    protected abstract String getFrameTitle();

    public void setFrameTitle(String title) {
        this.frameTitleLabel.setText(title);
    }

    protected void removeHideButton() {
        this.miscPanel.remove(hideButton);
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
