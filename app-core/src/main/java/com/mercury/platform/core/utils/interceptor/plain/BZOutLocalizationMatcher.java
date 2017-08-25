package com.mercury.platform.core.utils.interceptor.plain;

import org.apache.commons.lang3.StringUtils;

public class BZOutLocalizationMatcher extends LocalizationMatcher {
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
