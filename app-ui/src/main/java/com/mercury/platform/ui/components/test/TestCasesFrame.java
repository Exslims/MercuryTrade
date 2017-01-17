package com.mercury.platform.ui.components.test;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.frame.ComponentFrame;
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
public class TestCasesFrame extends ComponentFrame {
    private List<String> randomMessages;
    private List<String> nickNames;

    public TestCasesFrame() {
        super("Test cases");
    }

    @Override
    protected void init() {
        super.init();
        add(getTestCasesPanel(), BorderLayout.CENTER);
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
                ItemMessage message = new ItemMessage(nickNames.get(new Random().nextInt(9)),null,"TEST", 3d,"exalted","tab","");
//                messages.add(new Message("2017/01/11 13:41:25 1457739437 951 [INFO Client 6836] @From <(ROA)> " + nickNames.get(new Random().nextInt(9)) + ": " + randomMessages.get(new Random().nextInt(7))));
                EventRouter.INSTANCE.fireEvent(new NewWhispersEvent(message));
            }
        });
        testPanel.add(button,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel = componentsFactory.getTextLabel("Random message");
        testPanel.add(textLabel,titleColumn);
        titleColumn.gridy++;

        JButton button6 = componentsFactory.getBorderedButton("Click");
        button6.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                CurrencyMessage message = new CurrencyMessage(nickNames.get(new Random().nextInt(9)),
                        "qweqweqwwerwerwerwerwerweredsfsdfsdfsdfsdfsdfsd", null,
                        3d, "exalted", 540d, "chaos");
                EventRouter.INSTANCE.fireEvent(new NewWhispersEvent(message));
            }
        });
        testPanel.add(button6,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel16 = componentsFactory.getTextLabel("Test message from currency.poe.trade");
        testPanel.add(textLabel16,titleColumn);
        titleColumn.gridy++;

        JButton button8 = componentsFactory.getBorderedButton("Click");
        button8.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                CurrencyMessage message = new CurrencyMessage(nickNames.get(new Random().nextInt(9)),
                        "qweqweqwwerwerwerwerwerweredsfsdfsdfsdfsdfsdfsd", null,
                        10d, "exalted", 540d, "chaos");
                EventRouter.INSTANCE.fireEvent(new OutTradeMessageEvent(message));
            }
        });
        testPanel.add(button8,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel18 = componentsFactory.getTextLabel("Test outgoing trade message");
        testPanel.add(textLabel18,titleColumn);
        titleColumn.gridy++;

        JButton button5 = componentsFactory.getBorderedButton("Click");
        button5.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new WhisperNotificationEvent());

//                messages.add(new Message("ДолбоебСРусскимНиком","2016/12/26 05:20:19 Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2) блабалблабалабала"));
//                EventRouter.fireEvent(new NewWhispersEvent(messages));
            }
        });
        testPanel.add(button5,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel15 = componentsFactory.getTextLabel("Test font for non-eng letters");
        testPanel.add(textLabel15,titleColumn);
        titleColumn.gridy++;


        JButton button7 = componentsFactory.getBorderedButton("Click");
        button7.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.INSTANCE.fireEvent(new WhisperNotificationEvent());
                List<Message> messages = new ArrayList<>();
//                messages.add(new Message("WhisperColorTest","2016/12/26 05:20:19 Hi, I would like to buy your Corpse Whorl Diamond Ring Ring Ring Ring listed for 1 exalted in Breach (stash tab \"Gear\"; position: left 11, top 2)"));
//                EventRouter.fireEvent(new NewWhispersEvent(messages));

                Timer joinedTimer = new Timer(1000,null);
                joinedTimer.addActionListener(e1 -> {
                    EventRouter.INSTANCE.fireEvent(new PlayerJoinEvent("WhisperColorTest"));
                    joinedTimer.stop();
                });
                joinedTimer.start();

                Timer leftTimer = new Timer(2000,null);
                leftTimer.addActionListener(e1 -> {
                    EventRouter.INSTANCE.fireEvent(new PlayerLeftEvent("WhisperColorTest"));
                    leftTimer.stop();
                });
                leftTimer.start();
            }
        });
        testPanel.add(button7,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel17 = componentsFactory.getTextLabel("Test whisper font color after join/left area");
        testPanel.add(textLabel17,titleColumn);
        titleColumn.gridy++;
        testPanel.setBackground(AppThemeColor.TRANSPARENT);

        return testPanel;
    }

    @Override
    public void initHandlers() {

    }
}
