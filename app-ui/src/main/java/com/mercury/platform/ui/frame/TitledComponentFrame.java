package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 18.01.2017.
 */
public abstract class TitledComponentFrame extends ComponentFrame {
    private JPanel miscPanel;
    private JButton hideButton;
    private JLabel frameTitleLabel;
    protected TitledComponentFrame(String title) {
        super(title);
        miscPanel = new JPanel();
    }

    @Override
    protected void initialize() {
        super.initialize();
        initHeaderPanel();
    }

    private void initHeaderPanel(){
        if(layout instanceof BorderLayout) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(AppThemeColor.HEADER);
            headerPanel.setPreferredSize(new Dimension(100,26));

            JLabel appIcon = componentsFactory.getIconLabel("app/app-icon.png", 16);
            frameTitleLabel = componentsFactory.getTextLabel(getFrameTitle());
            frameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            frameTitleLabel.setVerticalAlignment(SwingConstants.CENTER);
            frameTitleLabel.addMouseListener(new DraggedFrameMouseListener());
            frameTitleLabel.addMouseMotionListener(new DraggedFrameMotionListener());

            appIcon.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            headerPanel.add(appIcon,BorderLayout.LINE_START);
            headerPanel.add(frameTitleLabel, BorderLayout.CENTER);

            miscPanel.setBackground(AppThemeColor.TRANSPARENT);
            hideButton = componentsFactory.getIconButton("app/close.png", 12, AppThemeColor.FRAME_1, "");
            hideButton.setBorder(BorderFactory.createEmptyBorder(2,0,0,2));
            hideButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    TitledComponentFrame.this.setVisible(false);
                }
            });
            miscPanel.add(hideButton);
            headerPanel.add(miscPanel, BorderLayout.LINE_END);
            this.add(headerPanel, BorderLayout.PAGE_START);
        }
    }
    protected abstract String getFrameTitle();

    protected void setFrameTitle(String title) {
        frameTitleLabel.setText(title);
    }
    protected void removeHideButton() {
        this.miscPanel.remove(hideButton);
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
