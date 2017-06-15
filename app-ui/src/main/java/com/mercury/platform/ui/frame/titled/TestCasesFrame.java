package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.entity.Message;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class TestCasesFrame extends AbstractTitledComponentFrame {
    private List<String> items;
    private List<String> currency;
    private List<String> nickNames;
    private List<String> offer;
    private List<String> leagues;
    private String poeTradeTemplate = "2017/02/11 18:40:32 9029890 951 [INFO Client 8980] @From %s: Hi, I would like to buy your %s listed for %d %s in %s (stash tab \"%d\"; position: left %d, top %d) %s";
    private String currencyTemplate = "2017/02/11 18:56:15 9973390 951 [INFO Client 8980] @From %s: Hi, I'd like to buy your %d %s for my %d %s in %s. %s";

    public TestCasesFrame() {
        super("MercuryTrade");
        items = new ArrayList<>();
        currency = new ArrayList<>();
        nickNames = new ArrayList<>();
        offer = new ArrayList<>();
        leagues = new ArrayList<>();
        items.add("Wondertrap Velvet Slippers");
        items.add("Rain of Arrows");
        items.add("Dreadarc Cleaver");
        items.add("Three-step Assault Shagreen Boots");
        items.add("Thunderous Skies");
        items.add("Honourhome Soldier Helmet");
        items.add("Pain Breaker Blood Sceptre");
        items.add("Freeze Mine");
        items.add("The Lunaris Priestess");

        currency.add("alteration");
        currency.add("alchemy");
        currency.add("apprentice  sextant");
        currency.add("divine");
        currency.add("exalted");
        currency.add("blessed");
        currency.add("esh's breachstone");
        currency.add("eber's");
        currency.add("regal");
        currency.add("armourer's");
        currency.add("augmentation");
        currency.add("bauble");
        currency.add("blessing of chayula");
        currency.add("blessing of esh");
        currency.add("blessing of tul");
        currency.add("blessing of uul-netol");
        currency.add("blessing of xoph");
        currency.add("chance");
        currency.add("chaos");
        currency.add("chayula's breachstone");
        currency.add("chimera");
        currency.add("chisel");
        currency.add("chrome");
        currency.add("coin");
        currency.add("dawn");
        currency.add("dusk");
        currency.add("eternal");
        currency.add("fusing");
        currency.add("gcp");
        currency.add("grief");
        currency.add("hope");
        currency.add("hydra");
        currency.add("ignorance");
        currency.add("inya's");
        currency.add("jeweller's");
        currency.add("journeyman sextant");
        currency.add("master sextant");
        currency.add("midnight");
        currency.add("minotaur");
        currency.add("mirror");
        currency.add("mortal set");
        currency.add("noon");
        currency.add("offering");
        currency.add("pale court");
        currency.add("phoenix");
        currency.add("portal");
        currency.add("rage");
        currency.add("regret");
        currency.add("sacrifice set");
        currency.add("scouring");
        currency.add("shaper set");
        currency.add("silver");
        currency.add("splinter of chayula");
        currency.add("splinter of esh");
        currency.add("splinter of tul");
        currency.add("splinter of uul-netol");
        currency.add("splinter of xoph");
        currency.add("transmutation");
        currency.add("tul's breachstone");
        currency.add("uul-netol's breachstone");
        currency.add("vaal");
        currency.add("volkuur's");
        currency.add("wisdom");
        currency.add("xoph's breachstone");
        currency.add("yriel's");

        nickNames.add("Example1");
        nickNames.add("Example2");
        nickNames.add("Example3");
        nickNames.add("Example4");
        nickNames.add("Example5");
        nickNames.add("Example6");

        offer.add("offer");
        offer.add(" ");
        offer.add("");
        offer.add("  ");
        offer.add("offer offer offer offer offer");
        offer.add("offer offer offer offer offer offer offer offer");
        offer.add("offer offer");
        offer.add("offer");

        leagues.add("Standard");
        leagues.add("Hardcore Legacy");
        leagues.add("Legacy");
        leagues.add("Hardcore");
        leagues.add("Beta Standard");
        leagues.add("Beta Hardcore");
        leagues.add("1 Week Legacy (JRE055)");
        leagues.add("1 Week Legacy HC (JRE055)");
    }

    @Override
    protected void initialize() {
        super.initialize();
        add(getTestCasesPanel(), BorderLayout.CENTER);
        pack();
    }

    @Override
    protected String getFrameTitle() {
        return "Example of usage";
    }

    private JPanel getTestCasesPanel(){
        MessageParser parser = new MessageParser();
        Random random = new Random();
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
                Message message = parser.parse(String.format(poeTradeTemplate,
                        nickNames.get(random.nextInt(nickNames.size())),
                        items.get(random.nextInt(items.size())),
                        random.nextInt(200),
                        currency.get(random.nextInt(currency.size())),
                        leagues.get(random.nextInt(leagues.size())),
                        random.nextInt(30),
                        random.nextInt(12) + 1,
                        random.nextInt(12) + 1,
                        offer.get(random.nextInt(offer.size()))
                ));
                MercuryStore.INSTANCE.messageSubject.onNext(message);
            }
        });
        testPanel.add(button,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel = componentsFactory.getTextLabel("Random item message");
        testPanel.add(textLabel,titleColumn);
        titleColumn.gridy++;

        JButton button1 = componentsFactory.getBorderedButton("Click");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Message message = parser.parse(String.format(currencyTemplate,
                        nickNames.get(random.nextInt(nickNames.size())),
                        random.nextInt(200),
                        currency.get(random.nextInt(currency.size())),
                        random.nextInt(200),
                        currency.get(random.nextInt(currency.size())),
                        leagues.get(random.nextInt(leagues.size())),
                        offer.get(random.nextInt(offer.size()))
                ));
                MercuryStore.INSTANCE.messageSubject.onNext(message);
            }
        });
        testPanel.add(button1,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel1 = componentsFactory.getTextLabel("Random currency message");
        testPanel.add(textLabel1,titleColumn);
        titleColumn.gridy++;



        JButton button2 = componentsFactory.getBorderedButton("Click");
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String nickname = nickNames.get(random.nextInt(nickNames.size()));
                Message message = parser.parse(String.format(currencyTemplate,
                        nickname,
                        random.nextInt(200),
                        currency.get(random.nextInt(currency.size())),
                        random.nextInt(200),
                        currency.get(random.nextInt(currency.size())),
                        leagues.get(random.nextInt(leagues.size())),
                        offer.get(random.nextInt(offer.size()))
                ));
                MercuryStore.INSTANCE.messageSubject.onNext(message);
                MercuryStore.INSTANCE.soundSubject.onNext(SoundType.MESSAGE);

                Timer joinedTimer = new Timer(1000,null);
                joinedTimer.addActionListener(e1 -> {
                    EventRouter.CORE.fireEvent(new PlayerJoinEvent(nickname));
                    joinedTimer.stop();
                });
                joinedTimer.start();

                Timer leftTimer = new Timer(2000,null);
                leftTimer.addActionListener(e1 -> {
                    EventRouter.CORE.fireEvent(new PlayerLeftEvent(nickname));
                    leftTimer.stop();
                });
                leftTimer.start();
            }
        });
        testPanel.add(button2,buttonColumn);
        buttonColumn.gridy++;
        JLabel textLabel2 = componentsFactory.getTextLabel("Test accessibility status");
        testPanel.add(textLabel2,titleColumn);
        titleColumn.gridy++;

