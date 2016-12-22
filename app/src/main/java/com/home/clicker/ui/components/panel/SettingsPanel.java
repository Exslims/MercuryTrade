package com.home.clicker.ui.components.panel;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.CloseFrameEvent;
import com.home.clicker.shared.events.custom.DraggedWindowEvent;
import com.home.clicker.shared.events.custom.RepaintEvent;
import com.home.clicker.core.misc.WhisperNotifierStatus;
import com.home.clicker.ui.components.ComponentsFactory;
import com.home.clicker.ui.components.fields.ExButton;
import com.home.clicker.ui.components.fields.label.ExLabel;
import com.home.clicker.ui.components.fields.ExTextField;
import com.home.clicker.ui.components.fields.label.FontStyle;
import com.home.clicker.ui.components.fields.label.TextAlignment;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.ui.misc.CustomButtonFactory;
import com.home.clicker.shared.PoeShortCastSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Константин on 10.12.2016.
 */
public class SettingsPanel extends JPanel {
    private ComponentsFactory componentsFactory;
    private JPanel settingsContainer;
    private JScrollPane scroll;
    private int x;
    private int y;
    private Dimension panelSize = new Dimension(800,500);
    private Map<ExTextField,ExTextField> inputs = new HashMap<>();
    public SettingsPanel() {
        super(new BorderLayout());
        componentsFactory = new ComponentsFactory();
        init();
    }

    private void init() {
        this.setPreferredSize(panelSize);
        this.setSize(panelSize);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);

        settingsContainer = new JPanel();
        settingsContainer.setLayout(new BoxLayout(settingsContainer,BoxLayout.Y_AXIS));
        settingsContainer.setBackground(AppThemeColor.TRANSPARENT);
        settingsContainer.setBorder(null);

        settingsContainer.add(getButtonsSettingsPanel());
        settingsContainer.add(getWhisperNotifierPanel());



