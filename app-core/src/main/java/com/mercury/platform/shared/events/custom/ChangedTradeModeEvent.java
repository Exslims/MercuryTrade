package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 29.12.2016.
 */
public class ChangedTradeModeEvent implements MercuryEvent {
    public static class ToDefaultTradeModeEvent implements MercuryEvent {}
    public static class ToSuperTradeModeEvent implements MercuryEvent {}
}
