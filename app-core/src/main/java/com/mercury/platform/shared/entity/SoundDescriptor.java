package com.mercury.platform.shared.entity;

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
