package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundDescriptor {
    private String wavPath;
    private Float db;
}
