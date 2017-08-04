package com.mercury.platform.core.utils.interceptor.filter;


public interface MessageFilter {
    boolean isMatching(String message);
}
