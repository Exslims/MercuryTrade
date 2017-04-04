package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.SoundNotificationEvent;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SoundSettingsPanel extends ConfigurationPanel {

    public SoundSettingsPanel() {
        super();
        this.createUI();
    }

    @Override
    public void createUI() {
        verticalScrollContainer.add(getVolumePanel(),BorderLayout.CENTER);
    }

    private JPanel getVolumePanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());

        JLabel volumeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "Volume");
        volumeLabel.setBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                        BorderFactory.createEmptyBorder(3,5,3,5)));


        JPanel container = componentsFactory.getTransparentPanel(new GridLayout(3, 2));
        container.setBackground(AppThemeColor.SETTINGS_BG);
        container.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3,0,3,0)));

        JSlider notificationSlider = componentsFactory.getSlider(-40, 6, 0);
        notificationSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                EventRouter.CORE.fireEvent(
                        new SoundNotificationEvent.WhisperSoundNotificationEvent(
                                notificationSlider.getValue() == -40 ? -80f : (float)notificationSlider.getValue())
                );
            }
        });
        JSlider chatScannerSlider = componentsFactory.getSlider(-40, 6, 0);
        chatScannerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                EventRouter.CORE.fireEvent(
                        new SoundNotificationEvent.ChatScannerSoundNotificationEvent(
                                chatScannerSlider.getValue() == -40 ? -80f : (float)chatScannerSlider.getValue())
                );
            }
        });
        JSlider clicksSlider = componentsFactory.getSlider(-40, 6, 0);
        clicksSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                EventRouter.CORE.fireEvent(
                        new SoundNotificationEvent.ClicksSoundNotificationEvent(
                                clicksSlider.getValue() == -40 ? -80f : (float)clicksSlider.getValue())
                );
            }
        });
        container.add(componentsFactory.getTextLabel("Notification:"));
        container.add(notificationSlider);
        container.add(componentsFactory.getTextLabel("Chat Scanner"));
        container.add(chatScannerSlider);
        container.add(componentsFactory.getTextLabel("Clicks"));
        container.add(clicksSlider);

        root.add(volumeLabel,BorderLayout.PAGE_START);
        root.add(container,BorderLayout.CENTER);
        return root;
    }

    @Override
    public boolean processAndSave() {
        return true;
    }

    @Override
    public void restore() {

    }
}
