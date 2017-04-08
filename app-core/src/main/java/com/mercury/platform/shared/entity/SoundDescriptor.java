package com.mercury.platform.shared.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundDescriptor {
    @JSONField
    private String wavPath;
    @JSONField
    private Float db;
}
