package com.mercury.platform.core.utils.interceptor.plain;

import org.apache.commons.lang3.StringUtils;

public class ArabicInLocalizationMatcher extends LocalizationMatcher {
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
