package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.fields.label.FontStyle;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends OverlaidFrame {
    private Map<JTextField,JTextField> inputs;
    public SettingsFrame(){
        super("MT-Settings");
    }

    @Override
    protected void init() {
        super.init();
        disableHideEffect();
        initContainer();
        setFocusable(true);
        setFocusableWindowState(true);
        setAlwaysOnTop(false);
        processingHideEvent = false;
        this.pack();
    }

    private void initContainer() {
        JPanel centralPanel = componentsFactory.getTransparentPanel(null);
        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));
        centralPanel.add(getGeneralSettingsPanel());
        centralPanel.add(getCustomButtonSettingsPanel());
        centralPanel.add(getAboutPanel());

        this.add(centralPanel, BorderLayout.CENTER);
        this.add(getBottomPanel(), BorderLayout.PAGE_END);
    }
    private JPanel getGeneralSettingsPanel(){
        JPanel panel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createMatteBorder(1,0,1,0,AppThemeColor.BORDER),
                null);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel generalPanel = getGeneralInnerPanel();
        JPanel titlePanel = getCollapsiblePanel("General",generalPanel, true);
        panel.add(titlePanel);
        panel.add(generalPanel);
        return panel;
    }
    private JPanel getGeneralInnerPanel(){
        JPanel settingsPanel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.BORDER),
                null
        );
        settingsPanel.setVisible(true);
        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.Y_AXIS));
        JComboBox secondsPicker = componentsFactory.getComboBox(new String[]{"0","1","2","3","4","5"});

        JPanel hideSettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        hideSettingsPanel.add(componentsFactory.getTextLabel("Hide to minimum opacity after:"));
        hideSettingsPanel.add(secondsPicker);
        hideSettingsPanel.add(componentsFactory.getTextLabel("seconds. 0 - always show"));

        JPanel minOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        minOpacitySettingsPanel.add(componentsFactory.getTextLabel("Minimum opacity: "));

        JPanel minValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel minValueField = componentsFactory.getTextLabel("60%"); //todo
        minValuePanel.add(minValueField);
        minValuePanel.setPreferredSize(new Dimension(35,30));
        minOpacitySettingsPanel.add(minValuePanel);

        JSlider minSlider = componentsFactory.getSlider(0,100,60);
        minSlider.addChangeListener(e -> {
            minValueField.setText(String.valueOf(minSlider.getValue()) + "%");
            SettingsFrame.this.repaint();
        });
        minOpacitySettingsPanel.add(minSlider);

        JPanel maxOpacitySettingsPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        maxOpacitySettingsPanel.add(componentsFactory.getTextLabel("Maximum opacity: "));

        JPanel maxValuePanel = componentsFactory.getTransparentPanel(new FlowLayout());
        JLabel maxValueField = componentsFactory.getTextLabel("60%"); //todo
        maxValuePanel.add(maxValueField);
        maxValuePanel.setPreferredSize(new Dimension(35,30));
        maxOpacitySettingsPanel.add(maxValuePanel);

        JSlider maxSlider = componentsFactory.getSlider(20,100,60);
        maxSlider.addChangeListener(e -> {
            maxValueField.setText(String.valueOf(maxSlider.getValue()) + "%");
            SettingsFrame.this.setOpacity(maxSlider.getValue()/100.0f);
        });
        maxOpacitySettingsPanel.add(maxSlider);

        JPanel notifierPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        notifierPanel.add(componentsFactory.getTextLabel("Trade messages notifier: "));
        JComboBox notifierStatusPicker = componentsFactory.getComboBox(new String[]{"Always", "While on al-tab","Never"});
        notifierPanel.add(notifierStatusPicker);

        settingsPanel.add(hideSettingsPanel);
        settingsPanel.add(minOpacitySettingsPanel);
        settingsPanel.add(maxOpacitySettingsPanel);
        settingsPanel.add(notifierPanel);
        return settingsPanel;
    }
    private JPanel getCustomButtonSettingsPanel(){
        JPanel panel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER),
                null);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel innerPanel = getInnerCbPanel();
        innerPanel.setVisible(false);
        JPanel titlePanel = getCollapsiblePanel("Message panel",innerPanel, false);

        panel.add(titlePanel);
        panel.add(innerPanel,BorderLayout.CENTER);
        return panel;
    }
    private JPanel getInnerCbPanel(){
        JPanel panel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.BORDER),
                new BorderLayout());
        Map<String, String> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints buttonColumn = new GridBagConstraints();
        GridBagConstraints titleColumn = new GridBagConstraints();
        GridBagConstraints valueColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        buttonColumn.fill = GridBagConstraints.HORIZONTAL;
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        buttonColumn.weightx = 0.05f;
        titleColumn.weightx = 0.05f;
        valueColumn.weightx = 0.89f;
        utilColumn.weightx = 0.01f;

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

        utilColumn.insets = new Insets(3,2,3,0);
        buttonColumn.insets = new Insets(3,0,3,0);
        titleColumn.insets = new Insets(3,2,3,0);
        valueColumn.insets = new Insets(3,2,3,0);

        inputs = new HashMap<>();

        panel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel buttonLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Example");
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Highlight");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Text to send");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(buttonLabel,buttonColumn);
        buttonColumn.gridy++;
        panel.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        panel.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        panel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,""),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach((title,value) ->{
            addNewRow(panel,title,value,buttonColumn,titleColumn,valueColumn,utilColumn);
        });

        JButton addNew = componentsFactory.getBorderedButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(inputs.size() <= 8) {
                    panel.remove(addNew);
                    addNewRow(panel, "expl", "example", buttonColumn, titleColumn, valueColumn, utilColumn);
                    panel.add(addNew, utilColumn);

                    SettingsFrame.this.pack();
                }
            }
        });
        panel.add(addNew,utilColumn);

        return panel;
    }
    private void addNewRow(JPanel rootPanel, String title, String value, GridBagConstraints bC, GridBagConstraints tC, GridBagConstraints vC, GridBagConstraints uC){
        JButton button = componentsFactory.getBorderedButton(title);
        rootPanel.add(button,bC);
        bC.gridy++;

        JTextField titleFiled = componentsFactory.getTextField(title,FontStyle.BOLD,14f);
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

        JTextField valueField = componentsFactory.getTextField(value,FontStyle.BOLD,14f);
        inputs.put(titleFiled,valueField);
        rootPanel.add(valueField,vC);
        vC.gridy++;

        JButton remove = componentsFactory.getBorderedButton("x");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rootPanel.remove(button);
                inputs.remove(titleFiled);
                rootPanel.remove(titleFiled);
                rootPanel.remove(valueField);
                rootPanel.remove(remove);

                SettingsFrame.this.pack();
            }
        });
        rootPanel.add(remove,uC);
        uC.gridy++;
    }

    private JPanel getAboutPanel(){
        JPanel panel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER),
                null
        );
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel aboutPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        aboutPanel.setVisible(false);
        aboutPanel.add(componentsFactory.getTextLabel("About program and contacts here."));
        aboutPanel.add(componentsFactory.getTextField("TYPE HERE", FontStyle.BOLD,16));
        aboutPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.BORDER));

        JPanel titlePanel = getCollapsiblePanel("About",aboutPanel, false);

        panel.add(titlePanel);
        panel.add(aboutPanel);
        return panel;
    }

    private JPanel getCollapsiblePanel(String title, JPanel innerPanel, boolean expand){
        JPanel collapsiblePanel = componentsFactory.getBorderedTransparentPanel(
                BorderFactory.createEmptyBorder(-4,0,-4,0),
                new FlowLayout(FlowLayout.LEFT));
        collapsiblePanel.setBackground(AppThemeColor.HEADER);
        collapsiblePanel.setBorder(BorderFactory.createEmptyBorder(-4,0,-4,0));
        String iconPath = expand ? "app/collapse.png":"app/expand.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath, 16);
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!innerPanel.isVisible()) {
                    expandButton.setIcon(componentsFactory.getIcon("app/collapse.png", 16));
                    innerPanel.setVisible(true);
                }else {
                    expandButton.setIcon(componentsFactory.getIcon("app/expand.png", 16));
                    innerPanel.setVisible(false);
                }
                SettingsFrame.this.pack();
            }
        });
        collapsiblePanel.add(expandButton);
        collapsiblePanel.add(componentsFactory.getTextLabel(title));
        return collapsiblePanel;
    }

    private JPanel getBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppThemeColor.HEADER);

        JButton save = componentsFactory.getBorderedButton("Save");
        JButton close = componentsFactory.getBorderedButton("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SettingsFrame.this.dispose();
            }
        });
        panel.add(save);
        panel.add(close);
        return panel;
    }


    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
