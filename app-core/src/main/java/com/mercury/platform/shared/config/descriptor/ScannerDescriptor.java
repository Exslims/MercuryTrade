package com.mercury.platform.shared.config.descriptor;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScannerDescriptor implements Serializable {
    private String words;
    private String responseMessage;
}
