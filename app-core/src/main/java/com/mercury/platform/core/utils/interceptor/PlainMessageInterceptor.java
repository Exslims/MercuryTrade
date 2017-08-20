package com.mercury.platform.core.utils.interceptor;


import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlainMessageInterceptor extends MessageInterceptor{
    private List<LocalizationMatcher> clients = new ArrayList<>();

    public PlainMessageInterceptor() {
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
        LocalizationMatcher localizationMatcher = this.clients.stream()
                .filter(matcher -> matcher.isSuitableFor(message))
                .findAny().orElse(null);
        if(localizationMatcher != null){
            localizationMatcher.processMessage(message);
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
        public abstract boolean isSuitableFor(String message);
        public abstract boolean isIncoming();
        public abstract String trimString(String message);
        public void processMessage(String message){
            Pattern pattern = Pattern.compile("^(\\<.+?\\>)?\\s?(.+?):(.+)$");
            Matcher matcher = pattern.matcher(this.trimString(message));
            if(matcher.find()){
                PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
                descriptor.setNickName(matcher.group(2));
                descriptor.setMessage(matcher.group(3));
                descriptor.setIncoming(this.isIncoming());
                MercuryStoreCore.plainMessageSubject.onNext(descriptor);
            }
        }
    }
    private class EngIncLocalizationMatcher extends LocalizationMatcher {
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@From");
        }

        @Override
        public boolean isIncoming() {
            return true;
        }

        @Override
        public String trimString(String message) {
            return StringUtils.substringAfter(message,"@From ");
        }
    }
    private class EngOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@To");
        }

        @Override
        public boolean isIncoming() {
            return false;
        }

        @Override
        public String trimString(String message) {
            return StringUtils.substringAfter(message,"@To ");
        }
    }
    private class RuIncLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@От кого");
        }

        @Override
        public boolean isIncoming() {
            return true;
        }

        @Override
        public String trimString(String message) {
            return StringUtils.substringAfter(message,"@От кого ");
        }
    }
    private class RuOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@Кому");
        }

        @Override
        public boolean isIncoming() {
            return false;
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@Кому ");
        }

    }
    private class ArabicInLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@จาก");
        }

        @Override
        public boolean isIncoming() {
            return true;
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@จาก");
        }
    }
    private class ArabicOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@ถึง");
        }

        @Override
        public boolean isIncoming() {
            return false;
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@ถึง");
        }
    }
    private class BZIncLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@De");
        }

        @Override
        public boolean isIncoming() {
            return true;
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@De");
        }
    }
    private class BZOutLocalizationMatcher extends LocalizationMatcher{
        @Override
        public boolean isSuitableFor(String message) {
            return message.contains("@Para");
        }

        @Override
        public boolean isIncoming() {
            return false;
        }

        @Override
        public String trimString(String src) {
            return StringUtils.substringAfter(src, "@Para");
        }
    }
}
