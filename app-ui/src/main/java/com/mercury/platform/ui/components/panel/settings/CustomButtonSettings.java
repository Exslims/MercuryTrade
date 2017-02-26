package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CustomButtonsChangedEvent;
import com.mercury.platform.shared.pojo.ResponseButton;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 05.01.2017.
 */
public class CustomButtonSettings extends ConfigurationPanel implements HasUI {
    private List<ValuePair> inputs;
    private JFrame owner;
    private int id;

    public CustomButtonSettings(JFrame owner) {
        super();
        this.owner = owner;
        createUI();
    }

    @Override
    public void processAndSave() {
        List<ResponseButton> buttons = new ArrayList<>();
        id = 0;
        inputs.forEach(pair -> {
            buttons.add(new ResponseButton(id,pair.title.getText(),pair.response.getText()));
            id++;
        });
        ConfigManager.INSTANCE.saveButtonsConfig(buttons);
        EventRouter.INSTANCE.fireEvent(new CustomButtonsChangedEvent());
    }

    @Override
    public void restore() {
        this.removeAll();
        createUI();
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return new GridBagLayout();
    }

    @Override
    public void createUI() {
        inputs = new ArrayList<>();
        List<ResponseButton> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();
        Collections.sort(buttonsConfig);
        GridBagConstraints titleColumn = new GridBagConstraints();
        GridBagConstraints valueColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        setUpGBConstants(titleColumn,valueColumn,utilColumn);

        this.setBackground(AppThemeColor.TRANSPARENT);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Label");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Response text");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        this.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        this.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,""),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach(button ->{
            addNewRow(button.getTitle(),button.getResponseText(),titleColumn,valueColumn,utilColumn);
        });

        JButton addNew = componentsFactory.getBorderedButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (inputs.size() <= 12) {
                        remove(addNew);
                        addNewRow("expl", "example", titleColumn, valueColumn, utilColumn);
                        add(addNew, utilColumn);

                        owner.pack();
                    }
                }
            }
        });
        this.add(addNew,utilColumn);
    }
    private void addNewRow(String title, String value, GridBagConstraints tC, GridBagConstraints vC, GridBagConstraints uC){
        JTextField titleFiled = componentsFactory.getTextField(title,FontStyle.BOLD,14f);
        titleFiled.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(titleFiled.getText().length() > 5){
                    e.consume();
                }
            }
        });

        this.add(titleFiled,tC);
        tC.gridy++;

        JTextField valueField = componentsFactory.getTextField(value,FontStyle.BOLD,14f);
        ValuePair pair = new ValuePair(titleFiled, valueField);
        inputs.add(pair);
        this.add(valueField,vC);
        vC.gridy++;

        JButton remove = componentsFactory.getBorderedButton("x");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    inputs.remove(pair);
                    remove(titleFiled);
                    remove(valueField);
                    remove(remove);

                    owner.pack();
                }
            }
        });
        this.add(remove,uC);
        uC.gridy++;
    }
    private void setUpGBConstants(GridBagConstraints titleColumn, GridBagConstraints valueColumn, GridBagConstraints utilColumn){
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        titleColumn.weightx = 0.01f;
        valueColumn.weightx = 0.97f;
        utilColumn.weightx = 0.002f;

        titleColumn.anchor = GridBagConstraints.NORTHWEST;
        valueColumn.anchor = GridBagConstraints.NORTHWEST;
        utilColumn.anchor = GridBagConstraints.NORTHWEST;

        titleColumn.gridy = 0;
        titleColumn.gridx = 1;
        valueColumn.gridy = 0;
        valueColumn.gridx = 2;
        utilColumn.gridy = 0;
        utilColumn.gridx = 3;

        utilColumn.insets = new Insets(3,2,3,0);
        titleColumn.insets = new Insets(3,2,3,0);
        valueColumn.insets = new Insets(3,2,3,0);
    }
    private class ValuePair {
        private JTextField title;
        private JTextField response;

        public ValuePair(JTextField title, JTextField response) {
            this.title = title;
            this.response = response;
        }
    }
}
