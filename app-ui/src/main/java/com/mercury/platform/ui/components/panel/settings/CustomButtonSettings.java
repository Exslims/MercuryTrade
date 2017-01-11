package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.HasUI;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.MessagePanelStyle;
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
 * Created by Константин on 05.01.2017.
 */
public class CustomButtonSettings extends ConfigurationPanel implements HasUI {
    private Map<JTextField,JTextField> inputs;
    private JFrame owner;

    public CustomButtonSettings(JFrame owner) {
        super();
        this.owner = owner;
        createUI();
    }

    @Override
    public void processAndSave() {
        Map<String,String> buttonsConfig = new HashMap<>();
        inputs.forEach((k,v)-> buttonsConfig.put(k.getText(),v.getText()));
        ConfigManager.INSTANCE.saveButtonsConfig(buttonsConfig);
    }

    @Override
    protected LayoutManager getPanelLayout() {
        return  new BorderLayout();
    }

    @Override
    public void createUI() {
        inputs = new HashMap<>();
        Map<String, String> buttonsConfig = ConfigManager.INSTANCE.getButtonsConfig();

        JPanel otherSettings = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        otherSettings.add(componentsFactory.getTextLabel("Flip x:"));
        otherSettings.add(new JCheckBox());

        JPanel tablePanel = componentsFactory.getTransparentPanel(new GridBagLayout());

        GridBagConstraints buttonColumn = new GridBagConstraints();
        GridBagConstraints titleColumn = new GridBagConstraints();
        GridBagConstraints valueColumn = new GridBagConstraints();
        GridBagConstraints utilColumn = new GridBagConstraints();

        setUpGBConstants(buttonColumn,titleColumn,valueColumn,utilColumn);
        inputs = new HashMap<>();

        tablePanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel buttonLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Example");
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Highlight");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,"Text to send");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tablePanel.add(buttonLabel,buttonColumn);
        buttonColumn.gridy++;
        tablePanel.add(titleLabel, titleColumn);
        titleColumn.gridy++;
        tablePanel.add(valueLabel,valueColumn);
        valueColumn.gridy++;
        tablePanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, null,15f,""),utilColumn);
        utilColumn.gridy++;

        buttonsConfig.forEach((title,value) ->{
            addNewRow(tablePanel,title,value,buttonColumn,titleColumn,valueColumn,utilColumn);
        });

        JButton addNew = componentsFactory.getBorderedButton("Add");
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(inputs.size() <= 8) {
                    tablePanel.remove(addNew);
                    addNewRow(tablePanel, "expl", "example", buttonColumn, titleColumn, valueColumn, utilColumn);
                    tablePanel.add(addNew, utilColumn);

                    owner.pack();
                }
            }
        });
        tablePanel.add(addNew,utilColumn);


        JPanel msgPanelWrapper = componentsFactory.getTransparentPanel(new FlowLayout());
//        MessagePanel msgPanel = new MessagePanel("Example","2016/12/26 05:20:19 Hi, I would like to buy your Example Example listed for 1 exalted in Breach. (offer example example)", MessagePanelStyle.BIGGEST);
//        msgPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
//        msgPanelWrapper.add(msgPanel);
        this.add(otherSettings,BorderLayout.PAGE_START);
        this.add(tablePanel,BorderLayout.CENTER);
        this.add(msgPanelWrapper,BorderLayout.PAGE_END);
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

                owner.pack();
            }
        });
        rootPanel.add(remove,uC);
        uC.gridy++;
    }
    private void setUpGBConstants(GridBagConstraints buttonColumn, GridBagConstraints titleColumn, GridBagConstraints valueColumn, GridBagConstraints utilColumn){
        buttonColumn.fill = GridBagConstraints.HORIZONTAL;
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        valueColumn.fill = GridBagConstraints.HORIZONTAL;
        utilColumn.fill = GridBagConstraints.HORIZONTAL;

        buttonColumn.weightx = 0.01f;
        titleColumn.weightx = 0.01f;
        valueColumn.weightx = 0.97f;
        utilColumn.weightx = 0.002f;

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
    }
}