//        JButton button3 = componentsFactory.getBorderedButton("Click");
//        button3.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                EventRouter.CORE.fireEvent(new UpdateReadyEvent());
//            }
//        });
//        testPanel.add(button3,buttonColumn);
//        buttonColumn.gridy++;
//        JLabel textLabel3 = componentsFactory.getTextLabel("Test update frame");
//        testPanel.add(textLabel3,titleColumn);
//        titleColumn.gridy++;
//        testPanel.setBackground(AppThemeColor.TRANSPARENT);

//        JButton button4 = componentsFactory.getBorderedButton("Click");
//        button4.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                Message message = parser.parse(String.format(poeTradeTemplate,
//                        "Example",
//                        items.get(random.nextInt(items.size())),
//                        random.nextInt(200),
//                        "chaos",
//                        random.nextInt(12) + 1,
//                        random.nextInt(12) + 1,
//                        "can sell cheaper??"
//                ));
//                EventRouter.CORE.fireEvent(new NewWhispersEvent(message));
//            }
//        });
//        testPanel.add(button4,buttonColumn);
//        buttonColumn.gridy++;
//        JLabel textLabel4 = componentsFactory.getTextLabel("Placeholder");
//        testPanel.add(textLabel4,titleColumn);
//        titleColumn.gridy++;
        testPanel.setBackground(AppThemeColor.TRANSPARENT);

        return testPanel;
    }

    @Override
    public void initHandlers() {

    }
}
