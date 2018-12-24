package com.mercury.platform.core.utils.interceptor.plain;

import org.apache.commons.lang3.StringUtils;

public class GermanIncLocalizationMatcher extends LocalizationMatcher {
    @Override
    public boolean isSuitableFor(String message) {
        return message.contains("@Von");
    }

    @Override
    public boolean isIncoming() {
        return true;
    }

    @Override
    public String trimString(String message) {
        return StringUtils.substringAfter(message, "@Von ");
    }
}
