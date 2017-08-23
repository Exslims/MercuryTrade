package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class HotKeyPair implements Serializable {
    private HotKeyType type;
    private HotKeyDescriptor descriptor;
}
