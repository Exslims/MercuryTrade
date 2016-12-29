package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.SCEvent;

/**
 * Created by Константин on 29.12.2016.
 */
public class ChangedTradeModeEvent implements SCEvent {
    public static class ToDefaultTradeModeEvent implements SCEvent{}
    public static class ToSuperTradeModeEvent implements SCEvent{}
}
