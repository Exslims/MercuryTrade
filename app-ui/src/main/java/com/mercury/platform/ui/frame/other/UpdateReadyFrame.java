package com.mercury.platform.ui.frame.other;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UpdateReadyFrame extends AbstractOverlaidFrame {
    public UpdateReadyFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
    }

    @Override
    public void onViewInit() {
        this.add(getUpdatePanel());
        this.setOpacity(this.applicationConfig.get().getMaxOpacity() / 100f);
        this.pack();
    }

    private JPanel getUpdatePanel() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                16f,
                "New version available:");
        label.setBorder(null);
        Dimension dimension = new Dimension(80, 26);
        JButton showInfo = componentsFactory.getBorderedButton("Show info");
        showInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                MercuryStoreCore.checkOutPatchSubject.onNext(true);
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
    public void subscribe() {
        MercuryStoreCore.updateInfoSubject.subscribe(version -> {
            FrameDescriptor tbSettings = this.framesConfig.get("TaskBarFrame");
            Point tbLocation = tbSettings.getFrameLocation();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            if (tbLocation.y + 30 + this.getHeight() > dim.height) {
                this.setLocation(tbLocation.x, tbLocation.y - 44);
            } else {
                this.setLocation(tbLocation.x, tbLocation.y + 44);
            }
            int deltaWidth = dim.width - (this.getLocation().x + this.getWidth());
            if (deltaWidth < 0) {
                this.setLocation(tbLocation.x - Math.abs(deltaWidth), this.getLocation().y);
            }
            if (!this.isVisible() && ProdStarter.APP_STATUS == FrameVisibleState.SHOW) {
                this.setVisible(true);
            } else {
                this.prevState = FrameVisibleState.SHOW;
            }
            this.pack();
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }
}
