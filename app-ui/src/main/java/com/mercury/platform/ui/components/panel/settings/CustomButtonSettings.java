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
            buttons.add(new ResponseButton(id,pair.title.getText(),pair.response.getText(),pair.kick.isSelected(),pair.close.isSelected()));
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
        GridBagConstraints kickColumn = new GridBagConstraints();
        GridBagConstraints closeColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        setUpGBConstants(titleColumn,valueColumn,kickColumn,closeColumn,utilColumn);

        this.setBackground(AppThemeColor.TRANSPARENT);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Label");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Response text");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel kickLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Kick");
        kickLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel closeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Close");
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        this.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        this.add(kickLabel,kickColumn);
        kickColumn.gridy++;
        this.add(closeLabel,closeColumn);
        closeColumn.gridy++;
        this.add(componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,""),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach(button ->{
            addNewRow(button.getTitle(),button.getResponseText(),button.isKick(),button.isClose(),titleColumn,valueColumn,kickColumn,closeColumn,utilColumn);
        });

        JButton addNew = componentsFactory.getBorderedButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (inputs.size() <= 12) {
                        remove(addNew);
                        addNewRow("expl", "example",false,false, titleColumn, valueColumn,kickColumn,closeColumn, utilColumn);
                        add(addNew, utilColumn);

                        owner.pack();
                    }
                }
            }
        });
        this.add(addNew,utilColumn);
    }
    private void addNewRow(String title, String value, boolean kick,
                           boolean close,
                           GridBagConstraints tC,
                           GridBagConstraints vC,
                           GridBagConstraints kC,
                           GridBagConstraints cC,
                           GridBagConstraints uC){
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
        this.add(valueField,vC);
        vC.gridy++;

        JPanel kickWrapper = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox kickCheckBox = new JCheckBox();
        kickCheckBox.setBackground(AppThemeColor.TRANSPARENT);
        kickCheckBox.setSelected(kick);
        kickWrapper.add(kickCheckBox);
        this.add(kickWrapper,kC);
        kC.gridy++;

        JPanel closeWrapper = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox closeCheckBox = new JCheckBox();
        closeCheckBox.setBackground(AppThemeColor.TRANSPARENT);
        closeCheckBox.setSelected(close);
        closeWrapper.add(closeCheckBox);
        this.add(closeWrapper,cC);
        cC.gridy++;
        ValuePair pair = new ValuePair(titleFiled, valueField, kickCheckBox, closeCheckBox);
        inputs.add(pair);


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
    private void setUpGBConstants(GridBagConstraints titleColumn,
                                  GridBagConstraints valueColumn,
                                  GridBagConstraints kickColumn,
                                  GridBagConstraints closeColumn,
                                  GridBagConstraints utilColumn){
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        kickColumn.fill = GridBagConstraints.HORIZONTAL;
        closeColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        titleColumn.weightx = 0.01f;
        valueColumn.weightx = 0.97f;
        kickColumn.weightx = 0.002f;
        closeColumn.weightx = 0.002f;
        utilColumn.weightx = 0.002f;

        titleColumn.anchor = GridBagConstraints.NORTHWEST;
        valueColumn.anchor = GridBagConstraints.NORTHWEST;
        kickColumn.anchor = GridBagConstraints.NORTHWEST;
        closeColumn.anchor = GridBagConstraints.NORTHWEST;
        utilColumn.anchor = GridBagConstraints.NORTHWEST;

        titleColumn.gridy = 0;
        titleColumn.gridx = 1;
        valueColumn.gridy = 0;
        valueColumn.gridx = 2;
        kickColumn.gridy = 0;
        kickColumn.gridx = 3;
        closeColumn.gridy = 0;
        closeColumn.gridx = 4;
        utilColumn.gridy = 0;
        utilColumn.gridx = 5;

        utilColumn.insets = new Insets(3,2,3,0);
        titleColumn.insets = new Insets(3,2,3,0);
        kickColumn.insets = new Insets(3,2,3,0);
        closeColumn.insets = new Insets(3,2,3,0);
        valueColumn.insets = new Insets(3,2,3,0);
    }
    private class ValuePair {
        private JTextField title;
        private JTextField response;
        private JCheckBox kick;
        private JCheckBox close;

        public ValuePair(JTextField title, JTextField response, JCheckBox kick, JCheckBox close) {
            this.title = title;
            this.response = response;
            this.kick = kick;
            this.close = close;
        }
    }
}
