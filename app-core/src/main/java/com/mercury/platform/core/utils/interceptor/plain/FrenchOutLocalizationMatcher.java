package com.mercury.platform.core.utils.interceptor.plain;

import org.apache.commons.lang3.StringUtils;

public class FrenchOutLocalizationMatcher extends LocalizationMatcher {
    @Override
    public boolean isSuitableFor(String message) {
        return message.contains("@À");
    }

    @Override
    public boolean isIncoming() {
        return false;
    }

    @Override
    public String trimString(String message) {
        return StringUtils.substringAfter(message, "@À ");
    }
}
