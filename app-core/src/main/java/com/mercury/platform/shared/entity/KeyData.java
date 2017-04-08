package com.mercury.platform.shared.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyData<T> {
    @JSONField
    private String key;
    @JSONField
    private T data;
}
