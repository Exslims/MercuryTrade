package com.mercury.platform.core.utils.interceptor.filter;

/**
 * Created by Константин on 11.01.2017.
 */
public interface MessageFilter {
    boolean isMatching(String message);
}
