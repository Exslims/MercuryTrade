package com.mercury.platform.ui.frame.other;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CheckOutPatchNotes;
import com.mercury.platform.shared.events.custom.UpdateInfoEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UpdateReadyFrame extends OverlaidFrame {
    public UpdateReadyFrame() {
        super("MercuryTrade");
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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,16f,"New version available:");
        label.setBorder(null);
        Dimension dimension = new Dimension(80, 26);
        JButton showInfo = componentsFactory.getBorderedButton("Show info");
        showInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                EventRouter.CORE.fireEvent(new CheckOutPatchNotes());
            }
        });
        JButton dismiss = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Dismiss",
                14f);
        dismiss.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
            }
        });
        showInfo.setPreferredSize(dimension);
        dismiss.setPreferredSize(dimension);
        panel.add(label);
        panel.add(showInfo);
        panel.add(dismiss);
        return panel;
    }

    @Override
    public void initHandlers() {
        EventRouter.CORE.registerHandler(UpdateInfoEvent.class, event -> {
            FrameSettings tbSettings = ConfigManager.INSTANCE.getFrameSettings("TaskBarFrame");
            Point tbLocation = tbSettings.getFrameLocation();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            if(tbLocation.y + 30 + this.getHeight() > dim.height){
                this.setLocation(tbLocation.x,tbLocation.y - 44);
            }else {
                this.setLocation(tbLocation.x, tbLocation.y + 44);
            }
            int deltaWidth = dim.width - (this.getLocation().x + this.getWidth());
            if(deltaWidth < 0){
                this.setLocation(tbLocation.x - Math.abs(deltaWidth), this.getLocation().y);
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
