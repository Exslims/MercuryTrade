package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TradeIncMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    private PlainConfigurationService<NotificationSettingsDescriptor> config;
    private List<LocalizationMatcher> clients = new ArrayList<>();
    private Map<String, String> expiresMessages;

    public TradeIncMessagesInterceptor() {
        this.config = Configuration.get().notificationConfiguration();
        this.clients.add(new EngIncLocalizationMatcher());
        this.clients.add(new RuIncLocalizationMatcher());
        this.clients.add(new ArabicInLocalizationMatcher());
        this.clients.add(new BZIncLocalizationMatcher());
        this.expiresMessages = ExpiringMap.builder()
                .expiration(1, TimeUnit.HOURS)
                .build();
        MercuryStoreCore.expiredNotificationSubject.subscribe(notificationDescriptor -> {
            this.expiresMessages.put(UUID.randomUUID().toString(), StringUtils.substringAfter(notificationDescriptor.getSourceString(), ":"));
        });
    }

    @Override
    protected void process(String message) {
        if (this.config.get().isIncNotificationEnable()) {
            LocalizationMatcher localizationMatcher = this.clients.stream()
                    .filter(matcher -> matcher.isSuitableFor(message))
                    .findAny().orElse(null);
            if (localizationMatcher != null) {
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
        public boolean isSuitableFor(String message) {
            return message.contains("Hi, I would like") ||
                    message.contains("Hi, I'd like") ||
                    (message.contains("wtb") && message.contains("(stash"));
        }

        public abstract String trimString(String src);

        public NotificationDescriptor getDescriptor(String message) {
            return messageParser.parse(this.trimString(message));
        }

        public void processMessage(String message) {
            NotificationDescriptor notificationDescriptor = this.getDescriptor(message);
            if (notificationDescriptor != null) {
                if (!expiresMessages.containsValue(StringUtils.substringAfter(notificationDescriptor.getSourceString(), ":"))) {
                    MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
                    MercuryStoreCore.newNotificationSubject.onNext(notificationDescriptor);
                }
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

    private class RuIncLocalizationMatcher extends LocalizationMatcher {
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@От кого") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@От кого");
        }
    }

    private class ArabicInLocalizationMatcher extends LocalizationMatcher {
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@จาก") && super.isSuitableFor(message);
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@จาก");
        }
    }

    private class BZIncLocalizationMatcher extends LocalizationMatcher {
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
