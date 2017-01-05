package com.mercury.platform.ui.components.test;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewWhispersEvent;
import com.mercury.platform.shared.events.custom.PlayerJoinEvent;
import com.mercury.platform.shared.events.custom.PlayerLeftEvent;
import com.mercury.platform.shared.events.custom.WhisperNotificationEvent;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 26.12.2016.
 */
public class TestCasesFrame extends OverlaidFrame {
    private List<String> randomMessages;
    private List<String> nickNames;

    public TestCasesFrame() {
        super("Test cases");
    }

    @Override
    protected void init() {
        super.init();
        add(getTestCasesPanel(), BorderLayout.CENTER);
        disableHideEffect();
        pack();
        randomMessages = new ArrayList<>();
        nickNames = new ArrayList<>();
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring listed for 90 chaos in Breach (stash tab \"Gear\"; position: left 11, top 2) offer");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring Corpse Ring Corpse Ring listed for 1 chaos in Breach (stash tab \"Gear\"; position: left 11, top 2) offerofferofferofferoffer");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring Corpse Ring listed for 150 fusing in Breach (stash tab \"Gear\"; position: left 11, top 2) offerqqqweeeee");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring Corpse Ring listed for 10 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring Corpse Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) offffffe");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring listed for 1 chaos in Breach (stash tab \"Gear\"; position: left 11, top 2)");
        randomMessages.add("2016/12/26 05:20:19 Hi, I would like to buy your Corpse Ring listed for 45 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) блабалблабалабала");

        nickNames.add("Kanarid");
        nickNames.add("Vin");
        nickNames.add("Rrra");
        nickNames.add("Pabeesari");
        nickNames.add("Joll");
        nickNames.add("Jashicore");
        nickNames.add("Xach");
        nickNames.add("Yaitaly");
        nickNames.add("xXxДолбоебxXx");
    }

    @Override
    protected String getFrameTitle() {
        return "Test cases";
    }
    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
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
                List<Message> messages = new ArrayList<>();
                messages.add(new Message(nickNames.get(new Random().nextInt(9)),randomMessages.get(new Random().nextInt(7))));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel = componentsFactory.getTextLabel("Random message");
        testPanel.add(textLabel,titleColumn);
        titleColumn.gridy++;

        JButton button5 = componentsFactory.getBorderedButton("Click");
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("ДолбоебСРусскимНиком","2016/12/26 05:20:19 Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) блабалблабалабала"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button5,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel15 = componentsFactory.getTextLabel("Test font for non-eng letters");
        testPanel.add(textLabel15,titleColumn);
        titleColumn.gridy++;


        JButton button6 = componentsFactory.getBorderedButton("Click");
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("WhisperColorTest","2016/12/26 05:20:19 Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)"));
                EventRouter.fireEvent(new NewWhispersEvent(messages));

                Timer joinedTimer = new Timer(1000,null);
                joinedTimer.addActionListener(e1 -> {
                    EventRouter.fireEvent(new PlayerJoinEvent("WhisperColorTest"));
                    joinedTimer.stop();
                });
                joinedTimer.start();

                Timer leftTimer = new Timer(2000,null);
                leftTimer.addActionListener(e1 -> {
                    EventRouter.fireEvent(new PlayerLeftEvent("WhisperColorTest"));
                    leftTimer.stop();
                });
                leftTimer.start();
            }
        });
        testPanel.add(button6,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel16 = componentsFactory.getTextLabel("Test whisper font color after join/left area");
        testPanel.add(textLabel16,titleColumn);
        titleColumn.gridy++;
        testPanel.setBackground(AppThemeColor.TRANSPARENT);

        return testPanel;
    }

    @Override
    public void initHandlers() {

    }
}
