package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.ui.frame.AbstractComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractTitledComponentFrame extends AbstractComponentFrame {
    protected JPanel miscPanel;
    protected JButton hideButton;
    protected JPanel headerPanel;
    private JLabel frameTitleLabel;
    protected AbstractTitledComponentFrame() {
        super();
        this.miscPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.initHeaderPanel();
    }

    private void initHeaderPanel(){
        if(layout instanceof BorderLayout) {
            this.headerPanel = new JPanel(new BorderLayout());
            this.headerPanel.setBackground(AppThemeColor.HEADER);
            this.headerPanel.setPreferredSize(new Dimension(100,26));
            this.headerPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER));

            JLabel appIcon = componentsFactory.getIconLabel("app/app-icon.png", 16);
            this.frameTitleLabel = componentsFactory.getTextLabel(getFrameTitle());
            this.frameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            this.frameTitleLabel.setVerticalAlignment(SwingConstants.CENTER);
            this.frameTitleLabel.addMouseListener(new DraggedFrameMouseListener());
            this.frameTitleLabel.addMouseMotionListener(new DraggedFrameMotionListener());

            appIcon.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            this.headerPanel.add(appIcon,BorderLayout.LINE_START);
            this.headerPanel.add(this.frameTitleLabel, BorderLayout.CENTER);

            this.miscPanel.setBackground(AppThemeColor.TRANSPARENT);
            this.hideButton = componentsFactory.getIconButton("app/close.png", 14, AppThemeColor.FRAME_ALPHA, "");
            this.hideButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
            this.hideButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        hideComponent();
                    }
                }
            });
            this.miscPanel.add(hideButton);
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