        ExButton title = new ExButton(PoeShortCastSettings.APP_VERSION);
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        title.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                EventRouter.fireEvent(new DraggedWindowEvent(e.getLocationOnScreen().x -x,e.getLocationOnScreen().y - y));
            }
        });

        this.add(title, BorderLayout.PAGE_START);
        this.add(settingsContainer,BorderLayout.CENTER);
        this.setVisible(true);

    }

    private JPanel getWhisperNotifierPanel() {
        JPanel wnSettingPanel = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        Dimension size = new Dimension(Integer.MAX_VALUE,60);
        wnSettingPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3));
        wnSettingPanel.setSize(size);
        wnSettingPanel.setPreferredSize(size);
        wnSettingPanel.setMaximumSize(size);
        wnSettingPanel.setBackground(AppThemeColor.TRANSPARENT);
        JLabel wnLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Whisper notifier:");
        wnSettingPanel.add(wnLabel, BorderLayout.LINE_START);

        DefaultComboBoxModel status = new DefaultComboBoxModel();
        status.addElement("Always");
        status.addElement("On alt-tab");
        status.addElement("None");

        JComboBox statusBox = new JComboBox(status);
        statusBox.setSelectedIndex(0);
        statusBox.setMaximumSize(new Dimension(50,120));
        statusBox.setPreferredSize(new Dimension(50,120));
        statusBox.setSize(new Dimension(50,120));

        wnSettingPanel.add(statusBox,BorderLayout.CENTER);

        ExButton saveButton = new ExButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Map<String,String> buttonsConfig = new HashMap<>();
                inputs.forEach((k,v)->{
                    buttonsConfig.put(k.getText(),v.getText());
                });
                CustomButtonFactory.saveNewButtonsConfig(buttonsConfig);

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
                EventRouter.fireEvent(new CloseFrameEvent());
            }
        });

        ExButton closeButton = new ExButton("Close");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new CloseFrameEvent());
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(AppThemeColor.TRANSPARENT);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(closeButton);

        wnSettingPanel.add(buttonsPanel, BorderLayout.PAGE_END);
        return wnSettingPanel;
    }
    private JPanel getButtonsSettingsPanel(){
        Map<String, String> buttonsConfig = CustomButtonFactory.getButtonsConfig();
        JPanel rootPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        rootPanel.setLayout(new GridBagLayout());

        Dimension dimension = new Dimension(Integer.MAX_VALUE, 45*buttonsConfig.size() + 40);
        rootPanel.setSize(dimension);
        rootPanel.setPreferredSize(dimension);
        rootPanel.setMaximumSize(dimension);

        GridBagConstraints buttonColumn = new GridBagConstraints();
        GridBagConstraints titleColumn = new GridBagConstraints();
        GridBagConstraints valueColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        buttonColumn.fill = GridBagConstraints.HORIZONTAL;
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        buttonColumn.weightx = 0.25f;
        titleColumn.weightx = 0.25f;
        valueColumn.weightx = 0.9;
        utilColumn.weightx = 0.05;

        buttonColumn.anchor = GridBagConstraints.NORTHWEST;
        titleColumn.anchor = GridBagConstraints.NORTHWEST;
        valueColumn.anchor = GridBagConstraints.NORTHWEST;
        utilColumn.anchor = GridBagConstraints.NORTHWEST;

        buttonColumn.gridy = 0;
        buttonColumn.gridx = 0;
        titleColumn.gridy = 0;
        titleColumn.gridx = 1;
        valueColumn.gridy = 0;
        valueColumn.gridx = 2;
        utilColumn.gridy = 0;
        utilColumn.gridx = 3;

        utilColumn.insets = new Insets(3,0,3,0);
        buttonColumn.insets = new Insets(3,0,3,0);
        titleColumn.insets = new Insets(3,0,3,0);
        valueColumn.insets = new Insets(3,0,3,0);

        rootPanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel buttonLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Example");
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Highlight");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Text to send");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        rootPanel.add(buttonLabel,buttonColumn);
        buttonColumn.gridy++;
        rootPanel.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        rootPanel.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        rootPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"UI"),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach((title,value) ->{
            addNewRow(rootPanel,title,value,buttonColumn,titleColumn,valueColumn,utilColumn);
        });

        ExButton addNew = new ExButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(inputs.size() <= 8) {
                    rootPanel.remove(addNew);
                    Dimension dimension = new Dimension(Integer.MAX_VALUE, rootPanel.getSize().height + 40);
                    rootPanel.setSize(dimension);
                    rootPanel.setPreferredSize(dimension);
                    rootPanel.setMaximumSize(dimension);

                    addNewRow(rootPanel, "expl", "example", buttonColumn, titleColumn, valueColumn, utilColumn);

                    rootPanel.add(addNew, utilColumn);

                    SettingsPanel.this.revalidate();
                    SettingsPanel.this.repaint();
                    EventRouter.fireEvent(new RepaintEvent());
                }
            }
        });
        rootPanel.add(addNew,utilColumn);
        return rootPanel;
    }

    private void addNewRow(JPanel rootPanel, String title, String value, GridBagConstraints bC, GridBagConstraints tC, GridBagConstraints vC, GridBagConstraints uC){
        ExButton button = new ExButton(title);

        rootPanel.add(button,bC);
        bC.gridy++;

        ExTextField titleFiled = new ExTextField(title);
        titleFiled.setBackground(new Color(57,57,57));
        titleFiled.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                button.setText(titleFiled.getText());
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if(titleFiled.getText().length() > 5){
                    e.consume();
                }
            }
        });

        rootPanel.add(titleFiled,tC);
        tC.gridy++;

        ExTextField valueField = new ExTextField(value);
        valueField.setBackground(new Color(57, 57, 57));

        inputs.put(titleFiled,valueField);

        rootPanel.add(valueField,vC);
        vC.gridy++;

        ExButton remove = new ExButton("x");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rootPanel.remove(button);
                inputs.remove(titleFiled);
                rootPanel.remove(titleFiled);
                rootPanel.remove(valueField);
                rootPanel.remove(remove);

                SettingsPanel.this.revalidate();
                SettingsPanel.this.repaint();
                EventRouter.fireEvent(new RepaintEvent());
            }
        });
        rootPanel.add(remove,uC);
        uC.gridy++;
    }
}
