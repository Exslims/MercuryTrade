package com.mercury.platform.ui.components.test;

import com.mercury.platform.ui.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 26.12.2016.
 */
public class TestCasesFrame extends OverlaidFrame {

    protected TestCasesFrame() {
        super("Test cases");
    }

    @Override
    protected void init() {
        super.init();
        add(getTestCasesPanel());
        pack();
    }

    private JPanel getTestCasesPanel(){
        JPanel testPanel = new JPanel(new GridBagLayout());

        GridBagConstraints buttonColumn = new GridBagConstraints();
        GridBagConstraints titleColumn = new GridBagConstraints();
        buttonColumn.fill = GridBagConstraints.HORIZONTAL;
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        buttonColumn.weightx = 0.25f;
        titleColumn.weightx = 0.9f;
        buttonColumn.anchor = GridBagConstraints.NORTHWEST;
        titleColumn.anchor = GridBagConstraints.NORTHWEST;
        buttonColumn.gridy = 0;
        buttonColumn.gridx = 0;
        titleColumn.gridy = 0;
        titleColumn.gridx = 1;
        buttonColumn.insets = new Insets(3,0,3,0);
        titleColumn.insets = new Insets(3,0,3,0);

//        JButton button = componentsFactory.getButton("1 message");
//        testPanel.add(button,buttonColumn);
//        componentsFactory.getTextLabel("")


        testPanel.setBackground(AppThemeColor.TRANSPARENT);

        return testPanel;
    }

    @Override
    public void initHandlers() {

    }
}
