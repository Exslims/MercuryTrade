package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.UpdateReadyEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.manager.UpdateManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UpdateReadyFrame extends OverlaidFrame {
    public UpdateReadyFrame() {
        super("MT-UpdateNotification");
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
        this.add(getUpdatePanel());
        this.setOpacity(ConfigManager.INSTANCE.getMaxOpacity()/100f);
        this.pack();
    }
    private JPanel getUpdatePanel(){
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));

        JLabel label = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,16f,"Ready for update: ");
        JLabel restartLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT,TextAlignment.LEFTOP,16f,"Restart");
        restartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                UpdateManager.INSTANCE.doUpdate();
            }
        });
        restartLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT),
                BorderFactory.createEmptyBorder(2,2,2,2)
        ));
        label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        panel.add(label);
        panel.add(restartLabel);
        return panel;
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(UpdateReadyEvent.class, event -> {
            FrameSettings tbSettings = ConfigManager.INSTANCE.getFrameSettings("TaskBarFrame");
            Point tbLocation = tbSettings.getFrameLocation();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            if(tbLocation.y + 30 + this.getHeight() > dim.height){
                this.setLocation(tbLocation.x,tbLocation.y - 44);
            }else {
                this.setLocation(tbLocation.x, tbLocation.y + 40);
            }
            if (!this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
                this.setVisible(true);
            } else {
                prevState = FrameStates.SHOW;
            }
            pack();
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }

}
