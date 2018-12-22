package com.mercury.platform.shared.config.descriptor;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskBarDescriptor implements Serializable {
    private boolean inGameDnd;
    private String dndResponseText = "Response message";
    private HotKeyDescriptor hideoutHotkey = new HotKeyDescriptor();
    private HotKeyDescriptor helpIGHotkey = new HotKeyDescriptor();
}
