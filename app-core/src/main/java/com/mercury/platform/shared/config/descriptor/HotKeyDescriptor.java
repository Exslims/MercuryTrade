package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotKeyDescriptor implements Serializable {
    private String title = "...";
    private int virtualKeyCode;
    private boolean menuPressed;
    private boolean shiftPressed;
    private boolean controlPressed;
}
