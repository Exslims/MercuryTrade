package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.titled.NotesFrame;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.frame.titled.TestCasesFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.manager.HideSettingsManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

public class GeneralSettings extends ConfigurationPanel {
    private PlainConfigurationService<ApplicationDescriptor> applicationConfig;
    private JSlider minSlider;
    private JSlider maxSlider;
    private JSlider fadeTimeSlider;
    private JComboBox notifierStatusPicker;
    private JCheckBox checkEnable;
    private JTextField gamePathField;
    private WrongGamePathListener poeFolderTooltipListener;
    public GeneralSettings() {
        super();
        this.applicationConfig = Configuration.get().applicationConfiguration();
        this.createUI();
    }

    @Override
    public void createUI() {
        verticalScrollContainer.add(getSettingsSlidePanel());
        verticalScrollContainer.add(getSettingsPanel());
    }
    private JPanel getSettingsPanel() {
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(7,2,4,4));
        root.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        root.setBackground(AppThemeColor.SLIDE_BG);

        this.checkEnable = new JCheckBox();
        this.checkEnable.setBackground(AppThemeColor.TRANSPARENT);
        this.checkEnable.setSelected(this.applicationConfig.get().isCheckOutUpdate());

        this.fadeTimeSlider = this.componentsFactory.getSlider(0,10, this.applicationConfig.get().getFadeTime());
        this.fadeTimeSlider.addChangeListener(e -> {
            MercuryStoreUI.repaintSubject.onNext(SettingsFrame.class);
        });

        this.minSlider = this.componentsFactory.getSlider(40,100,this.applicationConfig.get().getMinOpacity());
        this.minSlider.addChangeListener(e -> {
            if(!(this.minSlider.getValue() > this.maxSlider.getValue())) {
                MercuryStoreUI.repaintSubject.onNext(SettingsFrame.class);
            }else {
                minSlider.setValue(minSlider.getValue()-1);
            }
        });

        this.maxSlider = this.componentsFactory.getSlider(40,100,this.applicationConfig.get().getMaxOpacity());
        this.maxSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(minSlider.getValue() > maxSlider.getValue()){
                    minSlider.setValue(maxSlider.getValue());
                }
            }
        });

        this.notifierStatusPicker = this.componentsFactory.getComboBox(new String[]{"Always play a sound", "Only when tabbed out","Never"});
        WhisperNotifierStatus whisperNotifier = this.applicationConfig.get().getNotifierStatus();
        this.notifierStatusPicker.setSelectedIndex(whisperNotifier.getCode());

        this.gamePathField = this.componentsFactory.getTextField(this.applicationConfig.get().getGamePath());
        this.gamePathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
        ));

        JPanel poeFolderPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        poeFolderPanel.add(this.gamePathField);
        JButton changeButton = this.componentsFactory.getBorderedButton("Change");
        poeFolderPanel.add(changeButton);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        changeButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                this.gamePathField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER,1),
                        BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
                ));
                this.gamePathField.setText(fileChooser.getSelectedFile().getPath());
                this.gamePathField.removeMouseListener(poeFolderTooltipListener);
                this.poeFolderTooltipListener = null;
                this.repaint();
            }
        });

        root.add(this.componentsFactory.getTextLabel("Notify me when an update is available", FontStyle.REGULAR));
        root.add(this.checkEnable);
        root.add(this.componentsFactory.getTextLabel("Component fade out time: ", FontStyle.REGULAR));
        root.add(this.fadeTimeSlider);
        root.add(this.componentsFactory.getTextLabel("Min opacity: ", FontStyle.REGULAR));
        root.add(this.minSlider);
        root.add(this.componentsFactory.getTextLabel("Max opacity: ", FontStyle.REGULAR));
        root.add(this.maxSlider);
        root.add(this.componentsFactory.getTextLabel("Notification sound alerts: ", FontStyle.REGULAR));
        root.add(this.notifierStatusPicker);
        root.add(this.componentsFactory.getTextLabel("Path of Exile folder: ", FontStyle.REGULAR));
        root.add(poeFolderPanel);
        return root;
    }
    private JPanel getSettingsSlidePanel(){
        JPanel root = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton openTutorial =
                componentsFactory.getIconButton("app/tutorial.png",
                        30,
                        AppThemeColor.TRANSPARENT,
                        "Open tutorial");
        openTutorial.addActionListener(action -> {
            FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
            FramesManager.INSTANCE.preShowFrame(NotesFrame.class);
        });
        JButton checkUpdates =
                componentsFactory.getIconButton("app/check-update.png",
                        30,
                        AppThemeColor.TRANSPARENT,
                        "Check for updates");
        checkUpdates.addActionListener(action -> {
            ApplicationHolder.getInstance().setManualRequest(true);
            MercuryStoreCore.INSTANCE.requestPatchSubject.onNext(true);
        });
        JButton openTests =
                componentsFactory.getIconButton("app/open-tests.png",
                        30,
                        AppThemeColor.TRANSPARENT,
                        "Open tests");
        openTests.addActionListener(action -> {
            FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
            FramesManager.INSTANCE.preShowFrame(TestCasesFrame.class);
        });
        root.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MercuryStoreUI.repaintSubject.onNext(SettingsFrame.class);
            }
        });
        root.add(openTutorial);
        root.add(checkUpdates);
        root.add(openTests);
        return root;
    }
    @Override
    public boolean processAndSave() {
        int minOpacity = this.minSlider.getValue();
        int maxOpacity = this.maxSlider.getValue();
        int timeToDelay= this.fadeTimeSlider.getValue();
        HideSettingsManager.INSTANCE.apply(timeToDelay,minOpacity,maxOpacity);
        this.applicationConfig.get().setNotifierStatus(WhisperNotifierStatus.get(this.notifierStatusPicker.getSelectedIndex()));
        this.applicationConfig.get().setCheckOutUpdate(this.checkEnable.isSelected());
        if (this.isValidGamePath(this.gamePathField.getText())){
            this.applicationConfig.get().setGamePath(this.gamePathField.getText()+ File.separator);
            MercuryStoreCore.INSTANCE.poeFolderChangedSubject.onNext(true);
        } else {
            this.gamePathField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT,1),
                    BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
            ));
            if(poeFolderTooltipListener == null){
                this.poeFolderTooltipListener = new WrongGamePathListener();
                this.gamePathField.addMouseListener(poeFolderTooltipListener);
            }
            return false;
        }
        return true;
    }

    private boolean isValidGamePath(String gamePath){
        File file = new File(gamePath + File.separator + "logs" + File.separator + "Client.txt");
        return file.exists();
    }

    @Override
    public void restore() {
        this.verticalScrollContainer.removeAll();
        this.createUI();
    }

    private class WrongGamePathListener extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            MercuryStoreCore.INSTANCE.tooltipSubject.onNext(
                    "Wrong Path of Exile folder! Open Task Manager (CTRL + Shift + ESC) and check the path here!");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MercuryStoreCore.INSTANCE.tooltipSubject.onNext(null);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MercuryStoreCore.INSTANCE.tooltipSubject.onNext(null);
        }
    }
}
