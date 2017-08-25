package com.mercury.platform.core.utils.interceptor.plain;


import com.mercury.platform.shared.entity.message.PlainMessageDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LocalizationMatcher {
    public abstract boolean isSuitableFor(String message);

    public abstract boolean isIncoming();

    public abstract String trimString(String message);

    public PlainMessageDescriptor getPlainMessage(String message) {
        Pattern pattern = Pattern.compile("^(\\<.+?\\>)?\\s?(.+?):(.+)$");
        Matcher matcher = pattern.matcher(this.trimString(message));
        if (matcher.find()) {
            PlainMessageDescriptor descriptor = new PlainMessageDescriptor();
            descriptor.setNickName(matcher.group(2));
            descriptor.setMessage(matcher.group(3));
            descriptor.setIncoming(this.isIncoming());
            return descriptor;
        }
        return null;
    }
}