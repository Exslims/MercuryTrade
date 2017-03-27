package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;

public class RepaintEvent implements MercuryEvent{
    public static class RepaintTaskBar extends RepaintEvent {

    }
    public static class RepaintMessageFrame extends RepaintEvent {

    }
    public static class RepaintItemGrid extends RepaintEvent {

    }
    public static class RepaintSettingFrame extends RepaintEvent {
    }
}
