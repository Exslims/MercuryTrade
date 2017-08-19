package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private List<LocalizationMatcher> clients = new ArrayList<>();

    public TradeMessagesInterceptor() {
        this.config = Configuration.get().notificationConfiguration();
        this.clients.add(new EngLocalizationMatcher());
        this.clients.add(new RuLocalizationMatcher());
        this.clients.add(new ArabicLocalizationMatcher());
        this.clients.add(new BZLocalizationMatcher());
    }

    @Override
    protected void process(String message) {
        if(this.config.get().isIncNotificationEnable()) {
            LocalizationMatcher localizationMatcher = this.clients.stream()
                    .filter(matcher -> matcher.isSuitableFor(message))
                    .findAny().orElse(null);
            NotificationDescriptor notificationDescriptor = messageParser.parse(localizationMatcher.trimString(message));
            if (notificationDescriptor != null) {
                MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
                MercuryStoreCore.newNotificationSubject.onNext(notificationDescriptor);
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
    }
    private class EngLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@From") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@From");
        }
    }
    private class RuLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@От кого") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@От кого");
        }
    }
    private class ArabicLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@จาก") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@จาก");
        }
    }
    private class BZLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@De") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@De");
        }
    }
}
