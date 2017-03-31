package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class DismissStashTabInfoEvent implements MercuryEvent {
    private TabInfoPanel tabInfoPanel;
}
