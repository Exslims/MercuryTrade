package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundDescriptor implements Serializable {
    private String wavPath = "...";
    private Float db = 0f;
}
