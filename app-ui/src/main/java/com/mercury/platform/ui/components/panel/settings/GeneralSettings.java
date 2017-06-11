package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.AbstractComponentFrame;
import com.mercury.platform.ui.frame.titled.NotesFrame;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.frame.titled.TestCasesFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.manager.HideSettingsManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.RepaintEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

public class GeneralSettings extends ConfigurationPanel {
    private JSlider minSlider;
    private JSlider maxSlider;
    private JSlider fadeTimeSlider;
    //private JComboBox secondsPicker;
    private JComboBox notifierStatusPicker;
    private AbstractComponentFrame owner;
    private JCheckBox checkEnable;
    private JTextField gamePathField;
    private WrongGamePathListener poeFolderTooltipListener;
    public GeneralSettings(AbstractComponentFrame owner) {
        super();
        this.owner = owner;
        createUI();
    }

    @Override
    public void createUI() {
        verticalScrollContainer.add(getSettingsSlidePanel());
        verticalScrollContainer.add(getSettingsPanel());
    }
    private JPanel getSettingsPanel() {
        Dimension elementsSize = new Dimension(260, 25);
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(7,2));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JLabel notifyLabel = componentsFactory.getTextLabel("Notify me when an update is available", FontStyle.REGULAR);
        checkEnable = new JCheckBox();
        checkEnable.setBackground(AppThemeColor.TRANSPARENT);
        checkEnable.setSelected(ConfigManager.INSTANCE.isCheckUpdateOnStartUp());
        checkEnable.setPreferredSize(elementsSize);


        /*
        JLabel hideSettingsLabel = componentsFactory.getTextLabel("Fade time (seconds). 0 - Always show", FontStyle.REGULAR);
        secondsPicker = componentsFactory.getComboBox(new String[]{"0","1","2","3","4","5"});
        int decayTime = ConfigManager.INSTANCE.getDecayTime();
        secondsPicker.setSelectedIndex(decayTime);
        secondsPicker.setPreferredSize(elementsSize);
        */

        //

