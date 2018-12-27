package com.mercury.platform.ui.frame.titled;


import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import com.mercury.platform.ui.components.panel.chat.HtmlMessageBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestEngine {
    private List<String> items = new ArrayList<>();
    private List<String> currency = new ArrayList<>();
    private List<String> nickNames = new ArrayList<>();
    private List<String> offer = new ArrayList<>();
    private List<String> leagues = new ArrayList<>();
    private HtmlMessageBuilder messageBuilder = new HtmlMessageBuilder();
    private MessageParser parser = new MessageParser();
    private Random random = new Random();
    private String poeTradeTemplate = "%s: Hi, I would like to buy your %s listed for %d %s in %s (stash tab \"%d\"; position: left %d, top %d) %s";
    private String currencyTemplate = "%s: Hi, I'd like to buy your %d %s for my %d %s in %s. %s";
    private String currencyMapBulkTemplate = "%s: Hi, I'd like to buy your %s for my %d %s in %s. %s";

    public TestEngine() {
        this.messageBuilder.setChunkStrings(Arrays.asList("Hi", "buy", "listed", "like"));

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
        currency.add("Mirror Shard");
        currency.add("Exalted Shard");
        currency.add("Annulment Shard");
        currency.add("Ancient Orb");
        currency.add("Engineer's Orb");
        currency.add("Harbinger's Orb");
        currency.add("Orb of Horizons");
        currency.add("Orb of Binding");
        currency.add("Orb of Annulment");
        currency.add("Divine Vessel");

        nickNames.add("<QWE> Example1");
        nickNames.add("Example2");
        nickNames.add("<QWE> Example3");
        nickNames.add("Example4");
        nickNames.add("<QWE> Example5");
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

    public NotificationDescriptor getRandomItemIncMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(poeTradeTemplate,
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
        notificationDescriptor.setRelatedMessages(this.getChatHistory());
        return notificationDescriptor;
    }

    public NotificationDescriptor getRandomCurrencyIncMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(currencyTemplate,
                nickNames.get(random.nextInt(nickNames.size())),
                random.nextInt(1500) + 1,
                currency.get(random.nextInt(currency.size())),
                random.nextInt(1500) + 1,
                currency.get(random.nextInt(currency.size())),
                leagues.get(random.nextInt(leagues.size())),
                offer.get(random.nextInt(offer.size()))
        ));
        notificationDescriptor.setRelatedMessages(this.getChatHistory());
        return notificationDescriptor;
    }

    public NotificationDescriptor getRandomItemOutMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(poeTradeTemplate,
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
        notificationDescriptor.setRelatedMessages(this.getChatHistory());
        notificationDescriptor.setType(NotificationType.OUT_ITEM_MESSAGE);
        return notificationDescriptor;
    }

    public NotificationDescriptor getRandomCurrencyOutMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(currencyTemplate,
                nickNames.get(random.nextInt(nickNames.size())),
                random.nextInt(1500) + 1,
                currency.get(random.nextInt(currency.size())),
                random.nextInt(1500) + 1,
                currency.get(random.nextInt(currency.size())),
                leagues.get(random.nextInt(leagues.size())),
                offer.get(random.nextInt(offer.size()))
        ));
        notificationDescriptor.setRelatedMessages(this.getChatHistory());
        notificationDescriptor.setType(NotificationType.OUT_CURRENCY_MESSAGE);
        return notificationDescriptor;
    }

    public PlainMessageDescriptor getRandomScannerMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(poeTradeTemplate,
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
        PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
        descriptor.setMessage(messageBuilder.build(notificationDescriptor.getSourceString()));
        descriptor.setNickName(notificationDescriptor.getWhisperNickname());
        return descriptor;
    }

    private List<PlainMessageDescriptor> getChatHistory() {
        List<PlainMessageDescriptor> list = new ArrayList<>();
        PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
        descriptor.setMessage("offer 1c");
        descriptor.setIncoming(true);

        PlainMessageDescriptor descriptor1 = new PlainMessageDescriptor();
        descriptor1.setMessage("nope");

        PlainMessageDescriptor descriptor2 = new PlainMessageDescriptor();
        descriptor2.setMessage("offer 2c");
        descriptor2.setIncoming(true);

        PlainMessageDescriptor descriptor3 = new PlainMessageDescriptor();
        descriptor3.setMessage("nope");

        PlainMessageDescriptor descriptor6 = new PlainMessageDescriptor();
        descriptor6.setMessage("wtf man");
        descriptor6.setIncoming(true);

        PlainMessageDescriptor descriptor7 = new PlainMessageDescriptor();
        descriptor7.setMessage("nope");

        PlainMessageDescriptor descriptor4 = new PlainMessageDescriptor();
        descriptor4.setMessage("offer 100c");
        descriptor4.setIncoming(true);

        PlainMessageDescriptor descriptor5 = new PlainMessageDescriptor();
        descriptor5.setMessage("deal");
        list.add(descriptor);
        list.add(descriptor1);
        list.add(descriptor2);
        list.add(descriptor3);
        list.add(descriptor6);
        list.add(descriptor7);
        list.add(descriptor4);
        list.add(descriptor5);
        return list;
    }

    public NotificationDescriptor getRandomMapBulkMessage() {
        NotificationDescriptor notificationDescriptor = parser.parse(String.format(currencyMapBulkTemplate,
                nickNames.get(random.nextInt(nickNames.size())),
                "1 Ivory Temple Map (T12), 1 Lava Lake Map (T15), 1 Zana's Precinct Map (T6) & 1 Burial Chambers Map (T11)",
                random.nextInt(1500) + 1,
                currency.get(random.nextInt(currency.size())),
                leagues.get(random.nextInt(leagues.size())),
                offer.get(random.nextInt(offer.size()))
        ));
        notificationDescriptor.setRelatedMessages(this.getChatHistory());
        return notificationDescriptor;
    }

}
