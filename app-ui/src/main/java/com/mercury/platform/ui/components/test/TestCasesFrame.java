package com.mercury.platform.ui.components.test;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.WhisperNotificationEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 26.12.2016.
 */
public class TestCasesFrame extends OverlaidFrame {

    private int x;
    private int y;

    public TestCasesFrame() {
        super("Test cases");
    }

    @Override
    protected void init() {
        super.init();
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER,1));
        JPanel topPanel = new JPanel();
        topPanel.setBackground(AppThemeColor.TRANSPARENT);
        JLabel title = componentsFactory.getTextLabel("Test cases");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(title);
        add(topPanel, BorderLayout.PAGE_START);

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
                e.translatePoint(TestCasesFrame.this.getLocation().x - x,TestCasesFrame.this.getLocation().y - y);
                TestCasesFrame.this.setLocation(e.getX(),e.getY());
                configManager.saveComponentLocation(TestCasesFrame.this.getClass().getSimpleName(),TestCasesFrame.this.getLocation());
            }
        });
        add(getTestCasesPanel(), BorderLayout.CENTER);
        disableHideEffect();
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
        buttonColumn.insets = new Insets(3,3,3,0);
        titleColumn.insets = new Insets(3,3,3,0);

        JButton button = componentsFactory.getBorderedButton("Click");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel = componentsFactory.getTextLabel("Short message");
        testPanel.add(textLabel,titleColumn);
        titleColumn.gridy++;

        JButton button1 = componentsFactory.getBorderedButton("Click");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button1,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel1 = componentsFactory.getTextLabel("Medium message");
        testPanel.add(textLabel1,titleColumn);
        titleColumn.gridy++;

        JButton button2 = componentsFactory.getBorderedButton("Click");
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button2,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel12 = componentsFactory.getTextLabel("Biggest message");
        testPanel.add(textLabel12,titleColumn);
        titleColumn.gridy++;

        JButton button3 = componentsFactory.getBorderedButton("Click");
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) #########################################"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button3,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel13 = componentsFactory.getTextLabel("Biggest message + offer");
        testPanel.add(textLabel13,titleColumn);
        titleColumn.gridy++;

        JButton button4 = componentsFactory.getBorderedButton("Click");
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) #########################################"));
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) #########################################"));
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) #########################################"));
                messages.add(new Message("Test############","Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) #########################################"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button4,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel14 = componentsFactory.getTextLabel("Multiple biggest messages + offer");
        testPanel.add(textLabel14,titleColumn);
        titleColumn.gridy++;


        testPanel.setBackground(AppThemeColor.TRANSPARENT);

        return testPanel;
    }

    @Override
    public void initHandlers() {

    }
}
