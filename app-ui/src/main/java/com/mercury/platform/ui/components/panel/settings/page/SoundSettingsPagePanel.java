package com.mercury.platform.ui.components.panel.settings.page;


import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.SoundDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SoundSettingsPagePanel extends SettingsPagePanel {
    private Map<String, String> wavPaths;

    private JSlider notificationSlider;
    private JSlider chatScannerSlider;
    private JSlider clicksSlider;
    private JSlider updateSlider;

    private Map<String, SoundDescriptor> soundSnapshot;
    private KeyValueConfigurationService<String, SoundDescriptor> soundConfiguration;

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.wavPaths = new HashMap<>();
        this.wavPaths.put("notification", "app/notification.wav");
        this.wavPaths.put("chat_scanner", "app/chat-filter.wav");
        this.wavPaths.put("clicks", "app/sounds/click1/button-pressed-10.wav");
        this.wavPaths.put("update", "app/patch_tone.wav");
        this.soundConfiguration = Configuration.get().soundConfiguration();
        this.soundSnapshot = CloneHelper.cloneObject(this.soundConfiguration.getMap());

        JPanel vPanel = this.getVolumePanel();
        JPanel volumePanel = this.adrComponentsFactory.getCounterPanel(vPanel, "Volume:", AppThemeColor.ADR_BG, true);
        volumePanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        this.container.add(this.componentsFactory.wrapToSlide(volumePanel));
    }

    private JPanel getVolumePanel() {
        JPanel container = componentsFactory.getTransparentPanel(new GridLayout(0, 2, 0, 4));
        container.setBackground(AppThemeColor.ADR_BG);
        container.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3, 0, 3, 0)));

        notificationSlider = componentsFactory.getSlider(-40, 6, soundSnapshot.get("notification").getDb().intValue(), AppThemeColor.ADR_BG);
        notificationSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStoreCore.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("notification"),
                        notificationSlider.getValue() == -40 ? -80f : (float) notificationSlider.getValue()
                ));
            }
        });
        chatScannerSlider = componentsFactory.getSlider(-40, 6, soundSnapshot.get("chat_scanner").getDb().intValue(), AppThemeColor.ADR_BG);
        chatScannerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStoreCore.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("chat_scanner"),
                        chatScannerSlider.getValue() == -40 ? -80f : (float) chatScannerSlider.getValue()
                ));
            }
        });
        clicksSlider = componentsFactory.getSlider(-40, 6, soundSnapshot.get("clicks").getDb().intValue(), AppThemeColor.ADR_BG);
        clicksSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStoreCore.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("clicks"),
                        clicksSlider.getValue() == -40 ? -80f : (float) clicksSlider.getValue()
                ));
            }
        });
        updateSlider = componentsFactory.getSlider(-40, 6, soundSnapshot.get("update").getDb().intValue(), AppThemeColor.ADR_BG);
        updateSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStoreCore.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("update"),
                        updateSlider.getValue() == -40 ? -80f : (float) updateSlider.getValue()
                ));
            }
        });
        container.add(componentsFactory.getTextLabel("Notification:", FontStyle.REGULAR, 16));
        container.add(notificationSlider);
        container.add(componentsFactory.getTextLabel("Chat Scanner", FontStyle.REGULAR, 16));
        container.add(chatScannerSlider);
        container.add(componentsFactory.getTextLabel("Clicks", FontStyle.REGULAR, 16));
        container.add(clicksSlider);
        container.add(componentsFactory.getTextLabel("Update notification", FontStyle.REGULAR, 16));
        container.add(updateSlider);
        return container;
    }

    @Override
    public void onSave() {
        soundSnapshot.get("notification")
                .setDb((float) notificationSlider.getValue());
        soundSnapshot.get("chat_scanner")
                .setDb((float) chatScannerSlider.getValue());
        soundSnapshot.get("clicks")
                .setDb((float) clicksSlider.getValue());
        soundSnapshot.get("update")
                .setDb((float) updateSlider.getValue());
        this.soundConfiguration.set(CloneHelper.cloneObject(this.soundSnapshot));
    }

    @Override
    public void restore() {
        this.soundSnapshot = CloneHelper.cloneObject(this.soundConfiguration.getMap());
        this.removeAll();
        this.onViewInit();
    }
}
