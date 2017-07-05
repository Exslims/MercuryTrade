package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotKeyDescriptor {
    private int virtualKeyCode;
    private char keyChar;
    private boolean menuPressed;
    private boolean shiftPressed;
    private boolean controlPressed;
    private boolean extendedKey;
}
