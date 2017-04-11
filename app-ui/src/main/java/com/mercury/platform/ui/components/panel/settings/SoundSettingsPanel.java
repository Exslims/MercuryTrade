package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.SoundDescriptor;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SoundSettingsPanel extends ConfigurationPanel {
    private Map<String,String> wavPaths;

    private JSlider notificationSlider;
    private JSlider chatScannerSlider;
    private JSlider clicksSlider;
    private JSlider updateSlider;

    public SoundSettingsPanel() {
        super();
        this.wavPaths = new HashMap<>();
        this.wavPaths.put("notification","app/notification.wav");
        this.wavPaths.put("chat_scanner","app/chat-filter.wav");
        this.wavPaths.put("clicks","app/sounds/click1/button-pressed-10.wav");
        this.wavPaths.put("update","app/patch_tone.wav");
        this.createUI();
    }

    @Override
    public void createUI() {
        verticalScrollContainer.add(getVolumePanel());
        verticalScrollContainer.add(getSoundPickerPanel());
    }

    private JPanel getVolumePanel(){
        Map<String, SoundDescriptor> map = Configuration.get().soundConfiguration().getMap();

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());

        JLabel volumeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "Volume");
        volumeLabel.setBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                        BorderFactory.createEmptyBorder(3,5,3,5)));


        JPanel container = componentsFactory.getTransparentPanel(new GridLayout(4, 2));
        container.setBackground(AppThemeColor.SETTINGS_BG);
        container.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3,0,3,0)));

        notificationSlider = componentsFactory.getSlider(-40, 6,
                map.get("notification").getDb().intValue() == -80? -40: map.get("notification").getDb().intValue());
        notificationSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStore.INSTANCE.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("notification"),
                        notificationSlider.getValue() == -40 ? -80f : (float)notificationSlider.getValue()
                ));
            }
        });
        chatScannerSlider = componentsFactory.getSlider(-40, 6,
                map.get("chat_scanner").getDb().intValue() == -80? -40: map.get("chat_scanner").getDb().intValue());
        chatScannerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStore.INSTANCE.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("chat_scanner"),
                        chatScannerSlider.getValue() == -40 ? -80f : (float)chatScannerSlider.getValue()
                ));
            }
        });
        clicksSlider = componentsFactory.getSlider(-40, 6,
                map.get("clicks").getDb().intValue() == -80? -40: map.get("clicks").getDb().intValue());
        clicksSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStore.INSTANCE.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("clicks"),
                        clicksSlider.getValue() == -40 ? -80f : (float)clicksSlider.getValue()
                ));
            }
        });
        updateSlider = componentsFactory.getSlider(-40, 6,
                map.get("update").getDb().intValue() == -80? -40: map.get("update").getDb().intValue());
        updateSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                MercuryStore.INSTANCE.soundSettingsSubject.onNext(new SoundDescriptor(
                        wavPaths.get("update"),
                        updateSlider.getValue() == -40 ? -80f : (float)updateSlider.getValue()
                ));
            }
        });
        container.add(componentsFactory.getTextLabel("Notification:",FontStyle.REGULAR));
        container.add(notificationSlider);
        container.add(componentsFactory.getTextLabel("Chat Scanner",FontStyle.REGULAR));
        container.add(chatScannerSlider);
        container.add(componentsFactory.getTextLabel("Clicks",FontStyle.REGULAR));
        container.add(clicksSlider);
        container.add(componentsFactory.getTextLabel("Update notification",FontStyle.REGULAR));
        container.add(updateSlider);

        root.add(volumeLabel,BorderLayout.PAGE_START);
        root.add(container,BorderLayout.CENTER);
        return root;
    }

    private JPanel getSoundPickerPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());

        JLabel soundLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 17f, "Sound");
        soundLabel.setBorder(
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                        BorderFactory.createEmptyBorder(3,5,3,5)));

        JPanel container = componentsFactory.getTransparentPanel(new GridLayout(2, 2,0,1));
        container.setBackground(AppThemeColor.SETTINGS_BG);
        container.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.MSG_HEADER_BORDER),
                BorderFactory.createEmptyBorder(3,0,3,0)));

        JComboBox notificationComboBox = componentsFactory.getComboBox(new String[]{"Mercury Notification", "Mercury Chat Scanner", "Browse"});
        notificationComboBox.addItemListener(e -> {
            switch (e.getStateChange()){
                case ItemEvent.SELECTED: {
                    if(notificationComboBox.getSelectedItem().equals("Browse")){
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("*.wav","wav"));
                        int returnVal = fileChooser.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            System.out.println(fileChooser.getSelectedFile().getPath());
                        }
                    }
                    break;
                }
            }
        });


        JComboBox chatScannerComboBox = componentsFactory.getComboBox(new String[]{"Mercury Notification", "Mercury Chat Scanner", "Browse"});


        container.add(componentsFactory.getTextLabel("Notification:",FontStyle.REGULAR));
        container.add(notificationComboBox);
        container.add(componentsFactory.getTextLabel("Chat Scanner",FontStyle.REGULAR));
        container.add(chatScannerComboBox);
        root.add(soundLabel,BorderLayout.PAGE_START);
        root.add(container,BorderLayout.CENTER);

        return root;
    }


    @Override
    public boolean processAndSave() {
        Map<String, SoundDescriptor> map = Configuration.get().soundConfiguration().getMap();
        map.get("notification")
                .setDb(notificationSlider.getValue() == -40 ? -80f : (float)notificationSlider.getValue());
        map.get("chat_scanner")
                .setDb(chatScannerSlider.getValue() == -40 ? -80f : (float)chatScannerSlider.getValue());
        map.get("clicks")
                .setDb(clicksSlider.getValue() == -40 ? -80f : (float)clicksSlider.getValue());
        map.get("update")
                .setDb(updateSlider.getValue() == -40 ? -80f : (float)updateSlider.getValue());
        Configuration.get().soundConfiguration().save();
        return true;
    }

    @Override
    public void restore() {
        Map<String, SoundDescriptor> map = Configuration.get().soundConfiguration().getMap();
        notificationSlider.setValue(
                map.get("notification").getDb().intValue() == -80? -40: map.get("notification").getDb().intValue());
        chatScannerSlider.setValue(
                map.get("chat_scanner").getDb().intValue() == -80? -40: map.get("chat_scanner").getDb().intValue());
        clicksSlider.setValue(
                map.get("clicks").getDb().intValue() == -80? -40: map.get("clicks").getDb().intValue());
        updateSlider.setValue(
                map.get("update").getDb().intValue() == -80? -40: map.get("update").getDb().intValue());
    }
}
