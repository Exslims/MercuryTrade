package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.entity.ItemMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CloseGridItemEvent implements MercuryEvent {
    private ItemMessage message;
}
