package com.mercury.platform.core.utils.interceptor;

import com.mercury.platform.core.misc.SoundType;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.MessageParser;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TradeMessagesInterceptor extends MessageInterceptor {
    private MessageParser messageParser = new MessageParser();
    private PlainConfigurationService<NotificationDescriptor> config;
    private List<LocalizationMatcher> clients = new ArrayList<>();

    public TradeMessagesInterceptor() {
        this.config = Configuration.get().notificationConfiguration();
        this.clients.add(new EngLocalizationMatcher());
        this.clients.add(new RuLocalizationMatcher());
    }

    @Override
    protected void process(String message) {
        if(this.config.get().isNotificationEnable()) {
            LocalizationMatcher localizationMatcher = this.clients.stream()
                    .filter(matcher -> matcher.isSuitableFor(message))
                    .findAny().orElse(null);
            localizationMatcher.trimString(message);
            Message parsedMessage = messageParser.parse(localizationMatcher.trimString(message));
            if (parsedMessage != null) {
                MercuryStoreCore.soundSubject.onNext(SoundType.MESSAGE);
                MercuryStoreCore.messageSubject.onNext(parsedMessage);
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

}