        JPanel fadeTimeSettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        fadeTimeSettingsPanel.add(componentsFactory.getTextLabel("Component fade out time: ", FontStyle.REGULAR));
        fadeTimeSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0,-5,0,0));

        JPanel fadeTimePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel fadeTimeField = componentsFactory.getTextLabel(ConfigManager.INSTANCE.getFadeTime() + " sec", FontStyle.REGULAR);
        fadeTimePanel.add(fadeTimeField);
        fadeTimePanel.setPreferredSize(new Dimension(55,25));
        fadeTimeSettingsPanel.add(fadeTimePanel);

        if (ConfigManager.INSTANCE.getFadeTime() == 0) {
            fadeTimeField.setText("Never");
        }
        fadeTimeSlider = componentsFactory.getSlider(0,60, ConfigManager.INSTANCE.getFadeTime());
        fadeTimeSlider.addChangeListener(e -> {
            if (fadeTimeSlider.getValue() == 0)
                fadeTimeField.setText("Never");
            else
                fadeTimeField.setText(String.valueOf(fadeTimeSlider.getValue()) + " sec");

            owner.repaint();
        });
        fadeTimeSlider.setPreferredSize(elementsSize);

        //

        JPanel minOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Min opacity: ", FontStyle.REGULAR));
        minOpacitySettingsPanel.setBorder(BorderFactory.createEmptyBorder(0,-5,0,0));

        JPanel minValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel minValueField = componentsFactory.getTextLabel(ConfigManager.INSTANCE.getMinOpacity() + "%", FontStyle.REGULAR); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,25));
        minOpacitySettingsPanel.add(minValuePanel);

        minSlider = componentsFactory.getSlider(10,100,ConfigManager.INSTANCE.getMinOpacity());
        minSlider.addChangeListener(e -> {
            if(!(minSlider.getValue() > maxSlider.getValue())) {
                minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
                owner.repaint();
            }else {
                minSlider.setValue(minSlider.getValue()-1);
            }
        });
        minSlider.setPreferredSize(elementsSize);

        JPanel maxOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Max opacity: ", FontStyle.REGULAR));
        maxOpacitySettingsPanel.setBorder(BorderFactory.createEmptyBorder(0,-5,0,0));
        JPanel maxValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel maxValueField = componentsFactory.getTextLabel(ConfigManager.INSTANCE.getMaxOpacity() + "%", FontStyle.REGULAR); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);
        maxSlider = componentsFactory.getSlider(20,100,ConfigManager.INSTANCE.getMaxOpacity());
        maxSlider.addChangeListener(e -> {
            maxValueField.setText(String.valueOf(maxSlider.getValue()) + "%");
            owner.setOpacity(maxSlider.getValue()/100.0f);
        });
        maxSlider.setPreferredSize(elementsSize);
        maxSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(minSlider.getValue() > maxSlider.getValue()){
                    minSlider.setValue(maxSlider.getValue());
                }
            }
        });

        JLabel notifierLabel = componentsFactory.getTextLabel("Notification sound alerts: ", FontStyle.REGULAR);

        notifierStatusPicker = componentsFactory.getComboBox(new String[]{"Always play a sound", "Only when tabbed out","Never"});
        WhisperNotifierStatus whisperNotifier = ConfigManager.INSTANCE.getWhisperNotifier();
        notifierStatusPicker.setSelectedIndex(whisperNotifier.getCode());
        notifierStatusPicker.setPreferredSize(elementsSize);

        JLabel poeFolder = componentsFactory.getTextLabel("Path of Exile folder: ", FontStyle.REGULAR);
        gamePathField = componentsFactory.getTextField(ConfigManager.INSTANCE.getGamePath());
        gamePathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
        ));
        gamePathField.setPreferredSize(new Dimension(200,25));

        JPanel poeFolderPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        poeFolderPanel.add(gamePathField);
        JButton changeButton = componentsFactory.getBorderedButton("Change");
        poeFolderPanel.add(changeButton);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        changeButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                gamePathField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER,1),
                        BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
                ));
                gamePathField.setText(fileChooser.getSelectedFile().getPath());
                gamePathField.removeMouseListener(poeFolderTooltipListener);
                poeFolderTooltipListener = null;
                repaint();
            }
        });

        root.add(notifyLabel);
        root.add(wrapCellElement(checkEnable));
        root.add(fadeTimeSettingsPanel);
        root.add(wrapCellElement(fadeTimeSlider));
        //root.add(wrapCellElement(secondsPicker));
        root.add(minOpacitySettingsPanel);
        root.add(wrapCellElement(minSlider));
        root.add(maxOpacitySettingsPanel);
        root.add(wrapCellElement(maxSlider));
        root.add(notifierLabel);
        root.add(wrapCellElement(notifierStatusPicker));
        root.add(poeFolder);
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
            EventRouter.CORE.fireEvent(new RequestPatchNotesEvent());
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
                EventRouter.UI.fireEvent(new RepaintEvent.RepaintSettingFrame());
            }
        });
        root.add(openTutorial);
        root.add(checkUpdates);
        root.add(openTests);
        return root;
    }
    @Override
    public boolean processAndSave() {
        int minOpacity = minSlider.getValue();
        int maxOpacity = maxSlider.getValue();
        int timeToDelay= fadeTimeSlider.getValue(); // ----
        HideSettingsManager.INSTANCE.apply(timeToDelay,minOpacity,maxOpacity);
        ConfigManager.INSTANCE.setWhisperNotifier(WhisperNotifierStatus.get(notifierStatusPicker.getSelectedIndex()));
        ConfigManager.INSTANCE.setCheckUpdateOnStartUp(checkEnable.isSelected());
        if (ConfigManager.INSTANCE.isValidGamePath(gamePathField.getText())){
            ConfigManager.INSTANCE.setGamePath(gamePathField.getText()+ File.separator);
            MercuryStore.INSTANCE.poeFolderChangedSubject.onNext(true);
        } else {
            gamePathField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT,1),
                    BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)
            ));
            if(poeFolderTooltipListener == null){
                poeFolderTooltipListener = new WrongGamePathListener();
                gamePathField.addMouseListener(poeFolderTooltipListener);
            }
            return false;
        }
        return true;
    }

    @Override
    public void restore() {
        verticalScrollContainer.removeAll();
        this.createUI();
        owner.setOpacity(maxSlider.getValue()/100.0f);
    }
    private JPanel wrapCellElement(Component component){
        JPanel panel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return panel;
    }

    private class WrongGamePathListener extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            MercuryStore.INSTANCE.tooltipSubject.onNext(
                    "Wrong Path of Exile folder! Open Task Manager (CTRL + Shift + ESC) and check the path here!");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MercuryStore.INSTANCE.tooltipSubject.onNext(null);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MercuryStore.INSTANCE.tooltipSubject.onNext(null);
        }
    }
}
