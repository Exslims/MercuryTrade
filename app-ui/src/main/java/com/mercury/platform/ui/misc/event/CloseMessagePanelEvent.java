package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Data
@AllArgsConstructor
public class CloseMessagePanelEvent implements MercuryEvent {
    private Message message;
}
