package com.mercury.platform.core.utils.interceptor.plain;

import org.apache.commons.lang3.StringUtils;

public class BZIncLocalizationMatcher extends LocalizationMatcher {
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
