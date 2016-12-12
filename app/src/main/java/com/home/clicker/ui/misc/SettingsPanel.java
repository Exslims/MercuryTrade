package com.home.clicker.ui.misc;

import com.home.clicker.misc.WhisperNotifierStatus;
import com.home.clicker.utils.PoeShortCastSettings;
import com.sun.java.swing.plaf.motif.MotifBorders;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        add(getWhisperNotifierPanel(), BorderLayout.PAGE_START);
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }

    public JPanel getWhisperNotifierPanel() {
        JPanel settingPanel = new JPanel(new BorderLayout());
        settingPanel.add(new JLabel(" Whisper notifier: "), BorderLayout.CENTER);

        DefaultComboBoxModel status = new DefaultComboBoxModel();
        status.addElement("Always");
        status.addElement("On alt-tab");
        status.addElement("None");

        JComboBox statusBox = new JComboBox(status);
        statusBox.setSelectedIndex(0);

        settingPanel.add(statusBox,BorderLayout.LINE_END);

        JButton saveButton = new JButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (statusBox.getSelectedIndex()){
                    case 0:
                        PoeShortCastSettings.WHISPER_NOTIFIER_STATUS = WhisperNotifierStatus.ALWAYS;
                        break;
                    case 1:
                        PoeShortCastSettings.WHISPER_NOTIFIER_STATUS = WhisperNotifierStatus.ALTAB;
                        break;
                    case 2:
                        PoeShortCastSettings.WHISPER_NOTIFIER_STATUS = WhisperNotifierStatus.NONE;
                        break;
                }
                SettingsPanel.this.setVisible(false);
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SettingsPanel.this.setVisible(false);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(saveButton);
        buttonsPanel.add(closeButton);

        settingPanel.add(buttonsPanel, BorderLayout.PAGE_END);
        return settingPanel;
    }
}
