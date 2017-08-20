package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TradeIncMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private List<LocalizationMatcher> clients = new ArrayList<>();

    public TradeIncMessagesInterceptor() {
        this.config = Configuration.get().notificationConfiguration();
        this.clients.add(new EngIncLocalizationMatcher());
        this.clients.add(new EngOutLocalizationMatcher());
        this.clients.add(new RuIncLocalizationMatcher());
        this.clients.add(new RuOutLocalizationMatcher());
        this.clients.add(new ArabicInLocalizationMatcher());
        this.clients.add(new ArabicOutLocalizationMatcher());
        this.clients.add(new BZIncLocalizationMatcher());
        this.clients.add(new BZOutLocalizationMatcher());
    }

    @Override
    protected void process(String message) {
        if(this.config.get().isIncNotificationEnable()) {
            LocalizationMatcher localizationMatcher = this.clients.stream()
                    .filter(matcher -> matcher.isSuitableFor(message))
                    .findAny().orElse(null);
            if(localizationMatcher != null){
                localizationMatcher.processMessage(message);
            }
        }
    }

    @Override
    protected MessageFilter getFilter() {
        return message ->
                this.clients.stream()
                        .filter(matcher -> matcher.isSuitableFor(message))
                        .findAny().orElse(null) != null;
    }
    private abstract class LocalizationMatcher {
        public boolean isSuitableFor(String message){
            return message.contains("Hi, I would like") ||
                    message.contains("Hi, I'd like") ||
                    (message.contains("wtb") && message.contains("(stash"));
        }
        public abstract String trimString(String src);
        public NotificationDescriptor getDescriptor(String message){
            return messageParser.parse(this.trimString(message));
        }
        public void processMessage(String message){
            NotificationDescriptor notificationDescriptor = this.getDescriptor(message);
            if (notificationDescriptor != null) {
                MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
                MercuryStoreCore.newNotificationSubject.onNext(notificationDescriptor);
            }
        }
    }
    private class EngIncLocalizationMatcher extends LocalizationMatcher {
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@From") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@From");
        }
    }
    private class EngOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@To") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@To");
        }

        @Override
        public NotificationDescriptor getDescriptor(String message) {
            NotificationDescriptor descriptor = messageParser.parse(this.trimString(message));
            if(descriptor instanceof ItemTradeNotificationDescriptor){
                descriptor.setType(NotificationType.OUT_ITEM_MESSAGE);
            }else {
                descriptor.setType(NotificationType.OUT_CURRENCY_MESSAGE);
            }
            return descriptor;
        }
    }
    private class RuIncLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@От кого") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@От кого");
        }
    }
    private class RuOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@Кому") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@Кому");
        }

        @Override
        public NotificationDescriptor getDescriptor(String message) {
            NotificationDescriptor descriptor = messageParser.parse(this.trimString(message));
            if(descriptor instanceof ItemTradeNotificationDescriptor){
                descriptor.setType(NotificationType.OUT_ITEM_MESSAGE);
            }else {
                descriptor.setType(NotificationType.OUT_CURRENCY_MESSAGE);
            }
            return descriptor;
        }
    }
    private class ArabicInLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@จาก") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@จาก");
        }
    }
    private class ArabicOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@ถึง") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@ถึง");
        }

        @Override
        public NotificationDescriptor getDescriptor(String message) {
            NotificationDescriptor descriptor = messageParser.parse(this.trimString(message));
            if(descriptor instanceof ItemTradeNotificationDescriptor){
                descriptor.setType(NotificationType.OUT_ITEM_MESSAGE);
            }else {
                descriptor.setType(NotificationType.OUT_CURRENCY_MESSAGE);
            }
            return descriptor;
        }
    }
    private class BZIncLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@De") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@De");
        }
    }
    private class BZOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@Para") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@Para");
        }

        @Override
        public NotificationDescriptor getDescriptor(String message) {
            NotificationDescriptor descriptor = messageParser.parse(this.trimString(message));
            if(descriptor instanceof ItemTradeNotificationDescriptor){
                descriptor.setType(NotificationType.OUT_ITEM_MESSAGE);
            }else {
                descriptor.setType(NotificationType.OUT_CURRENCY_MESSAGE);
            }
            return descriptor;
        }
    }
}
